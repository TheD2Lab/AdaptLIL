import datetime
import json
import numpy as np
import os
import shutil
import sklearn.metrics
import sys
import tensorflow as tf
from collections import OrderedDict
from csv import DictWriter
from keras.callbacks import CSVLogger
from scipy.io import arff
from sklearn import model_selection
from sklearn.model_selection import StratifiedKFold
from tensorflow.keras import Sequential
from tensorflow.keras import layers
from tensorflow.keras.layers import Dense, LSTM, LeakyReLU, Dropout
os.environ['TF_CPP_MIN_LOG_LEVEL'] = '2'

def convertArffToDataFrame(fileName):
    dataset = arff.loadarff(open(fileName))
    data_list = [list(item) for item in dataset[0]]
    return np.array(data_list, dtype=np.float32)


def convertDataToLTSMFormat(data, timeSequences, numMetaAttrs):
    x = []
    y = []
    ts_index = 0
    window = []
    for i in data:
        correct = i[-1]
        reshaped = np.array(i[:-1], dtype=np.float32)
        window.append(reshaped.flatten())
        ts_index += 1
        if (ts_index == timeSequences):
            x.append(np.array(window))
            y.append(correct)
            ts_index = 0
            window = []

        # split_data = np.array(np.array_split(np.array(reshaped).flatten(), windowSize, axis=0))
        # for j in range(windowSize):

    x = np.array(x)
    y = np.array(y)
    return [x, y]

def calc_conf_matrix_rates(conf_matrix):
    true_positive = conf_matrix[0][0] / (sum(conf_matrix[0]))
    true_negative = conf_matrix[1][1] / (sum(conf_matrix[1]))

    return {
        'true_positive': true_positive,
        'true_negative': true_negative
    }


if __name__ == '__main__':
    timeSequences = 2
    numAttributes = 600
    numMetaAttrs = 0

    testDataDir = "/home/notroot/Desktop/d2lab/gazepoint/train_test_data_output/bpog only/"
    testData = convertArffToDataFrame(testDataDir + "/trainData_500.0mssec_P43 p43.conf.list.csv.arff")
    models = []
    # models.append(tf.keras.models.load_model("/home/notroot/Desktop/d2lab/gazepoint/python/2023-12-01 09_16_12,973606/stacked_lstm_v2-Adagrad0,008 wdecay: None ema:False.h5"));
    models.append(tf.keras.models.load_model("/home/notroot/Desktop/d2lab/gazepoint/python/2023-12-01 15_00_56,728099/stacked_lstm_v2-Adam0,0014 b1: 0.9 wdecay: None ema:False.h5"));
    xTest, yTest = convertDataToLTSMFormat(testData, timeSequences, numMetaAttrs)
    # xTest = normalizeData(xTest, windowSize, numAttributes, numMetaAttrs, attr_min_max)

    '''
    Done fitting on multiple participants, time for real world data testing
    '''
    y_hats = []
    for model in models:

        y_hat = model.predict(xTest)
        # results = model.evlauate(xVal, yTest)
        # y_hat = [(1.0 if y_pred >= 0.5 else 0.0) for y_pred in y_hat]
        y_hats.append(y_hat)

    summed_y_hats = np.sum(y_hats, axis=0)
    summed_y_hats = np.mean(y_hats, axis=0)
    outcomes = [(1.0 if y_pred >= 0.5 else 0.0) for y_pred in summed_y_hats]

    conf_matrix = sklearn.metrics.confusion_matrix(yTest, outcomes, labels=[1.0, 0.0])
    print(conf_matrix)
