import json
import os, shutil
from collections import OrderedDict

import sklearn.metrics
from sklearn import model_selection
import tensorflow as tf

from keras.callbacks import CSVLogger
from tensorflow.keras import Sequential
from tensorflow.keras import layers
from tensorflow.keras.layers import Dense, LSTM
from scipy.io import arff
import numpy as np
import datetime
import sys

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



def print_hi(name):
    # Use a breakpoint in the code line below to debug your script.
    print(f'Hi, {name}')  # Press Ctrl+F8 to toggle the breakpoint.

def getModelConfig():
    models = {}
    model_simple_ltsm = Sequential()
    model_simple_ltsm.add(LSTM(4, input_shape=(300,8)))
    model_simple_ltsm.add(Dense(8, activation='relu'))
    model_simple_ltsm.add(Dense(16, activation='relu'))
    model_simple_ltsm.add(Dense(4, activation='relu'))
    model_simple_ltsm.add(Dense(1, activation='sigmoid'))

    model_one_layer_ltsm = Sequential()
    model_one_layer_ltsm.add(LSTM(4, input_shape=(300,8)))
    model_one_layer_ltsm.add(Dense(8, activation='relu'))
    model_one_layer_ltsm.add(Dense(1, activation='sigmoid'))

    model_one_layer_ltsm_smaller = Sequential()
    model_one_layer_ltsm_smaller.add(LSTM(4, input_shape=(300,8)))
    model_one_layer_ltsm_smaller.add(Dense(8, activation='relu'))
    model_one_layer_ltsm_smaller.add(Dense(1, activation='sigmoid'))

    #v2 has 4 more nodes in the intermediate dense layer
    model_one_layer_ltsm_v2 = Sequential()
    model_one_layer_ltsm_v2.add(LSTM(4, input_shape=(300,8)))
    model_one_layer_ltsm_v2.add(Dense(12, activation='relu'))
    model_one_layer_ltsm_v2.add(Dense(1, activation='sigmoid'))

    model_bigger_lstm = Sequential()
    model_bigger_lstm.add(LSTM(8, input_shape=(300, 8)))
    model_bigger_lstm.add(Dense(8, activation='relu'))
    model_bigger_lstm.add(Dense(2, activation='relu'))
    model_bigger_lstm.add(Dense(1, activation='sigmoid'))

    model_bigger_bigger_lstm = Sequential()
    model_bigger_bigger_lstm.add(LSTM(12, input_shape=(300, 8)))
    model_bigger_bigger_lstm.add(Dense(8, activation='relu'))
    model_bigger_bigger_lstm.add(Dense(1, activation='sigmoid'))

    model_bigger_biggest_lstm = Sequential()
    model_bigger_biggest_lstm.add(LSTM(16, input_shape=(300, 8)))
    model_bigger_biggest_lstm.add(Dense(4, activation='relu'))
    model_bigger_biggest_lstm.add(Dense(1, activation='sigmoid'))

    model_bigger_biggest_lstm_v2_leaky_relu = Sequential()
    model_bigger_biggest_lstm_v2_leaky_relu.add(LSTM(32, input_shape=(300, 8)))
    model_bigger_biggest_lstm_v2_leaky_relu.add(Dense(16, activation='leaky_relu'))
    model_bigger_biggest_lstm_v2_leaky_relu.add(Dense(4, activation='leaky_relu'))
    model_bigger_biggest_lstm_v2_leaky_relu.add(Dense(1, activation='sigmoid'))

    model_bigger_biggest_lstm_v2 = Sequential()
    model_bigger_biggest_lstm_v2.add(LSTM(32, input_shape=(300, 8)))
    model_bigger_biggest_lstm_v2.add(Dense(16, activation='relu'))
    model_bigger_biggest_lstm_v2.add(Dense(4, activation='relu'))
    model_bigger_biggest_lstm_v2.add(Dense(1, activation='sigmoid'))

    model_bigger_biggest_lstm_v3 = Sequential()
    model_bigger_biggest_lstm_v3.add(LSTM(64, input_shape=(300, 8)))
    model_bigger_biggest_lstm_v3.add(Dense(16, activation='relu'))
    model_bigger_biggest_lstm_v3.add(Dense(4, activation='relu'))
    model_bigger_biggest_lstm_v3.add(Dense(1, activation='sigmoid'))

    model_bigger_biggest_lstm_v4 = Sequential()
    model_bigger_biggest_lstm_v4.add(LSTM(56, input_shape=(300, 8)))
    model_bigger_biggest_lstm_v4.add(Dense(4, activation='relu'))
    model_bigger_biggest_lstm_v4.add(Dense(1, activation='sigmoid'))

    model_bigger_biggest_lstm_v5 = Sequential()
    model_bigger_biggest_lstm_v5.add(LSTM(24, input_shape=(300, 8)))
    model_bigger_biggest_lstm_v5.add(Dense(4, activation='relu'))
    model_bigger_biggest_lstm_v5.add(Dense(1, activation='sigmoid'))

    model_bigger_biggest_lstm_v6 = Sequential()
    model_bigger_biggest_lstm_v6.add(LSTM(128, input_shape=(300, 8)))
    model_bigger_biggest_lstm_v6.add(Dense(8, activation='relu'))
    model_bigger_biggest_lstm_v6.add(Dense(8, activation='relu'))
    model_bigger_biggest_lstm_v6.add(Dense(1, activation='sigmoid'))

    model_bigger_biggest_lstm_v7 = Sequential()
    model_bigger_biggest_lstm_v7.add(LSTM(256, input_shape=(300, 8)))
    model_bigger_biggest_lstm_v7.add(Dense(8, activation='relu'))
    model_bigger_biggest_lstm_v7.add(Dense(8, activation='relu'))
    model_bigger_biggest_lstm_v7.add(Dense(1, activation='sigmoid'))

    model_bigger_biggest_lstm_v8 = Sequential()
    model_bigger_biggest_lstm_v8.add(LSTM(256, input_shape=(300, 8)))
    model_bigger_biggest_lstm_v8.add(Dense(8, activation='relu'))
    model_bigger_biggest_lstm_v8.add(Dense(8, activation='relu'))
    model_bigger_biggest_lstm_v8.add(Dense(8, activation='relu'))
    model_bigger_biggest_lstm_v8.add(Dense(8, activation='relu'))
    model_bigger_biggest_lstm_v8.add(Dense(8, activation='relu'))
    model_bigger_biggest_lstm_v8.add(Dense(8, activation='relu'))
    model_bigger_biggest_lstm_v8.add(Dense(8, activation='leaky_relu'))
    model_bigger_biggest_lstm_v8.add(Dense(1, activation='sigmoid'))


    model_bigger_biggest_lstm_v9 = Sequential()
    model_bigger_biggest_lstm_v9.add(LSTM(128, input_shape=(300, 8)))
    model_bigger_biggest_lstm_v9.add(Dense(16, activation='relu'))
    model_bigger_biggest_lstm_v9.add(Dense(4, activation='relu'))
    model_bigger_biggest_lstm_v9.add(Dense(1, activation='sigmoid'))


    model_bigger_lstm_v2 = Sequential()
    model_bigger_lstm_v2.add(LSTM(8, input_shape=(300, 8)))
    model_bigger_lstm_v2.add(Dense(12, activation='relu'))
    model_bigger_lstm_v2.add(Dense(36, activation='relu'))
    model_bigger_lstm_v2.add(Dense(1, activation='sigmoid'))

    model_double_lstm = Sequential()
    model_double_lstm.add(LSTM(8, input_shape=(300,8), return_sequences=True))
    model_double_lstm.add(LSTM(16))
    model_double_lstm.add(Dense(8, activation='relu'))
    model_double_lstm.add(Dense(4, activation='relu'))
    model_double_lstm.add(Dense(1, activation='sigmoid'))

    model_double_lstm_double_layers = Sequential()
    model_double_lstm_double_layers.add(LSTM(16, input_shape=(300,8), return_sequences=True))
    model_double_lstm_double_layers.add(LSTM(16, return_sequences=True))
    model_double_lstm_double_layers.add(LSTM(8))
    model_double_lstm_double_layers.add(Dense(4, activation='relu'))
    model_double_lstm_double_layers.add(Dense(1, activation='sigmoid'))

    model_double_lstm_double_layers_more_nodes = Sequential()
    model_double_lstm_double_layers_more_nodes.add(LSTM(8, input_shape=(300,8), return_sequences=True))
    model_double_lstm_double_layers_more_nodes.add(LSTM(16))
    model_double_lstm_double_layers_more_nodes.add(Dense(12, activation='relu'))
    model_double_lstm_double_layers_more_nodes.add(Dense(36, activation='relu'))
    model_double_lstm_double_layers_more_nodes.add(Dense(8, activation='relu'))
    model_double_lstm_double_layers_more_nodes.add(Dense(1, activation='sigmoid'))

    model_ltsm_straight_to_output = Sequential()
    model_ltsm_straight_to_output.add(LSTM(8, input_shape=(300,8)))
    model_ltsm_straight_to_output.add(Dense(1, activation='sigmoid'))

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
    #Past here, overfitting occurs
    # models['model_double_stm'] = model_double_lstm;
    models['model_double_lstm_double_layers'] = model_double_lstm_double_layers
    models['model_bigger_lstm_v2'] = model_bigger_lstm_v2
    models['model_ltsm_straight_to_output'] = model_ltsm_straight_to_output
    # models['model_double_lstm_double_layers_more_nodes'] = model_double_lstm_double_layers_more_nodes

    return models

def convertDataToLTSMFormat(data,windowSize):
    x = []
    y = []
    for i in data:
        correct = i[-1]
        reshaped = np.array(i[:-1], dtype=np.float32)

        split_data = np.array(np.array_split(np.array(reshaped).flatten(), windowSize, axis=0))
        x.append(split_data)
        # for j in range(windowSize):

        y.append(correct)

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
    weight_for_0 = (1 / neg) * (total / 2.0)  # TODO, pay more attention to this sample.
    weight_for_1 = (1 / pos) * (total / 2.0)
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
        tf.keras.optimizers.Adagrad(learning_rate=0.008, name='Adagrad'),
        # tf.keras.optimizers.SGD(learning_rate=0.001),
        # tf.keras.optimizers.SGD(learning_rate=0.008),
        # tf.keras.optimizers.SGD(learning_rate=0.04),
        # tf.keras.optimizers.SGD(learning_rate=0.08),
        tf.keras.optimizers.Adagrad(learning_rate=0.001, name='Adagrad'),
        # tf.keras.optimizers.Adagrad(learning_rate=0.0015, name='Adagrad'),
        tf.keras.optimizers.Adagrad(learning_rate=0.002, name='Adagrad'),
        # tf.keras.optimizers.SGD(learning_rate=0.01),
        # tf.keras.optimizers.SGD(learning_rate=0.012),
        # tf.keras.optimizers.SGD(learning_rate=0.015),
        tf.keras.optimizers.Adam(learning_rate=1e-3),
        tf.keras.optimizers.Adam(learning_rate=1.2e-3),
        tf.keras.optimizers.Adam(learning_rate=1.4e-3),
        tf.keras.optimizers.Adam(learning_rate=1.8e-3),
        tf.keras.optimizers.Adam(learning_rate=2e-3),
        tf.keras.optimizers.Adam(learning_rate=1e-2),
        tf.keras.optimizers.Nadam(learning_rate=1e-3),
        tf.keras.optimizers.Nadam(learning_rate=1.5e-3),
        tf.keras.optimizers.Nadam(learning_rate=2e-3),
        tf.keras.optimizers.Nadam(learning_rate=1e-2),
        tf.keras.optimizers.Nadam(learning_rate=1e-1),

        # tf.keras.optimizers.RMSprop
    ]
# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    windowSize=300
    epochs=20    #20 epochs is pretty good, will train with 24 next as 3x is a good rule of thumb.
    shuffle=False
    consoleOut = sys.stdout  # assign console output to a variable

    trainData = convertArffToDataFrame("C:\\Users\\nickj\\Downloads\\gazepoint-data-analysis-master\\train_test_data_output\\2023-10-23T23;36;37.310313800 anat,conf\\trainData_2000.0mssec_window_1.arff")
    # trainData = convertArffToDataFrame("E:\\trainData_2sec_window_1_no_v.arff")
    targetColumn = "correct"

    '''
    Can't use reshape, must do mannualy
    Take the 2D stretched window and make it to a 3D window represented by
    (samples,windowSize,attributes)
    Also pair the correct answers together.
    '''
    x,y = convertDataToLTSMFormat(trainData,windowSize)
    print_both(x.shape)
    print_both(y.reshape(-1).flatten().shape)
    print_both('epochs: ' + str(epochs))
    print_both('Shuffle: ' + str(shuffle))
        #how do we add here?
        #split the whole row by 300


    print_both(y)
    #split ^^ 80/20, preserve order
    x_train, x_test, y_train, y_test = model_selection.train_test_split(x, y, test_size=0.2, random_state=0, shuffle=shuffle)
    print_both(x_train.shape)
    validationData = convertArffToDataFrame("C:\\Users\\nickj\\Downloads\\gazepoint-data-analysis-master\\train_test_data_output\\2023-10-23T23;36;37.310313800 anat,conf\\testData_2000.0msec_window_1.arff")
    # validationData = convertArffToDataFrame("E:\\testData_2sec_window_1_no_v.arff")
    xVal, yVal = convertDataToLTSMFormat(validationData, windowSize)
    models = getModelConfig()
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
                print_both("optimizer: " + str(optimizer._name) + str(optimizer.learning_rate))
                print_both("-------------------------------")

                model.compile(optimizer=optimizer, loss=tf.keras.losses.BinaryCrossentropy(),metrics=get_metrics_for_model())
                hist = model.fit(x_train, y_train, validation_data=(x_test, y_test), epochs=epochs, class_weight=weights, shuffle=shuffle)
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
                    'val_accuracy': hist.history['val_accuracy']
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

# See PyCharm help at https://www.jetbrains.com/help/pycharm/
# os.close(outputFile)