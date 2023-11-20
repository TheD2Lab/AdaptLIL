package server;
import analysis.ScanPath;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.opencsv.exceptions.CsvValidationException;
import org.deeplearning4j.nn.modelimport.keras.*;
import org.deeplearning4j.nn.modelimport.keras.exceptions.*;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.websockets.WebSocketAddOn;
import org.glassfish.grizzly.websockets.WebSocketEngine;
import server.gazepoint.api.XmlObject;
import server.gazepoint.api.recv.RecXmlObject;
import server.gazepoint.api.set.SetEnableSendCommand;

import java.io.*;
import java.util.Scanner;

public class ServerMain {

//    public static ServerMain serverMain;
    public static final String url = "localhost";
    public static final int port = 8080;
    public static float gazeWindowSizeInMilliseconds = 1000;
    static boolean simulateGazepointServer = true;

//    public static String modelConfigPath = "/home/notroot/Desktop/d2lab/models/";
//    public static String modelName = "/stacked_lstm-Adam0,0014_10-30 20_31_55.h5";

    public static String modelConfigPath = "/home/notroot/Desktop/d2lab/gazepoint/models/";
    public static String modelName = "stacked_lstm-.h5";
    public static void serializationTest() {
        XmlMapper mapper = new XmlMapper();
        String serialString = "<REC CNT=\"34\"/>";
        String serialString2 = "<REC TIME_TICK=\"3434344\"/>";
        String fixationString = "<REC FPOGX=\"0.48439\" FPOGY=\"0.50313\" FPOGS=\"1891.86768\" FPOGD=\"0.49280\" FPOGID=\"1599\" FPOGV=\"1\" />";
        XmlObject someXmlObj = null;

        someXmlObj =  GazeApiCommands.mapToXmlObject(fixationString);

        if (someXmlObj instanceof RecXmlObject)
            System.out.println(((RecXmlObject) someXmlObj).getFixation().getX());


    }



    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Beginning GP3 Real-Time Prototype Stream");
        try {
            System.out.println("Loading Classification Model: " + modelName);
            MultiLayerNetwork model = loadKerasModel(modelConfigPath, modelName);
            System.out.println("Loaded keras model : " + modelName);
            System.out.println("Starting grizzly HTTP server");
            HttpServer server = initHttpServerAndWebSocketPort();
            System.out.println("Initialized HTTP Server & WebSocket port");
            System.out.println("Starting gp3 socket");
            GP3Socket gp3Socket = initGP3Socket(simulateGazepointServer);
            System.out.println("Initializing VisualizationWebsocket");
            VisualizationWebsocket visualizationWebsocket = initVisualizationWebsocket(gp3Socket);
            System.out.println(" initialized VisualizationWebsocket");


            System.out.println("Starting gaze window");
            GazeWindow gazeWindow = initGazeWindow(gazeWindowSizeInMilliseconds);
            System.out.println("Initialized gaze window");
            System.out.println("Building AdaptationMediator");
            AdaptationMediator adaptationMediator = initAdapationMediator(visualizationWebsocket, gp3Socket, model, gazeWindow);
            System.out.println("Constructed AdaptationMediator");
            System.out.println("Main thread access goes to adaptationMediator.");
            System.out.println("Sever stays alive by waiting for input so type anything to exit");
            boolean runAdaptations = true;
            while (runAdaptations) {
                adaptationMediator.start();
            }
        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * https://javaee.github.io/grizzly/websockets.html
     */
    public static HttpServer initHttpServerAndWebSocketPort() {
        final HttpServer server = HttpServer.createSimpleServer("/var/www", port);
        final WebSocketAddOn addon = new WebSocketAddOn();
        server.getListener("grizzly").registerAddOn(addon);

        try {
            server.start();
            System.out.println("Websocket started on  " + url + ":" + port);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return server;
    }

    public static VisualizationWebsocket initVisualizationWebsocket(GP3Socket gp3Socket) {

        VisualizationWebsocket visWebSocket = new VisualizationWebsocket(gp3Socket);
        WebSocketEngine.getEngine().register("", "/gaze", visWebSocket);
        return visWebSocket;

    }

    public static GP3Socket initGP3Socket(boolean simulateGazepointServer) throws IOException, CsvValidationException {
        XmlMapper mapper = new XmlMapper();
        String gp3Hostname = "localhost";
        int gp3Port = 4242;
        File gazeFile = new File("/home/notroot/Desktop/d2lab/gazepoint/p1.LIL.Anatomy_all_gaze.csv");
        if (simulateGazepointServer) {
            GazepointSimulationServer simulationServer = new GazepointSimulationServer(gp3Hostname, gp3Port, gazeFile, true);
            Thread serverSimThread = new Thread(simulationServer);
            serverSimThread.start();
        }
        GP3Socket gp3Socket = new GP3Socket(gp3Hostname, gp3Port);
        //screenheight && width should be an ack for consistentcy. see gazepoint documentation
        float screenHeight = 1920;
        float screenWidth = 1080;
        gp3Socket.connect();
        System.out.println("Connected to GP3");
        System.out.println("Starting Data Stream via thread");
        gp3Socket.startGazeDataStream();
        gp3Socket.write((mapper.writeValueAsString(new SetEnableSendCommand(GazeApiCommands.ENABLE_SEND_POG_FIX, true))));
        gp3Socket.write((mapper.writeValueAsString(new SetEnableSendCommand(GazeApiCommands.ENABLE_SEND_PUPIL_LEFT, true))));
        gp3Socket.write((mapper.writeValueAsString(new SetEnableSendCommand(GazeApiCommands.ENABLE_SEND_EYE_LEFT, true))));
        gp3Socket.write((mapper.writeValueAsString(new SetEnableSendCommand(GazeApiCommands.ENABLE_SEND_PUPIL_RIGHT, true))));
        gp3Socket.write((mapper.writeValueAsString(new SetEnableSendCommand(GazeApiCommands.ENABLE_SEND_EYE_RIGHT, true))));
        gp3Socket.write((mapper.writeValueAsString(new SetEnableSendCommand(GazeApiCommands.ENABLE_SEND_PUPILMM, true))));
        //gp3Socket.write((mapper.writeValueAsString(new SetEnableSendCommand(GazeApiCommands.ENABLE_SEND_BLINK, true))));
        //gp3Socket.write((mapper.writeValueAsString(new SetEnableSendCommand(GazeApiCommands.ENABLE_SEND_COUNTER, true))));
        //
        //gp3Socket.write((mapper.writeValueAsString(new SetEnableSendCommand(GazeApiCommands.ENABLE_SEND_DIAL, true))));
        //gp3Socket.write((mapper.writeValueAsString(new SetEnableSendCommand(GazeApiCommands.ENABLE_SEND_HR, true))));
        //gp3Socket.writeSetCommand(new SetEnableSendCommand(GazeApiCommands.ENABLE_SEND_TIME_TICK, true));
        //gp3Socket.writeSetCommand(new SetEnableSendCommand(GazeApiCommands.ENABLE_SEND_TIME, true));


        //gp3Socket.writeSetCommand(new SetEnableSendCommand(GazeApiCommands.ENABLE_SEND_CURSOR, true));
        return gp3Socket;

    }

    public static MultiLayerNetwork loadKerasModel(String modelConfigPath, String modelName) {
        //Uses resource directory below. Using hardcoding for now and will revisit when I clean this up.
        //String simpleMlp = new ClassPathResource("simple_mlp.h5").getFile().getPath();
        MultiLayerNetwork classifier = null;
        try {
            InputStream modelByteStream = new BufferedInputStream(new FileInputStream(modelConfigPath + modelName));
            classifier = KerasModelImport.importKerasSequentialModelAndWeights(modelByteStream);
        } catch (IOException e) {
            System.err.println("IOException when importing model (likely invalid path)");
            throw new RuntimeException(e);
        } catch (InvalidKerasConfigurationException e) {
            System.err.println("KerasConfigIssue (model related)");
            throw new RuntimeException(e);
        } catch (UnsupportedKerasConfigurationException e) {
            System.err.println("Keras Model not support (model related)");
            throw new RuntimeException(e);
        }

        return classifier;
    }

    public static GazeWindow initGazeWindow(float windowSizeInMilliseconds) {
        return new GazeWindow(false, windowSizeInMilliseconds);
    }

    public static AdaptationMediator initAdapationMediator(VisualizationWebsocket websocket, GP3Socket gp3Socket, MultiLayerNetwork model, GazeWindow window) {
        return new AdaptationMediator(websocket, gp3Socket, model, window);
    }

}
