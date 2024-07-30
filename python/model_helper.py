from typing import List
import tensorflow as tf
import shutil
import matplotlib.pyplot as plt
from scipy.io import arff
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
    """

    :param resultDir:
    :return:
    """
    script_path = os.path.abspath(__file__)
    destination_path = resultDir + "/main.py"
    shutil.copy(script_path, destination_path)
    print(f"Script saved to: {destination_path}")

def convertArffToDataFrame(fileName):
    """
    Convert .ARFF time to dataframe.
    :param fileName:
    :return:
    """
    dataset = arff.loadarff(open(fileName))
    data_list = [list(item) for item in dataset[0]]
    return np.array(data_list, dtype=np.float32)


def plotLines(lines: dict[str, List], resultDir: 'str', unique_model_id: str, show_plot=False):
    """
    Helper function to plot lines
    :param lines: Contains the label of the line and then the line itself.
    :param resultDir: Pathlike string
    :param unique_model_id: Model id to save for the images of these plots to.
    :param show_plot:
    :return:
    """
    for label,line in lines.items():
        plt.plot(range(0, len(line)), line, label=label)

    plt.legend()
    plt.title('TP% & TN% over cross fold of true unseen participants')
    plt.savefig(resultDir + '/' + unique_model_id + '.png')
    if (show_plot):
        plt.show()


def get_weight_bias(y_data):
    """
    Creates a weight value based on the inversion of the y-values. Good for imbalanced classes.
    Note: Only supports Binary classification
    :param y_data:
    :return:
    """
    neg = len([i for i in y_data if i == 0])
    pos = len([i for i in y_data if i == 1])

    total = len(y_data)
    if (neg != 0):
        weight_for_0 = (1 / neg) * (total)
    else:
        weight_for_0 = 0
    if (pos != 0):
        weight_for_1 = (1 / pos) * (total)
    else:
        weight_for_1 = 0
    return {0: weight_for_0, 1: weight_for_1}


def convertDataToLTSMFormat(data, timeSequences):
    """
    Convert 2D data into the a time-series format for LSTM -> into a 3d shape
    :param data:
    :param timeSequences:
    :return:
    """
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

    x = np.array(x)
    y = np.array(y)
    return [x, y]

