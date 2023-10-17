import os, shutil

import sklearn.metrics
from sklearn import model_selection
import tensorflow as tf

from keras.callbacks import CSVLogger
tf.config.experimental.enable_op_determinism()
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
    model_bigger_bigger_lstm.add(Dense(2, activation='relu'))
    model_bigger_bigger_lstm.add(Dense(1, activation='sigmoid'))
    model_bigger_biggest_lstm = Sequential()
    model_bigger_biggest_lstm.add(LSTM(16, input_shape=(300, 8)))
    model_bigger_biggest_lstm.add(Dense(8, activation='relu'))
    model_bigger_biggest_lstm.add(Dense(2, activation='relu'))
    model_bigger_biggest_lstm.add(Dense(1, activation='sigmoid'))
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
    model_double_lstm_double_layers.add(LSTM(8, input_shape=(300,8), return_sequences=True))
    model_double_lstm_double_layers.add(LSTM(16))
    model_double_lstm_double_layers.add(Dense(12, activation='relu'))
    model_double_lstm_double_layers.add(Dense(8, activation='relu'))
    model_double_lstm_double_layers.add(Dense(4, activation='relu'))
    model_double_lstm_double_layers.add(Dense(1, activation='sigmoid'))

    model_double_lstm_double_layers_more_nodes = Sequential()
    model_double_lstm_double_layers_more_nodes.add(LSTM(8, input_shape=(300,8), return_sequences=True))
    model_double_lstm_double_layers_more_nodes.add(LSTM(16))
    model_double_lstm_double_layers_more_nodes.add(Dense(12, activation='relu'))
    model_double_lstm_double_layers_more_nodes.add(Dense(36, activation='relu'))
    model_double_lstm_double_layers_more_nodes.add(Dense(8, activation='relu'))
    model_double_lstm_double_layers_more_nodes.add(Dense(1, activation='sigmoid'))
    models['model_bigger_lstm'] = model_bigger_lstm;
    models['model_one_layer_ltsm'] = model_one_layer_ltsm
    models['one_layer_ltsm_v2'] = model_one_layer_ltsm_v2;
    models['model_bigger_bigger_lstm'] = model_bigger_bigger_lstm;
    #Past here, overfitting occurs
    models['model_double_stm'] = model_double_lstm;
    models['model_double_lstm_double_layers'] = model_double_lstm_double_layers
    models['model_bigger_lstm_v2'] = model_bigger_lstm_v2
    models['model_double_lstm_double_layers_more_nodes'] = model_double_lstm_double_layers_more_nodes
    # models['model_simple_ltsm'] = model_simple_ltsm

    return models

def convertDataToLTSMFormat(data):
    x = []
    y = []
    for i in data:
        correct = i[-1]
        reshaped = i[:-1]
        x.append(np.array_split(np.array(reshaped).flatten(), windowSize, axis=0))
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


# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    windowSize=300
    debug=False

    # if not debug:
    #     sys.stdout = open(os.path.join(resultDir,'output.txt'), 'wt')
    # csvLogger = CSVLogger(os.path.join(resultDir, "log.csv"), append=True)
    metrics=[
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

    trainData = convertArffToDataFrame("C:\\Users\\nickj\\Documents\\trainData_2sec_window_1_no_v.arff")
    targetColumn = "correct"

    '''
    Can't use reshape, must do mannualy
    Take the 2D stretched window and make it to a 3D window represented by
    (samples,windowSize,attributes)
    Also pair the correct answers together.
    '''
    x,y = convertDataToLTSMFormat(trainData)
    print_both(x.shape)
    print_both(y.reshape(-1).flatten().shape)
        #how do we add here?
        #split the whole row by 300


    print_both(y)
    #split ^^ 80/20, preserve order
    x_train, x_test, y_train, y_test = model_selection.train_test_split(x, y, test_size=0.2, random_state=0)
    print_both(x_train.shape)
    validationData = convertArffToDataFrame("C:\\Users\\nickj\\Documents\\testData_2sec_window_1_no_v.arff")
    xVal, yVal = convertDataToLTSMFormat(validationData)
    models = getModelConfig()
    '''
    Weight biasing
    25% are only wrong, that leads to data training bias.
    '''
    neg = len([i for i in y_train if i!=1])
    # print(neg)
    pos = len([i for i in y_train if i==1])
    # print(pos)
    total = len(y_train)
    weight_for_0 = (1 / neg) * (total/2.0)
    weight_for_1 = (1 / pos) * (total / 2.0)
    print_both('weight0: ' + str(weight_for_0))
    print_both('weight1: ' + str(weight_for_1))

    for model_name,model in models.items():
        optimizers = [
                        tf.keras.optimizers.Adam(learning_rate=85e-4),
                        tf.keras.optimizers.Adam(learning_rate=90e-4),
                        tf.keras.optimizers.Adam(learning_rate=95e-4),
                        tf.keras.optimizers.Adam(learning_rate=98e-4),
                        tf.keras.optimizers.Adam(learning_rate=99e-4),
                        tf.keras.optimizers.Adam(learning_rate=1e-3),
                        tf.keras.optimizers.Adam(learning_rate=1.4e-3),
                        tf.keras.optimizers.Adam(learning_rate=2e-3),
                        tf.keras.optimizers.Nadam(learning_rate=88e-4),
                        tf.keras.optimizers.Nadam(learning_rate=1e-3),
                      # tf.keras.optimizers.RMSprop
                      ]

        print_both("*****************************************")
        print_both(model_name)
        print_both("*****************************************")
        for optimizer in optimizers:
            print_both("-------------------------------")
            if type(optimizer) != type(''):
                print_both("optimizer: " + str(optimizer._name) + str(optimizer.learning_rate))
            else:
                print_both("optimizer: " + optimizer)

            try:
                print_both("-------------------------------")
                temp = sys.stdout  # assign console output to a variable
                sys.stdout = outputFile
                model.compile(optimizer=optimizer, loss=tf.keras.losses.BinaryCrossentropy(),metrics=metrics)
                hist = model.fit(x_train, y_train, validation_data=(x_test, y_test), epochs=10, class_weight={0: weight_for_0, 1: weight_for_1})
                sys.stdout = temp  # set stdout back to console output

                y_hat = model.predict(xVal)
                #results = model.evlauate(xVal, yVal)
                y_hat = [(1.0 if y_pred > 0.5 else 0.0) for y_pred in y_hat]
                print_both(y_hat)

                print_both(sklearn.metrics.confusion_matrix(yVal, y_hat, labels=[1.0,0.0]))
            except:
                pass

# See PyCharm help at https://www.jetbrains.com/help/pycharm/
os.close(outputFile)