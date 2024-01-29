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

# https://github.com/timeseriesAI/tsai
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

# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    pass
# See PyCharm help at https://www.jetbrains.com/help/pycharm/
# os.close(outputFile)
