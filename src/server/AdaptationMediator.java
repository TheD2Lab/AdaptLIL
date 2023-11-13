package server;

import adaptations.Adaptation;
import adaptations.ColorAdaptation;
import adaptations.DeemphasisAdaptation;
import adaptations.HighlightingAdaptation;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;

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
    public AdaptationMediator(VisualizationWebsocket websocket, GP3Socket gp3Socket, MultiLayerNetwork classifierModel, GazeWindow gazeWindow) {
        this.websocket = websocket;
        this.gp3Socket = gp3Socket;
        this.classifierModel = classifierModel;
        this.gazeWindow = gazeWindow;
        this.isRunning = false;
        this.currentAdaptations = new HashMap<>();
        this.lastRiskScore = 0.0;
        this.thresholdForInvokation = 0.8;
    }

    public void start() {
        this.websocket.setMediator(this);
        this.gp3Socket.setMediator(this);
        //this.classifierModel.setMediator(this);
        this.gazeWindow.setMediator(this);
        boolean runAdapations = true;

        //Does this also need to run async? I think so.
        //This will work as the synchronization between all objects as well.
        while (runAdapations) {

            if (gazeWindow.isFull()) { //Likely change to a time series roll

                //Get classification

                //Request if Participant has finished a question and get their answer
                //Must be recent and contained within the current window.
                //Get last time used and current time.
                Float cognitiveLoadScore = gazeWindow.getCognitiveLoadScore();

                Integer classificationResult = classifierModel.predict()[0]; //TODO

                Integer participantWrongOrRight = null;
                //Calculate perilScore (risk score)
                double curRiskScore = this.calculateRiskScore(); //TODO
                double lastRiskScore = this.getLastRiskScore();
                double riskScoreChange = curRiskScore - lastRiskScore;
                if (riskScoreChange > this.thresholdForInvokation) {
                    this.invokeAdaptation(curRiskScore);
                }

                this.lastRiskScore = curRiskScore;

            } else { //Do nothing, loopback
                continue;
            }
            //TODO, exit condition

        }
    }

    public void invokeAdaptation(double curRiskScore) {
        //if no adaptations, create a new one
        if (this.currentAdaptations.isEmpty()) {
            this.observedAdaptation = getNewAdaptation();
            this.observedAdaptation.setBeingObservedByMediator(true);
            this.currentAdaptations.put(observedAdaptation.getType(), observedAdaptation);
        } else { //Has adaptations, review the currently observed one.

            if (curRiskScore > observedAdaptation.getScore()) { //Adaptation Risk increased.
                //Select new adaptation
                boolean selectNewAdaptation = false;
                if (selectNewAdaptation) {
                    this.observedAdaptation.setBeingObservedByMediator(false);
                    this.observedAdaptation = getNewAdaptation();
                    this.observedAdaptation.setBeingObservedByMediator(true);
                    this.currentAdaptations.put(observedAdaptation.getType(), observedAdaptation);
                } else {

                }
                //or iterate over current adaptation
            } else {
                //Adaptation improved

                double scoreDiff = curRiskScore - observedAdaptation.getScore();
                if (scoreDiff > smallChangeThreshold) {//if slight improvement, make a change
                } else if (scoreDiff > bigChangeThreshold) { //if major improvement, good job, going in right direction
                    //apply change with similar as last change


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


    public double calculateRiskScore(Integer participantAnswer, Integer classificationResult, Float taskCompletionTime, Float cognitiveLoadScore) {

        return 0.0;
    }
}
