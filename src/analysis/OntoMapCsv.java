package analysis;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import data_classes.Fixation;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OntoMapCsv {

    private static Map<String, File> filterForBaseLineFiles(String dir, boolean isGazePointFolder, boolean filterForLinkedList) {
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

                            filteredFiles.get(participantName).add(fileOrDir);

                        }
                    }

                } else {
                    //Going through task data directory.
                    Matcher participiantNameMatcher = participantNamePattern.matcher(fileOrDir.getName());
                    String participantName = participiantNameMatcher.group(0);
                    //look through files
                    for (File taskFileOrDir : fileOrDir.listFiles()) {

                        //check if it has .LIL.
                        if (filterForLinkedList && taskFileOrDir.getName().contains("LIL") || (!filterForLinkedList && taskFileOrDir.getName().contains("Matrix"))) {
                            if (!filteredFiles.containsKey(participantName))
                                filteredFiles.put(participantName, new ArrayList<>());

                            filteredFiles.get(participantName).add(fileOrDir);

                        }
                    }
                }

            }
        }
        return filteredFiles;
    }
    public static void main(String[] args) {

        String baseDir = "C:/Users/nickj/Desktop/d2 lab/DataVisStudy/Participant Data/";

        //open all participants
        //Filter for baseline (matrix shouldnt be used b/c it's a different chart)

        //foreach participant
            //open participant directory
            //Create participant object

            //Read fixation data
            //Store fixation data

            //Open participant directory
            //Store answers data

        //foreach participant
            //Read csv for answers
            //Get answers
            //foreach row
                //timeCutoff = csv.column(6)
                //rightOrWrong = csv.column(7)
                //read fixation data up to timecutoff
                //Store in fixationData : timeCutoff : rightOrWrong
            //Store data

        //grab list of classifiers
        //feed data and predict!

    }
}
