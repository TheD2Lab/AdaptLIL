package adaptlil.gazepoint.excel;

import adaptlil.Main;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import adaptlil.data_classes.*;
import adaptlil.GazeWindow;
import adaptlil.gazepoint.api.recv.RecXml;
import weka.core.*;
import weka.core.converters.ArffSaver;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This is a hacky one time use to make training data for participants. This file is for historic purposes and data replication.
 * This is not intended for main use as it pertains to creating data for the deep learning model and is dependent on
 * your goals.
 */
public class OntoMapCsv {

    /**
     * List of participantIDs that were discarded from the study.
     * @return
     */
    private static List<String> discardedParticipantIds() {
        return List.of("P9",
                "P18",
                "P21",
                "P28",
                "P31",
                "P33",
                "P41",
                "P44",
                "P57",
                "P75",
                "P66"
        );
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
     * @param filter
     * @return
     */
    private static Map<String, List<File>> mapParticipantsToStudyData(String dir, boolean isGazePointFolder, String filter) {
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

                    if (listContainsStr(discardedParticipantIds, participantName.toUpperCase()))
                        continue;
                    //look through files
                    for (File gpFileORDir : fileOrDir.listFiles()) {
                        //Skip xlsx files
                        if (gpFileORDir.getName().contains("xlsx"))
                            continue;

                        //check if it has .LIL.
                        if (gpFileORDir.getName().contains("LIL") || gpFileORDir.getName().contains("Matrix")) {
                            if (filter.equals("matrix") && gpFileORDir.getName().contains("Matrix"))
                                continue;
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
                    if (listContainsStr(discardedParticipantIds, participantName.toUpperCase()))
                        continue;

                    //look through files of raw data
                    FilenameFilter rawDataFilter = (dir1, name) -> !name.equals("Raw Data");
                    File[] answerFiles = fileOrDir.listFiles(rawDataFilter);
                    for (File taskFileOrDir : answerFiles) {
                        if (taskFileOrDir.getName().contains("xlsx") || taskFileOrDir.isDirectory()) //ignore, cant read this in opencsv
                            continue;

                        //check if it has .LIL.

                        if (taskFileOrDir.getName().toLowerCase().contains("list") || taskFileOrDir.getName().toLowerCase().contains("matrix")) {
                            if (filter.equals("matrix") && taskFileOrDir.getName().contains("matrix"))
                                continue;
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

    public List<String> getParticipantsThatDidntTry() {
        return null;
    }
    public static void main(String[] args) {

        String baseDir = "/home/notroot/Desktop/d2lab/iav/train_test_data_output/DataVisStudy/Participant Data/";

        //open all participants
        //Filter for baseline (matrix shouldnt be used b/c it's a different chart)
        Map<String, List<File>> gazePointFilesByParticipant = OntoMapCsv.mapParticipantsToStudyData(baseDir+"Gazepoint", true, "");
        Map<String, List<File>> taskFilesByParticipant = OntoMapCsv.mapParticipantsToStudyData(baseDir+"Task Data", false, "");
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

    /**
     * Iterate through list of strings and return true if the needle matches.
     * @param haystack
     * @param needle
     * @return
     */
    private static boolean listContainsStr(List<String> haystack, String needle) {
        for(String hay : haystack) {
            if (hay.equals(needle))
                return true;
        }

        return false;
    }
    private static List<String> getRandomTestParticipantsIds(int numTestParticipants, HashMap<String, Participant> mapParticipants) {
        List<String> testParticipants = new ArrayList<>();
        List<String> excludedParticipants = OntoMapCsv.discardedParticipantIds();
        System.out.println("get random participants");
        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < numTestParticipants; ++i) {
            int randomId = random.nextInt(80) + 1;
            String randParticipantId = "P" + randomId;
            while (listContainsStr(excludedParticipants, randParticipantId) || listContainsStr(testParticipants, randParticipantId)
            || !mapParticipants.containsKey(randParticipantId) || mapParticipants.get(randParticipantId).getAnatomyAnswersFile().getName().toLowerCase().contains("matrix") || randParticipantId.equals("P66")) { //also has to bein the appropriate domain!!! so only check the participants that are selected due to previous criteria of domain
                randParticipantId = "P" + (random.nextInt(80) + 1);
            }
            testParticipants.add(randParticipantId);
        }
        return testParticipants;
    }

    private static void logTestParticipants(File outputDir, List<String> testParticipantIds) throws IOException {
        FileWriter testParticipantsFile = new FileWriter(outputDir.getAbsolutePath()+"/test_participants.txt");

        for (String participantId : testParticipantIds) {
            testParticipantsFile.write(participantId+"\n");
        }
        testParticipantsFile.close();
    }
    public static HashMap<String, Participant> mapParticipantsToId(Participant[] participants) {
        HashMap<String, Participant> map = new HashMap<>();
        for (int i = 0; i <participants.length; ++i)
            map.put(participants[i].getId(), participants[i]);
        return map;
    }

    public static void testParticipantInstances(Participant[] participants) throws Exception {
        //Use DenseInstance to create on the fly instances.
        //https://weka.sourceforge.io/doc.dev/weka/core/DenseInstance.html
        //foreach participant
        float windowSizeInMilliseconds = 5000;
        float observablePeriod = 60000;
        int numParticipantsForTestData = (int) Math.ceil(participants.length * 0.2); // 20% split
        Map<String, List<Instance>> participantTrainingInstances = new HashMap<>();
        Map<String, List<Instance>> participantTestInstances = new HashMap<>();
        List<Instance> trainInstanceList = new ArrayList<>();
        List<Instance> testInstanceList = new ArrayList<>();
        Instances trainDataInstances = null;
        Instances testDataInstances = null;
        List<String> nominalValues = new ArrayList<>(Arrays.asList("1", "0"));
        System.out.println();
        HashMap<String, Participant> participantById = OntoMapCsv.mapParticipantsToId(participants);
        List<String> participantIdsForTestDataset = OntoMapCsv.getRandomTestParticipantsIds(numParticipantsForTestData, participantById);
        int numPacketsDiscarded = 0;
        int totalNumPackets = 0;
        System.out.println("before making file");
        File outputDir = new File("/home/notroot/Desktop/d2lab/iav/train_test_data_output/" + LocalDateTime.now().toString().replace(':', ';'));
        File trainOutputDir = new File(outputDir.getAbsolutePath() + "/train");
        File testOutputDir = new File(outputDir.getAbsolutePath() + "/test");
        System.out.println("hello world");

        outputDir.mkdirs();
        trainOutputDir.mkdirs();
        testOutputDir.mkdirs();
        OntoMapCsv.logTestParticipants(outputDir, participantIdsForTestDataset);
        Random random = new Random();
        for (Participant p : participants) {
            System.out.println(p.getId());

            boolean useParticipantForTrainingData = !participantIdsForTestDataset.contains(p.getId().toUpperCase());
            File pOutputDir = null;
            if( useParticipantForTrainingData) {
                pOutputDir = new File(trainOutputDir.getAbsolutePath() + "/" + p.getId());
                new File(pOutputDir.getAbsolutePath() + "/train").mkdirs();
                new File(pOutputDir.getAbsolutePath() + "/validation").mkdirs();
            }
            else {
                pOutputDir = new File(testOutputDir.getAbsolutePath() + "/" + p.getId());
                new File(pOutputDir.getAbsolutePath() + "/retrain").mkdirs();
                new File(pOutputDir.getAbsolutePath() + "/test").mkdirs();
            }

            pOutputDir.mkdirs();
            if (useParticipantForTrainingData) {
                new File(pOutputDir.getAbsolutePath() + "/train").mkdirs();
                new File(pOutputDir.getAbsolutePath() + "/validation").mkdirs();
            } else {
                new File(pOutputDir.getAbsolutePath() + "/retrain").mkdirs();
                new File(pOutputDir.getAbsolutePath() + "/test").mkdirs();
            }
            GazeWindow participantWindow = new GazeWindow(windowSizeInMilliseconds, Main.EnvironmentConfig.EYETRACKER_REFRESH_RATE);


            File[] answersFiles = new File[]{ p.getAnatomyAnswersFile(), p.getConfAnswersFile() };

            for (File answerFile : answersFiles){
                List<String> timeCutoffs = new ArrayList<>();
                List<Boolean> rightOrWrongs = new ArrayList<>();
                List<Instance> questionInstanceList = new ArrayList<>();
                System.out.println("Answers file: " + answerFile.getName());
                String[] cells = null;
                FileReader answersFileReader = new FileReader(answerFile);
                CSVReader answersCsvReader = new CSVReader(answersFileReader);

                List<Integer> questionsForValid = Arrays.asList(random.nextInt(5),random.nextInt(5)+5, random.nextInt(4)+10);

                int numCorrect = 0; //used for validation
                int numWrong = 0; // used for validation
                //Retrieve the time the user answered the question and if they got it wrong/right.
                answersCsvReader.readNext(); //Skip first line.
                int timeStampIndex = 8;
                int correctIndex = 6;
                int questionToStartAt = 0;
                int qidToEndTrainingAt = 14;
                Double timeToComplete = Double.parseDouble(answersCsvReader.readAll().get(17)[11]);
                answersCsvReader.close();
                answersFileReader.close();
                answersFileReader = new FileReader(answerFile);
                answersCsvReader = new CSVReader(answersFileReader);
                answersCsvReader.readNext(); //Skip first line.

                if (timeToComplete < 14)
                    continue;

                while ((cells = answersCsvReader.readNext()) != null && timeCutoffs.size() <= qidToEndTrainingAt) {
                    String timeCutoff = cells[timeStampIndex];
                    Boolean rightOrWrong = cells[correctIndex] != null && cells[correctIndex].trim().contains("1");

                    if (rightOrWrong)
                        numCorrect++;
                    else
                        numWrong++;

                    timeCutoffs.add(timeCutoff);
                    rightOrWrongs.add(rightOrWrong);
                }

                answersFileReader.close();
                answersCsvReader.close();

                FileReader fixationFileReader = new FileReader(answerFile.equals(p.getAnatomyAnswersFile()) ? p.getAnatomyGazeFile() : p.getConfGazeFile());
                CSVReader fixationCsvReader = new CSVReader(fixationFileReader);

                int currentQuestionIndex = 0;
                List<String> headerRow = Arrays.asList(fixationCsvReader.readNext());

                String[] last = new String[0];
                int numWindowsAdded = 0;
                //Loop over each line of gaze data. until we reach the last question and then continue onto next participant.
                while ((cells = fixationCsvReader.readNext()) != null && currentQuestionIndex < rightOrWrongs.size()) { //ignore the multiple choice question for now.
                    //read fixation data up to the timecutoff
                    last=cells;
                    float timeCutoff = Float.parseFloat(timeCutoffs.get(currentQuestionIndex));
                    float questionDuration = currentQuestionIndex == 0 ? timeCutoff : timeCutoff - Float.parseFloat(timeCutoffs.get(currentQuestionIndex - 1));


                    boolean incrementCurrentQuestionIndexAfterLoopLogic = false;
                    RecXml recXml = OntoMapCsv.getRecXmlObjectFromCells(headerRow, cells);
                    ++totalNumPackets;
                    //If we have any invalid flags, should we discard?
                    if (questionDuration < observablePeriod) {
                        ++numPacketsDiscarded;

                        if ((recXml.getTime() * 1000) >= timeCutoff) {

                            System.out.println("question discarded because it is shorter than the observablePeriod: qid: " + currentQuestionIndex);
                            if (rightOrWrongs.get(currentQuestionIndex))
                                numCorrect--;
                            else
                                numWrong--;
                            currentQuestionIndex++;

                            numWindowsAdded = 0;
                            participantWindow.flush();

                        }
                        questionInstanceList.clear();
                            continue;
                    }
                    if ((recXml.getTime() * 1000) >= timeCutoff) {
                        System.out.println("going to next task: pwindow size: " + participantWindow.getInternalIndex());
                        //User now on other task
                        //If the above method doesn't work, we can weight the last window higher than the first few.
                        incrementCurrentQuestionIndexAfterLoopLogic = true;

                        //Discard current window, we don't want it because of data misalignment.
                        if (!participantWindow.isFull() && participantWindow.getInternalIndex() < participantWindow.getWindowSize() - 1)
                            participantWindow.flush();
                    }
                    //Reduce to only adding one window
                    //Add to windows for task
                    participantWindow.add(recXml);


                    //control window size and add to taskwindows once size is too large.
                    if (participantWindow.isFull() && numWindowsAdded < (observablePeriod/windowSizeInMilliseconds)) {
                        //preprocess data before sending it to instances
                        if (currentQuestionIndex >= questionToStartAt && currentQuestionIndex <= qidToEndTrainingAt) {
                            participantWindow.interpolateMissingValues();
                            //We set all instances to have the classification right/wrong
                            //Any window where the user got it right, is grouped into the good section -> 1
                            //any window where the user got it wrong, is grouped into the bad section -> 0
                            //Hopefully we can then compute a probability that they will get it right
                            //given the current gaze data.
                            Instance windowInstance = participantWindow.toDenseInstance(false, false);

                            Instances dataset = new Instances("GazeWindowDataset", participantWindow.getAttributeList(false, false), 1);
                            Attribute correctAttribute = new Attribute("correct", nominalValues);
                            dataset.insertAttributeAt(correctAttribute, dataset.numAttributes());
                            windowInstance.insertAttributeAt(windowInstance.numAttributes()); //Insert a slot for the correct attribute for the window instance
                            dataset.setClassIndex(dataset.attribute(correctAttribute.name()).index());
                            //Set the nominal class value for each window if is correct [0,1]
                            //Insert the correct attribute/class (it's attribute name will be set when we merge all instances)
                            windowInstance.setDataset(dataset);
                            windowInstance.setValue(windowInstance.numAttributes() - 1, rightOrWrongs.get(currentQuestionIndex) ? "1" : "0");


                            questionInstanceList.add(windowInstance);


                            numWindowsAdded += 1;

                        }
                        //Clear window data by repointing to new instantiation
                        participantWindow.flush();

                    }

                    if (incrementCurrentQuestionIndexAfterLoopLogic) {



                        if (rightOrWrongs.get(currentQuestionIndex))
                            numCorrect--;
                        else
                            numWrong--;

                        Instances instancesForFile = OntoMapCsv.listInstanceToInstances(questionInstanceList, nominalValues);
                        if (useParticipantForTrainingData) {
                            String validOrTestSwitch = questionsForValid.contains(currentQuestionIndex) ? "/validation/" : "/train/";
                            OntoMapCsv.saveInstancesToFile(instancesForFile, pOutputDir.getAbsolutePath() + validOrTestSwitch + "/gaze_qid_" + currentQuestionIndex);
                        } else { //Forst test
                            //Switch up 80-20 rule, 20% for prediction and 80% for test.
                            String validOrTestSwitch = questionsForValid.contains(currentQuestionIndex) ? "/retrain/" : "/test/";
                            OntoMapCsv.saveInstancesToFile(instancesForFile, pOutputDir.getAbsolutePath() + validOrTestSwitch + "/gaze_qid_" + currentQuestionIndex);
                        }
                        questionInstanceList.clear();

                        currentQuestionIndex++;
                        numWindowsAdded = 0;
                    }

                }
                System.out.println(numCorrect == 0 ? " validation passed for numCorrect" : "FAILED numCorrect");
                if (numCorrect != 0)
                {
                    System.out.println(last);
                System.out.println("Debug line");
                }
                if (numWrong != 0) {
                    System.out.println("Debug line");
                }
                System.out.println(numCorrect);

                System.out.println(numWrong == 0 ? " validation passed for numWrong" : "FAILED numWrong");
                System.out.println(numWrong);

                fixationFileReader.close();
                fixationCsvReader.close();
            }

            System.out.println("processed: " + p.getId());

        }
        System.out.println("discarded packets: " + numPacketsDiscarded + " total packets: " + totalNumPackets);
        OntoMapCsv.saveTestAndTrainingToOneFile(trainInstanceList, testInstanceList, nominalValues, outputDir);

    }

    public static Instances listInstanceToInstances(List<Instance> instanceList, List<String> targetValues) {
        ArrayList<Attribute> attributeList = Collections.list(instanceList.get(0).enumerateAttributes());
        attributeList.add(new Attribute("correct", targetValues)); //Weka Instance for the window will not include the additional attribute added with correct/wrong pairing. Maybe we could add wrong/right to the window for classificaiton in real time.

        Instances instances = new Instances("OntoMapTrainGaze", attributeList, instanceList.get(0).numAttributes());
        instances.setClassIndex(instances.numAttributes() - 1);

        for (int i = 0; i < instanceList.size(); ++i) {
            instances.add(instanceList.get(i));
        }
        return instances;
    }

    /**
     * Save training and test data to a single file
     * @param trainInstanceList
     * @param testInstanceList
     * @param nominalValues
     * @param outputDir
     * @throws IOException
     */
    public static void saveTestAndTrainingToOneFile(List<Instance> trainInstanceList, List<Instance> testInstanceList, List<String> nominalValues, File outputDir) throws IOException {
        ArrayList<Attribute> attributeList = Collections.list(testInstanceList.get(0).enumerateAttributes());
        attributeList.add(new Attribute("correct", nominalValues)); //Weka Instance for the window will not include the additional attribute added with correct/wrong pairing. Maybe we could add wrong/right to the window for classificaiton in real time.
        //Merging all instances together again.
        Instances trainDataInstances = new Instances("OntoMapTrainGaze", attributeList, trainInstanceList.get(0).numAttributes());
        Instances testDataInstances = new Instances("OntoMapTestGaze", attributeList, testInstanceList.get(0).numAttributes());

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


        OntoMapCsv.saveInstancesToFile(trainDataInstances, outputDir.getPath()+"/trainData.arff");
        OntoMapCsv.saveInstancesToFile(testDataInstances, outputDir.getPath()+"/testData.arff");
    }

    /**
     * Save an individual participant's gaze training/test data to a single file.
     * @param useParticipantForTrainingData
     * @param p
     * @param instanceList
     * @param nominalValues
     * @param answerFile
     * @param outputDir
     * @throws IOException
     */
    public static void saveParticipantDataAsSeparateFile(boolean useParticipantForTrainingData, Participant p, List<Instance> instanceList, List<String> nominalValues, File answerFile, File outputDir) throws IOException {
        ArrayList<Attribute> attributeList = Collections.list(instanceList.get(0).enumerateAttributes());
        attributeList.add(new Attribute("correct", nominalValues)); //Weka Instance for the window will not include the additional attribute added with correct/wrong pairing. Maybe we could add wrong/right to the window for classificaiton in real time.
        Instances instances;
        if (useParticipantForTrainingData) {

            instances = new Instances("OntoMapTrainGaze", attributeList, instanceList.get(0).numAttributes());

            for (int i = 1; i < instanceList.size(); ++i) {
                instances.add(instanceList.get(i));
            }
            instances.setClassIndex(instances.numAttributes() - 1);
            OntoMapCsv.saveInstancesToFile(instances, outputDir.getPath() + "/trainData_" + p.getId() + " " + answerFile.getName() + ".arff");
        } else {
            instances = new Instances("OntoMapTestGaze", attributeList, instanceList.get(0).numAttributes());
            for (int i = 1; i < instanceList.size(); ++i) {
                instances.add(instanceList.get(i));
            }
            instances.setClassIndex(instances.numAttributes() - 1);
            OntoMapCsv.saveInstancesToFile(instances, outputDir.getPath() + "/testData_" + p.getId() + " " + answerFile.getName() + ".arff");
        }
    }

    public static RecXml getRecXmlObjectFromCells(List<String> headerRow, String[] cells) {
        RecXml recXml = new RecXml();
        Fixation fixation = Fixation.getFixationFromCSVLine(
                headerRow.indexOf("FPOGD"),
                headerRow.indexOf("FPOGX"),
                headerRow.indexOf("FPOGY"),
                headerRow.indexOf("FPOGID"),
                headerRow.indexOf("FPOGS"),
                cells
        );



        RightEyePupil rightEyePupil = RightEyePupil.getRightEyePupilFromCsvLine(
                headerRow.indexOf("RPCX"),
                headerRow.indexOf("RPCY"),
                headerRow.indexOf("RPS"),
                headerRow.indexOf("RPD"),
                headerRow.indexOf("RPV"),
                cells
        );

        LeftEyePupil leftEyePupil = LeftEyePupil.getLeftEyePupilFromCsvLine(
                headerRow.indexOf("LPCX"),
                headerRow.indexOf("LPCY"),
                headerRow.indexOf("LPS"),
                headerRow.indexOf("LPD"),
                headerRow.indexOf("LPV"),
                cells
        );


        BestPointOfGaze bestPointOfGaze = BestPointOfGaze.getBestPointOfGaze(
                headerRow.indexOf("BPOGX"),
                headerRow.indexOf("BPOGY"),
                headerRow.indexOf("BPOGV"),
                cells
        );

        recXml.setTime(
                Double.valueOf(
                        cells[headerRow.stream().filter(str -> str.contains("TIME"))
                                .map(str -> headerRow.indexOf(str))
                                .findFirst()
                                .orElse(-1)]
                )
        );

        recXml.setFixation(fixation);
        recXml.setBestPointOfGaze(bestPointOfGaze);
        recXml.setLeftEyePupil(leftEyePupil);
        recXml.setRightEyePupil(rightEyePupil);

        return recXml;
    }

    public static void saveInstancesToFile(Instances instances, String fileName) throws IOException {
        ArffSaver saver = new ArffSaver();
        saver.setInstances(instances);
        saver.setFile(new File(fileName));
        saver.writeBatch();

    }
}
