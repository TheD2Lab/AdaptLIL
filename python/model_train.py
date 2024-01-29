from collections import OrderedDict

import model_helper as model_helper
import numpy as np
import model_helper
import sys
import os
import model_conf as model_conf
from numpy.random import seed
import tensorflow as tf
import datetime
seed(0)
tf.config.experimental.enable_op_determinism()
tf.keras.utils.set_random_seed(0)


def print_both(*args):
    temp = sys.stdout  # assign console output to a variable
    print(' '.join([str(arg) for arg in args]))
    sys.stdout = outputFile
    print(' '.join([str(arg) for arg in args]))
    sys.stdout = temp  # set stdout back to console output



def getTrainingDataNewFormat(dir, timeSequences, shape):
    trainData = np.empty( shape=shape, dtype=np.float32) #{'x': np.array([]), 'y': np.array([])}
    validData = np.empty( shape=shape, dtype=np.float32)
    participants = {}
    for pDirName in os.listdir(dir):
        pDir = os.path.join(dir, pDirName)
        if (os.path.isdir(pDir)):
            participantData = {}

            trainDir = os.path.join(pDir, "train")
            validDir = os.path.join(pDir, "validation")
            for trainFileName in os.listdir(trainDir):
                trainFile = os.path.join(trainDir, trainFileName)
                data = model_helper.convertArffToDataFrame(trainFile)
                trainData = np.append(trainData, data, axis=0)

            for validationFileName in os.listdir(validDir):
                validationFile = os.path.join(validDir, validationFileName)
                data = model_helper.convertArffToDataFrame(validationFile)
                validData = np.append(validData, data, axis=0)

            if (np.all(validData) != None and np.all(trainData) != None):
                participants[pDirName] = {
                    'training': model_helper.convertDataToLTSMFormat(trainData,timeSequences=timeSequences),
                    'validation': model_helper.convertDataToLTSMFormat(validData,timeSequences=timeSequences)
                }

    return participants


def getTestingDataNewFormat(dir, timeSequences, shape):
    retrainData = np.empty(shape=shape, dtype=np.float32)
    testData = np.empty(shape=shape, dtype=np.float32)
    participants = {}
    for pDirName in os.listdir(dir):
        pDir = os.path.join(dir, pDirName)
        if (os.path.isdir(pDir) and len(os.listdir(pDir)) > 0):
            participantData = {}
            # p1, p2...
            retrainDir = os.path.join(pDir, "retrain")
            testDir = os.path.join(pDir, "test")
            for retrainFileName in os.listdir(retrainDir):
                retrainFile = os.path.join(retrainDir, retrainFileName)
                data = model_helper.convertArffToDataFrame(retrainFile)
                retrainData = np.append(retrainData, data, axis=0)

            for testFileName in os.listdir(testDir):
                testFile = os.path.join(testDir, testFileName)
                data = model_helper.convertArffToDataFrame(testFile)
                testData = np.append(testData, data, axis=0)


            if (np.all(testData) != None and np.all(retrainData) != None):
                participants[pDirName] = {
                    'retrain': model_helper.convertDataToLTSMFormat(retrainData,timeSequences=timeSequences),
                    'test': model_helper.convertDataToLTSMFormat(testData,timeSequences=timeSequences)
                }

    return participants

1

if __name__ == '__main__':

    resultDir = "model_out/"+str(datetime.datetime.now()).replace(":", "_").replace(".", ",")
    os.mkdir(resultDir)

    outputFile = open(os.path.join(resultDir, "output.txt"), 'wt')
    model_helper.savePythonFile(resultDir)
    timeSequences = 6
    numAttributes = 150
    numMetaAttrs = 0
    windowSize = 75 #75
    # TODO, if after the current test run, it moves more towards 50%/50%, lower epochs
    epochs = 2 # 20 epochs is pretty good, will train with 24 next as 3x is a good rule of thumb.
    numFolds = 14;
    shuffle = False

    callback = tf.keras.callbacks.EarlyStopping(monitor='loss', patience=10,
                                                restore_best_weights=False)

    yAll = np.array([])
    print_both('epochs: ' + str(epochs))
    print_both('Shuffle on compile: ' + str(shuffle))

    consoleOut = sys.stdout  # assign console output to a variable

    targetColumn = "correct"
    baseDataDir = "/home/notroot/Desktop/d2lab/iav/train_test_data_output/lessval/" #try lesspicky and newformat_17sec
    trainDataByParticipant = getTrainingDataNewFormat(os.path.join(baseDataDir,'train'), shape=(0,numAttributes+1), timeSequences=timeSequences)
    testDataByParticipant = getTestingDataNewFormat(os.path.join(baseDataDir, 'test'), shape=(0,numAttributes+1), timeSequences=timeSequences)
    transformer = model_conf.getModelConfig(timeSequences, numAttributes, windowSize)['transformer_model']
    transformer.compile(optimizer=tf.keras.optimizers.Adam(learning_rate=1e-3), loss=tf.keras.losses.BinaryCrossentropy(),
                  metrics=model_conf.get_metrics_for_model())

    hist_averages = {'accuracy': [], 'val_accuracy': []}
    for p, pData in trainDataByParticipant.items():
        print(p)
        #train for one p, plot
        x_train, y_train = pData['training']
        if (not x_train.any() or not y_train.any()):
            continue
        x_val, y_val = pData['validation']
        if (not x_val.any() or not y_val.any()):
            continue
        hist = transformer.fit(
            x_train,
            y_train,
            validation_data=(x_val, y_val),
            epochs=epochs,
            # class_weight=model_helper
            shuffle=shuffle,
            # batch_size=1,
            # callbacks=[callback]
        )
        hist_averages['accuracy'].append(hist.history['accuracy'][-1])
        hist_averages['val_accuracy'].append(hist.history['val_accuracy'][-1])
        # transformer.reset_states()
        # tf.keras.backend.clear_session()

    transformer.save(resultDir + "/" + "transformer_ult" + ".keras", save_format='keras')

    model_helper.plotLines({'Acc %': hist_averages['accuracy'], 'Val Acc %' : hist_averages['val_accuracy']}, resultDir, "mymodel_train", show_plot=True)

    retrain_hist_avg = {'accuracy': [], 'val_accuracy': []}
    for p, pData in testDataByParticipant.items():
        cloned_transformer = tf.keras.models.load_model(resultDir + "/" + "transformer_ult" + ".keras")
        print(p)

        # train for one p, plot
        x_train, y_train = pData['retrain']
        if (not x_train.any() or not y_train.any()):
            continue
        x_val, y_val = pData['test']
        if (not x_val.any() or not y_val.any()):
            continue
        hist = cloned_transformer.fit(
            x_train,
            y_train,
            validation_data=(x_val, y_val),
            epochs=epochs,
            # class_weight=None if useLoo else weights,
            shuffle=shuffle,
            # batch_size=1,
            # callbacks=[callback]
        )
        retrain_hist_avg['accuracy'].append(hist.history['accuracy'][-1])
        retrain_hist_avg['val_accuracy'].append(hist.history['val_accuracy'][-1])
        model_helper.clear_model(cloned_transformer)

    model_helper.plotLines({'Acc %': retrain_hist_avg['accuracy'], 'Val Acc %' : retrain_hist_avg['val_accuracy']}, resultDir, "mymodel_test", show_plot=True)


    all_models_by_tp_and_tn = {}
    all_models_stats = []
    trainDataParticipants = []
    testDataParticipants = []

    participants = []
    for filename in os.listdir(baseDataDir):
        f = os.path.join(baseDataDir, filename)
        print_both(filename)
        if "trainData" not in filename and "testData" not in filename:
            continue

        if "trainData" in filename:
            trainData = model_helper.convertArffToDataFrame(f)
            participants.append(filename)
            x_part, y_part = model_helper.convertDataToLTSMFormat(trainData, timeSequences, numMetaAttrs)

            print_both('full input shape: ' + str(x_part.shape))
            yAll = np.concatenate((yAll, y_part), axis=0)

            trainDataParticipants.append({'x': x_part, 'y': y_part, 'fileName': filename})
        elif "testData" in filename:
            testData = model_helper.convertArffToDataFrame(f)
            xTest, yTest = model_helper.convertDataToLTSMFormat(testData, timeSequences, numMetaAttrs)
            testDataParticipants.append({'x': xTest, 'y': yTest, 'fileName': filename})



    # with strategy.scope():
    model = model_conf.getModelConfig(timeSequences, attributes=numAttributes, windowSize=windowSize)
    optimizers = model_conf.get_optimizers()
    print_both("*****************************************")
    histories = []
    numPart = 0
    avg_tn_tp = []

    stats_by_participant = {}
    max_ratio = 0
    for optimizer in optimizers:

        tp_rates = []
        tn_rates = []
        unseen_acc = []
        fold_scores = []

        """
        1) Combine train files to 1 set
        2) Combine val to 1 set
        3) Train
        #Map Acc and Val according to Participant in x dir
        """

        for i in range(int(len(trainDataParticipants))):
            print_both("trainig on file: " + str(trainDataParticipants[i]['fileName']))
            x_part = trainDataParticipants[i]['x']
            y_part = trainDataParticipants[i]['y']
            # Define per-fold score containers <-- these are new
            acc_per_fold = []
            loss_per_fold = []

            # x_train, x_val, y_train, y_val = model_selection.train_test_split(x_part, y_part, test_size=0.2, random_state=0, shuffle=True)

            split_enumerator = kfold.split(x_part, y_part);

            # todo, we need to separate each participant
            # the model should train against only the participants train data to
            # have a representation of that person
            # then we retrain on the next person, so on and so forth.
            model = model_conf.getModelConfig(timeSequences, numAttributes, windowSize)['transformer_model']
            model.compile(optimizer='adam', loss=tf.keras.losses.BinaryCrossentropy(),
                          metrics=model_conf.get_metrics_for_model())

            print_both("*****************************************")
            print_both(model_name)

            sys.stdout = outputFile
            model.summary()
            sys.stdout = consoleOut  # set stdout back to console output
            model.summary()
            if type(optimizer) != type(""):
                unique_model_id = model_name + "-" + str(type(optimizer).__name__) + str(
                    tf.keras.backend.eval(optimizer.lr)).replace(".", ",")
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


            weights = model_helper.get_weight_bias(y_part[train])

            print(x_part[train].shape)
            hist = model.fit(
                x_part[train],
                y_part[train],
                # validation_data=(x_val, y_val),
                epochs=epochs,
                # class_weight=None if useLoo else weights,
                shuffle=shuffle,
                # batch_size=1,
                # callbacks=[callback]
            )
            scores = model.evaluate(x_part[test], y_part[test], verbose=0)

            histories.append(hist)
            fold_scores.append(scores[1])
            cur_tp_tn = 0
            '''
            Test on each fold.
            '''
            y_hat = model.predict(x_part[test])

            y_hat = [(1.0 if y_pred >= 0.50 else 0.0) for y_pred in y_hat]
            conf_matrix = sklearn.metrics.confusion_matrix(y_part[test], y_hat, labels=[1.0, 0.0])
            true_pos = conf_matrix[0][0] / (conf_matrix[0][0] + conf_matrix[0][1])
            true_neg = conf_matrix[1][1] / (conf_matrix[1][0] + conf_matrix[1][1])
            acc_rate = (conf_matrix[0][0] + conf_matrix[1][1]) / (conf_matrix[0][0] + conf_matrix[0][1] + conf_matrix[1][0] + conf_matrix[1][1])
            print_both(conf_matrix)
            curRatio = (true_pos + true_neg) / 2
            if (curRatio > max_ratio):
                max_ratio = curRatio
            cur_tp_tn += ((true_pos + true_neg) / 2) / len(testDataParticipants)
            print_both("Acc: " + str(acc_rate) + ", " + "tp: %: " + str(true_pos) + " tn: %: " + str(true_neg))
            tp_rates.append(true_pos)
            tn_rates.append(true_neg)
            unseen_acc.append(acc_rate)


            '''
            Done fitting on multiple participants, time for real world data testing
            '''
            print_both("************************************")
            print_both("--------FINISHED FITTING MODEL------")
            print_both("************************************")
            for testPInd in range(0, len(testDataParticipants)):
                testP = testDataParticipants[testPInd]
                print_both('Testing on : ' + str(testP['fileName']))
                xTest = testP['x']
                yTest = testP['y']
                y_hat = model.predict(xTest)
                # results = model.evlauate(xVal, yTest)
                y_hat = [(1.0 if y_pred >= 0.5 else 0.0) for y_pred in y_hat]
                conf_matrix = sklearn.metrics.confusion_matrix(yTest, y_hat, labels=[1.0, 0.0])
                print_both(conf_matrix)

        model.save(resultDir + "/" + unique_model_id + ".h5", save_format='h5')


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
                # key: [e1,e2,e3]
                for i in range(epochs):
                    total_histories_of_cross[key][i] += history.history[key][i]

                # now calculate avg
                for i in range(epochs):
                    total_histories_of_cross[key][i] /= epochs
            # now we have cross -> [key : [e1,e3]/avg,...]
            histories_of_all_cross.append(total_histories_of_cross)

        all_models_by_tp_and_tn[unique_model_id] = conf_matrix

        # Saving breaks the rest of the trianings and corrupts the rest of the configurations!
        # Only save when using Linux keras 2.14!!!
        print_both("max ratio achieved: " + str(max_ratio))

        model_helper.plotLines({'tp%': tp_rates, 'tn%': tn_rates, 'acc%': unseen_acc, 'val fold acc%': fold_scores}, resultDir, unique_model_id)

        model.save(resultDir + "/" + unique_model_id + ".h5", save_format='h5')
            # model.reset_states()
            # tf.compat.v1.reset_default_graph()
            # tf.keras.backend.clear_session()
            # del model
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


