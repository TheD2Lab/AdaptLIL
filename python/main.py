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
from sklearn.model_selection import StratifiedKFold, LeavePOut
from tensorflow.keras import Sequential
from tensorflow.keras import layers
from tensorflow.keras.layers import Dense, LSTM, LeakyReLU, Dropout, MaxPooling1D, Conv1D
#https://github.com/timeseriesAI/tsai
resultDir = str(datetime.datetime.now()).replace(":", "_").replace(".", ",")
os.mkdir(resultDir)

outputFile = open(os.path.join(resultDir, "output.txt"), 'wt')
from numpy.random import seed
seed(0)
tf.config.experimental.enable_op_determinism()
tf.keras.utils.set_random_seed(0)
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


def normalizeData(data, numPolls, numAttributes, numMetaAttrs, attributesMinMax):
    '''
    Given 3D data
    [
        window_0[
            poll_0: [

                [attr_0,...,attr_8], size k = 8
            ],
            ...
            poll_n (300): [
                [attr_0,...,attr_8],
            ]
        ]
        window_m[]
    where m = # of windows, n = # of sequences per window
    calculate the min an max for each attribute_k
    '''

    # get min max

    # normalize and return
    for i in range(len(data)):
        for j in range(len(data[i])):
            for k in range(len(data[i][j])):
                if ( k > (numPolls * numAttributes)): #skip the meta attributes
                    continue;
                l = k % numAttributes
                data[i][j][k] = (data[i][j][k] - attributesMinMax[l]['min']) / (
                            attributesMinMax[l]['max'] - attributesMinMax[l]['min'])
    return data


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

def normalizationByPollingSample(data, numAttributes, numMetaAttrs, attribute_min_max={}) :
    for i in data:
        correct = i[-1]
        attrs = i[:(-1 * (1 + numMetaAttrs))]
        # print_both("norm by sample, attr shape: ")
        # print_both(np.array(attrs).shape)

        for j in range(len(attrs)):
            k = j % numAttributes
            if (k not in attribute_min_max):
                attribute_min_max[k] = {'min': float('inf'), 'max': float('-inf')}
            else:
                attribute_min_max[k]['min'] = min(attribute_min_max[k]['min'], attrs[j])
                attribute_min_max[k]['max'] = max(attribute_min_max[k]['max'], attrs[j])

    return attribute_min_max

def print_both(*args):
    temp = sys.stdout  # assign console output to a variable
    print(' '.join([str(arg) for arg in args]))
    sys.stdout = outputFile
    print(' '.join([str(arg) for arg in args]))
    sys.stdout = temp  # set stdout back to console output


def calc_conf_matrix_rates(conf_matrix):
    true_positive = conf_matrix[0][0] / (sum(conf_matrix[0]))
    true_negative = conf_matrix[1][1] / (sum(conf_matrix[1]))

    return {
        'true_positive': true_positive,
        'true_negative': true_negative
    }


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


def get_metrics_for_model():
    return [
        tf.keras.metrics.BinaryAccuracy(name='accuracy'),

        # tf.keras.metrics.BinaryCrossentropy(name='cross entropy'),  # same as model's loss
        # tf.keras.metrics.MeanSquaredError(name='Brier score'),
        tf.keras.metrics.TruePositives(name='tp'),
        # tf.keras.metrics.FalsePositives(name='fp'),
        tf.keras.metrics.TrueNegatives(name='tn'),
        # tf.keras.metrics.FalseNegatives(name='fn'),
        tf.keras.metrics.Precision(name='precision'),
        # tf.keras.metrics.Recall(name='recall'),
        # tf.keras.metrics.AUC(name='auc'),
        # tf.keras.metrics.AUC(name='prc', curve='PR'),  # precision-recall curve

    ]


def get_optimizers():
    return [
       # tf.keras.optimizers.Adagrad(learning_rate=0.008, name='Adagrad'),
    #     "adam"
    #     tf.keras.optimizers.Adam(learning_rate=1e-4, beta_1=0.9, beta_2=0.98),
    #     tf.keras.optimizers.Adam(learning_rate=1e-9, beta_1=0.9, beta_2=0.98),
        
        #tf.keras.optimizers.SGD(learning_rate=1e-4, momentum=0.9),
        # tf.keras.optimizers.SGD(learning_rate=0.008),
        # tf.keras.optimizers.SGD(learning_rate=0.04),
        # tf.keras.optimizers.SGD(learning_rate=0.08),

        # tf.keras.optimizers.Adagrad(learning_rate=0.0013, name='Adagrad'),
        # tf.keras.optimizers.Adagrad(learning_rate=0.0014, name='Adagrad'),
        #tf.keras.optimizers.Adagrad(learning_rate=0.001, name='Adagrad'),

        # tf.keras.optimizers.Adagrad(learning_rate=0.01, name='Adagrad'),
        # tf.keras.optimizers.Adagrad(learning_rate=0.0015, name='Adagrad'),
        # tf.keras.optimizers.Adagrad(learning_rate=0.002, name='Adagrad'),
        # tf.keras.optimizers.Adam(learning_rate=1.4e-3, use_ema=True),
        # tf.keras.optimizers.Adam(learning_rate=1.45e-3, use_ema=True),
        # tf.keras.optimizers.Adam(learning_rate=1.4e-3, beta_1=0.53, use_ema=True),
        # tf.keras.optimizers.Adam(learning_rate=1.4e-3, beta_1=0.54, use_ema=True),
        # tf.keras.optimizers.Adam(learning_rate=1.4e-3, beta_1=0.55, use_ema=True),
        # tf.keras.optimizers.Adam(learning_rate=1.4e-3, beta_1=0.65, use_ema=True),
        # tf.keras.optimizers.Adam(learning_rate=1.4e-3, beta_1=0.6, use_ema=True),
        #
        # tf.keras.optimizers.Adam(learning_rate=1.4e-3, beta_1=0.45, use_ema=True, weight_decay=2e-4),
        # tf.keras.optimizers.Adam(learning_rate=1.4e-3, beta_1=0.48, use_ema=True, weight_decay=2e-4),
        # tf.keras.optimizers.Adam(learning_rate=1.4e-3, beta_1=0.47, use_ema=True, weight_decay=2e-4),
        # tf.keras.optimizers.Adam(learning_rate=1.4e-3, beta_1=0.49, use_ema=True, weight_decay=2e-4),
        # tf.keras.optimizers.Adam(learning_rate=1.4e-3, beta_1=0.40, use_ema=True, weight_decay=2e-4),
        #
        tf.keras.optimizers.Adam(learning_rate=1.4e-3, use_ema=False), #control
        # tf.keras.optimizers.Adam(learning_rate=1.45e-3, use_ema=False),
        # tf.keras.optimizers.Adam(learning_rate=1.4e-3, beta_1=0.38, use_ema=False),
        # tf.keras.optimizers.Adam(learning_rate=1.4e-3, beta_1=0.39, use_ema=False),
        # tf.keras.optimizers.Adam(learning_rate=1.4e-3, beta_1=0.35, use_ema=False),
        # tf.keras.optimizers.Adam(learning_rate=1.4e-3, beta_1=0.32, use_ema=False),
        # tf.keras.optimizers.Adam(learning_rate=1.4e-3, beta_1=0.6, use_ema=False),
        #
        # tf.keras.optimizers.Adam(learning_rate=1.4e-3, beta_1=0.8, use_ema=False, weight_decay=2e-4),
        # tf.keras.optimizers.Adam(learning_rate=1.4e-3, beta_1=0.95, use_ema=False, weight_decay=2e-4),
        # tf.keras.optimizers.Adam(learning_rate=1.4e-3, beta_1=0.85, use_ema=False, weight_decay=2e-4),
        # tf.keras.optimizers.Adam(learning_rate=1.4e-3, beta_1=0.7, use_ema=False, weight_decay=2e-4),
        # tf.keras.optimizers.Adam(learning_rate=1.4e-3, beta_1=0.6, use_ema=False, weight_decay=2e-4),

        # tf.keras.optimizers.SGD(learning_rate=0.01),
        # tf.keras.optimizers.SGD(learning_rate=0.012),
        # tf.keras.optimizers.SGD(learning_rate=0.015),
        # tf.keras.optimizers.Adam(learning_rate=1.1e-3),
        # tf.keras.optimizers.Adam(learning_rate=1.2e-3),
        # tf.keras.optimizers.Adam(learning_rate=1.4e-3),
        # tf.keras.optimizers.Adam(learning_rate=1.6e-3),
        # tf.keras.optimizers.Adam(learning_rate=1.7e-3),
        # tf.keras.optimizers.Adam(learning_rate=1.8e-3),
        # tf.keras.optimizers.Adam(learning_rate=1.9e-3),
        #tf.keras.optimizers.Adam(learning_rate=1e-1),
        # tf.keras.optimizers.Adam(learning_rate=2e-3),
        # tf.keras.optimizers.Adam(learning_rate=1e-2),
        #tf.keras.optimizers.Nadam(learning_rate=1e-3),
        tf.keras.optimizers.Nadam(learning_rate=1.5e-3),
        # tf.keras.optimizers.Nadam(learning_rate=2e-3),
        #tf.keras.optimizers.Nadam(learning_rate=1e-2),
        # tf.keras.optimizers.Nadam(learning_rate=1e-1),

        # tf.keras.optimizers.RMSprop
    ]

def transformer_encoder(inputs, head_size, num_heads, ff_dim, dropout=0):
    #Norm and Attention
    x = layers.LayerNormalization(epsilon=1e-6)(inputs) #What does passing inputs do to x?
    x = layers.MultiHeadAttention(key_dim=head_size, num_heads=num_heads, dropout=dropout)(x,x) #What does passing x,x do?
    x = layers.Dropout(dropout)(x)
    res = x + inputs #res? 

    #feed foward
    x = layers.LayerNormalization(epsilon=1e-6)(res)
    x = layers.Conv1D(filters=ff_dim, kernel_size=1, activation='relu')(x) #Might need to put the activation layer as separate var for d4j
    x = layers.Dropout(dropout)(x)
    x = layers.Conv1D(filters=inputs.shape[-1], kernel_size=1)(x)
    return x + res

def build_transformer_model(input_shape, head_size, num_heads, ff_dim, num_transformer_blocks, mlp_units, dropout=0, mlp_dropout=0):
    inputs = tf.keras.Input(shape=input_shape)
    x = inputs
    for _ in range(num_transformer_blocks):
        x = transformer_encoder(x, head_size, num_heads, ff_dim, dropout)

    x = layers.GlobalAveragePooling1D(data_format="channels_first")(x) #What does channels_first do?
    for dim in mlp_units:
        x = layers.Dense(dim, activation='relu')(x) #What does passing x do here?
        x = layers.Dropout(mlp_dropout)(x)

    outputs = layers.Dense(1, activation='sigmoid')(x) #2 here is because we have a binary class.
    return tf.keras.Model(inputs, outputs)

def getModelConfig(timeSequences, attributes, windowSize):
    input_shape=(timeSequences, attributes)
    models = {}
    '''Bigger moddels are showing higher returns for transformers. Continue running bigger transformers'''
    transformer_model = build_transformer_model(input_shape, head_size=64, num_heads=8, ff_dim=4, num_transformer_blocks=8, mlp_units=[256], mlp_dropout=0.15, dropout=0.25)
    model_simple_ltsm = Sequential()
    model_simple_ltsm.add(LSTM(4, input_shape=(timeSequences, attributes)))
    model_simple_ltsm.add(Dense(8, activation='relu'))
    model_simple_ltsm.add(Dense(16, activation='relu'))
    model_simple_ltsm.add(Dense(4, activation='relu'))
    model_simple_ltsm.add(Dense(1, activation='sigmoid'))

    model_one_layer_ltsm = Sequential()
    model_one_layer_ltsm.add(LSTM(2400, input_shape=(timeSequences, attributes)))
    model_one_layer_ltsm.add(Dense(8, activation='relu'))
    model_one_layer_ltsm.add(Dense(1, activation='sigmoid'))

    model_one_layer_ltsm_smaller = Sequential()
    model_one_layer_ltsm_smaller.add(LSTM(4, input_shape=(timeSequences, attributes)))
    model_one_layer_ltsm_smaller.add(Dense(8, activation='relu'))
    model_one_layer_ltsm_smaller.add(Dense(1, activation='sigmoid'))

    # v2 has 4 more nodes in the intermediate dense layer
    model_one_layer_ltsm_v2 = Sequential()
    model_one_layer_ltsm_v2.add(LSTM(4, input_shape=(timeSequences, attributes)))
    model_one_layer_ltsm_v2.add(Dense(12, activation='relu'))
    model_one_layer_ltsm_v2.add(Dense(1, activation='sigmoid'))

    model_bigger_lstm = Sequential()
    model_bigger_lstm.add(LSTM(8, input_shape=(timeSequences, attributes)))
    model_bigger_lstm.add(Dense(8, activation='relu'))
    model_bigger_lstm.add(Dense(2, activation='relu'))
    model_bigger_lstm.add(Dense(1, activation='sigmoid'))

    model_bigger_bigger_lstm = Sequential()
    model_bigger_bigger_lstm.add(LSTM(12, input_shape=(timeSequences, attributes)))
    model_bigger_bigger_lstm.add(Dense(8, activation='relu'))
    model_bigger_bigger_lstm.add(Dense(1, activation='sigmoid'))

    model_bigger_biggest_lstm = Sequential()
    model_bigger_biggest_lstm.add(LSTM(16, input_shape=(timeSequences, attributes)))
    model_bigger_biggest_lstm.add(Dense(4, activation='relu'))
    model_bigger_biggest_lstm.add(Dense(1, activation='sigmoid'))

    stacked_lstm = Sequential()
    '''
    10-27-2023 I am noticing a bigger first lstm layer followed by two subsequent smaller lstm layers (size 16 each) and a dense layer of size 16
    works better when the first layer is larger than the other layers
    It's possible that 148 was a good sweet spot because there are 75 attributes, this leads one lstm node per attribute
    and therefore can trigger it to forget or keep the memory for each 0,1 class.
    I.e., for each attribute, allocate 1 node per class.
    '''
    stacked_lstm.add(Dropout(0.1, input_shape=input_shape))
    stacked_lstm.add(LSTM(75, input_shape=(timeSequences, attributes), return_sequences=True))
    # stacked_lstm.add(LSTM(150, input_shape=(timeSequences, attributes), return_sequences=True))
    # stacked_lstm.add(LSTM(128, return_sequences=True))
    stacked_lstm.add(LSTM(16, return_sequences=True))
    stacked_lstm.add(LSTM(16, return_sequences=True))
    stacked_lstm.add(LSTM(16))
    # stacked_lstm.add(LSTM(64)))
    stacked_lstm.add(Dense(16))
    stacked_lstm.add(LeakyReLU())
    stacked_lstm.add(Dense(1, activation='sigmoid'))

    conv_stacked_lstm = Sequential()
    '''
    10-27-2023 I am noticing a bigger first lstm layer followed by two subsequent smaller lstm layers (size 16 each) and a dense layer of size 16
    works better when the first layer is larger than the other layers
    It's possible that 148 was a good sweet spot because there are 75 attributes, this leads one lstm node per attribute
    and therefore can trigger it to forget or keep the memory for each 0,1 class.
    I.e., for each attribute, allocate 1 node per class.
    '''
    '''
    Using pure point of gaze, applying convolution to a dense layer, some dropout and max pooling, and then the lstm followed by another dense layer and the prediction
    Based on paper "Toward a deep convolutional LSTM for eye gaze spatiotemporal data sequence classification
    '''
    kernelSize = 2 #filters is the num windows, and 2 b/c (x,y)
    filterSize = windowSize
    
    conv_stacked_lstm.add(
        Conv1D(25, kernelSize, input_shape=(timeSequences, attributes))
    ) #filter size of 25 to split the window into three frames.

    conv_stacked_lstm.add(
        MaxPooling1D(pool_size=1)
    )
    conv_stacked_lstm.add(Dropout(0.10))
    conv_stacked_lstm.add(LSTM(int(attributes/kernelSize), return_sequences=True, dropout=0.05))
    conv_stacked_lstm.add(LSTM(int(attributes/kernelSize), dropout=0.05))
    # stacked_lstm.add(LSTM(128, return_sequences=True))
#    conv_stacked_lstm.add(LSTM(1200, return_sequences=True, go_backwards=True, dropout=0.15, recurrent_dropout=0.2))
    # stacked_lstm_v2.add(LSTM(600, return_sequences=True,dropout=0.15))
#    conv_stacked_lstm.add(LSTM(1200, dropout=0.15, go_backwards=True))
    # stacked_lstm.add(LSTM(64)))
    conv_stacked_lstm.add(Dropout(0.10))
    conv_stacked_lstm.add(Dense(int(attributes/kernelSize)))
    conv_stacked_lstm.add(LeakyReLU())
    conv_stacked_lstm.add(Dense(1, activation='sigmoid'))

    stacked_lstm_v2 = Sequential()
    '''
    10-27-2023 I am noticing a bigger first lstm layer followed by two subsequent smaller lstm layers (size 16 each) and a dense layer of size 16
    works better when the first layer is larger than the other layers
    It's possible that 148 was a good sweet spot because there are 75 attributes, this leads one lstm node per attribute
    and therefore can trigger it to forget or keep the memory for each 0,1 class.
    I.e., for each attribute, allocate 1 node per class.
    '''
    stacked_lstm_v2.add(LSTM(1200, input_shape=(timeSequences, attributes), return_sequences=True, go_backwards=True, dropout=0.4, recurrent_dropout=0.2))
    # stacked_lstm.add(LSTM(128, return_sequences=True))
    stacked_lstm_v2.add(LSTM(1200, return_sequences=True, go_backwards=True, dropout=0.15, recurrent_dropout=0.2))
    #stacked_lstm_v2.add(LSTM(600, return_sequences=True,dropout=0.15))
    stacked_lstm_v2.add(LSTM(1200,dropout=0.15, go_backwards=True))
    # stacked_lstm.add(LSTM(64)))
    stacked_lstm_v2.add(Dropout(0.10))
    stacked_lstm_v2.add(Dense(1200))
    stacked_lstm_v2.add(LeakyReLU())
    stacked_lstm_v2.add(Dense(1, activation='sigmoid'))

    model_bigger_biggest_lstm_v2_leaky_relu = Sequential()
    model_bigger_biggest_lstm_v2_leaky_relu.add(LSTM(32, input_shape=(timeSequences, attributes)))
    model_bigger_biggest_lstm_v2_leaky_relu.add(Dense(16, activation='leaky_relu'))
    model_bigger_biggest_lstm_v2_leaky_relu.add(Dense(4, activation='leaky_relu'))
    model_bigger_biggest_lstm_v2_leaky_relu.add(Dense(1, activation='sigmoid'))

    model_bigger_biggest_lstm_v2 = Sequential()
    model_bigger_biggest_lstm_v2.add(LSTM(32, input_shape=(timeSequences, attributes)))
    model_bigger_biggest_lstm_v2.add(Dense(16, activation='relu'))
    model_bigger_biggest_lstm_v2.add(Dense(4, activation='relu'))
    model_bigger_biggest_lstm_v2.add(Dense(1, activation='sigmoid'))

    model_bigger_biggest_lstm_v3 = Sequential()
    model_bigger_biggest_lstm_v3.add(LSTM(64, input_shape=(timeSequences, attributes)))
    model_bigger_biggest_lstm_v3.add(Dense(16, activation='relu'))
    model_bigger_biggest_lstm_v3.add(Dense(4, activation='relu'))
    model_bigger_biggest_lstm_v3.add(Dense(1, activation='sigmoid'))

    model_bigger_biggest_lstm_v4 = Sequential()
    model_bigger_biggest_lstm_v4.add(LSTM(56, input_shape=(timeSequences, attributes)))
    model_bigger_biggest_lstm_v4.add(Dense(4, activation='relu'))
    model_bigger_biggest_lstm_v4.add(Dense(1, activation='sigmoid'))

    model_bigger_biggest_lstm_v5 = Sequential()
    model_bigger_biggest_lstm_v5.add(LSTM(24, input_shape=(timeSequences, attributes)))
    model_bigger_biggest_lstm_v5.add(Dense(4, activation='relu'))
    model_bigger_biggest_lstm_v5.add(Dense(1, activation='sigmoid'))

    model_bigger_biggest_lstm_v6 = Sequential()
    model_bigger_biggest_lstm_v6.add(LSTM(128, input_shape=(timeSequences, attributes)))
    model_bigger_biggest_lstm_v6.add(Dense(8, activation='relu'))
    model_bigger_biggest_lstm_v6.add(Dense(8, activation='relu'))
    model_bigger_biggest_lstm_v6.add(Dense(1, activation='sigmoid'))

    model_bigger_biggest_lstm_v7 = Sequential()
    model_bigger_biggest_lstm_v7.add(LSTM(256, input_shape=(timeSequences, attributes)))
    model_bigger_biggest_lstm_v7.add(Dense(8, activation='relu'))
    model_bigger_biggest_lstm_v7.add(Dense(8, activation='relu'))
    model_bigger_biggest_lstm_v7.add(Dense(1, activation='sigmoid'))

    model_bigger_biggest_lstm_v8 = Sequential()
    model_bigger_biggest_lstm_v8.add(LSTM(256, input_shape=(timeSequences, attributes)))
    model_bigger_biggest_lstm_v8.add(Dense(8, activation='relu'))
    model_bigger_biggest_lstm_v8.add(Dense(8, activation='relu'))
    model_bigger_biggest_lstm_v8.add(Dense(8, activation='relu'))
    model_bigger_biggest_lstm_v8.add(Dense(8, activation='relu'))
    model_bigger_biggest_lstm_v8.add(Dense(8, activation='relu'))
    model_bigger_biggest_lstm_v8.add(Dense(8, activation='relu'))
    model_bigger_biggest_lstm_v8.add(Dense(8, activation='leaky_relu'))
    model_bigger_biggest_lstm_v8.add(Dense(1, activation='sigmoid'))

    model_bigger_biggest_lstm_v12_half_bank = Sequential()
    model_bigger_biggest_lstm_v12_half_bank.add(LSTM(256, input_shape=(timeSequences, attributes)))
    model_bigger_biggest_lstm_v12_half_bank.add(Dense(12, activation='relu'))
    model_bigger_biggest_lstm_v12_half_bank.add(Dense(12, activation='relu'))
    model_bigger_biggest_lstm_v12_half_bank.add(Dense(12, activation='relu'))
    model_bigger_biggest_lstm_v12_half_bank.add(Dense(12, activation='relu'))
    model_bigger_biggest_lstm_v12_half_bank.add(Dense(12, activation='relu'))
    model_bigger_biggest_lstm_v12_half_bank.add(Dense(12, activation='leaky_relu'))
    model_bigger_biggest_lstm_v12_half_bank.add(Dense(12, activation='relu'))
    model_bigger_biggest_lstm_v12_half_bank.add(Dense(12, activation='relu'))
    model_bigger_biggest_lstm_v12_half_bank.add(Dense(12, activation='relu'))
    model_bigger_biggest_lstm_v12_half_bank.add(Dense(12, activation='relu'))
    model_bigger_biggest_lstm_v12_half_bank.add(Dense(12, activation='leaky_relu'))
    model_bigger_biggest_lstm_v12_half_bank.add(Dense(1, activation='sigmoid'))

    model_bigger_biggest_lstm_v16 = Sequential()
    model_bigger_biggest_lstm_v16.add(LSTM(256, input_shape=(timeSequences, attributes)))
    model_bigger_biggest_lstm_v16.add(Dense(16, activation='relu'))
    model_bigger_biggest_lstm_v16.add(Dense(16, activation='relu'))
    model_bigger_biggest_lstm_v16.add(Dense(16, activation='relu'))
    model_bigger_biggest_lstm_v16.add(Dense(16, activation='relu'))
    model_bigger_biggest_lstm_v16.add(Dense(16, activation='relu'))
    model_bigger_biggest_lstm_v16.add(Dense(16, activation='relu'))
    model_bigger_biggest_lstm_v16.add(Dense(16, activation='relu'))
    model_bigger_biggest_lstm_v16.add(Dense(16, activation='relu'))
    model_bigger_biggest_lstm_v16.add(Dense(16, activation='relu'))
    model_bigger_biggest_lstm_v16.add(Dense(16, activation='relu'))
    model_bigger_biggest_lstm_v16.add(Dense(16, activation='relu'))
    model_bigger_biggest_lstm_v16.add(Dense(16, activation='relu'))
    model_bigger_biggest_lstm_v16.add(Dense(16, activation='relu'))
    model_bigger_biggest_lstm_v16.add(Dense(16, activation='leaky_relu'))
    model_bigger_biggest_lstm_v16.add(Dense(1, activation='sigmoid'))
    model_bigger_biggest_lstm_v16 = Sequential()

    '''
    Inspired by https://arxiv.org/abs/1406.1078
    and my love for cars,
    Imagine a DOHC engine. For each cylinder, there are two cam shafts per bank of cylinder head. One shaft for intake valves, and the other for exhaust valves.
    Each bank will look like this intake -> cylinder -> exhaust.
    The RNN will look like this: Dense -> LSTM -> Dense
    we can think of each lstm as a 'cyinder', remembering and storing data, dense nodes are used for information traversal.

    '''
    model_stacked_v6 = Sequential()
    model_stacked_v6.add(LSTM(256, input_shape=(timeSequences, attributes), return_sequences=True))
    model_stacked_v6.add(LSTM(256, return_sequences=True))
    model_stacked_v6.add(LSTM(128, return_sequences=True))
    model_stacked_v6.add(LSTM(128, return_sequences=True))

    model_stacked_v6.add(LSTM(64, ))
    model_stacked_v6.add(Dense(64, activation='relu'))
    model_stacked_v6.add(Dense(1, activation='sigmoid'))

    model_bigger_biggest_lstm_v9 = Sequential()
    model_bigger_biggest_lstm_v9.add(LSTM(128, input_shape=(timeSequences, attributes)))
    model_bigger_biggest_lstm_v9.add(Dense(16, activation='relu'))
    model_bigger_biggest_lstm_v9.add(Dense(4, activation='relu'))
    model_bigger_biggest_lstm_v9.add(Dense(1, activation='sigmoid'))

    model_bigger_lstm_v2 = Sequential()
    model_bigger_lstm_v2.add(LSTM(8, input_shape=(timeSequences, attributes)))
    model_bigger_lstm_v2.add(Dense(12, activation='relu'))
    model_bigger_lstm_v2.add(Dense(36, activation='relu'))
    model_bigger_lstm_v2.add(Dense(1, activation='sigmoid'))

    model_double_lstm = Sequential()
    model_double_lstm.add(LSTM(8, input_shape=(timeSequences, attributes), return_sequences=True))
    model_double_lstm.add(LSTM(16))
    model_double_lstm.add(Dense(8, activation='relu'))
    model_double_lstm.add(Dense(4, activation='relu'))
    model_double_lstm.add(Dense(1, activation='sigmoid'))

    model_double_lstm_double_layers = Sequential()
    model_double_lstm_double_layers.add(LSTM(16, input_shape=(timeSequences, attributes), return_sequences=True))
    model_double_lstm_double_layers.add(LSTM(16, return_sequences=True))
    model_double_lstm_double_layers.add(LSTM(8))
    model_double_lstm_double_layers.add(Dense(4, activation='relu'))
    model_double_lstm_double_layers.add(Dense(1, activation='sigmoid'))

    model_double_lstm_double_layers_more_nodes = Sequential()
    model_double_lstm_double_layers_more_nodes.add(
        LSTM(8, input_shape=(timeSequences, attributes), return_sequences=True))
    model_double_lstm_double_layers_more_nodes.add(LSTM(16))
    model_double_lstm_double_layers_more_nodes.add(Dense(12, activation='relu'))
    model_double_lstm_double_layers_more_nodes.add(Dense(36, activation='relu'))
    model_double_lstm_double_layers_more_nodes.add(Dense(8, activation='relu'))
    model_double_lstm_double_layers_more_nodes.add(Dense(1, activation='sigmoid'))

    model_ltsm_straight_to_output = Sequential()
    model_ltsm_straight_to_output.add(LSTM(8, input_shape=(timeSequences, attributes)))
    model_ltsm_straight_to_output.add(Dense(1, activation='sigmoid'))
    # models['transformer_model'] = transformer_model
    #
    #models['stacked_lstm'] = stacked_lstm;

    models['conv_stacked_lstm'] = conv_stacked_lstm
    models['stacked_lstm_v2'] = stacked_lstm_v2;


    models['model_one_layer_ltsm'] = model_one_layer_ltsm

    models['model_simple_ltsm'] = model_simple_ltsm
    """
    models['model_one_layer_ltsm_v2'] = model_one_layer_ltsm_v2;
    models['model_bigger_lstm'] = model_bigger_lstm;
    models['model_bigger_bigger_lstm'] = model_bigger_bigger_lstm
    models['model_bigger_biggest_lstm'] = model_bigger_biggest_lstm
    models['model_bigger_biggest_lstm_v2'] = model_bigger_biggest_lstm_v2
    models['model_bigger_biggest_lstm_v3'] = model_bigger_biggest_lstm_v3
    models['model_bigger_biggest_lstm_v4'] = model_bigger_biggest_lstm_v4

    models['model_bigger_biggest_lstm_v5'] = model_bigger_biggest_lstm_v5
    models['model_bigger_biggest_lstm_v6'] = model_bigger_biggest_lstm_v6
    models['model_bigger_biggest_lstm_v7'] = model_bigger_biggest_lstm_v7
    models['model_bigger_biggest_lstm_v8'] = model_bigger_biggest_lstm_v8
    models['model_bigger_biggest_lstm_v9'] = model_bigger_biggest_lstm_v9
    models['model_bigger_biggest_lstm_v2_leaky_relu'] = model_bigger_biggest_lstm_v2_leaky_relu
    models['model_bigger_biggest_lstm_v12_half_bank'] = model_bigger_biggest_lstm_v12_half_bank
    models['model_bigger_biggest_lstm_v16'] = model_bigger_biggest_lstm_v16
    # Past here, overfitting occurs
    models['model_double_stm'] = model_double_lstm;
    models['model_double_lstm_double_layers'] = model_double_lstm_double_layers
    models['model_ltsm_straight_to_output'] = model_ltsm_straight_to_output
    models['model_double_lstm_double_layers_more_nodes'] = model_double_lstm_double_layers_more_nodes
    #
    # '''Unique Architectures'''
    
    """
    # models['model_bigger_lstm_v2'] = model_bigger_lstm_v2
    models['model_bigger_biggest_lstm_v8'] = model_bigger_biggest_lstm_v8

    models['model_stacked_v6'] = model_stacked_v6
    return models


def getMinMax(data):
    attribute_min_max = {}
    '''
    TODO
    switch to numpy for faster calcs
    '''
    for i in range(len(data)):
        for j in range(len(data[i])):
            for k in range(len(data[i][j])):
                if (k not in attribute_min_max):
                    attribute_min_max[k] = {'min': float('inf'), 'max': float('-inf')}
                else:
                    attribute_min_max[k]['min'] = min(attribute_min_max[k]['min'], data[i][j][k])
                    attribute_min_max[k]['max'] = max(attribute_min_max[k]['max'], data[i][j][k])
    return attribute_min_max;


# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    timeSequences = 2
    numAttributes = 150
    numMetaAttrs = 0
    windowSize = 75
    #TODO, if after the current test run, it moves more towards 50%/50%, lower epochs
    epochs = 50  # 20 epochs is pretty good, will train with 24 next as 3x is a good rule of thumb.
    numFolds = 14;
    shuffle = False
    useLoo = False
    if useLoo:
        print_both("using Leave one out")
    else:
        print_both("Using K Fold")
    callback = tf.keras.callbacks.EarlyStopping(monitor='loss', patience=10, start_from_epoch=15, baseline=0.75, mode='min', restore_best_weights=True)

    yAll = np.array([])
    print_both('epochs: ' + str(epochs))
    print_both('Shuffle on compile: ' + str(shuffle))
    # strategy = tf.distribute.MirroredStrategy()
    # print_both('Number of devices: {}'.format(strategy.num_replicas_in_sync))

    consoleOut = sys.stdout  # assign console output to a variable

 #   allTrainData = convertArffToDataFrame(
  #      "/home/notroot/Desktop/d2lab/gazepoint/train_test_data_output/2023-10-26T15;17;59.575004/trainData_500.0mssec_window_1.arff")
    #Todo
    #More advanced normalization
    #figure out min max for one data sample and apply to all via modulation
    # xAttrMinMax = normalizationByPollingSample(allTrainData, numAttributes)

    #xAll, yAll = convertDataToLTSMFormat(allTrainData, timeSequences)

    # trainData = convertArffToDataFrame("E:\\trainData_2sec_window_1_no_v.arff")
    targetColumn = "correct"
    baseDirForTrainData = "/home/notroot/Desktop/d2lab/gazepoint/train_test_data_output/creat_conf/"

    # models = getModelConfig(timeSequences, (numAttributes * windowSize) + numMetaAttrs)
    models = getModelConfig(timeSequences, numAttributes, windowSize)
    all_models_by_tp_and_tn = {};
    all_models_stats = []
    trainDataParticipants = []
    testDataParticipants = []
    attr_min_max = {}
    bc = 0

    print_both("normalization through files")
    for filename in os.listdir(baseDirForTrainData):
        f = os.path.join(baseDirForTrainData, filename)
        # Skip non trainData files.
        continue;
        if "trainData" not in filename:
            continue
        #if bc > 0:
        #    continue
        bc += 1
        trainData = convertArffToDataFrame(f)

        attr_min_max = normalizationByPollingSample(trainData, numAttributes, numMetaAttrs, attr_min_max)
        '''
        Can't use reshape, must do mannualy
        Take the 2D stretched window and make it to a 3D window represented by
        (samples,windowSize,attributes)
        Also pair the correct answers together.
        '''

    print_both("Done printing normalizations")
    cc = 0
    participants = []
    for filename in os.listdir(baseDirForTrainData):
        f = os.path.join(baseDirForTrainData, filename)
        print_both(filename)
        if "trainData" not in filename and "testData" not in filename:
            continue
        #if cc > 0:
        #    continue
        cc += 1 
        if "trainData" in filename:
            trainData = convertArffToDataFrame(f)
            participants.append(filename)
            x_part, y_part = convertDataToLTSMFormat(trainData, timeSequences, numMetaAttrs)

            print_both('full input shape: ' + str(x_part.shape))
            yAll = np.concatenate((yAll, y_part), axis=0)
            # x_part = normalizeData(x_part, windowSize, numAttributes, numMetaAttrs, attr_min_max)
            trainDataParticipants.append({'x': x_part, 'y': y_part, 'fileName' : filename})
        elif "testData" in filename:
            testData = convertArffToDataFrame(f)
            xTest, yTest = convertDataToLTSMFormat(testData, timeSequences, numMetaAttrs)
            testDataParticipants.append({'x': xTest, 'y': yTest, 'fileName': filename})

    print_both("normalization data attributes (keep handy)")
    print_both(json.dumps(str(attr_min_max)))

    # xTest = normalizeData(xTest, windowSize, numAttributes, numMetaAttrs, attr_min_max)

    # with strategy.scope():
    for model_name, model_uncloned in models.items():
            model = tf.keras.models.clone_model(model_uncloned)
            optimizers = get_optimizers()

            print_both("*****************************************")
            print_both(model_name)

            sys.stdout = outputFile
            model.summary()
            sys.stdout = consoleOut  # set stdout back to console output
            model.summary()

            print_both("*****************************************")
            histories = []
            numPart = 0
            stats_by_participant = {}
            for optimizer in optimizers:
                if type(optimizer) != type(""):
                    unique_model_id = model_name + "-" + str(type(optimizer).__name__) + str(tf.keras.backend.eval(optimizer.lr)).replace(".", ",")
                else:
                    unique_model_id = model_name + "-"
                if hasattr(optimizer, 'beta_1'):
                    unique_model_id += " b1: " + str(optimizer.beta_1)
                if hasattr(optimizer, 'weight_decay'):
                    unique_model_id += " wdecay: " + str(optimizer.weight_decay)
                if hasattr(optimizer, 'use_ema'):
                    unique_model_id += " ema:" + str(optimizer.use_ema)
                print_both("-------------------------------")
                print_both("unique model id: " + unique_model_id)
                if (type(optimizer) != type("")):
                    print_both("optimizer: " + str(optimizer.name) + str(optimizer.learning_rate))
                else:
                    print_both("optimizer: " + optimizer)
                print_both("-------------------------------")
                for i in range(len(trainDataParticipants)):
                    print_both("trainig on file: " + str(trainDataParticipants[i]['fileName']))
                    x_part = trainDataParticipants[i]['x']
                    y_part = trainDataParticipants[i]['y']
                    # Define per-fold score containers <-- these are new
                    acc_per_fold = []
                    loss_per_fold = []
                    # Since we are training on 'profiles' of people, we should always shuffle their data for training.
                    loo = LeavePOut(1)
                    if useLoo:
                        print_both("loo splits: " + str(loo.get_n_splits(x_part)))
                    kfold = StratifiedKFold(n_splits=numFolds,shuffle=False)
                    # x_train, x_val, y_train, y_val = model_selection.train_test_split(x_part, y_part, test_size=0.2,
                    #                                                                   random_state=0, shuffle=True)
                    fold_no = 0
                    if useLoo:
                        split_enumerator = enumerate(loo.split(x_part))
                    else:
                        split_enumerator = kfold.split(x_part, y_part);

                    for train, test in split_enumerator:
                        model.compile(optimizer=optimizer, loss=tf.keras.losses.BinaryCrossentropy(),
                                      metrics=get_metrics_for_model())
                        # todo, we need to separate each participant
                        # the model should train against only the participants train data to
                        # have a representation of that person
                        # then we retrain on the next person, so on and so forth.

                        if not useLoo:
                            weights = get_weight_bias(y_part[train])

                            print_both('class 0 weight: ' + str(weights[0]))
                            print_both('class 1 weight: ' + str(weights[1]))
                        hist = model.fit(
                                x_part[train], 
                                y_part[train], 


                                         # validation_data=(x_val, y_val),
                                         epochs=epochs,
                                         class_weight=None if useLoo else weights,
                                         shuffle=shuffle,
                  #                       batch_size=64,
                  #                         callbacks=[callback]
                                         )
                        #scores = model.evaluate(x_part[test], y_part[test], verbose=0)
                        #print_both(f'Score for fold {fold_no}: {model.metrics_names[0]} of {scores[0]}; {model.metrics_names[1]} of {scores[1] * 100}%')
                        print_both('fold: ' + str(fold_no))
                        #fold_no += 1
                        histories.append(hist)
                #hist_avg = np.average(hist.histroy['val_accuracy'])
                #print_both('avh: ' + str(hist_avg))
                        if (np.average(hist.history['accuracy'][-20:]) < 0.50):
                            if (participants[i] not in stats_by_participant):
                                stats_by_participant[participants[i]] = []

                            stats_by_participant[participants[i]].append({'f': participants[i], 'model_id': model_name, 'accuracy' : np.average(hist.history['accuracy'][-20:])})
                            print_both("Participant reached > 0.8 val_acc: " +participants[i])
                '''
                Done fitting on multiple participants, time for real world data testing
                '''
                for testPInd in range(0,len(testDataParticipants)):
                    testP = testDataParticipants[testPInd]
                    print_both('Testing on : ' + str(testP['fileName']))
                    xTest = testP['x']
                    yTest = testP['y']
                    y_hat = model.predict(xTest)
                    # results = model.evlauate(xVal, yTest)
                    y_hat = [(1.0 if y_pred >= 0.5 else 0.0) for y_pred in y_hat]

                    '''
                    Metrics
                    '''

                    hist_str = ''
                    print_both(str(histories))
                    histories_of_all_cross = []
                    for history in histories:
                        metrics_by_epoch = [];
                        total_histories_of_cross = {}
                        for key in history.history.keys():
                            epochs = len(history.history[key])
                            total_histories_of_cross[key] = [0 for i in range(epochs)]
                            #key: [e1,e2,e3]
                            for i in range(epochs):
                                total_histories_of_cross[key][i] += history.history[key][i]

                            #now calculate avg
                            for i in range(epochs):
                                total_histories_of_cross[key][i] /= epochs
                        #now we have cross -> [key : [e1,e3]/avg,...]
                        histories_of_all_cross.append(total_histories_of_cross)

                    #Don't use
                    #now we can go by key of total_histories_of_all_cross and calculate metrics
                    #avg_history_by_epoch = []
                    #for key in histories[0].history.keys():
                    #     #generate a 0 value for each epoch in the metric
                    #    avg_history_by_epoch[key]=[0 for i in range(len(histories[0].history[key]))]
                    #     #then for each of the cross folds metrics, calculate the averages
                    #    for h in histories:
                          #calculate the average metric value per epoch
                    #        for i in range(h.history[key]):
                    #            avg_history_by_epoch['history'][key][i] += h.history[key][i]
                    #
                    #        for i in range(h.history[key]):
                    #            avg_history_by_epoch['history'][key][i] /= len(h.history[key])
                    #

                    hist_str = ''
                    for key in histories_of_all_cross[0].keys():
                        hist_str += str(key) + " : " + str(sum( sum(his[key]) for his in histories_of_all_cross) / len(histories_of_all_cross)) + "\n"
                    #     #finally, we now have the avg history of each metric per each epoch and per each model


                    print_both(hist_str)

                    conf_matrix = sklearn.metrics.confusion_matrix(yTest, y_hat, labels=[1.0, 0.0])
                    print_both('putting in conf matrix')
                    all_models_by_tp_and_tn[unique_model_id] = conf_matrix
                    print_both(conf_matrix)

                    # Saving breaks the rest of the trianings and corrupts the rest of the configurations!
                    # Only save when using Linux keras 2.14!!!
                model.save(resultDir + "/" + unique_model_id+".h5", save_format='h5')
                    #
                #    all_models_stats.append({
                #        'model_name': model_name, 'optimizer': str(type(optimizer).__name__) if type(optimizer) != type('') else optimizer,
                #        'lr': str(tf.keras.backend.eval(optimizer.lr)) if (type(optimizer) != type('')) else '',
                #        'accuracy': sum( sum(his['accuracy']) for his in histories_of_all_cross) / len(histories_of_all_cross),
                #        'val_accuracy': sum( sum(his['val_accuracy']) for his in histories_of_all_cross) / len(histories_of_all_cross),
                #        'tp %': str(conf_matrix[0][0] / (conf_matrix[0][0] + conf_matrix[0][1])),
                #        'tn %': str(conf_matrix[1][1] / (conf_matrix[1][1] + conf_matrix[1][0]))
                #    })



    sorted_all_models_by_tp_and_tn = OrderedDict(sorted(all_models_by_tp_and_tn.items(), key=lambda k:
    (all_models_by_tp_and_tn.get(k[0])[1][1] / (
                all_models_by_tp_and_tn.get(k[0])[1][0] + all_models_by_tp_and_tn.get(k[0])[1][1]),
     all_models_by_tp_and_tn.get(k[0])[0][0] / (
                 all_models_by_tp_and_tn.get(k[0])[0][0] + all_models_by_tp_and_tn.get(k[0])[0][1])
     )))  # sort by what? true negative accuracy by true positive accuracy.

    for model_id, conf_matrix in reversed(sorted_all_models_by_tp_and_tn.items()):
        print_both(model_id)
        print_both(conf_matrix)
        print_both('tn: %: ' + str(conf_matrix[1][1] / (conf_matrix[1][1] + conf_matrix[1][0])) + ' tp %: ' + str(
            conf_matrix[0][0] / (conf_matrix[0][0] + conf_matrix[0][1])))

    # plot values

    #save python code
    for key in stats_by_participant:
        print("participant: " + key)
        print(str(stats_by_participant[key]))
    keys = all_models_stats[0].keys()
    with open(os.path.join(resultDir, 'modelResults.csv'), 'w', encoding='utf8', newline='') as output_file:
        dict_writer = DictWriter(output_file, keys)
        dict_writer.writeheader()
        dict_writer.writerows(all_models_stats)
# See PyCharm help at https://www.jetbrains.com/help/pycharm/
# os.close(outputFile)
