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
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This is a hacky one time use to make training data for participants. This is really messy. please dont read it.
 */
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
        //We should do this randomly.
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

    private static List<String> getRandomTestParticipantsIds(int numTestParticipants) {
        String[] testParticipants = new String[numTestParticipants];
        List<String> excludedParticipants = OntoMapCsv.discardedParticipantIds();
        Random random = new Random();
        for (int i = 0; i < numTestParticipants; ++i) {
            int randomId = random.nextInt(1,81);
            String randParticipantId = "P" + randomId;
            while (excludedParticipants.contains(randParticipantId)) {
                randParticipantId = "P" + random.nextInt(1, 81);
            }
            testParticipants[i] = randParticipantId;
        }
        return List.of(testParticipants);
    }

    private static void logTestParticipants(File outputDir, List<String> testParticipantIds) throws IOException {
        FileWriter testParticipantsFile = new FileWriter(outputDir.getAbsolutePath()+"/test_participants.txt");

        for (String participantId : testParticipantIds) {
            testParticipantsFile.write(participantId+"\n");
        }
        testParticipantsFile.close();
    }
    public static void testParticipantInstances(Participant[] participants) throws Exception {
        //Use DenseInstance to create on the fly instances.
        //https://weka.sourceforge.io/doc.dev/weka/core/DenseInstance.html
        //foreach participant
        float windowSizeInMilliseconds = 2000;
        int numParticipantsForTestData = (int) Math.ceil(participants.length * 0.35); // 20% split

        List<Instance> trainInstanceList = new ArrayList<>();
        List<Instance> testInstanceList = new ArrayList<>();
        Instances trainDataInstances = null;
        Instances testDataInstances = null;
        List<String> nominalValues = new ArrayList<>(Arrays.asList("1", "0"));

        List<String> participantIdsForTestDataset = OntoMapCsv.getRandomTestParticipantsIds(numParticipantsForTestData);

        File outputDir = new File("C:\\Users\\nickj\\Downloads\\gazepoint-data-analysis-master\\train_test_data_output\\" + LocalDateTime.now().toString().replace(':', ';'));
        outputDir.mkdirs();
        OntoMapCsv.logTestParticipants(outputDir, participantIdsForTestDataset);
        for (Participant p : participants) {

            boolean useParticipantForTrainingData = !participantIdsForTestDataset.contains(p.getId().toUpperCase());
            GazeWindow participantWindow = new GazeWindow(false, windowSizeInMilliseconds);

            List<String> timeCutoffs = new ArrayList<>();
            List<Boolean> rightOrWrongs = new ArrayList<>();
            File[] answersFiles = new File[]{p.getAnatomyAnswersFile()
//            };
                    , p.getConfAnswersFile()};

            for (File answerFile : answersFiles){
                System.out.println("Answers file: " + answerFile.getName());
                String[] cells = null;
                FileReader answersFileReader = new FileReader(answerFile);
                CSVReader answersCsvReader = new CSVReader(answersFileReader);


                int numCorrect = 0; //used for validation
                int numWrong = 0; // used for validation
                //Retrieve the time the user answered the question and if they got it wrong/right.
                answersCsvReader.readNext(); //Skip first line.
                int timeStampIndex = 8;
                int correctIndex = 6;
                while ((cells = answersCsvReader.readNext()) != null && timeCutoffs.size() < 13) {
                    String timeCutoff = cells[timeStampIndex];
                    Boolean rightOrWrong = cells[correctIndex].trim().contains("1");

                    if (rightOrWrong)
                        numCorrect++;
                    else
                        numWrong++;

                    timeCutoffs.add(timeCutoff);
                    System.out.println("right or wrong: " + (rightOrWrong ? "true" : "false"));
                    rightOrWrongs.add(rightOrWrong);
                }

                answersFileReader.close();
                answersCsvReader.close();

                //Read fixation file
                //@Nick
                //TODO move this over to a method
                //Input: fixxationFile, timecutoffs, wrong or rights, numWrong/numRight (validation)
                //Output: Instance (train or test depends on participant id)
                FileReader fixationFileReader = new FileReader(p.getAnatomyGazeFile());
                CSVReader fixationCsvReader = new CSVReader(fixationFileReader);

                int currentQuestionIndex = 0;
                List<String> headerRow = Arrays.asList(fixationCsvReader.readNext());

                //Loop over each line of gaze data. until we reach the last question and then continue onto next participant.
                while ((cells = fixationCsvReader.readNext()) != null && currentQuestionIndex < rightOrWrongs.size()) { //ignore the multiple choice question for now.
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
                        Attribute correctAttribute = new Attribute("correct", nominalValues);
                        dataset.insertAttributeAt(correctAttribute, dataset.numAttributes());
                        windowInstance.insertAttributeAt(windowInstance.numAttributes()); //Insert a slot for the correct attribute for the window instance
                        dataset.setClassIndex(dataset.attribute(correctAttribute.name()).index());
                        //Set the nominal class value for each window if is correct [0,1]
                        //Insert the correct attribute/class (it's attribute name will be set when we merge all instances)
                        windowInstance.setDataset(dataset);
                        windowInstance.setValue(windowInstance.numAttributes() - 1, rightOrWrongs.get(currentQuestionIndex) ? "1" : "0");

                        if (useParticipantForTrainingData) {
                            trainInstanceList.add(windowInstance);
                        } else {
                            testInstanceList.add(windowInstance);
                        }
                        //Clear window data by repointing to new instantiation
                        participantWindow.flush();

                    }


                    if (incrementCurrentQuestionIndexAfterLoopLogic) {

                        if (rightOrWrongs.get(currentQuestionIndex))
                            numCorrect--;
                        else
                            numWrong--;
                        currentQuestionIndex++;
                    }

                }
                System.out.println(numCorrect == 0 ? " validation passed for numCorrect" : "FAILED numCorrect");
                System.out.println(numCorrect);

                System.out.println(numWrong == 0 ? " validation passed for numWrong" : "FAILED numWrong");
                System.out.println(numWrong);
                fixationFileReader.close();
                fixationCsvReader.close();
            }
            System.out.println("processed: " + p.getId());
        }

//        Classifier[] classifiers = WekaExperiment.getClassificationClassifiers();
//        HashMap<String, HashMap<String, Double>> totalResultsOfClassifiers = new HashMap<>();
//        for (Classifier c : classifiers) {
//            totalResultsOfClassifiers.put(c.getClass().getName(), new HashMap<>());
//        }
        int totalNumInstance = 0;

        ArrayList<Attribute> attributeList = Collections.list(trainInstanceList.get(0).enumerateAttributes());
        attributeList.add(new Attribute("correct", nominalValues)); //Weka Instance for the window will not include the additional attribute added with correct/wrong pairing. Maybe we could add wrong/right to the window for classificaiton in real time.
        //Merging all instances together again.
        trainDataInstances = new Instances("OntoMapTrainGaze", attributeList, trainInstanceList.get(0).numAttributes());
        testDataInstances = new Instances("OntoMapTrainGaze", attributeList, trainInstanceList.get(0).numAttributes());

        //Set Class index for last attribute (correct attr).
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


        OntoMapCsv.saveInstancesToFile(trainDataInstances, outputDir.getPath()+"/trainData_"+windowSizeInMilliseconds+"mssec_window_1.arff");
        OntoMapCsv.saveInstancesToFile(testDataInstances, outputDir.getPath()+"/testData_"+windowSizeInMilliseconds+"msec_window_1.arff");
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
        recXmlObject.FPOGV=null;
        recXmlObject.LPCX=null;
        recXmlObject.LPCY=null;
        recXmlObject.LPS=null;
        recXmlObject.LPV=null;
        recXmlObject.RPCX=null;
        recXmlObject.RPCY=null;
        recXmlObject.RPS=null;
        recXmlObject.RPV=null;
        recXmlObject.BPOGV=null;
        return recXmlObject;
    }

    public static void saveInstancesToFile(Instances instances, String fileName) throws IOException {
        ArffSaver saver = new ArffSaver();
        saver.setInstances(instances);
        saver.setFile(new File(fileName));
        saver.writeBatch();

    }
}
