package xplane12_data_parser;

import java.io.IOException;
import com.opencsv.exceptions.CsvValidationException;

public class Main {

	//Changes in the Airport: All you would need to do is change the field elevation and the minimums as shown in the approach chart\
	//Changes in Aircraft: All aircraft has different approach speed, the speeds for the method named SpeedILSCalcPenalty will need to be changed
	//Changes in the weight of scoring each section: Currently every single method of scoring is weighted the same. If you would like to change this
	//you would only need to change the  MAX_PTS_PER_DATA_POINT_ILS, MAX_PTS_PER_DATA_POINT_ROUNDOUT,  MAX_PTS_PER_DATA_POINT_LANDING
	public static void main(String[] args) throws CsvValidationException, IOException 
	{
		// Create Strings for input file and output file directories
		String txtFilePath = "C:\\Users\\kayla\\OneDrive\\Desktop\\Fall 2022\\Direct Studies\\gazepoint-data-analysis\\data\\Data.txt";
		String outputFolderPath = "C:\\Users\\kayla\\OneDrive\\Desktop\\Fall 2022\\Direct Studies\\gazepoint-data-analysis\\data\\results";
		
		// Parse the CSV files for the data points
		String originalCSVFilePath = parser.txtToCSV(txtFilePath, outputFolderPath);
		String refactoredCSVFilePath = parser.parseData(originalCSVFilePath, outputFolderPath);

		//initializes the start and stop time for the ILS, Roundout, and landing phases
		parser.initializeNumbers();
		parser.parseOutSections(refactoredCSVFilePath, outputFolderPath);
		
		// Calculate the score for the approach to landing
		scoreCalcuations score = new scoreCalcuations();
		score.scoreCalc(outputFolderPath);
		
		// Print out values
		//System.out.println("Data Points = " + score.getDataPoints() + " * 3 = Highest Possible Points = " + score.getHighestScore() + "\n");
		System.out.println(score.getTotalScore() + " / " + score.getHighestScorePossible() + ":");
		System.out.println(" %" + score.getPercentageScore());
		
	}
	
}