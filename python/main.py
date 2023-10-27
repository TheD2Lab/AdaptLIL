import json
import os, shutil
from collections import OrderedDict

import sklearn.metrics
from keras.layers import RepeatVector, TimeDistributed
from sklearn import model_selection
import tensorflow as tf

from keras.callbacks import CSVLogger
from sklearn.preprocessing import StandardScaler
from tensorflow.keras import Sequential
from tensorflow.keras import layers
from tensorflow.keras.layers import Dense, LSTM
from scipy.io import arff
import numpy as np
import datetime
import sys
import csv
resultDir = str(datetime.datetime.now()).replace(":", "_").replace(".", ",")
os.mkdir(resultDir)

outputFile = open(os.path.join(resultDir, "output.txt"), 'wt')

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


def normalizeData(data):
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

    #get min max
    attribute_min_max = {}
    for i in range(len(data)):
        for j in range(len(data[i])):
            for k in range(len(data[i][j])):
                if (k not in attribute_min_max):
                    attribute_min_max[k] = {'min': float('inf'), 'max': float('-inf') }
                else:
                    attribute_min_max[k]['min'] = min(attribute_min_max[k]['min'], data[i][j][k])
                    attribute_min_max[k]['max'] = max(attribute_min_max[k]['max'], data[i][j][k])
    #normalize and return
    for i in range(len(data)):
        for j in range(len(data[i])):
            for k in range(len(data[i][j])):
                data[i][j][k] = (data[i][j][k] - attribute_min_max[k]['min']) / (attribute_min_max[k]['max'] - attribute_min_max[k]['min'])
    return data
def convertDataToLTSMFormat(data,timeSequences):
    x = []
    y = []
    ts_index=0
    window=[]
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
    return [x,y]

def print_both(*args):
    temp = sys.stdout #assign console output to a variable
    print (' '.join([str(arg) for arg in args]))
    sys.stdout = outputFile
    print(' '.join([str(arg) for arg in args]))
    sys.stdout = temp #set stdout back to console output

def calc_conf_matrix_rates(conf_matrix):
    true_positive = conf_matrix[0][0]/(sum(conf_matrix[0]))
    true_negative = conf_matrix[1][1]/(sum(conf_matrix[1]))

    return {
        'true_positive' : true_positive,
        'true_negative': true_negative
    }

def get_weight_bias(y_data):
    neg = len([i for i in y_data if i == 0])
    # print(neg)
    pos = len([i for i in y_data if i == 1])
    # print(pos)
    total = len(y_data)
    weight_for_0 = (1 / neg) * (total / 2.0)
    weight_for_1 = (1 / pos) * (total / 2.0) # TODO, pay more attention to this weight distrib.
    return {0: weight_for_0, 1: weight_for_1}
def get_metrics_for_model():
    return [
        # tf.keras.metrics.BinaryCrossentropy(name='cross entropy'),  # same as model's loss
        # tf.keras.metrics.MeanSquaredError(name='Brier score'),
        tf.keras.metrics.TruePositives(name='tp'),
        # tf.keras.metrics.FalsePositives(name='fp'),
        tf.keras.metrics.TrueNegatives(name='tn'),
        # tf.keras.metrics.FalseNegatives(name='fn'),
        tf.keras.metrics.BinaryAccuracy(name='accuracy'),
        tf.keras.metrics.Precision(name='precision'),
        # tf.keras.metrics.Recall(name='recall'),
        # tf.keras.metrics.AUC(name='auc'),
        # tf.keras.metrics.AUC(name='prc', curve='PR'),  # precision-recall curve

    ]
def get_optimizers():
    return [
        # tf.keras.optimizers.Adagrad(learning_rate=0.008, name='Adagrad'),
        tf.keras.optimizers.Adam(learning_rate=1e-3),
        # tf.keras.optimizers.SGD(learning_rate=1e-4, momentum=0.9),
        # tf.keras.optimizers.SGD(learning_rate=0.008),
        # tf.keras.optimizers.SGD(learning_rate=0.04),
        # tf.keras.optimizers.SGD(learning_rate=0.08),

        # tf.keras.optimizers.Adagrad(learning_rate=0.0013, name='Adagrad'),
        # tf.keras.optimizers.Adagrad(learning_rate=0.0014, name='Adagrad'),
        tf.keras.optimizers.Adagrad(learning_rate=0.001, name='Adagrad'),

        tf.keras.optimizers.Adagrad(learning_rate=0.01, name='Adagrad'),
        tf.keras.optimizers.Adagrad(learning_rate=0.0015, name='Adagrad'),
        # tf.keras.optimizers.Adagrad(learning_rate=0.002, name='Adagrad'),
        # tf.keras.optimizers.SGD(learning_rate=0.01),
        # tf.keras.optimizers.SGD(learning_rate=0.012),
        # tf.keras.optimizers.SGD(learning_rate=0.015),

        tf.keras.optimizers.Adam(learning_rate=1.2e-3),
        # tf.keras.optimizers.Adam(learning_rate=1.4e-3),
        tf.keras.optimizers.Adam(learning_rate=1.8e-3),

        # tf.keras.optimizers.Adam(learning_rate=2e-3),
        tf.keras.optimizers.Adam(learning_rate=1e-2),
        tf.keras.optimizers.Nadam(learning_rate=1e-3),
        # tf.keras.optimizers.Nadam(learning_rate=1.5e-3),
        # tf.keras.optimizers.Nadam(learning_rate=2e-3),
        tf.keras.optimizers.Nadam(learning_rate=1e-2),
        tf.keras.optimizers.Nadam(learning_rate=1e-1),

        # tf.keras.optimizers.RMSprop
    ]

def getModelConfig(timeSequences, attributes):
    models = {}
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
    '''
    stacked_lstm.add(LSTM(148, input_shape=(timeSequences, attributes), return_sequences=True))
    # stacked_lstm.add(LSTM(128, return_sequences=True))
    stacked_lstm.add(LSTM(16, return_sequences=True))
    stacked_lstm.add(LSTM(16))
    # stacked_lstm.add(LSTM(64)))
    stacked_lstm.add(Dense(16, activation='leaky_relu'))
    stacked_lstm.add(Dense(1, activation='sigmoid'))

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
    model_double_lstm_double_layers_more_nodes.add(LSTM(8, input_shape=(timeSequences, attributes), return_sequences=True))
    model_double_lstm_double_layers_more_nodes.add(LSTM(16))
    model_double_lstm_double_layers_more_nodes.add(Dense(12, activation='relu'))
    model_double_lstm_double_layers_more_nodes.add(Dense(36, activation='relu'))
    model_double_lstm_double_layers_more_nodes.add(Dense(8, activation='relu'))
    model_double_lstm_double_layers_more_nodes.add(Dense(1, activation='sigmoid'))

    model_ltsm_straight_to_output = Sequential()
    model_ltsm_straight_to_output.add(LSTM(8, input_shape=(timeSequences, attributes)))
    model_ltsm_straight_to_output.add(Dense(1, activation='sigmoid'))

    models['stacked_lstm'] = stacked_lstm;
    models['model_simple_ltsm'] = model_simple_ltsm
    models['model_one_layer_ltsm'] = model_one_layer_ltsm
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
    models['model_bigger_lstm_v2'] = model_bigger_lstm_v2
    models['model_ltsm_straight_to_output'] = model_ltsm_straight_to_output
    models['model_double_lstm_double_layers_more_nodes'] = model_double_lstm_double_layers_more_nodes

    '''Unique Architectures'''
    models['model_stacked_v6'] = model_stacked_v6
    return models

# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    timeSequences=4
    numAttributes = 8
    windowSize = 75
    epochs=100    #20 epochs is pretty good, will train with 24 next as 3x is a good rule of thumb.
    shuffle=True
    consoleOut = sys.stdout  # assign console output to a variable

    trainData = convertArffToDataFrame("/home/notroot/Desktop/d2lab/gazepoint/train_test_data_output/2023-10-26T15;17;59.575004/trainData_500.0mssec_window_1.arff")
    # trainData = convertArffToDataFrame("E:\\trainData_2sec_window_1_no_v.arff")
    targetColumn = "correct"

    '''
    Can't use reshape, must do mannualy
    Take the 2D stretched window and make it to a 3D window represented by
    (samples,windowSize,attributes)
    Also pair the correct answers together.
    '''
    x,y = convertDataToLTSMFormat(trainData,timeSequences)
    x = normalizeData(x)
    print_both(x.shape)
    print_both(y.reshape(-1).flatten().shape)
    print_both('epochs: ' + str(epochs))
    print_both('Shuffle: ' + str(shuffle))
        #how do we add here?
        #split the whole row by 300


    print_both(y)
    #split ^^ 80/20, preserve order
    x_train, x_test, y_train, y_test = model_selection.train_test_split(x, y, test_size=0.2, random_state=0, shuffle=False)
    print_both(x_train.shape)
    validationData = convertArffToDataFrame("/home/notroot/Desktop/d2lab/gazepoint/train_test_data_output/2023-10-26T15;17;59.575004/testData_500.0msec_window_1.arff")
    # validationData = convertArffToDataFrame("E:\\testData_2sec_window_1_no_v.arff")
    xVal, yVal = convertDataToLTSMFormat(validationData, timeSequences)
    xVal = normalizeData(xVal)
    models = getModelConfig(timeSequences,numAttributes*windowSize)
    '''
    Weight biasing
    25% are only wrong, that leads to data training bias.
    '''
    weights = get_weight_bias(y_train)

    print_both('weight0: ' + str(weights[0]))
    print_both('weight1: ' + str(weights[1]))
    all_models_by_tp_and_tn = {};
    all_models_stats = []

    for model_name,model_uncloned in models.items():
        model = tf.keras.models.clone_model(model_uncloned)
        optimizers = get_optimizers()

        print_both("*****************************************")
        print_both(model_name)

        sys.stdout = outputFile
        model.summary()
        sys.stdout = consoleOut  # set stdout back to console output
        model.summary()

        print_both("*****************************************")
        for optimizer in optimizers:
            try:
                unique_model_id = model_name + "-" + str(type(optimizer).__name__) + str(tf.keras.backend.eval(optimizer.lr)).replace(".", ",")

                print_both("-------------------------------")
                print_both("unique model id: " + unique_model_id)
                print_both("optimizer: " + str(optimizer.name) + str(optimizer.learning_rate))
                print_both("-------------------------------")

                model.compile(optimizer=optimizer, loss=tf.keras.losses.BinaryCrossentropy(),metrics=get_metrics_for_model())
                #todo, we need to separate each participant
                #the model should train against only the participants train data to
                #have a representation of that person
                #then we retrain on the next person, so on and so forth.
                hist = model.fit(x_train, y_train,
                                 validation_data=(x_test, y_test),
                                 epochs=epochs,
                                 # class_weight=weights,
                                 shuffle=shuffle,
                                 )
                hist_str = ''
                for key in hist.history.keys():
                    hist_str += str(key) + " : " + str(hist.history[key]) + "\n"
                print_both(hist_str)
                y_hat = model.predict(xVal)
                #results = model.evlauate(xVal, yVal)
                y_hat = [(1.0 if y_pred >= 0.5 else 0.0) for y_pred in y_hat]

                '''
                Metrics
                '''
                conf_matrix = sklearn.metrics.confusion_matrix(yVal, y_hat, labels=[1.0,0.0])
                all_models_by_tp_and_tn[unique_model_id] = conf_matrix
                print_both(conf_matrix)

                all_models_stats.append({
                    'model_name': model_name, 'optimizer' : str(type(optimizer).__name__), 'lr': str(tf.keras.backend.eval(optimizer.lr)),
                    'accuracy': hist.history['accuracy'],
                    'val_accuracy': hist.history['val_accuracy'],
                    'tp %': str(conf_matrix[0][0]/(conf_matrix[0][0]+conf_matrix[0][1])),
                    'tn %': str(conf_matrix[1][1]/(conf_matrix[1][1]+conf_matrix[1][0]))
                })
                #Saving breaks the rest of the trianings and corrupts the rest of the configurations!
                #Only save when using Linux keras 2.14!!!
                # model.save(resultDir+"\\"+unique_model_id)

            except Exception as e:
                print(e)
                pass;

    sorted_all_models_by_tp_and_tn = OrderedDict(sorted(all_models_by_tp_and_tn.items(), key=lambda k:
    (all_models_by_tp_and_tn.get(k[0])[1][1]/ (all_models_by_tp_and_tn.get(k[0])[1][0] + all_models_by_tp_and_tn.get(k[0])[1][1]),
     all_models_by_tp_and_tn.get(k[0])[0][0]/ (all_models_by_tp_and_tn.get(k[0])[0][0] + all_models_by_tp_and_tn.get(k[0])[0][1])
     ))) #sort by what? true negative accuracy by true positive accuracy.
    for model_id, conf_matrix in reversed(sorted_all_models_by_tp_and_tn.items()):
        print_both(model_id)
        print_both(conf_matrix)
        print_both('tn: %: ' + str(conf_matrix[1][1]/(conf_matrix[1][1]+conf_matrix[1][0])) + ' tp %: ' + str(conf_matrix[0][0]/(conf_matrix[0][0]+conf_matrix[0][1])))
    keys = all_models_stats[0].keys()

    with open(os.path.join(resultDir, 'modelResults.csv'),  'w', encoding='utf8', newline='') as output_file:
        dict_writer = csv.DictWriter(output_file, keys)
        dict_writer.writeheader()
        dict_writer.writerows(all_models_stats)
# See PyCharm help at https://www.jetbrains.com/help/pycharm/
# os.close(outputFile)

