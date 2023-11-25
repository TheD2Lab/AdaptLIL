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

import data_classes.Fixation;
import data_classes.Saccade;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class saccade {

	public static Double[] getAllSaccadeLength(ArrayList<Object> allCoordinates) {
		ArrayList<Double> allSaccadeLengths = new ArrayList<Double>();
		int objectSize = allCoordinates.size();
		Double[] allLengths = new Double[(objectSize-1)];
		for(int i=0; i<objectSize; i++){
			Double[] earlyCoordinate = (Double[]) allCoordinates.get(i);

			if(i+1<objectSize){
				Double[] laterCoordinate = (Double[]) allCoordinates.get(i+1);
				if (earlyCoordinate[2] == laterCoordinate[2] - 1)
					allSaccadeLengths.add(Math.sqrt(Math.pow((laterCoordinate[0] - earlyCoordinate[0]), 2) + Math.pow((laterCoordinate[1] - earlyCoordinate[1]), 2)));
			}
		}
		
		allLengths = new Double[allSaccadeLengths.size()];
		return allSaccadeLengths.toArray(allLengths);
		//return allLengths;
	}

	public static Saccade getSaccade(Fixation a, Fixation b) {
		return new Saccade(a.getPoint(), b.getPoint(), b.getDuration() - a.getDuration());
	}

	/**
	 * Calculates saccade duration between sequential fixatiion.
	 * The saccade duration is the duration between two fixations
	 * e.g. given a fixation A that has timestamp T1 and duration D1,
	 * and a subsequent fixation B that has timestamp T2 and duration T2,
	 * the saccade duration between A and B is: T2-(T1+D1)
	 * 
	 * Note: Gazepoint analysis gives three time data values for each fixation.
	 * 	TIME: time elapsed in seconds since the last system initialization
	 * 	TIMETICK: CPU ticks recorded at time as TIME, can be used to synchronize data with other applications
	 * 	FPOGS: The starting time of the fixation POG in seconds since the system initialization or calibration.
	 * Make sure to use FPOGS as the timestamp when calculating saccade duration.
	 * 
	 * @param saccadeDetails start time, fixation duration, and ID for each fixation
	 * @returns list of saccade durations in seconds
	*/
	public static ArrayList<Double> getAllSaccadeDurations(ArrayList<Double[]> saccadeDetails){
		ArrayList<Double> allSaccadeDurations = new ArrayList<>();
		for (int i=0; (i+1)<saccadeDetails.size(); i++){
			Double[] currentDetail = (Double[]) saccadeDetails.get(i);
			Double[] subsequentDetail = (Double[]) saccadeDetails.get(i+1);
			
			if (currentDetail[2] == subsequentDetail[2] - 1) {
				double currentTimestamp = currentDetail[0];
				double currentFixationDuration = currentDetail[1];
				double subsequentTimestamp = subsequentDetail[0];

				double eachSaccadeDuration = subsequentTimestamp - (currentTimestamp + currentFixationDuration);

				allSaccadeDurations.add(eachSaccadeDuration);
			}
		}
		return allSaccadeDurations;
	}
	
	/*
	 * Returns the peak velocity of a given saccade calculated using a two point central difference algorithm.
	 * 
	 * @param	saccadePoints	A list of saccade data points, where each data point is a double array. 
	 * 							[0] = X position
	 * 							[1] = Y position
	 * 							[2] = Time of data point
	 * 
	 * @return	The peak velocity of a saccade
	 */
	public static double getPeakVelocity(ArrayList<Double[]> saccadePoints) {
		if (saccadePoints.size() == 0 || saccadePoints.size() == 1) {
			return 0;
		}
		
		double peakVelocity = 0;
		double conversionRate = 0.0264583333;
		double velocityThreshold = 700; // Maximum possible saccadic velocity
		
		for (int i = 1; i < saccadePoints.size(); i++) {
			Double[] currPoint = saccadePoints.get(i);
			Double[] prevPoint = saccadePoints.get(i - 1);

			double x1 = currPoint[0];
			double y1 = currPoint[1];
			double x2 = prevPoint[0];
			double y2 = prevPoint[1];
			
			int participantDistance = 65; // assume an average distance of 65cm from the participant to the screen
			double distance = Math.sqrt(Math.pow(Math.abs(x1 - x2), 2) + Math.pow(Math.abs(y1 - y2), 2)) * conversionRate;
			double timeDifference = Math.abs(currPoint[2] - prevPoint[2]);
			double amplitude = 180/Math.PI * Math.atan(distance/participantDistance);
			
			double velocity = amplitude/timeDifference;
			
			if (velocity > peakVelocity && velocity <= velocityThreshold) {
				peakVelocity = velocity;
			}
		}
		
		return peakVelocity;
	}
}
