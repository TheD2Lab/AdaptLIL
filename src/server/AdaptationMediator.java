package server;

import adaptations.Adaptation;
import adaptations.ColorAdaptation;
import adaptations.DeemphasisAdaptation;
import adaptations.HighlightingAdaptation;
import analysis.ontomap.OntoMapCsv;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.MutableTriple;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import server.gazepoint.api.recv.RecXmlObject;
import server.request.AdaptationInvokeRequest;
import weka.core.Instance;

import java.util.*;

/**
 * Loosely follows Mediator/Facade design pattern of a subsystem of components. It directs and controls how the adaptations are invoked and altered, how they are sent to the
 * frontend, and the classification that occurs by analyzing the gaze window.
 */
public class AdaptationMediator extends Mediator {
    private VisualizationWebsocket websocket;
    private GP3Socket gp3Socket;
    private MultiLayerNetwork classifierModel;
    private GazeWindow gazeWindow;
    private boolean isRunning;
    private double lastRiskScore;

    private Adaptation observedAdaptation;
    Map<String, Adaptation> currentAdaptations;


    //Thresholds & Constants

    private double thresholdForInvokation;
    private double smallChangeThreshold = 0.10;
    private double bigChangeThreshold = 0.30;

    private int numSequencesForClassification;
    public AdaptationMediator(VisualizationWebsocket websocket, GP3Socket gp3Socket, MultiLayerNetwork classifierModel, GazeWindow gazeWindow, int numSequencesForClassification) {
        this.websocket = websocket;
        this.gp3Socket = gp3Socket;
        this.classifierModel = classifierModel;
        this.gazeWindow = gazeWindow;
        this.isRunning = false;
        this.currentAdaptations = new HashMap<>();
        this.lastRiskScore = 0.0;
        this.thresholdForInvokation = 0.8;
        this.numSequencesForClassification = numSequencesForClassification;
    }

    public void start() {
        this.websocket.setMediator(this);
        this.gp3Socket.setMediator(this);
        //this.classifierModel.setMediator(this);
        this.gazeWindow.setMediator(this);
        boolean runAdapations = true;
        int numAttributes = 8;

        //Does this also need to run async? I think so.
        //This will work as the synchronization between all objects as well.
        List<INDArray> gazeWindowINDArrays = new ArrayList<>(this.numSequencesForClassification);
        System.out.println("mediator started");
        while (runAdapations) {

            //No websockets connected, do not begin reading from gaze buffer. (Note, we may need to flush buffer once we connect for the first time)
            if (this.websocket.getWebSockets().isEmpty())
                continue;
            else
                System.out.println("websocket connected");

            RecXmlObject recXmlObject = this.gp3Socket.readGazeDataFromBuffer();

                //Add to windows for task
                this.gazeWindow.add(recXmlObject);


            if (gazeWindow.isFull()) { //Likely change to a time series roll
                //Verify this operation does not slow the real time significantly
                gazeWindow.interpolateMissingValues();
                System.out.println("Gaze window full");
                //Get classification

                //Request if Participant has finished a question and get their answer
                //Must be recent and contained within the current window.
                //Get last time used and current time.
                Float cognitiveLoadScore = gazeWindow.getCognitiveLoadScore();
                INDArray gazeWindowInput = gazeWindow.toINDArray();
                gazeWindowINDArrays.add(gazeWindowInput);
                if (gazeWindowINDArrays.size() == this.numSequencesForClassification) {

                    //Error (can only insert a scalar in to another scalar
                    //Find way to join
                    INDArray classificationInput = Nd4j.create(gazeWindowINDArrays, gazeWindow.getWindowSize(), numAttributes, numSequencesForClassification);
                    Integer classificationResult = classifierModel.predict(classificationInput)[0]; //TODO
                    Integer participantWrongOrRight = null;
                    Float taskCompletionTime = null; //grab from websocket
                    //Calculate perilScore (risk score)
                    double curRiskScore = this.calculateRiskScore(classificationResult); //TODO
                    double lastRiskScore = this.getLastRiskScore();
                    double riskScoreChange = curRiskScore - lastRiskScore;
                    System.out.println("risk Score: " + curRiskScore);
                    if (riskScoreChange > this.thresholdForInvokation) {
                        this.invokeAdaptation(curRiskScore);
                    }

                    this.observedAdaptation.setScore(curRiskScore);
                    this.lastRiskScore = curRiskScore;
                    gazeWindowINDArrays.clear();
                }
                gazeWindow.flush();
            } else { //Do nothing, loopback
                continue;
            }
        }

    }

    public void invokeNewAdaptation() {
        this.observedAdaptation.setBeingObservedByMediator(false);
        this.observedAdaptation = getNewAdaptation();
        this.observedAdaptation.setBeingObservedByMediator(true);
        this.currentAdaptations.put(observedAdaptation.getType(), observedAdaptation);

        //TODO
        //Websocket, invoke new adaptation through message.
        //TODO, figure out toggle to turn off other adaptations.
        //Consider the following cases:
        //multiple adaptations at once
            //We don't have a situation where this arises yet.
        //one adaptation running.
            //toggle off the current observed
        this.websocket.invoke(new AdaptationInvokeRequest(this.observedAdaptation));
        System.out.println("invoke new adaptation");
    }

    public void invokeAdaptationChange() {
        //Websocket
        //Send over adaptation and the new config style.
        this.websocket.invoke(new AdaptationInvokeRequest(this.observedAdaptation));
    }

    public void invokeAdaptation(double curRiskScore) {
        double stepAmount = 0.25;
        if (this.currentAdaptations.isEmpty()) {
            this.observedAdaptation = getNewAdaptation();
            this.observedAdaptation.setBeingObservedByMediator(true);
            this.currentAdaptations.put(observedAdaptation.getType(), observedAdaptation);
        } else { //Has adaptations, review the currently observed one.

            if (curRiskScore > observedAdaptation.getScore()) { //Adaptation Risk increased.
                double scoreDifference = curRiskScore - observedAdaptation.getScore();
                //Select new adaptation
                boolean selectNewAdaptation = scoreDifference > bigChangeThreshold;
                boolean changeStyleChangeDirection = scoreDifference < bigChangeThreshold && scoreDifference > smallChangeThreshold;

                if (selectNewAdaptation) {
                    this.invokeNewAdaptation();
                } else if (changeStyleChangeDirection){ //Decrease was less substantial, try a different direction.

                    MutableTriple<String, Integer, Double> lastStyle = this.observedAdaptation.getLastStyleChange();
                    this.observedAdaptation.applyStyleChange(lastStyle.middle  * -1, stepAmount);
                    this.invokeAdaptationChange();
                } else {

                    //Too small of a change, may have leveld out.
                }
                //or iterate over current adaptation
            } else {
                //Adaptation improved

                double scoreDiff = curRiskScore - observedAdaptation.getScore();
                if (scoreDiff > smallChangeThreshold && scoreDiff < bigChangeThreshold) {//if slight improvement, make a change, may have leveld out.
                    //Potentially try a new adaptation?

                } else if (scoreDiff > bigChangeThreshold) { //if major improvement, good job, going in right direction
                    //apply change with similar as last change
                    //The direction of the last change
                    MutableTriple<String, Integer, Double> lastStyleChange = this.observedAdaptation.getLastStyleChange();
                    this.observedAdaptation.applyStyleChange(lastStyleChange.middle, stepAmount);
                    this.invokeAdaptationChange();

                }

            }
        }

    }

    /**
     * @deprecated invoking adaptation like this is overly complex. Listen to classifcation results only as that is suppose to be
     * an deep learning classification of cognitive load.
     * @param curRiskScore
     */
    public void _invokeAdaptation(double curRiskScore) {
        double stepAmount = 0.25;
        //if no adaptations, create a new one
        if (this.currentAdaptations.isEmpty()) {
            this.observedAdaptation = getNewAdaptation();
            this.observedAdaptation.setBeingObservedByMediator(true);
            this.currentAdaptations.put(observedAdaptation.getType(), observedAdaptation);
        } else { //Has adaptations, review the currently observed one.

            if (curRiskScore > observedAdaptation.getScore()) { //Adaptation Risk increased.
                double scoreDifference = curRiskScore - observedAdaptation.getScore();
                //Select new adaptation
                boolean selectNewAdaptation = scoreDifference > bigChangeThreshold;
                boolean changeStyleChangeDirection = scoreDifference < bigChangeThreshold && scoreDifference > smallChangeThreshold;

                if (selectNewAdaptation) {
                    this.invokeNewAdaptation();
                } else if (changeStyleChangeDirection){ //Decrease was less substantial, try a different direction.

                    MutableTriple<String, Integer, Double> lastStyle = this.observedAdaptation.getLastStyleChange();
                    this.observedAdaptation.applyStyleChange(lastStyle.middle  * -1, stepAmount);
                    this.invokeAdaptationChange();
                } else {

                    //Too small of a change, may have leveld out.
                }
                //or iterate over current adaptation
            } else {
                //Adaptation improved

                double scoreDiff = curRiskScore - observedAdaptation.getScore();
                if (scoreDiff > smallChangeThreshold && scoreDiff < bigChangeThreshold) {//if slight improvement, make a change, may have leveld out.
                    //Potentially try a new adaptation?

                } else if (scoreDiff > bigChangeThreshold) { //if major improvement, good job, going in right direction
                    //apply change with similar as last change
                    //The direction of the last change
                    MutableTriple<String, Integer, Double> lastStyleChange = this.observedAdaptation.getLastStyleChange();
                    this.observedAdaptation.applyStyleChange(lastStyleChange.middle, stepAmount);
                    this.invokeAdaptationChange();

                }

            }
        }
    }

    public Adaptation getNewAdaptation() {
        Random rand = new Random();
        List<Adaptation> adaptations = this.listOfAdaptations();
        int randomAdaptationIndex = rand.nextInt(adaptations.size());

        return adaptations.get(randomAdaptationIndex);
    }

    public List<Adaptation> listOfAdaptations() {
        ArrayList<Adaptation> adaptations = new ArrayList<>();
        adaptations.add(new DeemphasisAdaptation(true, System.currentTimeMillis(), -1, -1, null));
        adaptations.add(new HighlightingAdaptation(true, System.currentTimeMillis(), -1, -1, null));
        adaptations.add(new ColorAdaptation(true, System.currentTimeMillis(), -1, -1, null));
        return adaptations;
    }

    public Adaptation getObservedAdaptation() {
        return this.observedAdaptation;
    }

    /**
     * Gets the last risk score for the currently observed adaptation
     * @return
     */
    public double getLastRiskScore() {
        if (this.currentAdaptations.isEmpty()) {
            return 0.0;
        } else {
            return this.observedAdaptation.getScore();
        }
    }
    public VisualizationWebsocket getWebsocket() {
        return websocket;
    }

    public GP3Socket getGp3Socket() {
        return gp3Socket;
    }

    public MultiLayerNetwork getClassifierModel() {
        return classifierModel;
    }

    public GazeWindow getGazeWindow() {
        return gazeWindow;
    }

    public void setWebsocket(VisualizationWebsocket websocket) {
        this.websocket = websocket;
    }

    public void setGp3Socket(GP3Socket gp3Socket) {
        this.gp3Socket = gp3Socket;
    }

    public void setClassifierModel(MultiLayerNetwork classifierModel) {
        this.classifierModel = classifierModel;
    }

    public void setGazeWindow(GazeWindow gazeWindow) {
        this.gazeWindow = gazeWindow;
    }


    public double calculateRiskScore(int classificationResult) {
        //High weights:
        //participantAnswer, cognitiveLoadScore
        //Medium weights: classificaitonResult
        //Low weights: taskCompletionTime
        int numHighWeights = 1;
        int numMedWeights = 1;
        int numLowWeights = 1;
        double riskScore = 0;

        //Review weighting. A high weight, if all factors are risky, should flag the threshold.
        double highWeight = 0.80;
        double medWeight = 0.35;
        double lowWeight = 0.15;
        int negatedParticipantAnswer = 0;



        riskScore = highWeight * (classificationResult == 0 ? 1 : 0);

        return riskScore;
    }
}
