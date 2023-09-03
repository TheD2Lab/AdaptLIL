package xplane12_data_parser;
/*
 * How the Scoring Works
 * For every data point, we will assign 3 possible points (latitude, height, speed)
 * All the measurements will be given the same values: 1 point for latitude, 1 for height, and 1 for speed
 * For every mistake in latitude, height, or speed, the deduction will either be 1/4, 1/2, or 1 point off
 */

import java.io.IOException;

public class scoreCalcuations {

	private int highestScorePossible = 0;
	private double totalScore = 0; // Double because we will subtract decimals from it
	private double percentageScore = 0; // Score out of 100%

	private final int MAX_PTS_PER_DATA_POINT_ILS = 3;
	private final int MAX_PTS_PER_DATA_POINT_ROUNDOUT = 1; 
	private final int MAX_PTS_PER_DATA_POINT_LANDING = 2;

	/**
	 * initializes all the required data
	 */
	public scoreCalcuations()
	{
		// Highest score and totalScore start out at the highest possible score, and then totalScore 
		// decreases with each penalty
		highestScorePossible = parser.getNumOfILSData()*MAX_PTS_PER_DATA_POINT_ILS
				+ parser.getNumOfRoundoutData() * MAX_PTS_PER_DATA_POINT_LANDING
				+ parser.getNumOfLandingData() * MAX_PTS_PER_DATA_POINT_LANDING;
		totalScore = highestScorePossible;
	}


	/**
	 * returns the total score penalty for the localizer portion of the ILS approach
	 * @param double[] horizontalDef is all of the localizer position of the aircraft
	 * @return double Returns the total penalty
	 */
	//For the lateral/vertical of the plane
	//Check if its within +/- 2 degrees of the designated line
	private double localizerScorePenalty(double[]horizontalDef)
	{
		double maxPtPerMethod = MAX_PTS_PER_DATA_POINT_ILS/3;
		double penalty = 0;
		for(int i=0; i<horizontalDef.length; i++)
		{
			if(Math.abs(horizontalDef[i]) < 0.1)
			{
				continue;
			}
			else if(Math.abs(horizontalDef[i]) <= 0.5) // 0.5 degrees
			{
				penalty += 0.25 * maxPtPerMethod;
			}
			else if(Math.abs(horizontalDef[i]) <= 1) // 1 degree
			{
				penalty += 0.50 * maxPtPerMethod;
			}
			else if(Math.abs(horizontalDef[i]) <= 1.5) // 1.5 degrees
			{
				penalty += 0.75 * maxPtPerMethod;
			}
			else if(Math.abs(horizontalDef[i]) > 1.5) // Above 1.5 degrees
			{
				penalty += maxPtPerMethod;
			}
		}
		return penalty;
	}

	/**
	 * returns the total score penalty for the glideslope portion of the ILS approach
	 * @param double[] verticalDef is all of the vertical position of the aircraft
	 * @return double Returns the total penalty
	 */
	//For the lateral/vertical of the plane
	//Check if its within +/- 2 degrees of the designated line
	private double glideSlopeScorePenalty(double[]verticalDef)
	{
		double maxPtPerMethod = MAX_PTS_PER_DATA_POINT_ILS/3;
		double penalty = 0;
		for(int i=0; i<verticalDef.length; i++)
		{
			if(Math.abs(verticalDef[i]) < 0.1)
			{
				continue;
			}
			else if(Math.abs(verticalDef[i]) <= 0.5) // 0.5 degrees
			{
				penalty += 0.25 * maxPtPerMethod;
			}
			else if(Math.abs(verticalDef[i]) <= 1) // 1 degree
			{
				penalty += 0.50 * maxPtPerMethod;
			}
			else if(Math.abs(verticalDef[i]) <= 1.5) // 1.5 degrees
			{
				penalty += 0.75 * maxPtPerMethod;
			}
			else if(Math.abs(verticalDef[i]) > 1.5)
			{
				penalty += maxPtPerMethod; // Above 1.5 degrees 
			}
		}
		return penalty;
	}

	/**
	 * returns the total speed penalty for the speed portion of the ILS approach
	 * @param double[]speed The speed of the aircraft during the ILS approach
	 * @return double Returns the total penalty
	 */
	private double speedILSCalcPenalty(double[] speeds)
	{
		double maxPtPerMethod = MAX_PTS_PER_DATA_POINT_ILS/3;
		double penalty = 0;
		for(double speed: speeds)
		{
			if(89 < speed && speed < 91)
			{
				continue;
			}
			else if(80 < speed && speed < 100)
			{
				penalty += 0.25 * maxPtPerMethod;
			}
			else if(75 < speed && speed < 105)
			{
				penalty += 0.50 * maxPtPerMethod;
			}
			else
			{
				penalty += maxPtPerMethod;
			}
		}
		return penalty;
	}

	/**
	 * returns the total penalty for the ILS approach. Based on the localizer, glideslope, and speed
	 * @param double[] horiDef all of the localizer position of the aircraft
	 * @param double[] speed The speed of the aircraft during the ILS approach
	 * @param double[] vertDef all of the glideslope position of the aircraft
	 * @return double Returns the total penalty
	 */
	public double scoreILSCalc(double[]horiDef, double[]speed, double[]vertDef)
	{	
		double penalty = localizerScorePenalty(horiDef) + glideSlopeScorePenalty(vertDef) + speedILSCalcPenalty(speed);
		return penalty;
	}

	/**
	 * returns the total penalty for the roundout phase. Based on altitude. Looking to see that the plane is continuously descending
	 * @param double[]altitude contains all the altitude information for the aircraft
	 * @return double Returns the total Penalty
	 */
	public double scoreRoundOut(double[]altitude)
	{
		double penalty =0;
		double previousAlt = altitude[0];
		for(int altIndex = 1; altIndex < altitude.length; altIndex++)
		{
			previousAlt = altitude[altIndex];
			if(previousAlt >= altitude[altIndex])
			{
				continue;
			}
			else
			{
				penalty = MAX_PTS_PER_DATA_POINT_ROUNDOUT;
			}

		}
		return penalty;
	}

	/**
	 * returns the total penalty for the landing Phase. Based on centerline and altitude
	 * @param double[]altitude contains all the altitude information for the aircraft
	 * @return double Returns the total Penalty
	 */
	public double scoreLanding(double[]altitude, double[]horizontalDef)
	{
		double maxPtPerMethod = MAX_PTS_PER_DATA_POINT_LANDING/2;
		double penalty = 0; 
		penalty += localizerScorePenalty(horizontalDef);
		for(double alt: altitude)
		{
			if(alt > parser.getFieldElevation())
			{
				if(alt - parser.getFieldElevation() > 5)
				{
					penalty += 0.5 * maxPtPerMethod;
				}
				else
				{
					penalty += maxPtPerMethod;
				}
			}
		}
		return penalty;
	}


	/**
	 * returns the total penalty for the approach and landing
	 * @param double[] horiDef all of the localizer position of the aircraft
	 * @param double[]speed The speed of the aircraft during the ILS approach
	 * @return double Returns the total penalty
	 */
	public void scoreCalc(String outputFolderPath)throws IOException
	{
		double[]horiDefILS = parser.getData(outputFolderPath + "\\ILS_Data.csv", "copN1,h-def");
		double[]speedILS = parser.getData(outputFolderPath + "\\ILS_Data.csv", "_Vind,_kias");
		double[]vertDefILS = parser.getData(outputFolderPath + "\\ILS_Data.csv", "copN1,v-def");
		double[]altRoundOut = parser.getData(outputFolderPath + "\\RoundOut_Data.csv", "p-alt,ftMSL");
		double[]altLanding = parser.getData(outputFolderPath + "\\Landing_Data.csv", "p-alt,ftMSL");
		double[]horiDefLanding = parser.getData(outputFolderPath + "\\Landing_Data.csv", "copN1,h-def");

		totalScore -= scoreILSCalc(horiDefILS,speedILS, vertDefILS) + scoreRoundOut(altRoundOut) + scoreLanding(altLanding,horiDefLanding);
		percentageScore = (totalScore / highestScorePossible) * 100;
	}


	/**
	 * retrieves the highestScore possible
	 * @return returns the score
	 */
	public int getHighestScorePossible() {
		return highestScorePossible;
	}


	/**
	 * able to set the highest score possible
	 * @param highestScorePossible
	 */
	public void setHighestScorePossible(int highestScorePossible) {
		this.highestScorePossible = highestScorePossible;
	}


	/**
	 * able to set the total score
	 * @param totalScore
	 */
	public void setTotalScore(double totalScore) {
		this.totalScore = totalScore;
	}

	/**
	 * able to set the percentage
	 * @param percentageScore
	 */
	public void setPercentageScore(double percentageScore) {
		this.percentageScore = percentageScore;
	}

	/**
	 * retrieves the total score
	 * @return the total score 
	 */
	public double getTotalScore() {
		return totalScore;
	}
	/**
	 * returns the percentage
	 * @return the percent of the score
	 */
	public double getPercentageScore()
	{
		return percentageScore;
	}
}
