import os, shutil

import sklearn.metrics
from sklearn import model_selection

import tensorflow as tf
import tensorflow.keras
from tensorflow.keras import models
from tensorflow.keras import optimizers
from tensorflow.keras.layers import Dense
from tensorflow.keras import metrics
from scipy.io import arff
import pandas as pd
import numpy as np
# This is a sample Python script.

# Press Shift+F10 to execute it or replace it with your code.
# Press Double Shift to search everywhere for classes, files, tool windows, actions, and settings.

'''
Returns Arff file to a dataframe for training
'''
def convertArffToDataFrame(fileName):
    dataset = arff.loadarff(fileName)
    return pd.DataFrame(dataset['data'])



def print_hi(name):
    # Use a breakpoint in the code line below to debug your script.
    print(f'Hi, {name}')  # Press Ctrl+F8 to toggle the breakpoint.

def getModelConfig(input_dim):
    model = Sequential()
    model.add(Dense(450, activation='relu', input_dim=input_dim))
    model.add(Dense(30, activation='relu'))
    model.add(Dense(2, activation='softmax'))
    return model

# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    optimizers = [tf.keras.optimizers.Adam(learning_rate=1e-3), 'rmsprop']
    trainData = convertArffToDataFrame("D:\\trainData_2sec_window_1.arff")
    targetColumn = "correct"
    x = trainData[:-1]
    y = trainData[len(trainData) - 1]
    #split ^^ 80/20, preserve order
    x_train, x_test, y_train, y_test = model_selection.train_test_split(trainData, y, test_size=0.2, random_state=42, shuffle=False)
    validationData = convertArffToDataFrame("D:\\testData_2sec_window_1_no_v.arff")
    xVal = validationData[:-1]
    yVal = validationData[len(trainData) - 1]

    model = getModelConfig(len(x))

    for optimizer in optimizers:
        model.compile(optimizer=optimizer, loss=tf.keras.losses.BinaryCrossentropy(),
                      metrics = [tf.keras.metrics.TrueNegatives, tf.keras.metrics.TruePositives])
        y_hat = model.fit(x_train, y_train, validation_data=(x_test, y_test), shuffle=False, epochs=100)
        print(sklearn.metrics.confusion_matrix(y_train, y_hat))
# See PyCharm help at https://www.jetbrains.com/help/pycharm/
