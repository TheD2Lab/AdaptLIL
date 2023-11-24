package analysis;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
public class AOI {

	// Placeholder symbol for when value cannot be calculated

	private String name;
	private ArrayList<String[]> fixData;
	private int numBlinks = 0;
	private double durationViewed = 0;

	private AOI(String name) {
		this.name = name;
		this.fixData = new ArrayList<>();
	}

	private static final String filler = "NaN"; //

	/**
	 * Calculates descriptive gaze measures, transition features, and proportionate features for AOIs from a fixation csv file.
	 * Generates a sub-directory in the output location containing three csv files.
	 * @param rawAllGazeFile raw all gaze csv file
	 * @param cleanFixationFile cleansed fixation csv file
	 * @param outputLocation directory to save files
	 * @param name participant ID
	 * @param SCREEN_WIDTH width in pixels
	 * @param SCREEN_HEIGHT height in pixels
	 */
	public static void processAOIs(String rawAllGazeFile, String cleanFixationFile, String outputLocation, String name, int SCREEN_WIDTH, int SCREEN_HEIGHT) {		
		HashMap<String, AOI> aoiMap = new HashMap<String, AOI>();
		HashMap<String, Integer> aoiTransitions = new HashMap<>();
		Indexes csvIndexes = new Indexes();
		processAllGazeFile(rawAllGazeFile, aoiMap, csvIndexes);

		double[] fixTotals = processFixationFile(cleanFixationFile, aoiMap, aoiTransitions, csvIndexes);
		// create AOI directory
		new File(outputLocation).mkdirs();

		// calculates DGMs
		String aoiFdxResults = outputLocation + name + "_aoi_graphFDXResults.csv";
		writeFDXResults(aoiFdxResults, aoiMap, SCREEN_WIDTH, SCREEN_HEIGHT, csvIndexes, fixTotals[0], fixTotals[1]);

		// calculate transition features
		String transFeatures = outputLocation + name + "_aoi_transitionFeatures.csv";
		writeTransitions(transFeatures, aoiTransitions);

		// calculate proportionate features
		String proportionateFeatures = outputLocation + name + "_aoi_proportionateFeatures.csv";
		writeProportionate(proportionateFeatures, aoiMap, csvIndexes, fixTotals[0], fixTotals[1]);

	}

	/*
	 * calculates descriptive gaze measures and saves to csv file.
	 */
	private static void writeFDXResults(String outputFile, Map<String, AOI> aoiMap, int SCREEN_WIDTH, int SCREEN_HEIGHT,
			Indexes indexes, double totalFixations, double totalFixDuration) {
		
		try (
			// Initializing output writers
			FileWriter outputFileWriter = new FileWriter(outputFile);
			CSVWriter outputCSVWriter = new CSVWriter(outputFileWriter);
		){
			// Initialize list of headers and write it to the output .csv file
			ArrayList<String> headers = new ArrayList<>();
			headers.add("AOI Name");
			
			// fixation columns
			headers.add("total number of fixations");
			headers.add("sum of all fixation duration");
			headers.add("mean fixation duration (s)");
			headers.add("median fixation duration (s)");
			headers.add(" StDev of fixation durations (s)");
			headers.add("Min. fixation duration (s)");
			headers.add("Max. fixation duration (s)");

			// saccade columns
			headers.add("total number of saccades");
			headers.add("sum of all saccade length");
			headers.add("mean saccade length");
			headers.add("median saccade length");
			headers.add("StDev of saccade lengths");
			headers.add("min saccade length");
			headers.add("max saccade length");
			headers.add("sum of all saccade durations (s)");
			headers.add("mean saccade duration (s)");
			headers.add("median saccade duration (s)");
			headers.add("StDev of saccade durations (s)");
			headers.add("Min. saccade duration (s)");
			headers.add("Max. saccade duration (s)");

			// fixation v. saccade
			headers.add("scanpath duration");
			headers.add("fixation to saccade ratio");

			// absolute degrees
			headers.add("sum of all absolute degrees");
			headers.add("mean absolute degree");
			headers.add("median absolute degree");
			headers.add("StDev of absolute degrees");
			headers.add("min absolute degree");
			headers.add("max absolute degree");

			// relative degrees
			headers.add("sum of all relative degrees");
			headers.add("mean relative degree");
			headers.add("median relative degree");
			headers.add("StDev of relative degrees");
			headers.add("min relative degree");
			headers.add("max relative degree");


			headers.add("convex hull area");
			headers.add("Average Peak Saccade Velocity");
			headers.add("Average Blink Rate per Minute");
			
			outputCSVWriter.writeNext(headers.toArray(new String[headers.size()]));

			// Iterate through each AOI and calculate their gaze analytics
			for (AOI aoi : aoiMap.values()) {
				// Data row for output .csv file
				if (aoi.fixData.size() == 0) {
					continue;
				}
				ArrayList<String> data = new ArrayList<>();
				data.add(aoi.name);
				
				ArrayList<Point2D.Double> allPoints = new ArrayList<Point2D.Double>();
				ArrayList<Double> fixationDurations = new ArrayList<Double>();
				ArrayList<Object> allCoordinates = new ArrayList<Object>();
				ArrayList<Double[]> saccadeDetails = new ArrayList<Double[]>();
				
				// Iterate through each AOI data to populate the above lists
				for (int i = 0; i < aoi.fixData.size(); i++) {
					String[] entry = aoi.fixData.get(i);
					
					// Initalize details about each fixation
					double x = Double.valueOf(entry[indexes.xIndex]) * SCREEN_WIDTH;
					double y = Double.valueOf(entry[indexes.yIndex]) * SCREEN_HEIGHT;
					double id = Double.valueOf(entry[indexes.fixIdIndex]);
					double duration = Double.valueOf(entry[indexes.fixDurIndex]);
					double timestamp = Double.valueOf(entry[indexes.timeIndex]);
					
					
					// Add each point to a list
					Point2D.Double point = new Point2D.Double(x, y);
					allPoints.add(point);
					
					// Add each coordinate to a list
					Double[] coordinate = new Double[3];
					coordinate[0] = x;
					coordinate[1] = y;
					coordinate[2] = id;
					allCoordinates.add(coordinate);
					
					// Add each saccade detail into a list
					Double[] saccadeDetail = new Double[3];
					saccadeDetail[0] = timestamp;
					saccadeDetail[1] = duration;
					saccadeDetail[2] = id;

					saccadeDetails.add(saccadeDetail);
					
					// Add duration value to list
					fixationDurations.add(duration);
					
				}
				
				Double[] allSaccadeLengths = saccade.getAllSaccadeLength(allCoordinates);
				ArrayList<Double> allSaccadeDurations = saccade.getAllSaccadeDurations(saccadeDetails);
				ArrayList<Double> allAbsoluteDegrees = angle.getAllAbsoluteAngles(allCoordinates);
				ArrayList<Double> allRelativeDegrees = angle.getAllRelativeAngles(allCoordinates);
				
				
				data.add(String.valueOf(fixationDurations.size()));
				// check that there were fixations
				if (fixationDurations.size() > 0) {
					double aoiTotalDuration = descriptiveStats.getSumOfDoubles(fixationDurations);
					data.add(String.valueOf(aoiTotalDuration));
					data.add(String.valueOf(descriptiveStats.getMeanOfDoubles(fixationDurations)));
					data.add(String.valueOf(descriptiveStats.getMedianOfDoubles(fixationDurations)));
					data.add(String.valueOf(descriptiveStats.getStDevOfDoubles(fixationDurations)));
					data.add(String.valueOf(descriptiveStats.getMinOfDoubles(fixationDurations)));
					data.add(String.valueOf(descriptiveStats.getMaxOfDoubles(fixationDurations)));
				} 
				else {
					for (int i = 0; i < 8; i++) {
						data.add(filler);
					}
				}
				
				// check that there were saccades
				data.add(String.valueOf(allSaccadeLengths.length));
				if (allSaccadeLengths.length > 0) {
					data.add(String.valueOf(descriptiveStats.getSum(allSaccadeLengths)));
					data.add(String.valueOf(descriptiveStats.getMean(allSaccadeLengths)));
					data.add(String.valueOf(descriptiveStats.getMedian(allSaccadeLengths)));
					data.add(String.valueOf(descriptiveStats.getStDev(allSaccadeLengths)));
					data.add(String.valueOf(descriptiveStats.getMin(allSaccadeLengths)));
					data.add(String.valueOf(descriptiveStats.getMax(allSaccadeLengths)));
					data.add(String.valueOf(descriptiveStats.getSumOfDoubles(allSaccadeDurations)));
					data.add(String.valueOf(descriptiveStats.getMeanOfDoubles(allSaccadeDurations)));
					data.add(String.valueOf(descriptiveStats.getMedianOfDoubles(allSaccadeDurations)));
					data.add(String.valueOf(descriptiveStats.getStDevOfDoubles(allSaccadeDurations)));
					data.add(String.valueOf(descriptiveStats.getMinOfDoubles(allSaccadeDurations)));
					data.add(String.valueOf(descriptiveStats.getMaxOfDoubles(allSaccadeDurations)));

					data.add(String.valueOf(fixation.getScanpathDuration(fixationDurations, allSaccadeDurations)));
					data.add(String.valueOf(fixation.getFixationToSaccadeRatio(fixationDurations, allSaccadeDurations)));
				} 
				else {
					for (int i = 0; i < 14; i++) {
						data.add(filler);
					}
				}
				
				// absolute degrees 
				if (allAbsoluteDegrees.size() > 0) {
					data.add(String.valueOf(descriptiveStats.getSumOfDoubles(allAbsoluteDegrees)));
					data.add(String.valueOf(descriptiveStats.getMeanOfDoubles(allAbsoluteDegrees)));
					data.add(String.valueOf(descriptiveStats.getMedianOfDoubles(allAbsoluteDegrees)));
					data.add(String.valueOf(descriptiveStats.getStDevOfDoubles(allAbsoluteDegrees)));
					data.add(String.valueOf(descriptiveStats.getMinOfDoubles(allAbsoluteDegrees)));
					data.add(String.valueOf(descriptiveStats.getMaxOfDoubles(allAbsoluteDegrees)));
				} 
				else {
					for (int i = 0; i < 6; i++) {
						data.add(filler);
					}
				}

				// relative degrees
				if (allRelativeDegrees.size() > 0) {
					data.add(String.valueOf(descriptiveStats.getSumOfDoubles(allRelativeDegrees)));
					data.add(String.valueOf(descriptiveStats.getMeanOfDoubles(allRelativeDegrees)));
					data.add(String.valueOf(descriptiveStats.getMedianOfDoubles(allRelativeDegrees)));
					data.add(String.valueOf(descriptiveStats.getStDevOfDoubles(allRelativeDegrees)));
					data.add(String.valueOf(descriptiveStats.getMinOfDoubles(allRelativeDegrees)));
					data.add(String.valueOf(descriptiveStats.getMaxOfDoubles(allRelativeDegrees)));
				} 
				else {
					for (int i = 0; i < 6; i++) {
						data.add(filler);
					}
				}

				// Calculate the convex hull and its area 
				if (allPoints.size() > 2 ) {
				List<Point2D.Double> boundingPoints = convexHull.getConvexHull(allPoints);
				Point2D[] points = fixation.listToArray(boundingPoints);
				data.add(String.valueOf(convexHull.getPolygonArea(points)));
				} 
				else {
					data.add(filler);
				}
				
				data.add(String.valueOf(getAvgPeakSaccadeVelocity(aoi.fixData, indexes.peakVelocityIndex)));
				data.add(String.valueOf(aoi.getBlinkRate()));
				outputCSVWriter.writeNext(data.toArray(new String[data.size()]));
			}
			systemLogger.writeToSystemLog(Level.INFO, AOI.class.getName(), "Done writing AOI fixation results to " + outputFile);
		}
		catch(FileNotFoundException e) {
			fileNotFoundMessage(outputFile, e);
		}
		catch(IOException e) {
			ioExceptionMessage(outputFile, e);
		}
	}

	/*
	 * Calculates comparative/proportionate AOI features and saves values to csv file.
	 */
	private static void writeProportionate(String outputFile, Map<String, AOI> map, Indexes csvIndexes,
			double totalFixations, double totalFixDuration) {

		ArrayList<String> headers = new ArrayList<>();

		try (
			FileWriter outputFileWriter = new FileWriter(new File (outputFile));
			CSVWriter outputCSVWriter = new CSVWriter(outputFileWriter);
		){
			headers.add("AOI name");
			headers.add("Proportion of fixations spent in AOI");
			headers.add("Proportion of fixations durations spent in AOI");

			outputCSVWriter.writeNext(headers.toArray(new String[headers.size()]));

			for (AOI aoi : map.values()) {
				ArrayList<String> data = new ArrayList<>();
				ArrayList<Double> fixationDurations = new ArrayList<Double>();
				double aoiTotalDuration = 0;				

				for (String[] entry : aoi.fixData) {
					double duration = Double.valueOf(entry[csvIndexes.fixDurIndex]);
					fixationDurations.add(duration);
					aoiTotalDuration += duration;
				}

				data.add(aoi.name);
				data.add(String.valueOf(fixationDurations.size()/totalFixations));
				data.add(String.valueOf(aoiTotalDuration/totalFixDuration));

				// Write the data into the .csv file as a new row
				outputCSVWriter.writeNext(data.toArray(new String[data.size()]));
			}
			systemLogger.writeToSystemLog(Level.INFO, AOI.class.getName(), "Done writing AOI proportionate features to " + outputFile);

		}
		catch(FileNotFoundException e) {
			fileNotFoundMessage(outputFile, e);
		}
		catch(IOException e) {
			ioExceptionMessage(outputFile, e);
		}
	}
	
	/*
	 * Calculates transition AOI features and saves values to a csv file.
	 */
	private static void writeTransitions(String outputFile, Map<String, Integer> aoiTransitions) {

		double totalTrans = getTotalTransitions(aoiTransitions);
		ArrayList<String> headers = new ArrayList<>();

		try (
			FileWriter outputFileWriter = new FileWriter(new File (outputFile));
			CSVWriter outputCSVWriter = new CSVWriter(outputFileWriter);
		){

			headers.add("AOI Pair");
			headers.add("Transition Count");
			headers.add("Transition Proportion");

			outputCSVWriter.writeNext(headers.toArray(new String[headers.size()]));

			for (String key : aoiTransitions.keySet()) {
				ArrayList<String> data = new ArrayList<>();
				data.add(key);
				data.add(aoiTransitions.get(key).toString());
				String proportion = Double.toString(aoiTransitions.get(key)/ totalTrans);
				data.add(proportion);
				outputCSVWriter.writeNext(data.toArray(new String[data.size()]));
			}
			systemLogger.writeToSystemLog(Level.INFO, AOI.class.getName(), "Done writing AOI transition data to " + outputFile );
		}
		catch(FileNotFoundException e) {
			fileNotFoundMessage(outputFile, e);
		}
		catch(IOException e) {
			ioExceptionMessage(outputFile, e);
		}
	}

	/**
	 * Calculates the average peak saccade velocity.
	 * @param data gaze data
	 * @param peakVelocityIndex index of peak saccade velocity
	 * @return double average peak saccade velocity
	 */
	public static double getAvgPeakSaccadeVelocity(ArrayList<String[]> data, int peakVelocityIndex) {
		double total = 0;
		
		for (String[] entry : data) {
			total += Double.parseDouble(entry[peakVelocityIndex]);
		}
		
		return total / data.size();
	}

	/*
	 * Uses raw all gaze file to find number of blinks and duration viewed for each AOI
	 */
	private static void processAllGazeFile(String inputFile, Map<String, AOI> aois, Indexes csvIndexes) {
		try (
			FileReader fileReader = new FileReader(inputFile);
			CSVReader csvReader = new CSVReader(fileReader);
		){
			// Read input CSV file and initalize the column indexes for the data needed
			String[] nextLine = csvReader.readNext();     
			csvIndexes.findIndexes(nextLine);
			
			// Iterate through input file and group points by AOI
			double prevTime = 0;
			double curTime = 0;
			String aoiName = "";    // "" or AOI name
			int blinkID = 0;
			int prevBlinkID = 0;

			while ((nextLine = csvReader.readNext()) != null) {
				aoiName = nextLine[csvIndexes.aoiIndex];
				curTime = Double.valueOf(nextLine[csvIndexes.timeIndex]);

				if (!aoiName.equals("")) {
					// add AOI to map
					if (!aois.containsKey(aoiName))
						aois.put(aoiName, new AOI(aoiName));

					AOI aoi = aois.get(aoiName);

					blinkID = Integer.valueOf(nextLine[csvIndexes.blinkIdIndex]);
					// count blinks
					if (blinkID != 0 && blinkID != prevBlinkID) {
						aoi.numBlinks++;
						prevBlinkID = blinkID;
					}

					// Assumption: entire time was in AOI
					aoi.durationViewed += curTime - prevTime;
					
				}
				prevTime = curTime;  
			}
		}
		catch (FileNotFoundException e){
			fileNotFoundMessage(inputFile, e);
		}
		catch (IOException e) {
			ioExceptionMessage(inputFile, e);
		} catch (CsvValidationException e) {
			csvValidationExceptionMessage(inputFile, e);
		}
	}

	/*
	 * Separates fixations into AOIs. Excludes multi-AOI fixations
	 */
	private static double[] processFixationFile(String inputFile, Map<String, AOI> aoiMap,
			Map<String, Integer> aoiTransitions, Indexes csvIndexes) {
		double[] totals = {0,0}; // number of fixations, total fixation duration
		try (
			FileReader fileReader = new FileReader(inputFile);
			CSVReader csvReader = new CSVReader(fileReader);
		){
			// Read input CSV file and initalize the column indexes for the data needed
			String[] nextLine = csvReader.readNext();
			csvIndexes.findIndexes(nextLine);
			
			// Iterate through input file and group points by AOI

			String prevAoiName = "";   // AOI attributed to last fixation
			String aoiName = "";    // "" or AOI name

			while ((nextLine = csvReader.readNext()) != null) {
				aoiName = nextLine[csvIndexes.aoiIndex];
				totals[0]++;
				totals[1] += Double.valueOf(nextLine[csvIndexes.fixDurIndex]);

				// excludes non-AOIs or multi AOI fixations
				if (aoiMap.containsKey(aoiName)) {
					aoiMap.get(aoiName).fixData.add(nextLine);

					// count AOI transitions
					if (aoiName.equals(prevAoiName)) {
						continue;
					}
					else if (aoiMap.containsKey(prevAoiName)) {
						String aoiPair = prevAoiName + " -> " + aoiName;
						aoiTransitions.put(aoiPair, aoiTransitions.getOrDefault(aoiPair, 0)+1);
					}
				}
				prevAoiName = aoiName;
			}
		}
		catch (FileNotFoundException e) {
			fileNotFoundMessage(inputFile, e);
		}
		catch (IOException e) {
			ioExceptionMessage(inputFile, e);
		} 
		catch (CsvValidationException e) {
			csvValidationExceptionMessage(inputFile, e);
		} 
		return totals;
	}

	/*
	 * calculated blink rate
	 */
	private double getBlinkRate() {
		if (this.durationViewed > 0) {
			return this.numBlinks / (this.durationViewed / 60);
		}
		else {
			return 0;
		}
	}


	/*
	 * returns total number of transitions between all AOIs
	 */
	private static int getTotalTransitions(Map<String, Integer> aoiTransitions) {
		int total = 0;
		for (int value : aoiTransitions.values()) {
			total += value;
		}
		return total;
	}	

	/*
	 * error logging
	 */
	private static void ioExceptionMessage(String fileName, IOException e) {
		systemLogger.writeToSystemLog(Level.WARNING, AOI.class.getName(), "Error reading file " + fileName + "\n" + e.toString());
		System.out.println("Error reading file '" + fileName + "'");
	}

	/*
	 * error logging
	 */
	private static void fileNotFoundMessage(String fileName, FileNotFoundException e) {
		systemLogger.writeToSystemLog(Level.WARNING, AOI.class.getName(), "Unable to open file " + fileName + "\n" + e.toString());
		System.out.println("Unable to open file '" + fileName + "'");
	}

	private static void csvValidationExceptionMessage(String fileName, CsvValidationException e) {
		systemLogger.writeToSystemLog(Level.WARNING, AOI.class.getName(), "Problem reading from " + fileName + "\n" + e.toString());
		System.out.println("Problem reading from '" + fileName + "'");
	}

	/*
	 * Nested class to store column indexes for gaze data.
	 */
	private static class Indexes {
		int aoiIndex = -1;
		int xIndex = -1;
		int yIndex = -1;
		int fixDurIndex = -1;
		int fixIdIndex = -1;
		int timeIndex = -1;
		int peakVelocityIndex = -1;
		int blinkIdIndex = -1;

	/*
	 * Modifies an Indexes object to have indexes are important data columns in the csv file.
	 */
		private void findIndexes(String[] headers) {
		// Locate the indexes for required fields
			for (int i = 0; i < headers.length; i++) {
				String header = headers[i];
				
				switch(header) {
					case "FPOGS":
						this.timeIndex = i;
						break;
					case "AOI":
						this.aoiIndex = i;
						break;
					case "FPOGX":
						this.xIndex = i;
						break;
					case "FPOGY":
						this.yIndex = i;
						break;
					case "FPOGD":
						this.fixDurIndex = i;
						break;
					case "FPOGID":
						this.fixIdIndex = i;
						break;
					case "SACCADE_PV":
						this.peakVelocityIndex = i;
						break;
					case "BKID":
						this.blinkIdIndex = i;
						break;
					default:
						break;
				}
			}
		}
	}

}