package analysis.ontomap;

import analysis.Participant;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import data_classes.*;
import server.GazeWindow;
import server.gazepoint.api.recv.RecXmlObject;
import weka.classifiers.Classifier;
import weka.core.*;
import weka.core.converters.ArffSaver;
import wekaext.WekaExperiment;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OntoMapCsv {

    /**
     * List of participantIDs that were discarded from the study.
     * @return
     */
    private static List<String> discardedParticipantIds() {
        return List.of(new String[]{
                "P9",
                "P18",
                "P21",
                "P28",
                "P31",
                "P33",
                "P41",
                "P44",
                "P57",
                "P75"
        });
    }

    private static List<String> participantIdsForTestDataSet() {
        return List.of(new String[] {
                "P19",
                "P13",
                "P53",
                "P23",
                "P43",
                "P67"
        });
    }

    /**
     * Maps participiant names to their task data.
     * Excludes the discarded participants.
     * @param dir
     * @param isGazePointFolder
     * @param filterForLinkedList
     * @return
     */
    private static Map<String, List<File>> mapParticipantsToStudyData(String dir, boolean isGazePointFolder, boolean filterForLinkedList) {
        Map<String, List<File>> filteredFiles = new HashMap<>();
        File directoryFile = new File(dir);
        Pattern participantNamePattern = Pattern.compile("P\\d+");
        List<String> discardedParticipantIds = OntoMapCsv.discardedParticipantIds();
        for (File fileOrDir : directoryFile.listFiles()) {
            if (fileOrDir.isDirectory()) {

                //Expand
                //check if it is p
                if (isGazePointFolder) {
                    String participantName = fileOrDir.getName();

                    if (discardedParticipantIds.contains(participantName.toUpperCase()))
                        continue;
                    //look through files
                    for (File gpFileORDir : fileOrDir.listFiles()) {

                        //check if it has .LIL.
                        if (filterForLinkedList && gpFileORDir.getName().contains("LIL") || (!filterForLinkedList && gpFileORDir.getName().contains("Matrix"))) {

                            if (!filteredFiles.containsKey(participantName))
                                filteredFiles.put(participantName, new ArrayList<>());

                            filteredFiles.get(participantName).add(gpFileORDir);

                        }
                    }

                } else { //Task Data Directory mapping
                    //Going through task data directory.
                    Matcher participiantNameMatcher = participantNamePattern.matcher(fileOrDir.getName());
                    participiantNameMatcher.find();
                    String participantName = participiantNameMatcher.group(0);

                    //Ignore discarded participants.
                    if (discardedParticipantIds.contains(participantName.toUpperCase()))
                        continue;

                    //look through files of raw data
                    FilenameFilter filter = (dir1, name) -> name.equals("Raw Data");
                    File[] rawDataDir = fileOrDir.listFiles(filter);
                    for (File taskFileOrDir : rawDataDir[0].listFiles()) {

                        //check if it has .LIL.
                        if (filterForLinkedList && taskFileOrDir.getName().contains("list") || (!filterForLinkedList && taskFileOrDir.getName().contains("matrix"))) {
                            if (!filteredFiles.containsKey(participantName))
                                filteredFiles.put(participantName, new ArrayList<>());

                            filteredFiles.get(participantName).add(taskFileOrDir);

                        }
                    }
                }

            }
        }
        return filteredFiles;
    }
    public static void main(String[] args) {

        String baseDir = "E:\\datavisstudy-updated\\DataVisStudy\\Participant Data\\";

        //open all participants
        //Filter for baseline (matrix shouldnt be used b/c it's a different chart)
        Map<String, List<File>> gazePointFilesByParticipant = OntoMapCsv.mapParticipantsToStudyData(baseDir+"Gazepoint", true, true);
        Map<String, List<File>> taskFilesByParticipant = OntoMapCsv.mapParticipantsToStudyData(baseDir+"Task Data", false, true);
        Map<String, Participant> participantsById = new HashMap<>();


        //We
        int index = 0;
        for (String participantName : gazePointFilesByParticipant.keySet()) {

            //foreach participant
            //Create participant object
            Participant p = new Participant(participantName);

            //Store fixation files into the participant object
            for (File f : gazePointFilesByParticipant.get(participantName)) {
                System.out.println(f.getName());
                if (f.getName().toLowerCase().contains("all_gaze")) //store gaze data
                {
                    if (f.getName().toLowerCase().contains("anatomy"))
                        p.setAnatomyGazeFile(f);
                    else if (f.getName().toLowerCase().contains("baseline"))
                        p.setBaselineGazeFile(f);
                    else if (f.getName().toLowerCase().contains("conf"))
                        p.setConfGazeFile(f);
                }
                else if (f.getName().toLowerCase().contains("_fixations")) //Store fixation data
                {
                    if (f.getName().toLowerCase().contains("anatomy"))
                        p.setAnatomyFixationFile(f);
                    else if (f.getName().toLowerCase().contains("baseline"))
                        p.setBaselineFixationFile(f);
                    else if (f.getName().toLowerCase().contains("conf"))
                        p.setConfFixationFile(f);
                }
            }

            //Store answers files into participant object
            for (File f : taskFilesByParticipant.get(participantName)) {
                if (f.getName().toLowerCase().contains("tomy"))
                    p.setAnatomyAnswersFile(f);
                else if (f.getName().toLowerCase().contains("conf"))
                    p.setConfAnswersFile(f);
            }

            participantsById.put(participantName, p);

        }


        try {
            OntoMapCsv.testParticipantInstances(participantsById.values().toArray(new Participant[participantsById.size()]));
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        //grab list of classifiers
        //feed data and predict!

    }

    public static void testParticipantInstances(Participant[] participants) throws Exception {
        //Use DenseInstance to create on the fly instances.
        //https://weka.sourceforge.io/doc.dev/weka/core/DenseInstance.html
        //foreach participant
        float windowSizeInMilliseconds = 500;
        List<Instance> trainInstanceList = new ArrayList<>();
        List<Instance> testInstanceList = new ArrayList<>();
        Instances trainDataInstances = null;
        Instances testDataInstances = null;
        List<String> participantIdsForTestDataset = OntoMapCsv.participantIdsForTestDataSet();
        for (Participant p : participants) {


            boolean useForTrainInstances = !participantIdsForTestDataset.contains(p.getId().toUpperCase());
            GazeWindow participantWindow = new GazeWindow(false, windowSizeInMilliseconds);

            List<String> timeCutoffs = new ArrayList<>();
            List<Boolean> rightOrWrongs = new ArrayList<>();

            FileReader fileReader = new FileReader(p.getAnatomyAnswersFile());
            CSVReader csvReader = new CSVReader(fileReader);
            String[] cells = null;
            List<String> nominalValues = new ArrayList<>();
            nominalValues.add("1");
            nominalValues.add("0");

            //Retrieve the time the user answered the question and if they got it wrong/right.
            String[] nextLine = csvReader.readNext();
            int timeStampIndex = 8;
            int correctIndex = 6;
            while ((cells = csvReader.readNext()) != null) {
                String timeCutoff = cells[timeStampIndex];
                Boolean rightOrWrong = cells[correctIndex].equals("1");
                timeCutoffs.add(timeCutoff);
                System.out.println("right or worng: " + (rightOrWrong ? "true" : "false"));
                rightOrWrongs.add(rightOrWrong);
            }

            fileReader.close();
            csvReader.close();

            //Read fixation file
            fileReader = new FileReader(p.getAnatomyGazeFile());
            csvReader = new CSVReader(fileReader);

            int currentQuestionIndex = 0;
            List<String> headerRow = Arrays.asList(csvReader.readNext());

            //Loop over each line of gaze data. until we reach the last question and then continue onto next participant.
            while ((cells = csvReader.readNext()) != null && currentQuestionIndex < timeCutoffs.size() - 2) { //ignore the multiple choice question for now.
                //read fixation data up to the timecutoff
                float timeCutoff = Float.parseFloat(timeCutoffs.get(currentQuestionIndex));
                boolean incrementCurrentQuestionIndexAfterLoopLogic = false;

                RecXmlObject recXmlObject = OntoMapCsv.getRecXmlObjectFromCells(headerRow, cells);
                //If we have any invalid flags, should we discard?

                if ((recXmlObject.getTime() * 1000) >= timeCutoff) {
                    System.out.println("going to next task: pwindow size: " + participantWindow.getInternalIndex());
                    //User now on other task
                    //If the above method doesn't work, we can weight the last window higher than the first few.
                    incrementCurrentQuestionIndexAfterLoopLogic = true;

                    //Discard current window, we don't want it because of data misalignment.
                    if (!participantWindow.isFull())
                        participantWindow.flush();
                }
                //Add to windows for task
                participantWindow.add(recXmlObject);

                //control window size and add to taskwindows once size is too large.
                if (participantWindow.isFull()) {
                    //We set all instances to have the classification right/wrong
                    //Any window where the user got it right, is grouped into the good section -> 1
                    //any window where the user got it wrong, is grouped into the bad section -> 0
                    //Hopefully we can then compute a probability that they will get it right
                    //given the current gaze data.
                    Instance windowInstance = participantWindow.toDenseInstance(false);

                    Instances dataset = new Instances("GazeWindowDataset", participantWindow.getAttributeList(false), 1);
                    dataset.insertAttributeAt(new Attribute("correct", nominalValues), dataset.numAttributes() - 1);
                    dataset.setClassIndex(dataset.numAttributes()-1);
                    //Set the nominal class value for each window if is correct [0,1]
                    //Insert the correct attribute/class (it's attribute name will be set when we merge all instances)
                    windowInstance.setDataset(dataset);
                    windowInstance.setValue(windowInstance.numAttributes()-1, rightOrWrongs.get(correctIndex) ? "1" : "0");


                    if (useForTrainInstances) {
                       trainInstanceList.add(windowInstance);
                    }
                    else {
                        testInstanceList.add(windowInstance);
                    }
                    //Clear window data by repointing to new instantiation
                    participantWindow.flush();

                }
                

                if (incrementCurrentQuestionIndexAfterLoopLogic)
                    currentQuestionIndex++;

            }
            fileReader.close();
            csvReader.close();
            System.out.println("processed: " + p.getId());
        }

//        Classifier[] classifiers = WekaExperiment.getClassificationClassifiers();
//        HashMap<String, HashMap<String, Double>> totalResultsOfClassifiers = new HashMap<>();
//        for (Classifier c : classifiers) {
//            totalResultsOfClassifiers.put(c.getClass().getName(), new HashMap<>());
//        }
        int totalNumInstance = 0;

        ArrayList<Attribute> attributeList = Collections.list(trainInstanceList.get(0).enumerateAttributes());

        //Merging all instances together again.
        trainDataInstances = new Instances("OntoMapTrainGaze", attributeList, trainInstanceList.get(0).numAttributes());
        testDataInstances = new Instances("OntoMapTrainGaze", attributeList, trainInstanceList.get(0).numAttributes());

        //Set Class index for last attribute.
        trainDataInstances.setClassIndex(trainDataInstances.numAttributes() - 1);
        testDataInstances.setClassIndex(testDataInstances.numAttributes() - 1);

        for (int i = 1; i < trainInstanceList.size(); ++i) {
            trainDataInstances.add(trainInstanceList.get(i));
        }
        for (int i = 1; i < testInstanceList.size(); ++i) {
            testDataInstances.add(testInstanceList.get(i));
        }

        trainDataInstances.setClassIndex(trainDataInstances.numAttributes() - 1);
        testDataInstances.setClassIndex(testDataInstances.numAttributes() - 1);

        OntoMapCsv.saveInstancesToFile(trainDataInstances, "trainData_"+windowSizeInMilliseconds+"mssec_window_1.arff");
        OntoMapCsv.saveInstancesToFile(testDataInstances, "testData_"+windowSizeInMilliseconds+"msec_window_1.arff");
        /**
        Instances trainDataInstance = allInstances.trainCV(2, 0);
        Instances testDataInstance = trainDataInstance.testCV(2, 0);
        testDataInstance.setClassIndex(testDataInstance.numAttributes() - 1);

        HashMap<String, HashMap<String, Double>> resultsOfClassifiers = MachineLearningExperiments.evaluateAllClassifiers(classifiers, trainDataInstance, testDataInstance);
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
         **/
        //
        //System.out.println("-----------------------------------");
        //System.out.println("--------------Averages-------------");
        //System.out.println("-----------------------------------");
        //
        //for (String classifierName : totalResultsOfClassifiers.keySet()) {
        //    System.out.println("--------classifier: " + classifierName + " -------------");
        //    for (String resultKey : totalResultsOfClassifiers.get(classifierName).keySet()) {
        //        double avgVal = totalResultsOfClassifiers.get(classifierName).get(resultKey) / testDataFiles.size();
        //        System.out.println("key: " + resultKey + " Avg Val: " + avgVal);
        //    }
        //}
        //
        //System.out.printf("%d/%d Process Complete ^^^\n==============================", i + 1, trainDataFiles.size());
        //
    }

    public static RecXmlObject getRecXmlObjectFromCells(List<String> headerRow, String[] cells) {
        RecXmlObject recXmlObject = new RecXmlObject();
        Fixation fixation = analysis.fixation.getFixationFromCSVLine(
                headerRow.indexOf("FPOGD"),
                headerRow.indexOf("FPOGX"),
                headerRow.indexOf("FPOGY"),
                headerRow.indexOf("FPOGID"),
                headerRow.indexOf("FPOGS"),
                cells
        );



        RightEyePupil rightEyePupil = analysis.gaze.getRightEyePupilFromCsvLine(
                headerRow.indexOf("RPCX"),
                headerRow.indexOf("RPCY"),
                headerRow.indexOf("RPS"),
                headerRow.indexOf("RPD"),
                headerRow.indexOf("RPV"),
                cells
        );

        LeftEyePupil leftEyePupil = analysis.gaze.getLeftEyePupilFromCsvLine(
                headerRow.indexOf("LPCX"),
                headerRow.indexOf("LPCY"),
                headerRow.indexOf("LPS"),
                headerRow.indexOf("LPD"),
                headerRow.indexOf("LPV"),
                cells
        );


        BestPointOfGaze bestPointOfGaze = analysis.gaze.getBestPointOfGaze(
                headerRow.indexOf("BPOGX"),
                headerRow.indexOf("BPOGY"),
                headerRow.indexOf("BPOGV"),
                cells
        );
        recXmlObject.setTime(Double.valueOf(cells[headerRow.stream().filter(str -> str.contains("TIME")).map(str -> headerRow.indexOf(str)).findFirst().orElse(-1)]));
        recXmlObject.setFixation(fixation);
        recXmlObject.setBestPointOfGaze(bestPointOfGaze);
        recXmlObject.setLeftEyePupil(leftEyePupil);
        recXmlObject.setRightEyePupil(rightEyePupil);

        //Setting the ID of the fixation to null because we don't want it in our training data.
        //Temp fix, before we introduce annotations to ignore fields in instance construciton
        recXmlObject.FPOGID = null;
        recXmlObject.LPCX=null;
        recXmlObject.LPCY=null;
        recXmlObject.LPS=null;
        recXmlObject.RPCX=null;
        recXmlObject.RPCY=null;
        recXmlObject.RPS=null;
        return recXmlObject;
    }

    public static void saveInstancesToFile(Instances instances, String fileName) throws IOException {
        ArffSaver saver = new ArffSaver();
        saver.setInstances(instances);
        saver.setFile(new File(fileName));
        saver.writeBatch();

    }
}
