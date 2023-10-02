package analysis;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import data_classes.Fixation;
import server.GazeWindow;
import server.gazepoint.api.recv.RecXmlObject;
import weka.core.DenseInstance;

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

        String baseDir = "D:\\datavisstudy-updated\\DataVisStudy\\Participant Data\\";

        //open all participants
        //Filter for baseline (matrix shouldnt be used b/c it's a different chart)
        Map<String, List<File>> gazePointFilesByParticipant = OntoMapCsv.filterForBaseLineFiles(baseDir+"Gazepoint", true, true);
        Map<String, List<File>> taskFilesByParticipant = OntoMapCsv.filterForBaseLineFiles(baseDir+"Task Data", false, true);
        Map<String, Participant> participantsById = new HashMap<>();


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
            testParticipantInstances(participantsById.values().toArray(new Participant[participantsById.size()]));
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        //grab list of classifiers
        //feed data and predict!

    }

    public static void testParticipantInstances(Participant[] participants) throws CsvValidationException, IOException {
        //Use DenseInstance to create on the fly instances.
        //https://weka.sourceforge.io/doc.dev/weka/core/DenseInstance.html
        //foreach participant
        for (Participant p : participants) {
            GazeWindow participantWindow = new GazeWindow(false, 2000);
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
                Boolean rightOrWrong = Boolean.valueOf(cells[correctIndex]);
                timeCutoffs.add(timeCutoff);
                rightOrWrongs.add(rightOrWrong);
            }

            fileReader.close();
            csvReader.close();

            //Read fixation file
            fileReader = new FileReader(p.getAnatomyFixationFile());
            csvReader = new CSVReader(fileReader);
            int currentQuestionIndex = 0;

            List<String> headerRow = Arrays.asList(csvReader.readNext());

            while ((cells = csvReader.readNext()) != null) {
                Float timeCutoff = Float.parseFloat(timeCutoffs.get(currentQuestionIndex)) / 1000;
                //read fixation data up to timecutoff
                Fixation fixation = analysis.fixation.getFixationFromCSVLine(
                        headerRow.indexOf("FPOGD"),
                        headerRow.indexOf("FPOGX"),
                        headerRow.indexOf("FPOGY"),
                        headerRow.indexOf("FPOGID"),
                        headerRow.indexOf("TIME"),
                        cells
                );

                RecXmlObject recXmlObject = new RecXmlObject();
                recXmlObject.FPOGX = (float) fixation.getX();
                recXmlObject.FPOGY = (float) fixation.getY();
                recXmlObject.FPOGID = fixation.getId();
                recXmlObject.FPOGD = (float) fixation.getDuration();
                recXmlObject.time = (float) fixation.getStartTime();
                participantWindow.getGazeData().add(recXmlObject);

                if (fixation.getStartTime() >= timeCutoff) {
                    //Add to instances
                    //Parse window as is and store in instance
                    //User now on other task
                    currentQuestionIndex++;
                    participantWindow.getGazeData().clear();
                } else {
                    //Add to instance
                }
            }

            //Store in fixationData : timeCutoff : rightOrWrong
            //Store in instances


        }

    }
}
