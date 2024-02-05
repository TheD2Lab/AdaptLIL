package server;

import adaptations.Adaptation;
import adaptations.DeemphasisAdaptation;
import adaptations.HighlightingAdaptation;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import server.gazepoint.api.recv.RecXmlObject;
import server.websocket.request.AdaptationInvokeRequestModelWs;
import weka.core.Instance;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Loosely follows Mediator/Facade design pattern of a subsystem of components. It directs and controls how the adaptations are invoked and altered, how they are sent to the
 * frontend, and the classification that occurs by analyzing the gaze window.
 */
public class AdaptationMediator extends Mediator {
    private VisualizationWebsocket websocket;
    private GP3Socket gp3Socket;
    private KerasServerCore kerasServerCore;
    private GazeWindow gazeWindow;
    private boolean isRunning;
    private double lastRiskScore;

    private Adaptation observedAdaptation;
    Map<String, Adaptation> currentAdaptations;


    //Thresholds & Constants

    private double thresholdForInvokation;
    private double increaseStrengthThresh = 0.41;
    private double remainThresh = 0.6;
    private double smallChangeThreshold = 0.20;
    private double bigChangeThreshold = 0.30;

    private int numSequencesForClassification;
    private int newAdaptationStartSequenceIndex = 0;
    private int curSequenceIndex = 0;
    private double defaultStrength = 0.50;
    public AdaptationMediator(VisualizationWebsocket websocket, GP3Socket gp3Socket, KerasServerCore kerasServerCore, GazeWindow gazeWindow, int numSequencesForClassification) {
        this.websocket = websocket;
        this.gp3Socket = gp3Socket;
        this.kerasServerCore = kerasServerCore;
        this.gazeWindow = gazeWindow;
        this.isRunning = false;
        this.currentAdaptations = new HashMap<>();
        this.lastRiskScore = 0.0;
        this.thresholdForInvokation = 0.6;
        this.numSequencesForClassification = numSequencesForClassification;
    }

    public void start() {
        this.websocket.setMediator(this);
        this.gp3Socket.setMediator(this);

        //Flush the gaze queue to make sure data is fresh!
        this.gp3Socket.getGazeDataQueue().flush();
        //this.classifierModel.setMediator(this);
        this.gazeWindow.setMediator(this);
        boolean runAdapations = true;
        int numAttributes = 8;

        //Does this also need to run async? I think so.
        //This will work as the synchronization between all objects as well.
        List<INDArray> gazeWindowINDArrays = new ArrayList<>(this.numSequencesForClassification);
        System.out.println("mediator started");
        int[] classifications = new int[5];
        int classificationIndex = 0;
        while (runAdapations) {

            //No websockets connected, do not begin reading from gaze buffer. (Note, we may need to flush buffer once we connect for the first time)
            if (this.websocket.getWebSockets().isEmpty())
                continue;

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
                INDArray gazeWindowInput = gazeWindow.toINDArray(false);
                gazeWindowINDArrays.add(gazeWindowInput);
                if (gazeWindowINDArrays.size() == this.numSequencesForClassification) {
                    this.curSequenceIndex++;
                    System.out.println("Predicting...");
                    //Error (can only insert a scalar in to another scalar
                    //Find way to join
                    INDArray classificationInput = Nd4j.stack(0, gazeWindowINDArrays.get(0), gazeWindowINDArrays.get(1)).reshape(
                            new int[]{1, //Only feed 1 block of sequences at a time.
                                    (int) gazeWindowINDArrays.get(0).shape()[0], //Num attributes per sequence
                                    this.numSequencesForClassification // Num sequences to feed
                            });

                    Double probability = kerasServerCore.predict(classificationInput).getOutput().getDouble(0);
                    Integer classificationResult = probability >= 0.5 ? 1 : 0;
                            //kerasServerCore.output(classificationInput)[0].getDouble(0) >= 0.5 ? 1 : 0;
                    ServerMain.logFile.logLine("Prediction," +probability + "," +classificationResult+","+ System.currentTimeMillis());
                    System.out.println("Sequence: " + classificationIndex + " predicted as: " + classificationResult);
                    classifications[classificationIndex++] = classificationResult;
                    Integer participantWrongOrRight = null;
                    Float taskCompletionTime = null; //grab from websocket
                    //Calculate perilScore (risk score)
                    double curRiskScore = this.calculateRiskScore(classificationResult);
                    double lastRiskScore = this.getLastRiskScore();

                    if (classificationIndex == classifications.length) {
                        System.out.println("# of classifications to begin intervention has occured : " + classificationIndex);

                        double score = 0.0;
                        int numClassOne = 0;
                        for (int i = 0; i < classifications.length; ++i)
                            if (classifications[i] == 1)
                                numClassOne++;
                        System.out.println("# class of 1: " + numClassOne);
                        ServerMain.logFile.logLine("#Classifications," +numClassOne + "/5,"+ System.currentTimeMillis());

                        score = (double) numClassOne / classifications.length;
                        this.invokeAdaptation(score);
                        classificationIndex = 0;
                    }
                    gazeWindowINDArrays.clear();
                }
                gazeWindow.flush();
            } else { //Do nothing, loopback
                continue;
            }
        }

    }

    public void invokeNewAdaptation() {
        Adaptation nextAdaptation = getNewAdaptation();
        if (this.observedAdaptation != null) {
            this.observedAdaptation.setBeingObservedByMediator(false);
            //Cycle through if we pick the same one as is being observed.
            while (nextAdaptation.getType().equals(this.observedAdaptation.getType()))
                nextAdaptation = getNewAdaptation();
        }

        this.observedAdaptation = nextAdaptation;
        this.observedAdaptation.setBeingObservedByMediator(true);
        this.currentAdaptations.put(observedAdaptation.getType(), observedAdaptation);

        this.newAdaptationStartSequenceIndex = this.curSequenceIndex;
        //TODO
        //Websocket, invoke new adaptation through message.
        //TODO, figure out toggle to turn off other adaptations.
        //Consider the following cases:
        //multiple adaptations at once
            //We don't have a situation where this arises yet.
        //one adaptation running.
            //toggle off the current observed
        this.websocket.invoke(new AdaptationInvokeRequestModelWs(this.observedAdaptation));
        System.out.println("invoke new adaptation");
    }

    public void invokeAdaptationChange() {
        //Websocket
        //Send over adaptation and the new config style.
        this.websocket.invoke(new AdaptationInvokeRequestModelWs(this.observedAdaptation));
    }

    public void invokeAdaptation(double curRiskScore) {
        double stepAmount = 0.15;
        int numSequencesSinceNewAdaptation = this.curSequenceIndex - this.newAdaptationStartSequenceIndex;
        int gracePeriodByNumSequences = 5 * 12; //60 second grace period before invoking a new adaptation.
        //Case 0
        if (curRiskScore <= 0.0 && (this.currentAdaptations.isEmpty() || numSequencesSinceNewAdaptation > gracePeriodByNumSequences)) {
            this.invokeNewAdaptation();
            System.out.println("Added new adaptation because score was zero: " + this.observedAdaptation.getType());
        } else if (curRiskScore > 0.0){ //Has adaptations, review the currently observed one.
            if (!this.currentAdaptations.isEmpty()) {  //Has an adaptation, we can review it and compare to the base line (presumed that it's always classifcations: [0_0,0_1,...,0_n]

                //Case 1
                if (curRiskScore < this.increaseStrengthThresh) { //Adaptation Risk increased.
                    //Select new adaptation
                    if (this.observedAdaptation.hasFlipped() && (this.observedAdaptation.getStrength() <= 0.0 || this.observedAdaptation.getStrength() >= 1.0)) { //Cannot increase/decrease strenth of adaptation, must select a new one (c2.a)
                        this.observedAdaptation.flipDirection();
                        this.observedAdaptation.setStrength(defaultStrength);
                        this.invokeNewAdaptation();
                    }
                    else if (!this.observedAdaptation.hasFlipped() && (this.observedAdaptation.getStrength() <= 0.0 || this.observedAdaptation.getStrength() >= 1.0)) //(c2.b)
                    {
                        this.observedAdaptation.flipDirection(); //Flip direction
                        this.observedAdaptation.applyStyleChange(stepAmount);
                        this.invokeAdaptationChange();
                    }
                    //or iterate over current adaptation
                } else if (curRiskScore >= this.increaseStrengthThresh && curRiskScore < this.remainThresh) { //Case 2

                    //Check strength,
                    //If we can increase strength, do so in same direction
                    if (this.observedAdaptation.getStrength() > 0.0 && this.observedAdaptation.getStrength() <= 1.0) { //Not at max, increase adaptation strength (c2.a)
                        this.observedAdaptation.applyStyleChange(stepAmount);
                        this.invokeAdaptationChange();
                    } else { //(c2.b)
                        //Do nothing, reached max. Continue onwards.
                    }

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
        adaptations.add(new DeemphasisAdaptation(true, System.currentTimeMillis(), -1, -1, null, defaultStrength));
        adaptations.add(new HighlightingAdaptation(true, System.currentTimeMillis(), -1, -1, null, defaultStrength));
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

    public KerasServerCore getClassifierModel() {
        return kerasServerCore;
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

    public void setClassifierModel(KerasServerCore kerasServerCore) {
        this.kerasServerCore = kerasServerCore;
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
