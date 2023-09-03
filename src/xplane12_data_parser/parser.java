package xplane12_data_parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

public class parser {

	private static int numOfData = 0; 
	private static int numOfILSData = 0; 
	private static int numOfLandingData = 0; 
	private static int numOfRoundoutData =0;
	
	private static int fieldElevation; 
	private static int minimumsAltitude;
	
	/**
	 * initializes the required numbers. Numbers are based on the airport 
	 */
	public static void initializeNumbers()
	{
		//for ILS 34R KSEA
		fieldElevation = 1025;//432.3
		minimumsAltitude = 1090;
	}
	
	/**
	 * parses out only the useful/needed data into a different csv file
	 * @param filePath
	 * @param outputFolderPath
	 * @return String new csv file path
	 */
	public static String parseData(String filePath, String outputFolderPath) throws IOException, CsvValidationException
	{
		String refactoredFilePath = outputFolderPath + "\\Refactored_Data.csv";
		FileWriter outputFileWriter = new FileWriter(new File (refactoredFilePath));
		CSVWriter outputCSVWriter = new CSVWriter(outputFileWriter);
		FileReader fileReader = new FileReader(filePath);
		CSVReader csvReader = new CSVReader(fileReader);
		List<String[]> selectedColumns = new ArrayList<>();
		List<String> columnNames = Arrays.asList(
				"_totl,_time",
				"_Vind,_kias",
				"Vtrue,_ktas",
				"Vtrue,_ktgs",
				"hpath,_true",
				"vpath,__deg",
				"pitch,__deg",
				"_roll,__deg",
				"p-alt,ftMSL",
				"hding,_true",
				"hding,__mag",
				"__mag,_comp",
				"__lat,__deg",
				"__lon,__deg",
				"___CG,ftMSL",
				"copN1,h-def",
				"copN1,v-def"
				);
		int[] columnIndex = new int[columnNames.size()];
		try 
		{
			String[] headers = csvReader.readNext();
			for (int i = 0; i < columnNames.size(); i++) 
			{
				columnIndex[i] = Arrays.asList(headers).indexOf(columnNames.get(i));
				if (columnIndex[i] == -1) 
				{
					throw new IOException("Column not found: " + columnNames.get(i));
				}
			}
			String[] row;
			while ((row = csvReader.readNext()) != null) 
			{
				String[] selectedRow = new String[columnIndex.length];
				for (int i = 0; i < columnIndex.length; i++) 
				{
					selectedRow[i] = row[columnIndex[i]];
				}
				selectedColumns.add(selectedRow);
			}
			outputCSVWriter.writeNext(columnNames.toArray(new String[0])); // write the header row
			outputCSVWriter.writeAll(selectedColumns); // write the selected columns
		} 
		catch (IOException e) 
		{
			System.out.println(e);
		}
		finally
		{
			outputCSVWriter.close();
			csvReader.close();
		}
		return refactoredFilePath;
		
	}

	/**
	 * changes a txt file into a csv file
	 * @param filePath
	 * @param outputFolderPath
	 * @return String the csv file path
	 */
	public static String txtToCSV(String filePath, String outputFolderPath)
	{
		String csvFilePath = outputFolderPath + "\\Reformatted_Data.csv";
		try 
		{
			FileReader fileReader = new FileReader(filePath);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			FileWriter outputFileWriter = new FileWriter(new File (csvFilePath));
			CSVWriter outputCSVWriter = new CSVWriter(outputFileWriter);
			String line = bufferedReader.readLine();
			
			while ((line = bufferedReader.readLine()) != null) 
			{
				String[] fields = line.split("\\|");
				//removes all the spaces from each element
				for (int i = 0; i < fields.length; i++) 
				{
				    fields[i] = fields[i].replaceAll("\\s+", "");
				}
				outputCSVWriter.writeNext(fields);
				numOfData++;
			}
			outputCSVWriter.close();
			bufferedReader.close();
			fileReader.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			System.out.println(e);
		}
		return csvFilePath;
	}


	/**
	 * parses out the phases of the flight into different csv files
	 * @param filePath
	 * @param outputFolderPath
	 */
	public static void parseOutSections(String filePath, String outputFolderPath) throws IOException
	{
		String ILSOutputFilePath = outputFolderPath + "\\ILS_Data.csv";
		String roundOutOutputFilePath = outputFolderPath + "\\RoundOut_Data.csv";
		String landingOutputFilePath = outputFolderPath + "\\Landing_Data.csv";

		FileWriter outputILSFileWriter = new FileWriter(new File (ILSOutputFilePath));
		FileWriter outputroundOutFileWriter = new FileWriter(new File (roundOutOutputFilePath));
		FileWriter outputLandingFileWriter = new FileWriter(new File (landingOutputFilePath));
		CSVWriter outputLandingCSVWriter = new CSVWriter(outputLandingFileWriter);
		CSVWriter outputRoundOutCSVWriter = new CSVWriter(outputroundOutFileWriter);
		CSVWriter outputILSCSVWriter = new CSVWriter(outputILSFileWriter);
		
		FileReader fileReader = new FileReader(filePath);
		CSVReader csvReader = new CSVReader(fileReader);
		try 
		{
			int headerIndex = -1;

				String[] headers = csvReader.readNext();
				for (int i = 0; i < headers.length; i++) 
				{
					if(headers[i].equals("p-alt,ftMSL"))
					{
						headerIndex = i;
					}
				}
				outputILSCSVWriter.writeNext(headers);
				outputRoundOutCSVWriter.writeNext(headers);
				outputLandingCSVWriter.writeNext(headers);
				String[] row;
				
				while ((row = csvReader.readNext()) != null) 
				{
					if(Double.valueOf(row[headerIndex])>minimumsAltitude)
					{
						outputILSCSVWriter.writeNext(row);
						numOfILSData++;
					}
					else if(Double.valueOf(row[headerIndex])>fieldElevation)
					{
						outputRoundOutCSVWriter.writeNext(row);
						numOfRoundoutData++;
					}
					else
					{
						outputLandingCSVWriter.writeNext(row);
						numOfLandingData++;
					}
				}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println(e);
		}
		finally
		{
			outputILSCSVWriter.close();
			outputRoundOutCSVWriter.close();
			outputLandingCSVWriter.close();
			csvReader.close();
		}

	}
	
	/**
	 * retrieves the data that is from the column that matches the selected dataHeading
	 * @param filePath
	 * @param dataHeader
	 * @return double[] an array of the data selected
	 */
	public static double[]getData(String filePath, String dataHeader) throws IOException
	{
		double[]data = new double[numOfData];
		FileReader fileReader = new FileReader(filePath);
		CSVReader csvReader = new CSVReader(fileReader);
		int headerIndex = -1;
		try 
		{
			String[] headers = csvReader.readNext();
			for (int i = 0; i < headers.length; i++) 
			{
				if(headers[i].equals(dataHeader))
				{
					headerIndex = i;
				}
			}
			int index = 0; 
			String[] row;
			while ((row = csvReader.readNext()) != null) 
			{
				data[index] = Double.valueOf(row[headerIndex]);
				index++;
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		finally
		{
			csvReader.close();
		}
		return data;
	}
	/**
	 * @return the numOfData
	 */
	public static int getNumOfData() {
		return numOfData;
	}
	/**
	 * @param numOfData the numOfData to set
	 */
	public static void setNumOfData(int numOfData) {
		parser.numOfData = numOfData;
	}
	/**
	 * @return the numOfILSData
	 */
	public static int getNumOfILSData() {
		return numOfILSData;
	}
	/**
	 * @param numOfILSData the numOfILSData to set
	 */
	public static void setNumOfILSData(int numOfILSData) {
		parser.numOfILSData = numOfILSData;
	}
	/**
	 * @return the numOfLandingData
	 */
	public static int getNumOfLandingData() {
		return numOfLandingData;
	}
	/**
	 * @param numOfLandingData the numOfLandingData to set
	 */
	public static void setNumOfLandingData(int numOfLandingData) {
		parser.numOfLandingData = numOfLandingData;
	}
	/**
	 * @return the numOfRoundoutData
	 */
	public static int getNumOfRoundoutData() {
		return numOfRoundoutData;
	}
	/**
	 * @param numOfRoundoutData the numOfRoundoutData to set
	 */
	public static void setNumOfRoundoutData(int numOfRoundoutData) {
		parser.numOfRoundoutData = numOfRoundoutData;
	}
	/**
	 * @return the fieldElevation
	 */
	public static int getFieldElevation() {
		return fieldElevation;
	}
	/**
	 * @param fieldElevation the fieldElevation to set
	 */
	public static void setFieldElevation(int fieldElevation) {
		parser.fieldElevation = fieldElevation;
	}
	/**
	 * @return the minimumsAltitude
	 */
	public static int getMinimumsAltitude() {
		return minimumsAltitude;
	}
	/**
	 * @param minimumsAltitude the minimumsAltitude to set
	 */
	public static void setMinimumsAltitude(int minimumsAltitude) {
		parser.minimumsAltitude = minimumsAltitude;
	}
	

		
}
