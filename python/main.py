import os, shutil

import sklearn.metrics
from sklearn import model_selection

import tensorflow as tf
from tensorflow.keras import Sequential
from tensorflow.keras import layers
from tensorflow.keras.layers import Dense
from scipy.io import arff
import numpy as np
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
    model = Sequential()
    model.add(layers.LSTM(4, input_shape=(300,8)))
    model.add(Dense(10, activation='relu'))
    model.add(Dense(20, activation='relu'))
    # model.add(Dense(130, activation='relu'))
    model.add(Dense(1, activation='sigmoid'))
    return model

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

# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    windowSize=300
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

    optimizers = [tf.keras.optimizers.Adam(learning_rate=1e-3), 'rmsprop']
    trainData = convertArffToDataFrame("C:\\Users\\nickj\\Documents\\trainData_2sec_window_1_no_v.arff")
    targetColumn = "correct"

    '''
    Can't use reshape, must do mannualy
    Take the 2D stretched window and make it to a 3D window represented by
    (samples,windowSize,attributes)
    Also pair the correct answers together.
    '''
    x,y = convertDataToLTSMFormat(trainData)
    print(x.shape)
    print(y.reshape(-1).flatten().shape)
        #how do we add here?
        #split the whole row by 300


    print(y)
    #split ^^ 80/20, preserve order
    x_train, x_test, y_train, y_test = model_selection.train_test_split(x, y, test_size=0.2, random_state=0, shuffle=False)
    print(x_train.shape)
    validationData = convertArffToDataFrame("C:\\Users\\nickj\\Documents\\testData_2sec_window_1_no_v.arff")
    xVal, yVal = convertDataToLTSMFormat(validationData)
    model = getModelConfig()
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
    print('weight0: ' + str(weight_for_0))
    print('weight1: ' + str(weight_for_1))

    for optimizer in optimizers:
        model.compile(optimizer=optimizer, loss=tf.keras.losses.BinaryCrossentropy(),metrics=metrics)
        hist = model.fit(x_train, y_train, validation_data=(x_test, y_test), shuffle=False, epochs=10, class_weight={0: weight_for_0, 1: weight_for_1})
        y_hat = model.predict(xVal)
        y_hat = [(1.0 if y_pred > 0.5 else 0.0) for y_pred in y_hat]
        print(y_hat)
        print(sklearn.metrics.confusion_matrix(yVal, y_hat))
# See PyCharm help at https://www.jetbrains.com/help/pycharm/
