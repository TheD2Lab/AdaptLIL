import os
import numpy as np
import tensorflow as tf
import base64
import json
import struct

import requests
from flask import request
from flask import Flask, Manager
from markupsafe import escape
from tensorflow import keras


app = Flask(__name__)
manager = Manager(app)
javaServerPort = 8080
javaServerPath = "http://localhost:" + str(javaServerPort)
print("Sending ACK")
# Remeber to add the command to your Manager instance
manager.add_command('runserver', ackJavaServer())
manager.run()

def ackJavaServer():
    #MAKE POST REQUEST BACK TO JAVA BACKEND NOTIFYING IS ALIVE
    response = requests.post(javaServerPath+"/init/ackKerasServer", json={"message": "Server is alive", "resultCode": 1000})
    print(response)
    print(str(response.resultCode) + ": response from JAVA Server: " + str(response.message))

class DeepLearningClassifierEndpoint:
    def __init__(self):
        self.modelDir = '../models'
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
        'message' : 'Loaded model: ' + deepLearningObj.modelName
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




