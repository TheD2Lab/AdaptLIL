import os
import numpy as np
import tensorflow as tf
from flask import request
from flask import Flask
from markupsafe import escape
from tensorflow import keras

app = Flask(__name__)


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
@app.route("/predict", methods=['GET', "POST"])
def predict():
    requestJson = request.get_json()
    data = requestJson['data']
    input_data = None
    if (requestJson['encoding'] == 'byte_array'):
        #np array to byte_array
        input_data = np.frombuffer(data, dtype=np.double)
        input_data = np.reshape(input_data, newshape=tuple(requestJson['shape']))
    else:
        pass

    output = deepLearningObj.predict(input_data)
    return {
        'resultCode': 1000,
        'output' : output
    }


if __name__ == "__main__":
    app.run(debug=True)