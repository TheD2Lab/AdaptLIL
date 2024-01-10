import os
import tensorflow as tf
from flask import request
from flask import Flask
from markupsafe import escape
from tensorflow import keras

app = Flask(__name__)


class DeepLearningClassifierEndpoint:
    def __init(self):
        self.modelDir = '../models'
        pass

    def loadModel(self, modelName):
        self.model = create_model()
        self.model.load_weights(os.path.join(self.modelDir, modelName, ".h5"))

    def predict(self, data):
        if (self.model):
            output = self.model.predict(data)
            return output
        else:
            return None


deepLearningObj = DeepLearningClassifierEndpoint();
@app.route("/loadModel/<modelName>")
def loadModel():
    modelName = escape(modelName)
    try:
        deepLearningObj.loadModel(modelName)
        return {
            'message' : 'Loaded model: ' + deepLearningObj.modelName
        }
    except e:
        return {
            'message': 'Error loading specified model name: ' + deepLearningObj.modelName
        }

@app.route("/predict", methods=["POST"])
def predict():
    requestJson = request.get_json()
    data = requestJson.data
    output = deepLearningObj.predict(data)
    return {
        'output' : output
    }


