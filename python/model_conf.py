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
        tf.keras.metrics.TruePositives(name='tp'),
        tf.keras.metrics.TrueNegatives(name='tn'),
        tf.keras.metrics.Precision(name='precision'),
    ]


def get_optimizers():
    return [
        tf.keras.optimizers.Adagrad(learning_rate=0.008, name='Adagrad'),
        tf.keras.optimizers.SGD(learning_rate=1e-4, momentum=0.9),
        tf.keras.optimizers.Adam(learning_rate=1.4e-3, use_ema=True),
        tf.keras.optimizers.Adam(learning_rate=1.4e-3, beta_1=0.8, use_ema=False, weight_decay=2e-4),
        tf.keras.optimizers.SGD(learning_rate=0.01),
        tf.keras.optimizers.Nadam(learning_rate=1e-3),
        tf.keras.optimizers.Nadam(learning_rate=1.5e-4),
    ]


def transformer_encoder(inputs, head_size, num_heads, ff_dim, dropout=0):
    """
    Transformer encoder for time-series classification. See https://keras.io/examples/timeseries/timeseries_classification_transformer/
    :param inputs:
    :param head_size:
    :param num_heads:
    :param ff_dim:
    :param dropout:
    :return:
    """
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


def build_conv_lstm_transformer_model(input_shape, head_size, num_heads, ff_dim, num_transformer_blocks, mlp_units, dropout=0,
                                      mlp_dropout=0):
    """
    Builds the entire transformer.
    We use Conv LSTM + Time-Series Transformer Encoder to predict a single value.
    :param input_shape:
    :param head_size:
    :param num_heads:
    :param ff_dim:
    :param num_transformer_blocks:
    :param mlp_units:
    :param dropout:
    :param mlp_dropout:
    :return:
    """
    inputs = tf.keras.Input(shape=input_shape)
    x = inputs
    #pair inputs by two for x,y and place into 3 filters for 3 time sequences
    x = layers.Conv1D(3, 2, input_shape=input_shape)(x)
    x = layers.LSTM(100, return_sequences=True)(x)

    for _ in range(num_transformer_blocks):
        x = transformer_encoder(x, head_size, num_heads, ff_dim, dropout)

    x = layers.GlobalAveragePooling1D(data_format="channels_last")(x)  # What does channels_first do?
    for dim in mlp_units:
        x = layers.Dense(dim, activation='relu')(x)  # What does passing x do here?

    outputs = layers.Dense(1, activation='sigmoid')(x)  # 2 here is because we have a binary class.

    return tf.keras.Model(inputs, outputs)


def getModelConfig(timeSequences, attributes):
    """
    Returns model configurations. Modify this method to implement your custom models
    for evaluation.
    :param timeSequences:
    :param attributes:
    :return:
    """
    input_shape = (timeSequences, attributes)
    models = {}
    conv_lstm_transformer = build_conv_lstm_transformer_model(input_shape, head_size=4, num_heads=1, ff_dim=4,
                                                          num_transformer_blocks=2, mlp_units=[20,10], mlp_dropout=0.15,
                                                          dropout=0.1)

    models['conv_lstm_transformer'] = conv_lstm_transformer

    return models
