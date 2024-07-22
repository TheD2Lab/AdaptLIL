import os
import numpy as np
import tensorflow as tf
import base64
import json
import struct

import requests
from flask import request
from flask import Flask
from markupsafe import escape
from tensorflow import keras
from gevent.pywsgi import WSGIServer

#Must be at start of the script
app = Flask(__name__)


class DeepLearningClassifierEndpoint:
    def __init__(self):
        self.modelDir = os.path.join('models')
        self.modelName = None
        pass

    def loadModel(self, modelName):
        self.modelName = modelName
        self.model = tf.keras.models.load_model(os.path.join(self.modelDir, modelName))

    def predict(self, data):
        if (self.model):
            output = self.model.predict(data)
            return output
        else:
            return None


deepLearningObj = DeepLearningClassifierEndpoint();
@app.route("/loadModel", methods=["POST"])
def loadModel():
    requestJson = request.get_json()
    modelName = requestJson['modelName']
    modelName = escape(modelName)
    deepLearningObj.loadModel(modelName)
    return {
        'resultCode': 1000,
        'message': 'Loaded model: ' + deepLearningObj.modelName
    }


'''
RequestBody: {
'data': [] training data,
'shape': [] shape of data,
'encoding': 'byte_array' ... todo}
'''
@app.route("/predict", methods=["POST"])
def predict():
    requestJson = request.get_json()
    print(requestJson)
    data = requestJson['data']
    input_data = None
    if (requestJson['encoding'] == 'byte_array'):
        # data = struct.pack('%sf' % len(data), *data)
        #np array to byte_array
        # data = base64.b64decode(data,validate=True)
        print(data)
        # print(data)

        input_data = np.array(data)
        input_data = np.reshape(input_data, newshape=tuple(requestJson['shape']))
    else:
        pass

    output = deepLearningObj.predict(input_data)
    print(output)
    somejson = {
        'resultCode': 1000,
        'output': list(output.flatten('C').astype(np.double)),
        'outputShape': list(output.shape)
    }
    print(somejson)

    print(json.dumps(somejson))
    return somejson

@app.route("/close", methods=["POST"])
def close():
    requestJson = request.get_json()
    pid = 1010
    if (requestJson['pid'] == pid):
        func = request.environ.get('werkzeug.server.shutdown')
        exit()





def ackJavaServer():
    print("Sending ACK")

    javaServerPort = 8080
    javaServerPath = "http://localhost:" + str(javaServerPort)
    print("before making request")
    #MAKE POST REQUEST BACK TO JAVA BACKEND NOTIFYING IS ALIVE
    response = requests.post(javaServerPath+"/init/ackKerasServer", json={"message": "Server is alive", "resultCode": 1000})
    print("made request")
    print(response)
    print(response.json())
    responseJson = response.json()
    print(str(responseJson['resultCode']) + ": response from JAVA Server: " + str(responseJson['message']))

# with app.app_context():
http_server = WSGIServer(("localhost", 5000), app)
print("wsgI server init")

http_server.start()
ackJavaServer()
http_server.serve_forever()