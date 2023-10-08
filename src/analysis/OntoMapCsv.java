package analysis;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import data_classes.Fixation;
import server.GazeWindow;
import server.MachineLearningExperiments;
import server.gazepoint.api.recv.RecXmlObject;
import weka.classifiers.Classifier;
import weka.core.*;
import weka.core.converters.ArffSaver;
import wekaext.WekaExperiment;

import javax.mail.Part;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OntoMapCsv {

    private static Map<String, List<File>> filterForBaseLineFiles(String dir, boolean isGazePointFolder, boolean filterForLinkedList) {
        Map<String, List<File>> filteredFiles = new HashMap<>();
        File directoryFile = new File(dir);
        Pattern participantNamePattern = Pattern.compile("P\\d+");

        for (File fileOrDir : directoryFile.listFiles()) {
            if (fileOrDir.isDirectory()) {

                //Expand
                //check if it is p
                if (isGazePointFolder) {
                    String participantName = fileOrDir.getName();

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
        Map<String, List<File>> gazePointFilesByParticipant = OntoMapCsv.filterForBaseLineFiles(baseDir+"Gazepoint", true, true);
        Map<String, List<File>> taskFilesByParticipant = OntoMapCsv.filterForBaseLineFiles(baseDir+"Task Data", false, true);
        Map<String, Participant> participantsById = new HashMap<>();


        int index = 0;
        for (String participantName : gazePointFilesByParticipant.keySet()) {
            if ( (index++ % 2 == 0) || (index % 3 == 0))
                continue;

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
        List<Instances> instancesList = new ArrayList<>();
        for (Participant p : participants) {
            GazeWindow participantWindow = new GazeWindow(false, windowSizeInMilliseconds);
            //Read csv for answers
            FileReader fileReader = null;
            List<String> timeCutoffs = new ArrayList<>();
            List<Boolean> rightOrWrongs = new ArrayList<>();
            List<DenseInstance> instances = new ArrayList<>();
            fileReader = new FileReader(p.getAnatomyAnswersFile());
            CSVReader csvReader = new CSVReader(fileReader);
            String[] cells = null;
            //Get answers
            //foreach row
            String[] nextLine = csvReader.readNext();
            int timeStampIndex = 8;
            int correctIndex = 6;
            while((cells = csvReader.readNext()) != null) {
                String timeCutoff = cells[timeStampIndex];

                Boolean rightOrWrong = cells[correctIndex].equals("1") ? true : false;
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
            List<GazeWindow> taskWindows = new ArrayList<>();
            while ((cells = csvReader.readNext()) != null && currentQuestionIndex < timeCutoffs.size() - 2) { //ignore the multiple choice question for now.
                Float timeCutoff = Float.parseFloat(timeCutoffs.get(currentQuestionIndex));
                //read fixation data up to timecutoff
                Fixation fixation = analysis.fixation.getFixationFromCSVLine(
                        headerRow.indexOf("FPOGD"),
                        headerRow.indexOf("FPOGX"),
                        headerRow.indexOf("FPOGY"),
                        headerRow.indexOf("FPOGID"),
                        headerRow.stream().filter( str -> str.contains("TIME")).map(str -> headerRow.indexOf(str)).findFirst().orElse(-1),
                        cells
                );

                RecXmlObject recXmlObject = new RecXmlObject();
                recXmlObject.FPOGX = (float) fixation.getX();
                recXmlObject.FPOGY = (float) fixation.getY();
                //recXmlObject.FPOGID = fixation.getId();
                recXmlObject.FPOGD = (float) fixation.getDuration();
                recXmlObject.time = (float) fixation.getStartTime();

                //control window size and add to taskwindows once size is too large.
                if (participantWindow.getGazeData().size() >= participantWindow.getWindowSize()) {
                    taskWindows.add(participantWindow);
                    participantWindow = new GazeWindow(false, windowSizeInMilliseconds);
                    //Add to windows for task
                }

                participantWindow.getGazeData().add(recXmlObject);

                if ( (fixation.getStartTime() * 1000) >= timeCutoff) {
                    //Add to instances
                    //Parse window as is and store in instance
                    //User now on other task

                    //If the above method doesn't work, we can weight the last window higher than the first few.
                    currentQuestionIndex++;

                    //Add all instances for all windows for all tasks

                    for (GazeWindow window : taskWindows) {
                        //We set all instances to have the classification right/wrong
                        //Any window where the user got it right, is grouped into the good section -> 1
                        //any window where the user got it wrong, is grouped into the bad section -> 0
                        //Hopefully we can then compute a probability that they will get it right
                        //given the current gaze data.
                        Instances windowInstances = window.toDenseInstance();
                        List<String> nominalValues = new ArrayList<>();
                        nominalValues.add("1");
                        nominalValues.add("0");
                        Attribute correctAttr = new Attribute("correct", nominalValues);
                        windowInstances.insertAttributeAt(correctAttr, windowInstances.numAttributes());
                        for (int i = 0; i < windowInstances.numInstances(); ++i) {
                            windowInstances.get(i).setValue(windowInstances.numAttributes() - 1, rightOrWrongs.get(currentQuestionIndex) ? "1" : "0");
                            windowInstances.set(i, windowInstances.get(i));
                        }

                        Instance firstInstance = windowInstances.get(0);
                        Instance lastInstance =  windowInstances.get(windowInstances.numInstances() - 1);
                        System.out.println("shape:  " + windowInstances.size() + " x "  + windowInstances.get(0).numAttributes()
                                + " time difference: " + (lastInstance.value(3) - firstInstance.value(3)));
                        instancesList.add(windowInstances);
                    }

                    //Clear gaze data and task windows
                    participantWindow.getGazeData().clear();
                    taskWindows.clear();
                } else {
                    //Add to instance

                }
            }

            //Store in fixationData : timeCutoff : rightOrWrong
            //Store in instances


        }


        Classifier[] classifiers = WekaExperiment.getClassificationClassifiers();
        HashMap<String, HashMap<String, Double>> totalResultsOfClassifiers = new HashMap<>();
        for (Classifier c : classifiers) {
            totalResultsOfClassifiers.put(c.getClass().getName(), new HashMap<>());
        }
        int totalNumInstance = 0;
        Instances allInstances = instancesList.get(0);
        for (int i = 1; i < instancesList.size(); ++i) {
            allInstances.addAll(instancesList.get(i));
        }

        allInstances.setClassIndex(allInstances.numAttributes() - 1);

        OntoMapCsv.saveInstancesToFile(allInstances, "dataDump.arff");
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

    public static void saveInstancesToFile(Instances instances, String fileName) throws IOException {
        ArffSaver saver = new ArffSaver();
        saver.setInstances(instances);
        saver.setFile(new File(fileName));
        saver.writeBatch();

    }
}
