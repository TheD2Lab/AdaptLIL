package server;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import it.unimi.dsi.fastutil.Hash;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.websockets.WebSocketAddOn;
import org.glassfish.grizzly.websockets.WebSocketApplication;
import org.glassfish.grizzly.websockets.WebSocketEngine;
import server.gazepoint.api.XmlObject;
import server.gazepoint.api.recv.RecXmlObject;
import server.gazepoint.api.set.SetEnableSendCommand;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;
import weka.core.Range;
import weka.core.converters.ArffLoader;
import weka.experiment.*;
import wekaext.ClassifierResult;
import wekaext.WekaExperiment;

import javax.swing.*;
import java.beans.PropertyDescriptor;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class ServerMain {

//    public static ServerMain serverMain;
    public static final String url = "localhost";
    public static final int port = 8080;
    public static void serializationTest() {
        XmlMapper mapper = new XmlMapper();
        String serialString = "<REC CNT=\"34\"/>";
        String serialString2 = "<REC TIME_TICK=\"3434344\"/>";
        String fixationString = "<REC FPOGX=\"0.48439\" FPOGY=\"0.50313\" FPOGS=\"1891.86768\" FPOGD=\"0.49280\" FPOGID=\"1599\" FPOGV=\"1\" />";
        XmlObject someXmlObj = null;

        someXmlObj =  GazeApiCommands.mapToXmlObject(fixationString);

        if (someXmlObj instanceof RecXmlObject)
            System.out.println(((RecXmlObject) someXmlObj).getFixation().getX());


    }

    public static void classificationExperiment() {
        Scanner in = new Scanner(System.in);

//        System.out.print("Enter root directory: ");
        String rootDir =  "C:\\Users\\LeaseCalcs\\Desktop\\d2 lab\\result\\supervised learning data\\";
        String trainDataDir = rootDir + "\\train\\";//in.nextLine();
        String trainTestDataDir = rootDir + "\\test\\";
        boolean classification = false;
//        System.out.print("Enter C for classification or R for regression: ");
        String typeOfExperiment = "C";//in.nextLine();

        if (typeOfExperiment.equalsIgnoreCase("C")) {
            classification = true;
        }

        try {
            setupExperiment(classification, trainDataDir, trainTestDataDir);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        in.close();



    }

    public static void setupExperiment(boolean classification, String trainDataDir, String testDataDir) throws Exception {
        // set directory path
        File trainDirPath = new File(trainDataDir);
        File testDataDirPath = new File(testDataDir);

        // find all arff files
        ArrayList<String> trainDataFiles = new ArrayList<>();
        ArrayList<String> testDataFiles = new ArrayList<>();
        for (String fileName : trainDirPath.list()) {
            if (fileName.contains("arff")) {
                trainDataFiles.add(fileName);
            }
        }

        for (String fileName : testDataDirPath.list()) {
            if (fileName.contains("arff")) {
                testDataFiles.add(fileName);
            }
        }

        // sort all files by filename
        Collections.sort(trainDataFiles, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return extractInt(o1) - extractInt(o2);
            }

            int extractInt(String s) {
                String num = s.replaceAll("\\D", "");
                // return 0 if no digits found
                return num.isEmpty() ? 0 : Integer.parseInt(num);
            }
        });

        // build classifiers
        Classifier[] classifiers;
        System.out.println("starting classification");

        if (classification) {
            System.out.println("classificaiton");
            classifiers = WekaExperiment.getClassificationClassifiers();
        } else {
            classifiers = WekaExperiment.getRegressionClassifiers();
        }

        // run experiment

        ArrayList<ClassifierResult[]> allResults = new ArrayList<>();

        System.out.println(trainDirPath.getAbsolutePath());
        ArffLoader loader = new ArffLoader();

        HashMap<String, HashMap<String, Double>> totalResultsOfClassifiers = new HashMap<>();
        for (Classifier c : classifiers) {
            totalResultsOfClassifiers.put(c.getClass().getName(), new HashMap<>());
        }
        for (int i = 0; i < trainDataFiles.size(); i++) {
            String trainDataFileLocation = String.format("%s/%s", trainDirPath.getAbsolutePath(), trainDataFiles.get(i));
//            Instances result = new Instances(new BufferedReader(new FileReader(irl.getOutputFile())));

            loader.setFile(new File(trainDataFileLocation));
            Instances trainDataInstance = loader.getDataSet();
            trainDataInstance.setClassIndex(trainDataInstance.numAttributes() - 1);


            for (String testFile : testDataFiles) {
                String testDataFileLoc = testDataDirPath.getAbsolutePath() + "/" + testFile;
                System.out.println(testDataFileLoc);
                loader.setFile(new File(testDataFileLoc));
                Instances testDataInstance = loader.getDataSet();
                testDataInstance.setClassIndex(testDataInstance.numAttributes() - 1);

                HashMap<String, HashMap<String, Double>> resultsOfClassifiers = evaluateAllClassifiers(classifiers, trainDataInstance, testDataInstance);
                for (String classifierName : resultsOfClassifiers.keySet()) {
                    for (String resultKey : resultsOfClassifiers.get(classifierName).keySet()) {
                        if (totalResultsOfClassifiers.get(classifierName).containsKey(resultKey)) {
                            double newAvg = totalResultsOfClassifiers.get(classifierName).get(resultKey) +
                                    resultsOfClassifiers.get(classifierName).get(resultKey);

                            totalResultsOfClassifiers.get(classifierName).put(resultKey, newAvg);
                        } else {
                            totalResultsOfClassifiers.get(classifierName).put(resultKey, resultsOfClassifiers.get(classifierName).get(resultKey));
                        }
                    }
                }
            }
//            ResultMatrix matrix = runExperiment(classifiers, fileLocation, classification, false);

            // returns results from classification experiment
//            ClassifierResult[] res = WekaExperiment.getClassificationExperimentResults(classifiers, matrix);

//            allResults.add(res);

            System.out.println("-----------------------------------");
            System.out.println("--------------Averages-------------");
            System.out.println("-----------------------------------");

            for (String classifierName : totalResultsOfClassifiers.keySet()) {
                System.out.println("--------classifier: " + classifierName + " -------------");
                for (String resultKey : totalResultsOfClassifiers.get(classifierName).keySet()) {
                    double avgVal = totalResultsOfClassifiers.get(classifierName).get(resultKey) / testDataFiles.size();
                    System.out.println("key: " + resultKey + " Avg Val: " + avgVal);
                }
            }

            System.out.printf("%d/%d Process Complete ^^^\n==============================", i + 1, trainDataFiles.size());
        }

        // saves classification experiment to csv file
//        WekaExperiment.writeResultsToCSV(classifiers, allResults, fileNames,
//                String.format("%s/csv_results-%s.csv", dirPath.getParent(), dirPath.getName()));

        System.out.println("==============================\nProcess Complete");

    }

    private static HashMap<String, HashMap<String, Double>> evaluateAllClassifiers(Classifier[] classifiers, Instances train, Instances test) {
        // classifier evaluations
        ArrayList<String> dnw = new ArrayList<String>();

        HashMap<String, HashMap<String, Double>> totalResultsByClassifier = new HashMap<>();
        int classifierCount = 0;
        for (Classifier c : classifiers) {
            totalResultsByClassifier.put(c.getClass().getName(), new HashMap<String, Double>());
            ++classifierCount;
            try {
                c.buildClassifier(train);
                Evaluation eval = new Evaluation(train);
                eval.evaluateModel(c, test);
                HashMap<String, Double> results = printEvaluationStatistics(c, eval);

                for (String key : results.keySet()) {
                    HashMap<String, Double> resultsOfClassifier = totalResultsByClassifier.get(c.getClass().getName());
                    if (resultsOfClassifier.containsKey(key))
                        resultsOfClassifier.put(key, resultsOfClassifier.get(key) + results.get(key));
                    else
                        resultsOfClassifier.put(key, results.get(key));
                }
            } catch (Exception e) {
                e.printStackTrace();
                dnw.add(c.getClass().getSimpleName());

            }
        }

        for (String s : dnw) {
//            System.out.println(s);
        }

        return totalResultsByClassifier;


    }

    private static HashMap<String, Double> printEvaluationStatistics(Classifier c, Evaluation eval) {
        System.out.println("--------------" + c.getClass().getName() + "----------------");
        System.out.println("False Positive Rate of CS 0: " + eval.falsePositiveRate(0));
        System.out.println("False Positive Rate of CS 1: " + eval.falsePositiveRate(1));
        System.out.println("False Negative Rate of CS 0: " + eval.falseNegativeRate(0));
        System.out.println("False Negative Rate of CS 1: " + eval.falseNegativeRate(1));
        System.out.println("weighted true pos rate " + eval.weightedTruePositiveRate());
        System.out.println("% correct: " + eval.pctCorrect());
        System.out.println(" % incorrect: " + eval.pctIncorrect());
        System.out.println("**************************** END ************************");

        HashMap<String, Double> results = new HashMap<>();
        results.put("falsePosRate_0", eval.falsePositiveRate(0));
        results.put("falsePosRate_1", eval.falsePositiveRate(1));
        results.put("falseNegRate_0", eval.falseNegativeRate(0));
        results.put("falseNegRate_1", eval.falseNegativeRate(1));
        results.put("weighted_true_pos", eval.weightedTruePositiveRate());
        results.put("weighted_false_pos", eval.weightedFalseNegativeRate());

        results.put("pct_correct", eval.pctCorrect());
        results.put("pct_incorrect", eval.pctIncorrect());
        return results;
    }
    private static ResultMatrix runExperiment(Classifier[] classifiers, String fileLocation, boolean classification,
                                              Boolean logData) throws Exception {
        // setup weka.experiment
        Experiment exp = new Experiment();
        exp.setPropertyArray(new Classifier[0]);
        exp.setUsePropertyIterator(true);

        // setup for classification or regression
        SplitEvaluator se = null;
        Classifier sec = null;

        if (classification) {
            classification = true;
            se = new ClassifierSplitEvaluator();
            sec = ((ClassifierSplitEvaluator) se).getClassifier();
        } else {
            se = new RegressionSplitEvaluator();
            sec = ((RegressionSplitEvaluator) se).getClassifier();
        }

        // cross validation
        CrossValidationResultProducer cvrp = new CrossValidationResultProducer();
        cvrp.setNumFolds(10);
        cvrp.setSplitEvaluator(se);

        PropertyNode[] propertyPath = new PropertyNode[2];
        propertyPath[0] = new PropertyNode(se,
                new PropertyDescriptor("splitEvaluator", CrossValidationResultProducer.class),
                CrossValidationResultProducer.class);

        propertyPath[1] = new PropertyNode(sec, new PropertyDescriptor("classifier", se.getClass()), se.getClass());

        exp.setResultProducer(cvrp);
        exp.setPropertyPath(propertyPath);

        // set classifiers here
        exp.setPropertyArray(classifiers);

        DefaultListModel model = new DefaultListModel();

        // set dataset here
        File file = new File(fileLocation);

        model.addElement(file);

        exp.setDatasets(model);

        // *this is important for WEKA experimenter calculations*
        InstancesResultListener irl = new InstancesResultListener();

        irl.setOutputFile(new File(file.getParent() + "/output.csv"));
        exp.setResultListener(irl);

        exp.initialize();
        exp.runExperiment();
        exp.postProcess();

        PairedCorrectedTTester tester = new PairedCorrectedTTester();
        Instances result = new Instances(new BufferedReader(new FileReader(irl.getOutputFile())));

        tester.setInstances(result);
        tester.setSortColumn(-1);

        tester.setRunColumn(result.attribute("Key_Run").index());
        if (classification) {
            tester.setFoldColumn(result.attribute("Key_Fold").index());
        }
        tester.setDatasetKeyColumns(new Range("" + (result.attribute("Key_Dataset").index() + 1)));
        tester.setResultsetKeyColumns(new Range("" + (result.attribute("Key_Scheme").index() + 1) + ","
                + (result.attribute("Key_Scheme_options").index() + 1) + ","
                + (result.attribute("Key_Scheme_version_ID").index() + 1)));
        tester.setResultMatrix(new ResultMatrixPlainText());
        tester.setDisplayedResultsets(null);
        tester.setSignificanceLevel(0.05);
        tester.setShowStdDevs(true);

        // experiment results

        if (classification) {
            tester.multiResultsetFull(0, result.attribute("Percent_correct").index());
        } else {
            tester.multiResultsetFull(0, result.attribute("Root_mean_squared_error").index());
        }

        ResultMatrix matrix = tester.getResultMatrix();

        if (logData) {
            System.out.println(matrix.toStringMatrix());
        }

        // delete output file
        irl.getOutputFile().delete();

        return matrix;
    }



    public static void main(String[] args) {

        XmlMapper mapper = new XmlMapper();
        HttpServer server = null;
        System.out.println("Beginning GP3 Real-Time Prototype Stream");
        classificationExperiment();
//        serializationTest();
//        GP3Socket gp3Socket = new GP3Socket();
//        try {
//            gp3Socket.connect();
//            System.out.println("Connected to GP3");
//            System.out.println("Starting Data Stream via thread");
//            gp3Socket.startGazeDataStream();
//            gp3Socket.write((mapper.writeValueAsString(new SetEnableSendCommand(GazeApiCommands.ENABLE_SEND_POG_FIX, true))));
//            gp3Socket.write((mapper.writeValueAsString(new SetEnableSendCommand(GazeApiCommands.ENABLE_SEND_BLINK, true))));
//            gp3Socket.write((mapper.writeValueAsString(new SetEnableSendCommand(GazeApiCommands.ENABLE_SEND_COUNTER, true))));
//
//            gp3Socket.write((mapper.writeValueAsString(new SetEnableSendCommand(GazeApiCommands.ENABLE_SEND_DIAL, true))));
//            gp3Socket.write((mapper.writeValueAsString(new SetEnableSendCommand(GazeApiCommands.ENABLE_SEND_HR, true))));
//            gp3Socket.writeSetCommand(new SetEnableSendCommand(GazeApiCommands.ENABLE_SEND_TIME_TICK, true));
//            gp3Socket.writeSetCommand(new SetEnableSendCommand(GazeApiCommands.ENABLE_SEND_TIME, true));
//
//
//            gp3Socket.writeSetCommand(new SetEnableSendCommand(GazeApiCommands.ENABLE_SEND_CURSOR, true));
//
////            System.out.println("Started gaze data stream.");
//
////            System.out.println("Starting websocket...");
////            server = initWebSocket();
////
//            // All
////            System.out.println("Sever stays alive by waiting for input so type anything to exit");
////
//            System.in.read();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        } finally {
//            server.shutdown();
//        }
    }

    /**
     * https://javaee.github.io/grizzly/websockets.html
     */
    public static HttpServer initWebSocket() {
        final HttpServer server = HttpServer.createSimpleServer("/var/www", port);
        final WebSocketAddOn addon = new WebSocketAddOn();
        server.getListener("grizzly").registerAddOn(addon);
        WebSocketApplication adaptiveOntoMapp = new AdaptiveOntoMapApp();
        WebSocketEngine.getEngine().register("", "/gaze", adaptiveOntoMapp);

        try {
            server.start();
            System.out.println("Websocket started on  " + url + ":" + port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return server;
    }

}
