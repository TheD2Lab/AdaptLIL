package adaptlil.mediator;

import adaptlil.adaptations.Adaptation;
import adaptlil.adaptations.DeemphasisAdaptation;
import adaptlil.adaptations.HighlightingAdaptation;
import adaptlil.Main;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import adaptlil.GazeWindow;
import adaptlil.websocket.VisualizationWebsocket;
import adaptlil.gazepoint.api.GazepointSocket;
import adaptlil.gazepoint.api.recv.RecXml;
import adaptlil.http.KerasServerCore;
import adaptlil.websocket.request.AdaptationInvokeRequestModelWs;

import java.util.*;

/**
 * Loosely follows Mediator design pattern of a subsystem of components. It directs and controls how the adaptlil.adaptations are invoked and altered, how they are sent to the
 * frontend, and the classification that occurs by analyzing the gaze window.
 *
 * TODO:
 * Tidy up to better map # classifications/conditions -> rule-based selection process.
 */
public class AdaptationMediator extends Mediator {
    private VisualizationWebsocket websocket;
    private GazepointSocket gazepointSocket;
    private KerasServerCore kerasServerCore;
    private GazeWindow gazeWindow;
    private boolean isRunning;

    private Adaptation observedAdaptation;
    Map<String, Adaptation> currentAdaptations;


    //Thresholds & Constants
    private double thresholdForInvokation;
    private double increaseStrengthThresh = 0.41; //TODO replace w/ # classifications
    private double remainThresh = 0.6; //TODO replace w/ # classifications

    private int numSequencesForClassification;
    private int newAdaptationStartSequenceIndex = 0;
    private int curSequenceIndex = 0;
    private double defaultStrength = 0.50;

    public AdaptationMediator(VisualizationWebsocket websocket, GazepointSocket gazepointSocket, KerasServerCore kerasServerCore, GazeWindow gazeWindow, int numSequencesForClassification) {
        this.websocket = websocket;
        this.gazepointSocket = gazepointSocket;
        this.kerasServerCore = kerasServerCore;
        this.gazeWindow = gazeWindow;
        this.isRunning = false;
        this.currentAdaptations = new HashMap<>();
        this.thresholdForInvokation = 0.6;
        this.numSequencesForClassification = numSequencesForClassification;
    }


    /**
     * Starts the entire adaptation mediator process. GazeWindow data will be collected, predicted on, then processed through rule-based selection process and finally an adaptation
     * will be invoked.
     */
    public void start() {
        this.websocket.setMediator(this);
        this.gazepointSocket.setMediator(this);
        this.gazeWindow.setMediator(this);

        boolean runAdapations = true;
        int numAttributes = 8;
        int frameCount = 0;

        List<INDArray> gazeWindowINDArrays = new ArrayList<>(this.numSequencesForClassification);
        Main.runTimeLogfile.printAndLog("mediator started");
        int[] classifications = new int[5];
        int classificationIndex = 0;
        while (runAdapations) {

            //No websockets connected, do not begin reading from gaze buffer. (Note, we may need to flush buffer once we connect for the first time)
            if (this.websocket.getWebSockets().isEmpty())
                continue;

            if (frameCount == 0) //Flush the gaze queue to make sure data is fresh!
                this.gazepointSocket.getGazeDataQueue().flush();

            RecXml recXml = this.gazepointSocket.readGazeDataFromBuffer();

            //Add to windows for task
            this.gazeWindow.add(recXml);

            if (gazeWindow.isFull()) {
                Main.runTimeLogfile.printAndLog("Gaze window full");

                gazeWindow.interpolateMissingValues();
                gazeWindowINDArrays.add(gazeWindow.toINDArray(false));

                if (gazeWindowINDArrays.size() == this.numSequencesForClassification) {
                    this.curSequenceIndex++;
                    Main.runTimeLogfile.printAndLog("Predicting...");

                    INDArray classificationInput = this.formatGazeWindowsToModelInput(gazeWindowINDArrays);
                    int taskSuccessClassification = this.classifyTaskSuccess(classificationInput);

                    Main.runTimeLogfile.printAndLog("Sequence: " + classificationIndex + " predicted as: " + taskSuccessClassification);
                    classifications[classificationIndex++] = taskSuccessClassification;

                    if (classificationIndex == classifications.length) {
                        Main.runTimeLogfile.printAndLog("# of classifications to begin intervention has occured : " + classificationIndex);
                        this.countTaskSuccessPredictionsAndInvokeAdaptation(classifications);
                        classificationIndex = 0;
                    }

                    gazeWindowINDArrays.clear();
                }

                gazeWindow.flush();
            } else { //Do nothing, loopback
                continue;
            }
            frameCount++;
        }

    }


    /**
     * Counts # task success predictions and runs through the rule-based adaptation selection
     * @param classifications
     */
    private void countTaskSuccessPredictionsAndInvokeAdaptation(int[] classifications) {
        int numClassOne = this.countTaskSuccess(classifications);
        Main.runTimeLogfile.printAndLog("# class of 1: " + numClassOne);
        Main.adaptationLogFile.logLine("#Classifications," +numClassOne + "/5,"+ System.currentTimeMillis());
        this.runRuleBasedAdaptationSelectionProcess((double) numClassOne / classifications.length);
    }


    /**
     * Counts Number of task success ocurrences (i.e. classification[i] == 1)
     * @param classifications
     * @return
     */
    private int countTaskSuccess(int[] classifications) {
        int numClassOne = 0;
        for (int classification : classifications)
            if (classification == 1)
                numClassOne++;
        return numClassOne;
    }


    /**
     * Classifies user's task success by sending the input to the python server and subsequently the deep learning model.
     * @param input  shape=(1,this.numSequencesForClassification, Gaze Attributes)
     * @return
     */
    public int classifyTaskSuccess(INDArray input) {
        Double probability = kerasServerCore.predict(input).getOutput().getDouble(0);
        int classificationResult = probability >= 0.5 ? 1 : 0 ;

        Main.adaptationLogFile.logLine("Prediction," +probability + "," +classificationResult+","+ System.currentTimeMillis());

        return classificationResult;
    }


    /**
     * Formats collected gazewindows into the deep learning model's input format. Uses INDArray for better performance.
     * @param gazeWindows
     * @return
     */
    public INDArray formatGazeWindowsToModelInput(List<INDArray> gazeWindows) {
        INDArray unshapedData = Nd4j.stack(0, gazeWindows.get(0), gazeWindows.get(1));

        return unshapedData.reshape(
                new int[] {
                        1, //Only feed 1 block of sequences at a time.
                        this.numSequencesForClassification, // Num sequences to feed\
                        (int) gazeWindows.get(0).shape()[0], //Num attributes per sequence
                }
            );
    }


    /**
     * Invokes a new type of adaptation and resets the state of the previous adaptation (so that if it circles back it starts at its default state)
     */
    public void invokeNewAdaptationType() {
        Adaptation nextAdaptation = this.getNewAdaptation();
        if (this.observedAdaptation != null) {
            this.observedAdaptation.setBeingObservedByMediator(false);
            //Cycle through if we pick the same one as is being observed.
            while (nextAdaptation.getType().equals(this.observedAdaptation.getType()))
                nextAdaptation = this.getNewAdaptation();
        }

        this.observedAdaptation = nextAdaptation;
        this.observedAdaptation.setBeingObservedByMediator(true);
        this.currentAdaptations.put(observedAdaptation.getType(), observedAdaptation);

        this.newAdaptationStartSequenceIndex = this.curSequenceIndex;
        this.websocket.invoke(new AdaptationInvokeRequestModelWs(this.observedAdaptation));
        Main.runTimeLogfile.printAndLog("invoke new adaptation");
    }

    /**
     * Get a new adaptation type
     * @return
     */
    private Adaptation getNewAdaptation() {
        Random rand = new Random();
        List<Adaptation> adaptations = this.listOfAdaptations();
        int randomAdaptationIndex = rand.nextInt(adaptations.size());

        return adaptations.get(randomAdaptationIndex);
    }

    /**
     * Returns the list of possible adaptation types.
     * @return
     */
    public List<Adaptation> listOfAdaptations() {
        ArrayList<Adaptation> adaptations = new ArrayList<>();
        adaptations.add(new DeemphasisAdaptation(true, null, defaultStrength));
        adaptations.add(new HighlightingAdaptation(true, null, defaultStrength));
        return adaptations;
    }


    /**
     * Rule-Based adaptation selection process that determines the appropriate action to take in terms of
     * invoking a new adaptation type, adjusting adaptation strength, or doing nothing.
     * @param curRiskScore
     */
    private void runRuleBasedAdaptationSelectionProcess(double curRiskScore) {
        double stepAmount = 0.25;
        int numSequencesSinceNewAdaptation = this.curSequenceIndex - this.newAdaptationStartSequenceIndex;
        int gracePeriodByNumSequences = 5 * 24; //120 second grace period before invoking a new adaptation.
        //Case 0
        if (curRiskScore <= 0.0 && (this.currentAdaptations.isEmpty() || numSequencesSinceNewAdaptation > gracePeriodByNumSequences)) {
            this.invokeNewAdaptationType();
            Main.runTimeLogfile.printAndLog("Added new adaptation because score was zero: " + this.observedAdaptation.getType());
        } else if (curRiskScore > 0.0){ //Has adaptovis.adaptations, review the currently observed one.
            if (!this.currentAdaptations.isEmpty()) {  //Has an adaptation, we can review it and compare to the base line (presumed that it's always classifcations: [0_0,0_1,...,0_n]

                //Case 1
                if (curRiskScore < this.increaseStrengthThresh) { //Adaptation Risk increased.
                    //Select new adaptation
                    if (this.observedAdaptation.hasFlipped() && (this.observedAdaptation.getStrength() <= 0.0 || this.observedAdaptation.getStrength() >= 1.0)) { //Cannot increase/decrease strenth of adaptation, must select a new one (c2.a)
                        this.observedAdaptation.flipDirection();
                        this.observedAdaptation.setStrength(defaultStrength);
                        this.invokeNewAdaptationType();
                    }
                    else if (!this.observedAdaptation.hasFlipped() && (this.observedAdaptation.getStrength() <= 0.0 || this.observedAdaptation.getStrength() >= 1.0)) //(c2.b)
                    {
                        this.observedAdaptation.flipDirection(); //Flip direction
                        this.observedAdaptation.applyStyleChange(stepAmount);
                        this.sendAdaptationToVisualization();
                    }
                    //or iterate over current adaptation
                } else if (curRiskScore >= this.increaseStrengthThresh && curRiskScore <= this.remainThresh) { //Case 2

                    //Check strength,
                    //If we can increase strength, do so in same direction
                    if (this.observedAdaptation.getStrength() > 0.0 && this.observedAdaptation.getStrength() <= 1.0) { //Not at max, increase adaptation strength (c2.a)
                        this.observedAdaptation.applyStyleChange(stepAmount);
                        this.sendAdaptationToVisualization();
                    } else { //(c2.b)
                        //Do nothing, reached max. Continue onwards.
                    }

                }
            }
        }
    }


    /**
     * Sends the adaptation to the visualization/frontend via the websocket.
     */
    private void sendAdaptationToVisualization() {
        this.websocket.invoke(new AdaptationInvokeRequestModelWs(this.observedAdaptation));
    }


    public Adaptation getObservedAdaptation() {
        return this.observedAdaptation;
    }

    public VisualizationWebsocket getWebsocket() {
        return websocket;
    }

    public GazepointSocket getGp3Socket() {
        return gazepointSocket;
    }

    public KerasServerCore getClassifierModel() {
        return kerasServerCore;
    }

    public GazeWindow getGazeWindow() {
        return gazeWindow;
    }

    public void setWebsocket(VisualizationWebsocket websocket) {
        this.websocket = websocket;
    }

    public void setGp3Socket(GazepointSocket gazepointSocket) {
        this.gazepointSocket = gazepointSocket;
    }

    public void setClassifierModel(KerasServerCore kerasServerCore) {
        this.kerasServerCore = kerasServerCore;
    }

    public void setGazeWindow(GazeWindow gazeWindow) {
        this.gazeWindow = gazeWindow;
    }

}
