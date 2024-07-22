import datetime
import json
import numpy as np
import os

os.environ['TF_CPP_MIN_LOG_LEVEL'] = '2'
import shutil
import sklearn.metrics
import sys
import tensorflow as tf
import random
import matplotlib.pyplot as plt
from collections import OrderedDict
from csv import DictWriter
from keras.callbacks import CSVLogger
from scipy.io import arff
from sklearn import model_selection
from sklearn.model_selection import StratifiedKFold, LeavePOut, KFold
from tensorflow.keras import Sequential
from tensorflow.keras import layers
from tensorflow.keras.layers import Dense, LSTM, LeakyReLU, Dropout, MaxPooling1D, Conv1D

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
        tf.keras.optimizers.Adagrad(learning_rate=0.008, name='Adagrad'),
        #     "adam"
        #     tf.keras.optimizers.Adam(learning_rate=1e-4, beta_1=0.9, beta_2=0.98),
        # tf.keras.optimizers.Adam(learning_rate=1e-9, beta_1=0.9, beta_2=0.98),

        tf.keras.optimizers.SGD(learning_rate=1e-4, momentum=0.9),
        # tf.keras.optimizers.SGD(learning_rate=0.008),
        # tf.keras.optimizers.SGD(learning_rate=0.04),
        # tf.keras.optimizers.SGD(learning_rate=0.08),

        # tf.keras.optimizers.Adagrad(learning_rate=0.0013, name='Adagrad'),
        # tf.keras.optimizers.Adagrad(learning_rate=0.0014, name='Adagrad'),
        # tf.keras.optimizers.Adagrad(learning_rate=0.001, name='Adagrad'),

        # tf.keras.optimizers.Adagrad(learning_rate=0.01, name='Adagrad'),
        # tf.keras.optimizers.Adagrad(learning_rate=0.0015, name='Adagrad'),
        # tf.keras.optimizers.Adagrad(learning_rate=0.002, name='Adagrad'),
        tf.keras.optimizers.Adam(learning_rate=1.4e-3, use_ema=True),
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
        # tf.keras.optimizers.Adam(learning_rate=1.35e-4, use_ema=False),  # control
        # tf.keras.optimizers.Adam(learning_rate=1.2e-4, use_ema=False),  # control
        # tf.keras.optimizers.Adam(learning_rate=1.5e-4, use_ema=False),  # control
        # tf.keras.optimizers.Adam(learning_rate=1.6e-4, use_ema=False),  # control
        # tf.keras.optimizers.Adam(learning_rate=1.7e-4, use_ema=False),  # control
        # tf.keras.optimizers.Adam(learning_rate=1.3e-4, use_ema=False),  # control

        # tf.keras.optimizers.Adam(learning_rate=1.45e-3, use_ema=False),
        # tf.keras.optimizers.Adam(learning_rate=1.4e-3, beta_1=0.38, use_ema=False),
        # tf.keras.optimizers.Adam(learning_rate=1.4e-3, beta_1=0.39, use_ema=False),
        # tf.keras.optimizers.Adam(learning_rate=1.4e-3, beta_1=0.35, use_ema=False),
        # tf.keras.optimizers.Adam(learning_rate=1.4e-3, beta_1=0.32, use_ema=False),
        # tf.keras.optimizers.Adam(learning_rate=1.4e-3, beta_1=0.6, use_ema=False),
        #
        tf.keras.optimizers.Adam(learning_rate=1.4e-3, beta_1=0.8, use_ema=False, weight_decay=2e-4),
        # tf.keras.optimizers.Adam(learning_rate=1.4e-3, beta_1=0.95, use_ema=False, weight_decay=2e-4),
        # tf.keras.optimizers.Adam(learning_rate=1.4e-3, beta_1=0.85, use_ema=False, weight_decay=2e-4),
        # tf.keras.optimizers.Adam(learning_rate=1.4e-3, beta_1=0.7, use_ema=False, weight_decay=2e-4),
        # tf.keras.optimizers.Adam(learning_rate=1.4e-3, beta_1=0.6, use_ema=False, weight_decay=2e-4),

        tf.keras.optimizers.SGD(learning_rate=0.01),
        # tf.keras.optimizers.SGD(learning_rate=0.012),
        # tf.keras.optimizers.SGD(learning_rate=0.015),
        # tf.keras.optimizers.Adam(learning_rate=1.1e-3),
        # tf.keras.optimizers.Adam(learning_rate=1.2e-3),
        # tf.keras.optimizers.Adam(learning_rate=1.4e-3),
        # tf.keras.optimizers.Adam(learning_rate=1.6e-3),
        # tf.keras.optimizers.Adam(learning_rate=1.7e-3),
        # tf.keras.optimizers.Adam(learning_rate=1.9e-4),

        # tf.keras.optimizers.Adam(learning_rate=1e-1),
        # tf.keras.optimizers.Adam(learning_rate=2e-3),
        # tf.keras.optimizers.Adam(learning_rate=1e-2),
        tf.keras.optimizers.Nadam(learning_rate=1e-3),
        # tf.keras.optimizers.Nadam(learning_rate=1.5e-4),
        tf.keras.optimizers.Nadam(learning_rate=1.5e-4),
        # tf.keras.optimizers.Nadam(learning_rate=2e-3),
        # tf.keras.optimizers.Nadam(learning_rate=1e-2),
        # tf.keras.optimizers.Nadam(learning_rate=1e-1),

        # tf.keras.optimizers.RMSprop
    ]


def transformer_encoder(inputs, head_size, num_heads, ff_dim, dropout=0):
    # Norm and Attention
    x = layers.BatchNormalization(epsilon=1e-3)(inputs)  # What does passing inputs do to x?
    x = layers.MultiHeadAttention(key_dim=head_size, num_heads=num_heads)(x,x)  # x,x i.e;. key, dim. (essentially output of layer norm is passed in as two separate inputs)
    x = layers.Dropout(dropout)(x)


    res = layers.Add()([x, inputs]);
    # feed foward
    x = layers.BatchNormalization(epsilon=1e-3)(res)
    x = layers.Conv1D(filters=ff_dim, kernel_size=1, activation='relu')(x)  # Might need to put the activation layer as separate var for d4j

    x = layers.Dropout(dropout)(x)
    x = layers.Conv1D(filters=inputs.shape[-1], kernel_size=1)(x)
    return layers.Add()([x, res])


def build_transformer_model(input_shape, head_size, num_heads, ff_dim, num_transformer_blocks, mlp_units, dropout=0,
                            mlp_dropout=0):
    inputs = tf.keras.Input(shape=input_shape)
    x = inputs
    #pair inputs by two for x,y and place into 3 filters for 3 time sequences
    x = layers.Conv1D(3, 2, input_shape=input_shape)(x)
    #WHERE's THE POOLING?
    x = layers.LSTM(100, return_sequences=True)(x)
    for _ in range(num_transformer_blocks):
        x = transformer_encoder(x, head_size, num_heads, ff_dim, dropout)

    x = layers.GlobalAveragePooling1D(data_format="channels_last")(x)  # What does channels_first do?
    for dim in mlp_units:
        x = layers.Dense(dim, activation='relu')(x)  # What does passing x do here?

    outputs = layers.Dense(1, activation='sigmoid')(x)  # 2 here is because we have a binary class.

    return tf.keras.Model(inputs, outputs)


def getModelConfig(timeSequences, attributes, windowSize):
    input_shape = (timeSequences, attributes)
    models = {}
    '''Bigger moddels are showing higher returns for transformers. Continue running bigger transformers'''
    transformer_model = build_transformer_model(input_shape, head_size=4, num_heads=1, ff_dim=4,
                                                num_transformer_blocks=2, mlp_units=[20,10], mlp_dropout=0.15,
                                                dropout=0.1)

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
    kernelSize = 3  # filters is the num windows, and 2 b/c (x,y)
    filterSize = int(windowSize/2)

    conv_stacked_lstm.add(
        Conv1D(3, kernelSize, input_shape=(timeSequences, attributes))
    )  # filter size of 25 to split the window into three frames.

    conv_stacked_lstm.add(
        MaxPooling1D(pool_size=1)
    )

    conv_stacked_lstm.add(LSTM(100, dropout=0.05))
    # stacked_lstm.add(LSTM(128, return_sequences=True))
    #    conv_stacked_lstm.add(LSTM(1200, return_sequences=True, go_backwards=True, dropout=0.15, recurrent_dropout=0.2))
    # stacked_lstm_v2.add(LSTM(600, return_sequences=True,dropout=0.15))
    #    conv_stacked_lstm.add(LSTM(1200, dropout=0.15, go_backwards=True))
    # stacked_lstm.add(LSTM(64)))
    conv_stacked_lstm.add(Dense(500))
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
    stacked_lstm_v2.add(
        LSTM(1200, input_shape=(timeSequences, attributes), return_sequences=True, go_backwards=True, dropout=0.4,
             recurrent_dropout=0.2))
    # stacked_lstm.add(LSTM(128, return_sequences=True))
    stacked_lstm_v2.add(LSTM(1200, return_sequences=True, go_backwards=True, dropout=0.15, recurrent_dropout=0.2))
    # stacked_lstm_v2.add(LSTM(600, return_sequences=True,dropout=0.15))
    stacked_lstm_v2.add(LSTM(1200, dropout=0.15, go_backwards=True))
    # stacked_lstm.add(LSTM(64)))
    stacked_lstm_v2.add(Dropout(0.10))
    stacked_lstm_v2.add(Dense(1200))
    stacked_lstm_v2.add(LeakyReLU())
    stacked_lstm_v2.add(Dense(1, activation='sigmoid'))



    models['transformer_model'] = transformer_model
    models['conv_stacked_lstm'] = conv_stacked_lstm
    # models['stacked_lstm'] = stacked_lstm;
    # models['conv_stacked_lstm'] = conv_stacked_lstm

    return models
