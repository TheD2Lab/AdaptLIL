import os, shutil

import sklearn.metrics
from sklearn import model_selection

import tensorflow as tf
import tensorflow.keras
from tensorflow.keras import Sequential
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
    dataset = arff.loadarff(open(fileName))
    data_list = [list(item) for item in dataset[0]]
    return np.array(data_list, dtype=np.float32)



def print_hi(name):
    # Use a breakpoint in the code line below to debug your script.
    print(f'Hi, {name}')  # Press Ctrl+F8 to toggle the breakpoint.

def getModelConfig():
    model = Sequential()
    model.add(Dense(450, activation='relu'))
    model.add(Dense(30, activation='relu'))
    model.add(Dense(1, activation='sigmoid'))
    return model

# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    optimizers = [tf.keras.optimizers.Adam(learning_rate=1e-3), 'rmsprop']
    trainData = convertArffToDataFrame("C:\\Users\\nickj\\Documents\\trainData_2sec_window_1_no_v.arff")
    print(trainData.shape)
    targetColumn = "correct"
    x = trainData[:, :-1]
    y = trainData[:,-1]
    print(y.shape)
    #split ^^ 80/20, preserve order
    x_train, x_test, y_train, y_test = model_selection.train_test_split(trainData, y, test_size=0.2, random_state=42, shuffle=False)

    validationData = convertArffToDataFrame("C:\\Users\\nickj\\Documents\\testData_2sec_window_1_no_v.arff")
    xVal = validationData[:,:-1]
    yVal = validationData[:,-1:]

    model = getModelConfig()

    for optimizer in optimizers:
        model.compile(optimizer=optimizer, loss=tf.keras.losses.BinaryCrossentropy())
        y_hat = model.fit(x_train, y_train, validation_data=(x_test, y_test), shuffle=False, epochs=100)
        print(sklearn.metrics.confusion_matrix(y_train, y_hat))
# See PyCharm help at https://www.jetbrains.com/help/pycharm/
