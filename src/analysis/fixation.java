package analysis;
/*
 * Copyright (c) 2013, Bo Fu
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.HashMap;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import data_classes.Fixation;


public class fixation {
/**
 *
 //get each fixation's duration
 String fixationDurationSeconds = nextLine[fixationDurationIndex];
 double eachDuration = Double.valueOf(fixationDurationSeconds);
 double fixationID = Double.valueOf(nextLine[fixationIDIndex]);

 //get each fixation's (x,y) coordinates
 String eachFixationX = nextLine[fixationXIndex];
 String eachFixationY = nextLine[fixationYIndex];
 double x = Double.valueOf(eachFixationX) * SCREEN_WIDTH;
 double y = Double.valueOf(eachFixationY) * SCREEN_HEIGHT;

 Point2D.Double eachPoint = new Point2D.Double(x,y);

 Double[] eachCoordinate = new Double[3];
 eachCoordinate[0] = x;
 eachCoordinate[1] = y;
 eachCoordinate[2] = fixationID;
 */
    public static Fixation getFixationFromCSVLine(int durationColIndex, int xColIndex, int yColIndex, int idColIndex, int timestampColIndex, String[] fixationLine) {
        float duration = Float.parseFloat(fixationLine[durationColIndex]);
        float x = Float.parseFloat(fixationLine[xColIndex]);
        float y = Float.parseFloat(fixationLine[yColIndex]);
        int id = Integer.parseInt(fixationLine[idColIndex]);
        float timestamp = Float.parseFloat(fixationLine[timestampColIndex]);
        return new Fixation(x, y, timestamp, duration, true, id);
    }

    public Map<String, Integer> getFixationDataIndices(String[] header) {
        HashMap<String, Integer> indices = new HashMap<>();
        int fixationDurationIndex = Arrays.asList(header).indexOf("FPOGD");
        int fixationIDIndex = Arrays.asList(header).indexOf("FPOGID");
        int fixationXIndex = Arrays.asList(header).indexOf("FPOGX");
        int fixationYIndex = Arrays.asList(header).indexOf("FPOGY");
        int timestampIndex = -1;
        for(int i = 0; i < header.length; i++)
        {
            if(header[i].contains("TIME") && timestampIndex == -1) {
                timestampIndex = i;
            }
        }
        indices.put("fixationDurationIndex", fixationDurationIndex);
        indices.put("fixationIDIndex", fixationIDIndex);
        indices.put("fixationXIndex", fixationXIndex);
        indices.put("fixationYIndex", fixationYIndex);
        indices.put("timestampIndex", timestampIndex);

        return indices;
    }


	public static void processFixationCsvFile(String inputFile, String outputFile, int SCREEN_WIDTH, int SCREEN_HEIGHT) throws IOException, CsvValidationException{

		String line = null;
		ArrayList<Double> allFixationDurations = new ArrayList<>();
		ArrayList<Object> allCoordinates = new ArrayList<>();
		List<Point2D.Double> allPoints = new ArrayList<>();
		ArrayList<Double[]> saccadeDetails = new ArrayList<>();

        FileWriter outputFileWriter = new FileWriter(new File (outputFile));
        CSVWriter outputCSVWriter = new CSVWriter(outputFileWriter);
        try {
        	FileReader fileReader = new FileReader(inputFile);
            CSVReader csvReader = new CSVReader(fileReader);
            String[] nextLine = csvReader.readNext();
            List<String> list = Arrays.asList(nextLine);
            
            int fixationDurationIndex = list.indexOf("FPOGD");
            int fixationIDIndex = list.indexOf("FPOGID");
            int fixationXIndex = list.indexOf("FPOGX");
            int fixationYIndex = list.indexOf("FPOGY");
            int aoiIndex = list.indexOf("AOI");
            int timestampIndex = list.indexOf("FPOGS");
         	
         	HashMap<String, Double> aoiProbability = new HashMap<String, Double>();
         	HashMap<String, HashMap<String, Double>> transitionProbability = new HashMap<String, HashMap<String, Double>>();
         	ArrayList<String> aoiSequence = new ArrayList<String>();
         	String lastAoi = "";
         	
            while((nextLine = csvReader.readNext()) != null) {
                //get each fixation's duration
                String fixationDurationSeconds = nextLine[fixationDurationIndex];
                double eachDuration = Double.valueOf(fixationDurationSeconds);
                double fixationID = Double.valueOf(nextLine[fixationIDIndex]);

				//get each fixation's (x,y) coordinates
				String eachFixationX = nextLine[fixationXIndex];
				String eachFixationY = nextLine[fixationYIndex];
				double x = Double.valueOf(eachFixationX) * SCREEN_WIDTH;
				double y = Double.valueOf(eachFixationY) * SCREEN_HEIGHT;

				Point2D.Double eachPoint = new Point2D.Double(x,y);

				Double[] eachCoordinate = new Double[3];
				eachCoordinate[0] = x;
				eachCoordinate[1] = y;
				eachCoordinate[2] = fixationID;

				//get timestamp of each fixation
				double timestamp = Double.valueOf(nextLine[timestampIndex]);
				Double[] eachSaccadeDetail = new Double[3];
				eachSaccadeDetail[0] = timestamp;
				eachSaccadeDetail[1] = eachDuration;
				eachSaccadeDetail[2] = fixationID;


                allFixationDurations.add(eachDuration);
                allCoordinates.add(eachCoordinate);
                allPoints.add(eachPoint);
                saccadeDetails.add(eachSaccadeDetail);
                
                String aoi = nextLine[aoiIndex];
                aoiSequence.add(aoi);
                
            	if (aoi.equals(""))
            		continue;
            	else if (aoiProbability.containsKey(aoi)) {
            		aoiProbability.put(aoi, aoiProbability.get(aoi) + 1);
            		if (!lastAoi.equals("")) {
            			HashMap<String, Double> relationMatrix = transitionProbability.get(lastAoi);
            			if (relationMatrix.containsKey(aoi)) {
	            			double count = relationMatrix.get(aoi);
	            			relationMatrix.put(aoi, count + 1);
            			} else {
            				relationMatrix.put(aoi, 1.0);
            			}
            		}
            		
            	} else {
            		aoiProbability.put(aoi, 1.0);
        			transitionProbability.put(aoi, new HashMap<String,Double>());
            	}
            	lastAoi = aoi;
            	
            }
            
            int fixationCount = Integer.valueOf(getFixationCount(inputFile));
            for (Map.Entry<String, Double> entry : aoiProbability.entrySet()) {
            	Double AOIFixationCount = entry.getValue();
            	Double probability = AOIFixationCount/fixationCount;
            	entry.setValue(probability);
            }
            
            
            for (Map.Entry<String, HashMap<String, Double>> entry : transitionProbability.entrySet()) {
            	int aoiTransitions = 0;
            	for (Map.Entry<String, Double> edge : entry.getValue().entrySet()) {
            		aoiTransitions += edge.getValue();
            	}
            	for (Map.Entry<String, Double> edge : entry.getValue().entrySet()) {
            		edge.setValue(edge.getValue()/aoiTransitions);
            	}
            }

            ArrayList<String> headers = new ArrayList<>();
            ArrayList<String> data = new ArrayList<>();

            headers.add("total number of fixations");
            data.add(String.valueOf(fixationCount));

			headers.add("sum of all fixation duration");
			data.add(String.valueOf(descriptiveStats.getSumOfDoubles(allFixationDurations)));

			headers.add("mean fixation duration (ms)");
			data.add(String.valueOf(descriptiveStats.getMeanOfDoubles(allFixationDurations)));

			headers.add("median fixation duration (ms)");
			data.add(String.valueOf(descriptiveStats.getMedianOfDoubles(allFixationDurations)));

			headers.add(" StDev of fixation durations (ms)");
			data.add(String.valueOf(descriptiveStats.getStDevOfDoubles(allFixationDurations)));

			headers.add("Min. fixation duration (ms)");
			data.add(String.valueOf(descriptiveStats.getMinOfDoubles(allFixationDurations)));

			headers.add("Max. fixation duration (ms)");
			data.add(String.valueOf(descriptiveStats.getMaxOfDoubles(allFixationDurations)));

			Double[] allSaccadeLengths = saccade.getAllSaccadeLength(allCoordinates);

			headers.add("total number of saccades");
			data.add(String.valueOf(allSaccadeLengths.length));

			headers.add("sum of all saccade length");
			data.add(String.valueOf(descriptiveStats.getSum(allSaccadeLengths)));

			headers.add("mean saccade length");
			data.add(String.valueOf(descriptiveStats.getMean(allSaccadeLengths)));


			headers.add("median saccade length");
			data.add(String.valueOf(descriptiveStats.getMedian(allSaccadeLengths)));

			headers.add("StDev of saccade lengths");
			data.add(String.valueOf(descriptiveStats.getStDev(allSaccadeLengths)));

			headers.add("min saccade length");
			data.add(String.valueOf(descriptiveStats.getMin(allSaccadeLengths)));


			headers.add("max saccade length");
			data.add(String.valueOf(descriptiveStats.getMax(allSaccadeLengths)));

			ArrayList<Double> allSaccadeDurations = saccade.getAllSaccadeDurations(saccadeDetails);

			headers.add("sum of all saccade durations");
			data.add(String.valueOf(descriptiveStats.getSumOfDoubles(allSaccadeDurations)));

			headers.add("mean saccade duration");
			data.add(String.valueOf(descriptiveStats.getMeanOfDoubles(allSaccadeDurations)));

			headers.add("median saccade duration");
			data.add(String.valueOf(descriptiveStats.getMedianOfDoubles(allSaccadeDurations)));


			headers.add("StDev of saccade durations");
			data.add(String.valueOf(descriptiveStats.getStDevOfDoubles(allSaccadeDurations)));

			headers.add("Min. saccade duration");
			data.add(String.valueOf(descriptiveStats.getMinOfDoubles(allSaccadeDurations)));

			headers.add("Max. saccade duration");
			data.add(String.valueOf(descriptiveStats.getMaxOfDoubles(allSaccadeDurations)));

			headers.add("scanpath duration");
			data.add(String.valueOf(getScanpathDuration(allFixationDurations, allSaccadeDurations)));


			headers.add("fixation to saccade ratio");
			data.add(String.valueOf(getFixationToSaccadeRatio(allFixationDurations, allSaccadeDurations)));

			ArrayList<Double> allAbsoluteDegrees = angle.getAllAbsoluteAngles(allCoordinates);

			headers.add("sum of all absolute degrees");
			data.add(String.valueOf(descriptiveStats.getSumOfDoubles(allAbsoluteDegrees)));

			headers.add("mean absolute degree");
			data.add(String.valueOf(descriptiveStats.getMeanOfDoubles(allAbsoluteDegrees)));

			headers.add("median absolute degree");
			data.add(String.valueOf(descriptiveStats.getMedianOfDoubles(allAbsoluteDegrees)));

			headers.add("StDev of absolute degrees");
			data.add(String.valueOf(descriptiveStats.getStDevOfDoubles(allAbsoluteDegrees)));


			headers.add("min absolute degree");
			data.add(String.valueOf(descriptiveStats.getMinOfDoubles(allAbsoluteDegrees)));


			headers.add("max absolute degree");
			data.add(String.valueOf(descriptiveStats.getMaxOfDoubles(allAbsoluteDegrees)));


			ArrayList<Double> allRelativeDegrees = angle.getAllRelativeAngles(allCoordinates);

			headers.add("sum of all relative degrees");
			data.add(String.valueOf(descriptiveStats.getSumOfDoubles(allRelativeDegrees)));

			headers.add("mean relative degree");
			data.add(String.valueOf(descriptiveStats.getMeanOfDoubles(allRelativeDegrees)));

			headers.add("median relative degree");
			data.add(String.valueOf(descriptiveStats.getMedianOfDoubles(allRelativeDegrees)));

			headers.add("StDev of relative degrees");
			data.add(String.valueOf(descriptiveStats.getStDevOfDoubles(allRelativeDegrees)));

			headers.add("min relative degree");
			data.add(String.valueOf(descriptiveStats.getMinOfDoubles(allRelativeDegrees)));


			headers.add("max relative degree");
			data.add(String.valueOf(descriptiveStats.getMaxOfDoubles(allRelativeDegrees)));

			//getting the convex hull using Graham Scan
			//i.e. Choose point p with smallest y-coordinate.
			//Sort points by polar angle with p to get simple polygon.
			//Consider points in order, and discard those that would create a clockwise turn.
			List<Point2D.Double> boundingPoints = convexHull.getConvexHull(allPoints);
			Point2D[] points = listToArray(boundingPoints);

            headers.add("convex hull area");
            data.add(String.valueOf(convexHull.getPolygonArea(points)));
            
            headers.add("Average Peak Saccade Velocity");
            data.add(avgPeakSaccadeVelocity(inputFile, outputFile));
            
            headers.add("Average Blink Rate per Minute");
            data.add(blinkRate(inputFile));
            
            headers.add("stationary entropy");
            data.add(String.valueOf(getStationaryEntropy(aoiProbability)));
            
            headers.add("transition entropy");
            data.add(String.valueOf(getTransitionEntropy(aoiProbability,transitionProbability)));

            outputCSVWriter.writeNext(headers.toArray(new String[headers.size()]));
            outputCSVWriter.writeNext(data.toArray(new String[data.size()]));
            outputCSVWriter.close();
            csvReader.close();
            
            generateAOISequenceFile(aoiSequence, outputFile);

			systemLogger.writeToSystemLog(Level.INFO, fixation.class.getName(), "done writing fixation data to " + outputFile);


		}
		catch(FileNotFoundException ex) 
		{
			systemLogger.writeToSystemLog(Level.WARNING, fixation.class.getName(), "Error with outputFile " + outputFile + "\n" + ex.toString());
		}
		catch(IOException ex) 
		{
			systemLogger.writeToSystemLog(Level.WARNING, fixation.class.getName(), "Error with outputFile " + outputFile + "\n" + ex.toString());
		}
	}

	public static String[] lineToArray(String lineOfData){

		StringTokenizer str = new StringTokenizer(lineOfData);
		String[] values = new String[str.countTokens()];

		while(str.hasMoreElements()) {
			for(int i=0; i<values.length; i++){
				values[i] = (String) str.nextElement();
			}
		}

		return values;
	}

	public static Point2D.Double[] listToArray(List<Point2D.Double> allPoints){
		Point2D.Double[] points = new Point2D.Double[allPoints.size()];
		for(int i=0; i<points.length; i++){
			points[i] = allPoints.get(i);
		}
		return points;
	}

	public static String getFixationCount(String inputFile) throws IOException 
	{
		File file = new File(inputFile);
		FileReader fileReader = new FileReader(file);
		CSVReader csvReader = new CSVReader(fileReader);
		Iterator<String[]> iter = csvReader.iterator();
		int count = -1;
		
		// Iterate through each entry until you reach the last
		// Since every entry is a valid fixation, count the amount of entries
		while (iter.hasNext())
		{
			count++;
			iter.next();
		}
		
		csvReader.close();
		return count + "";
	}
	
	public static String avgPeakSaccadeVelocity(String inputFile, String outputFile) throws IOException 
	{
		File file = new File(inputFile);
		FileReader fileReader = new FileReader(file);
		CSVReader csvReader = new CSVReader(fileReader);
		Iterator<String[]> iter = csvReader.iterator();
		String[] row = new String[0];
		double totalSaccadeVelocity = 0;
		
		// Locate the saccade velocity index
		String[] headers = iter.next();
		int sacVel = Arrays.asList(headers).indexOf("SACCADE_PV");
				
		while (iter.hasNext())
		{
			row = iter.next();			
			double saccadeVelocity = Double.valueOf(row[sacVel]);
			totalSaccadeVelocity += saccadeVelocity;
		}
		
		csvReader.close();
		return (totalSaccadeVelocity/Double.valueOf(getFixationCount(inputFile)) + "");
	}
	
	public static String blinkRate(String inputFile) throws IOException
	{
		File file = new File(inputFile);
		FileReader fileReader = new FileReader(file);
		CSVReader csvReader = new CSVReader(fileReader);
		Iterator<String[]> iter = csvReader.iterator();
		String[] row = new String[0];
		int minutes = 1; 
		int totalBlinks = 0;
		
		// Find the index of the blink rate
		int blinkIndex = Arrays.asList(iter.next()).indexOf("BKPMIN");
			
		while (iter.hasNext())
		{
			row = iter.next();
			
			if(Double.valueOf(row[3]) > minutes)
			{
				totalBlinks += Integer.valueOf(row[blinkIndex]);
				minutes++;
			}
		}
		
		csvReader.close();
		//returns avg blinks per minute
		return (totalBlinks/minutes) + "";
	}

	public static double getScanpathDuration(List<Double> allFixationDurations, List<Double> allSaccadeDurations) {
		double fixationDuration = descriptiveStats.getSumOfDoubles(allFixationDurations);
		double saccadeDuration = descriptiveStats.getSumOfDoubles(allSaccadeDurations);
		return fixationDuration + saccadeDuration;
	}

	public static double getFixationToSaccadeRatio(List<Double> allFixationDurations, List<Double> allSaccadeDurations){
		double fixationDuration = descriptiveStats.getSumOfDoubles(allFixationDurations);
		double saccadeDuration = descriptiveStats.getSumOfDoubles(allSaccadeDurations);
		return fixationDuration/saccadeDuration;
	}
	
	public static double getStationaryEntropy(HashMap<String, Double> aoiProbability) {
		double stationaryEntropy = 0;
		for (Map.Entry<String, Double> entry : aoiProbability.entrySet()) {
			double probability = entry.getValue();
			stationaryEntropy += -probability * Math.log10(probability);
		};
		
		return stationaryEntropy;
	}
	
	public static double getTransitionEntropy(HashMap<String, Double> aoiProbability, HashMap<String,HashMap<String,Double>> transitionMatrix){
		
		
    	double transitionEntropy = 0;
		for (Map.Entry<String,HashMap<String,Double>> entry : transitionMatrix.entrySet()) {
    		double pijSum = 0;
    		for (Map.Entry<String, Double> edge : entry.getValue().entrySet()) {
    			pijSum += edge.getValue() * Math.log(edge.getValue());
    		}
    		transitionEntropy += pijSum * -aoiProbability.get(entry.getKey());
    	}
		
		return transitionEntropy;
	}
	
	public static void generateAOISequenceFile(ArrayList<String> aoiSequence, String outputFile) {
		String participant = outputFile.substring(outputFile.lastIndexOf("/") + 1, outputFile.lastIndexOf("_"));
		String dir = outputFile.substring(0, outputFile.lastIndexOf("/"));
		String sequence = "";
		
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		int asciiTable = 65;
		
		for (String aoi : aoiSequence) {
			if (!map.containsKey(aoi)) {
				map.put(aoi,map.size() + asciiTable);
			}
			
			int ascii = map.get(aoi);
			char c = (char)ascii;
			
			sequence += c;
		}
		
		File aoiDescriptions = new File(dir + "/aoiDescriptions.txt");
		File sequences = new File(dir + "/sequences.txt");
		
		try {
			FileWriter aoiWriter = new FileWriter(aoiDescriptions);
			FileWriter sequencesWriter = new FileWriter(sequences);
			sequencesWriter.write(sequence);
						
			for (String aoi: map.keySet()) {
				int ascii = map.get(aoi);
				char c = (char)ascii;
				
				aoi = aoi.equals("") ? "No AOI" : aoi;
				aoiWriter.write(c + ", " + aoi + "\n");
			}
			
			aoiWriter.close();
			sequencesWriter.close();
			
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}