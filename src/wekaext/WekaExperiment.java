package wekaext;

// java libraries

import java.beans.PropertyDescriptor;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

// opencsv
import com.opencsv.CSVWriter;

// org.apache.commons
import org.apache.commons.math3.util.Precision;

// weka.jar
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Range;
import weka.core.Utils;

// weka classifiers
import weka.classifiers.*;
import weka.classifiers.bayes.*;
import weka.classifiers.meta.*;
import weka.classifiers.functions.*;
import weka.classifiers.lazy.*;
import weka.classifiers.misc.*;
import weka.classifiers.rules.*;
import weka.classifiers.trees.*;

// weka experiment
import weka.experiment.*;

import javax.swing.*;

public class WekaExperiment {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);

		System.out.print("Enter root directory: ");
		String rootDirectory = in.nextLine();

		boolean classification = false;
		System.out.print("Enter C for classification or R for regression: ");
		String typeOfExperiment = in.nextLine();

		if (typeOfExperiment.equalsIgnoreCase("C")) {
			classification = true;
		}

		try {
			setupExperiment(classification, rootDirectory);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		in.close();

	}

	public static void setupExperiment(boolean classification, String dirLocation) throws Exception {
		// set directory path
		File dirPath = new File(dirLocation);

		// find all arff files
		ArrayList<String> fileNames = new ArrayList<>();

		for (String s : dirPath.list()) {
			if (s.contains("arff")) {
				fileNames.add(s);
			}
		}

		// sort all files by filename
		Collections.sort(fileNames, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return extractInt(o1) - extractInt(o2);
			}

			int extractInt(String s) {
				String num = s.replaceAll("\\D", "");
				// return 0 if no digits found
				return num.isEmpty() ? 0 : Integer.parseInt(num);
			}
		});

		// build classifiers
		Classifier[] classifiers;

		if (classification) {
			classifiers = getClassificationClassifiers();
		} else {
			classifiers = getRegressionClassifiers();
		}

		// run experiment

		ArrayList<ClassifierResult[]> allResults = new ArrayList<>();

		for (int i = 0; i < fileNames.size(); i++) {
			String fileLocation = String.format("%s/%s", dirPath.getAbsolutePath(), fileNames.get(i));
			ResultMatrix matrix = runExperiment(classifiers, fileLocation, classification, false);

			// returns results from classification experiment
			ClassifierResult[] res = getClassificationExperimentResults(classifiers, matrix);

			allResults.add(res);
			System.out.printf("%d/%d Process Complete ^^^\n==============================", i + 1, fileNames.size());
		}

		// saves classification experiment to csv file
		writeResultsToCSV(classifiers, allResults, fileNames,
				String.format("%s/csv_results-%s.csv", dirPath.getParent(), dirPath.getName()));

		System.out.println("==============================\nProcess Complete");

	}

	public static void writeResultsToCSV(Classifier[] classifiers, ArrayList<ClassifierResult[]> results,
			ArrayList<String> fileNames, String fileLocation) {
		// first create file object for file placed at location
		// specified by filepath
		File file = new File(fileLocation);
		try {
			// create FileWriter object with file as parameter
			FileWriter outputfile = new FileWriter(file);

			// create CSVWriter object filewriter object as parameter
			CSVWriter writer = new CSVWriter(outputfile);

			// adding header to csv
			String[] header = new String[fileNames.size() + 1];
			header[0] = "Classifier Name";

			for (int i = 0; i < fileNames.size(); i++) {
				header[i + 1] = fileNames.get(i);
			}

			writer.writeNext(header);

			// add data to csv

			for (int i = 0; i < classifiers.length; i++) {

				String[] data = new String[fileNames.size() + 1];
				data[0] = classifiers[i].getClass().getSimpleName();
				for (int j = 0; j < results.size(); j++) {

					ClassifierResult r = results.get(j)[i];
					int sig = r.getSignificance();
					String score = r.getScore().toString();
					if (sig > 0 || sig < 0) {
						data[j + 1] = String.format("%s,%d", score, sig);
					} else {
						data[j + 1] = score;
					}

				}
				writer.writeNext(data);
			}

			// closing writer connection
			writer.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static Classifier[] getClassificationClassifiers() throws Exception {
		ArrayList<Classifier> classifiers = new ArrayList<>();

		// set baseline classifier here
//		classifiers.add(new ZeroR());

		// bayes
		classifiers.add(new BayesNet());
		classifiers.add(new NaiveBayes());
		classifiers.add(new NaiveBayesMultinomialText());
		classifiers.add(new NaiveBayesUpdateable());

		// functions
		classifiers.add(new Logistic());
		classifiers.add(new MultilayerPerceptron());
		classifiers.add(new SGD());
		classifiers.add(new SGDText());
		classifiers.add(new SimpleLogistic());
		classifiers.add(new SMO());
		classifiers.add(new VotedPerceptron());

//		// lazy
		classifiers.add(new IBk());
		classifiers.add(new KStar());
		classifiers.add(new LWL());
//
//		// meta classifiers
		classifiers.add(new AdaBoostM1());
		classifiers.add(new AttributeSelectedClassifier());
		classifiers.add(new Bagging());
		classifiers.add(new ClassificationViaRegression());
		classifiers.add(new CVParameterSelection());
		classifiers.add(new FilteredClassifier());
		classifiers.add(new IterativeClassifierOptimizer());
		classifiers.add(new LogitBoost());
		classifiers.add(new MultiClassClassifier());
		classifiers.add(new MultiClassClassifierUpdateable());
		classifiers.add(new MultiScheme());
		classifiers.add(new RandomCommittee());
		classifiers.add(new RandomizableFilteredClassifier());
		classifiers.add(new RandomSubSpace());
		classifiers.add(new Stacking());
		classifiers.add(new Vote());
		classifiers.add(new WeightedInstancesHandlerWrapper());
//
//		// misc
		InputMappedClassifier imc = new InputMappedClassifier();
		imc.setOptions(Utils.splitOptions("-M"));
		classifiers.add(imc);
//
//		// rules
		classifiers.add(new DecisionTable());
		classifiers.add(new JRip());
		classifiers.add(new OneR());
		classifiers.add(new PART());

////        // tree
		classifiers.add(new DecisionStump());
		classifiers.add(new HoeffdingTree());
		classifiers.add(new J48());
		classifiers.add(new LMT());
		classifiers.add(new RandomForest());
		classifiers.add(new RandomTree());
		classifiers.add(new REPTree());

		System.out.println("Retrieved classifiers");
		Classifier[] classifiersArr = new Classifier[classifiers.size()];
		return classifiers.toArray(classifiersArr);
	}

	public static Classifier[] getRegressionClassifiers() {
		Classifier[] classifiers = new Classifier[20];

		// set baseline classifier here
		classifiers[0] = new ZeroR();

		// functions
		classifiers[1] = new GaussianProcesses();
		classifiers[2] = new LinearRegression();
		classifiers[3] = new MultilayerPerceptron();
		classifiers[4] = new SimpleLinearRegression();
		classifiers[5] = new SMOreg();

		// meta
		classifiers[6] = new Bagging();
		classifiers[7] = new CVParameterSelection();
		classifiers[8] = new RegressionByDiscretization();

		classifiers[9] = new MultiScheme();
		classifiers[10] = new RandomCommittee();
		classifiers[11] = new RandomizableFilteredClassifier();
		classifiers[12] = new RandomSubSpace();
		classifiers[13] = new Stacking();
		classifiers[14] = new Vote();
		classifiers[15] = new WeightedInstancesHandlerWrapper();

		// rules
		classifiers[16] = new DecisionTable();
		classifiers[17] = new M5Rules();

		// trees
		classifiers[18] = new M5P();
		classifiers[19] = new REPTree();

		return classifiers;
	}

	private static void printClassifierOptions(Classifier classifier) {
		AbstractClassifier c = (AbstractClassifier) classifier;
		StringBuilder classifierOptions = new StringBuilder();

		String classifierName = c.getClass().getSimpleName();
		String[] options = c.getOptions();

		classifierOptions.append(classifierName + ": ");
		if (options.length > 0) {
			for (String s : options) {
				classifierOptions.append(s + " ");
			}
		}
		System.out.println(classifierOptions);
	}

	private static ResultMatrix runExperiment(Classifier[] classifiers, String fileLocation, boolean classification,
			Boolean logData) throws Exception {
		// setup weka.experiment
		Experiment exp = new Experiment();

		exp.setPropertyArray(new Classifier[0]);
		exp.setUsePropertyIterator(true);

		// setup for classification or regression
		SplitEvaluator se = null;
		Classifier sec = null;

		if (classification) {
			classification = true;
			se = new ClassifierSplitEvaluator();
			sec = ((ClassifierSplitEvaluator) se).getClassifier();
		} else {
			se = new RegressionSplitEvaluator();
			sec = ((RegressionSplitEvaluator) se).getClassifier();
		}

		// cross validation
		CrossValidationResultProducer cvrp = new CrossValidationResultProducer();
		cvrp.setNumFolds(10);
		cvrp.setSplitEvaluator(se);

		PropertyNode[] propertyPath = new PropertyNode[2];
		propertyPath[0] = new PropertyNode(se,
				new PropertyDescriptor("splitEvaluator", CrossValidationResultProducer.class),
				CrossValidationResultProducer.class);

		propertyPath[1] = new PropertyNode(sec, new PropertyDescriptor("classifier", se.getClass()), se.getClass());

		exp.setResultProducer(cvrp);
		exp.setPropertyPath(propertyPath);

		// set classifiers here
		exp.setPropertyArray(classifiers);

		DefaultListModel model = new DefaultListModel();

		// set dataset here
		File file = new File(fileLocation);

		model.addElement(file);


		exp.setDatasets(model);

		// *this is important for WEKA experimenter calculations*
		InstancesResultListener irl = new InstancesResultListener();

		irl.setOutputFile(new File(file.getParent() + "/output.csv"));
		exp.setResultListener(irl);

		exp.initialize();
		exp.runExperiment();
		exp.postProcess();

		PairedCorrectedTTester tester = new PairedCorrectedTTester();
		Instances result = new Instances(new BufferedReader(new FileReader(irl.getOutputFile())));

		tester.setInstances(result);
		tester.setSortColumn(-1);

		tester.setRunColumn(result.attribute("Key_Run").index());
		if (classification) {
			tester.setFoldColumn(result.attribute("Key_Fold").index());
		}
		tester.setDatasetKeyColumns(new Range("" + (result.attribute("Key_Dataset").index() + 1)));
		tester.setResultsetKeyColumns(new Range("" + (result.attribute("Key_Scheme").index() + 1) + ","
				+ (result.attribute("Key_Scheme_options").index() + 1) + ","
				+ (result.attribute("Key_Scheme_version_ID").index() + 1)));
		tester.setResultMatrix(new ResultMatrixPlainText());
		tester.setDisplayedResultsets(null);
		tester.setSignificanceLevel(0.05);
		tester.setShowStdDevs(true);

		// experiment results

		if (classification) {
			tester.multiResultsetFull(0, result.attribute("Percent_correct").index());
		} else {
			tester.multiResultsetFull(0, result.attribute("Root_mean_squared_error").index());
		}

		ResultMatrix matrix = tester.getResultMatrix();

		if (logData) {
			System.out.println(matrix.toStringMatrix());
		}

		// delete output file
		irl.getOutputFile().delete();

		return matrix;
	}

	public static ClassifierResult[] getClassificationExperimentResults(Classifier[] classifiers,
			ResultMatrix matrix) {
		ClassifierResult[] classifierResults = new ClassifierResult[classifiers.length];

		for (int i = 0; i < matrix.getColCount(); i++) {

			System.out.println("trying classifier: " + i + ", " + classifiers[i].getClass().getName());
			classifierResults[i] = new ClassifierResult(classifiers[i], Precision.round(matrix.getMean(i, 0), 2),
					matrix.getSignificance(i, 0));

		}

		return classifierResults;
	}

	private static void evaluateAllClassifiers(Classifier[] classifiers, Instances train, Instances test) {
		// classifier evaluations
		ArrayList<String> dnw = new ArrayList<String>();

		for (Classifier c : classifiers) {
			try {
				Evaluation eval = new Evaluation(train);
				eval.crossValidateModel(c, train, 10, new Random(1));
				eval.evaluateModel(c, test);
			} catch (Exception e) {
				e.printStackTrace();
				dnw.add(c.getClass().getSimpleName());

			}
		}

		for (String s : dnw) {
			System.out.println(s);
		}

	}

}
