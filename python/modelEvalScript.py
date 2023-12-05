import datetime
import json
import numpy as np
import os
os.environ['TF_CPP_MIN_LOG_LEVEL'] = '2'

import shutil
import sklearn.metrics
import sys
import tensorflow as tf
from collections import OrderedDict
from csv import DictWriter
from keras.callbacks import CSVLogger
from scipy.io import arff
from sklearn import model_selection
from sklearn.model_selection import StratifiedKFold, KFold
from tensorflow.keras import Sequential
from tensorflow.keras import layers
from tensorflow.keras.layers import Dense, LSTM, LeakyReLU, Dropout, Conv1D, MaxPooling1D, ReLU, Bidirectional
import matplotlib.pyplot as plt
os.environ['TF_CPP_MIN_LOG_LEVEL'] = '2'

def convertArffToDataFrame(fileName):
    dataset = arff.loadarff(open(fileName))
    data_list = [list(item) for item in dataset[0]]
    return np.array(data_list, dtype=np.float32)

def get_weight_bias(y_data):
    neg = len([i for i in y_data if i == 0])
    # print(neg)
    pos = len([i for i in y_data if i == 1])
    # print(pos)
    total = len(y_data)
    if (neg != 0):
        weight_for_0 = (1 / neg) * (total)
    else:
        weight_for_0 = 0
    if (pos != 0):
        weight_for_1 = (1 / pos) * (total)  # TODO, pay more attention to this weight distrib.
    else:
        weight_for_1 = 0
    return {0: weight_for_0, 1: weight_for_1}


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

def transformer_encoder(inputs, head_size, num_heads, ff_dim, dropout=0):
    # Norm and Attention
    x = layers.LayerNormalization(epsilon=1e-6)(inputs)  # What does passing inputs do to x?
    x = layers.MultiHeadAttention(key_dim=head_size, num_heads=num_heads, dropout=dropout)(x,
                                                                                           x)  # What does passing x,x do?
    x = layers.Dropout(dropout)(x)
    res = x + inputs  # res?

    # feed foward
    x = layers.LayerNormalization(epsilon=1e-6)(res)
    x = layers.Conv1D(filters=ff_dim, kernel_size=1, activation='relu')(
        x)  # Might need to put the activation layer as separate var for d4j
    x = layers.Dropout(dropout)(x)
    x = layers.Conv1D(filters=inputs.shape[-1], kernel_size=1)(x)
    return x + res


def build_transformer_model(input_shape, head_size, num_heads, ff_dim, num_transformer_blocks, mlp_units, dropout=0,
                            mlp_dropout=0):
    inputs = tf.keras.Input(shape=input_shape)
    x = inputs
    for _ in range(num_transformer_blocks):
        x = transformer_encoder(x, head_size, num_heads, ff_dim, dropout)

    x = layers.GlobalAveragePooling1D(data_format="channels_first")(x)  # What does channels_first do?
    for dim in mlp_units:
        x = layers.Dense(dim, activation='relu')(x)  # What does passing x do here?
        x = layers.Dropout(mlp_dropout)(x)

    outputs = layers.Dense(1, activation='sigmoid')(x)  # 2 here is because we have a binary class.
    return tf.keras.Model(inputs, outputs)


if __name__ == '__main__':
    timeSequences = 5
    numAttributes = 150
    windowSize = int(numAttributes / 2);
    numMetaAttrs = 0
    testPID = 11
    trainDir = "/home/notroot/Desktop/d2lab/gazepoint/train_test_data_output/bpog only/" + "/trainData_500.0mssec_P"+str(testPID)+" p"+str(testPID)+".conf.list.csv.arff"

    testDataDir = "/home/notroot/Desktop/d2lab/gazepoint/train_test_data_output/bpog only anat/"
    testData = convertArffToDataFrame(testDataDir + "/trainData_500.0mssec_P"+str(testPID)+" p"+str(testPID)+".anatomy.list.csv.arff")
    trainData = convertArffToDataFrame(trainDir)
    models = []
    input_shape = (timeSequences, numAttributes)
    # models.append(tf.keras.models.load_model("/home/notroot/Desktop/d2lab/gazepoint/python/2023-12-01 09_16_12,973606/stacked_lstm_v2-Adagrad0,008 wdecay: None ema:False.h5"));
    transformer = build_transformer_model(input_shape, head_size=numAttributes, num_heads=14, ff_dim=numAttributes,
                            num_transformer_blocks=2, mlp_units=[numAttributes * 2], mlp_dropout=0.15,
                            dropout=0.1)
    kernelSize = 2  # filters is the num windows, and 2 b/c (x,y)
    filterSize = int(windowSize/2)
    conv_stacked_lstm = Sequential()
    conv_stacked_lstm.add(Conv1D(2, kernelSize, input_shape=input_shape))  # filter size of 25 to split the window into three frames.

    conv_stacked_lstm.add(MaxPooling1D(pool_size=1))
    conv_stacked_lstm.add(Dropout(0.10))
    conv_stacked_lstm.add(Bidirectional(LSTM(int(numAttributes), dropout=0.2, return_sequences=True, activation='relu')))
    conv_stacked_lstm.add(Bidirectional(LSTM(int(numAttributes), dropout=0.2, return_sequences=True, activation='relu')))
    conv_stacked_lstm.add(Bidirectional(LSTM(int(numAttributes), dropout=0.2, return_sequences=True, activation='relu')))
    conv_stacked_lstm.add(Bidirectional(LSTM(int(numAttributes), dropout=0.2, activation='relu')))
    conv_stacked_lstm.add(Dropout(0.20))
    conv_stacked_lstm.add(Dense(int(numAttributes)))
    conv_stacked_lstm.add(LeakyReLU())
    conv_stacked_lstm.add(Dense(1, activation='sigmoid'))
    conv_stacked_lstm.compile(    optimizer=tf.keras.optimizers.Adam(learning_rate=1.4e-3),
    loss="binary_crossentropy",
                            metrics=[        tf.keras.metrics.BinaryAccuracy(name='accuracy'),
                                             tf.keras.metrics.TruePositives(name='tp'),
                                             tf.keras.metrics.TrueNegatives(name='tn'),
                                             ])
    models.append(conv_stacked_lstm);
    xTest, yTest = convertDataToLTSMFormat(testData, timeSequences, numAttributes)
    # xTest = normalizeData(xTest, windowSize, numAttributes, numMetaAttrs, attr_min_max)
    xTrain, yTrain = convertDataToLTSMFormat(trainData, timeSequences, numAttributes)
    # inputs = np.concatenate((xTrain, xTest), axis=0)
    # targets = np.concatenate((yTest, yTrain), axis=0)
    '''
    Done fitting on multiple participants, time for real world data testing
    '''
    y_hats = []
    acc_rates = []
    for model in models:
        for i in range(0,30):
            kfold = StratifiedKFold(n_splits=14, shuffle=False)
            fold_no = 0
            for train, test in kfold.split(xTrain, yTrain):
                weights = get_weight_bias(yTrain[train])

                model.fit(xTrain[train], yTrain[train],
                          # class_weight=weights
                          epochs=20
                          )
                print(xTest.shape)
                y_hat = model.predict(xTest)
                # results = model.evlauate(xVal, yTest)
                y_hat = [(1.0 if y_pred >= 0.5 else 0.0) for y_pred in y_hat]
                conf_matrix = sklearn.metrics.confusion_matrix(yTest, y_hat, labels=[1.0, 0.0])
                conf_matrix_rates = calc_conf_matrix_rates(conf_matrix)
                print(conf_matrix)

                tp_rate = conf_matrix_rates['true_positive']
                tn_rate = conf_matrix_rates['true_negative']
                acc_rate = (tp_rate + tn_rate) / 2
                acc_rates.append(acc_rate)
                print("acc rate: " + str(acc_rate))
                print("tp: " + str(tp_rate) + " tn: " + str(tn_rate))
                scores = model.evaluate(xTrain[test], yTrain[test], verbose=0)
                print(
                    f'Score for fold {fold_no}: {model.metrics_names[0]} of {scores[0]}; {model.metrics_names[1]} of {scores[1] * 100}%')
                fold_no += 1

    plt.plot(np.array(range(0,len(acc_rates))), np.array(acc_rates))
    plt.show()

    conf_matrix = sklearn.metrics.confusion_matrix(yTest, outcomes, labels=[1.0, 0.0])
