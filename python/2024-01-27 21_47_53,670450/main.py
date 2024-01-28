import tensorflow as tf
import shutil
import sklearn.metrics
import sys
import random
import matplotlib.pyplot as plt
from collections import OrderedDict
from csv import DictWriter
from keras.callbacks import CSVLogger
from scipy.io import arff
from sklearn import model_selection
import os
import numpy as np

def clear_model(model):
    """
    Resets states and clears model.
    :param model:
    :return:
    """
    model.reset_states()
    tf.keras.backend.clear_session()
    tf.compat.v1.reset_default_graph()
    del model


def savePythonFile(resultDir):
    script_path = os.path.abspath(__file__)
    destination_path = resultDir + "/main.py"
    shutil.copy(script_path, destination_path)
    print(f"Script saved to: {destination_path}")

'''
Returns Arff file to a dataframe for training
'''
def convertArffToDataFrame(fileName):
    dataset = arff.loadarff(open(fileName))
    data_list = [list(item) for item in dataset[0]]
    return np.array(data_list, dtype=np.float32)


def plotLines(lines, resultDir, unique_model_id, show_plot=False):
    for label,line in lines.items():
        plt.plot(range(0, len(line)), line, label=label)

    plt.legend()
    plt.title('TP% & TN% over cross fold of true unseen participants')
    plt.savefig(resultDir + '/' + unique_model_id + '.png')
    if (show_plot):
        plt.show()


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


def convertDataToLTSMFormat(data, timeSequences, numMetaAttrs=0):
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

