package server;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;

/**
 * Loosely follows Mediator/Facade design pattern of a subsystem of components. It directs and controls how the adaptations are invoked and altered, how they are sent to the
 * frontend, and the classification that occurs by analyzing the gaze window.
 */
public class AdaptationMediator extends Mediator {
    private VisualizationWebsocket websocket;
    private GP3Socket gp3Socket;
    private MultiLayerNetwork classifierModel;
    private GazeWindow gazeWindow;

    public AdaptationMediator(VisualizationWebsocket websocket, GP3Socket gp3Socket, MultiLayerNetwork classifierModel, GazeWindow gazeWindow) {
        this.websocket = websocket;
        this.gp3Socket = gp3Socket;
        this.classifierModel = classifierModel;
        this.gazeWindow = gazeWindow;
    }

    public void start() {
        this.websocket.setMediator(this);
        this.gp3Socket.setMediator(this);
        //this.classifierModel.setMediator(this);
        this.gazeWindow.setMediator(this);
        boolean runAdapations = true;

        while (runAdapations) {

            //TODO, exit condition

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
}
