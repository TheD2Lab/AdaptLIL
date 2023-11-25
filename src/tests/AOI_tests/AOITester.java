package tests.AOI_tests;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import analysis.systemLogger;
import analysis.AOI;
import analysis.WindowOperations;

/**
 * AOITester class tests the AOI.processAOI outputs for correctness.
 * Currently only the proportional and transition features are checked.
 * Make sure to compile with assertions enabled.
 */

public class AOITester {

	final static int SCREEN_WIDTH = 1920;
	final static int SCREEN_HEIGHT = 1080;
	private static String fixationFile;
	private static String allGazeFile;
	private static String outputFolder;
	private static String expectedFolder;
	private static String name = "Test";
	public static void main(String[] args) {

		// Change directory names if necessary
		Path currentRelativePath = Paths.get("");
		String currentPath = currentRelativePath.toAbsolutePath().toString() + "/src/tests/AOI_tests/";
		fixationFile = currentPath + "/aoi_test_fixations.csv";
		allGazeFile = currentPath + "/aoi_test_all_gaze.csv";
		outputFolder = currentPath + "/output/";
		new File(outputFolder).mkdirs();
		expectedFolder = currentPath + "/expected_output/";
		systemLogger.createSystemLog(outputFolder);

		generateOutput();
		checkProportionateFeatures();
		checkTransitionFeatures();
		systemLogger.writeToSystemLog(Level.INFO, AOITester.class.getName(), "AOI tests complete.");
	}

	private static void generateOutput() {
		AOI.processAOIs(allGazeFile, fixationFile, outputFolder, name, SCREEN_WIDTH, SCREEN_HEIGHT);
	}

	private static void checkProportionateFeatures() {
		String expectedFile = expectedFolder + "/Expected_aoi_proportionateFeatures.csv";
		String testFile = outputFolder + "/Test_aoi_proportionateFeatures.csv";
		assert compareFiles(expectedFile, testFile) : "Incorrect Proportionate Features";
	}

	private static void checkTransitionFeatures() {
		String expectedFile = expectedFolder + "/Expected_aoi_transitionFeatures.csv";
		String testFile = outputFolder + "/Test_aoi_transitionFeatures.csv";
		assert compareFiles(expectedFile, testFile) : "Incorrect Transition Features";
	}

	private static boolean compareFiles(String expectedFile, String testFile) {
		String eLine;
		String tLine;
		try (
			BufferedReader expectedCSV = new BufferedReader(new FileReader(expectedFile));
			BufferedReader testFileCSV = new BufferedReader(new FileReader(testFile));
		){
			while ((eLine = expectedCSV.readLine()) != null && (tLine = testFileCSV.readLine()) != null) {
				if (!eLine.equalsIgnoreCase(tLine)) {
					return false;
				}
			}
			return true;
		} catch (IOException e) {
			systemLogger.writeToSystemLog(Level.WARNING, AOITester.class.getName(), "Could not compare " + expectedFile + " and " + testFile);
		}
		return false;
	}
}
