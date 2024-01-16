package server;
import analysis.ScanPath;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.opencsv.exceptions.CsvValidationException;
import jakarta.ws.rs.core.UriBuilder;
import org.deeplearning4j.nn.conf.graph.ElementWiseVertex;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.modelimport.keras.*;
import org.deeplearning4j.nn.modelimport.keras.exceptions.*;
import org.deeplearning4j.nn.modelimport.keras.layers.core.KerasMerge;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.websockets.WebSocketAddOn;
import org.glassfish.grizzly.websockets.WebSocketEngine;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import server.gazepoint.api.XmlObject;
import server.gazepoint.api.recv.RecXmlObject;
import server.gazepoint.api.set.SetEnableSendCommand;
import server.http.HttpRequestCore;
import server.http.request.RequestLoadModel;


import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class ServerMain {
    public static ServerMain serverMain; //Singleton, keeps server alive

//    public static ServerMain serverMain;
    public static final String url = "localhost";
    public static final int port = 8080;
    public static String loadModelEndpoint = "loadModel";

    public static final String pythonServerURL = "localhost";
    public static final int pythonServerPort = 5000;
    public static float gazeWindowSizeInMilliseconds = 1000;
    public static int numSequencesForClassification = 2;
    static boolean simulateGazepointServer = true;

//    public static String modelConfigPath = "/home/notroot/Desktop/d2lab/models/";
//    public static String modelName = "/stacked_lstm-Adam0,0014_10-30 20_31_55.h5";

    public static String modelConfigPath = "/home/notroot/Desktop/d2lab/iav/models/";
    public static String modelName = "transformer_model_channels.h5";

    public static HttpServer javaServer;
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
        ServerMain serverMain = new ServerMain();
        //Give adaptation mediator the keras endpoint
        System.out.println("Loaded keras model : " + modelName);
        System.out.println("Starting grizzly HTTP server");
        serverMain.initHttpServerAndWebSocketPort();

    }

    public static void execKerasServerAck() {

        try {
            //TODO
            //Start keras server from python script and wait for ACK from python that the server started.
            System.out.println("Starting python server..");
            Path curPath = Paths.get("");
            System.out.println(curPath.toAbsolutePath().toString());
            String flaskServerExecCommand = "scripts/start_flask.bat"; //Might need to use current dir
            Process p = Runtime.getRuntime().exec(new String[]{flaskServerExecCommand});

            System.out.println("Started python server.");
            System.out.println("Loading Classification Model: " + modelName);
            KerasServerCore kerasServerCore = new KerasServerCore(pythonServerURL, pythonServerPort);


            //Make POST request to keras endpoint
            kerasServerCore.loadKerasModel(modelName);


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
            AdaptationMediator adaptationMediator = initAdapationMediator(visualizationWebsocket, gp3Socket, kerasServerCore, gazeWindow);
            System.out.println("Constructed AdaptationMediator");
            System.out.println("Main thread access goes to adaptationMediator.");
            System.out.println("Sever stays alive by waiting for input so type anything to exit");
            boolean runAdaptations = true;
            adaptationMediator.start();
        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * https://javaee.github.io/grizzly/websockets.html
     */
    public static HttpServer initHttpServerAndWebSocketPort() {
        URI uri = UriBuilder.fromUri("http://" + url).port(port).build();

        ResourceConfig rc = new ResourceConfig().packages("server.http.endpoints");
        rc.register(JacksonFeature.class);

        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(uri, rc, false);

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
        Path curPath = Paths.get("");

        File gazeFile = new File(curPath.resolve("p1.LIL.Anatomy_all_gaze.csv").toAbsolutePath().toString());
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


    public static GazeWindow initGazeWindow(float windowSizeInMilliseconds) {
        return new GazeWindow(false, windowSizeInMilliseconds);
    }

    public static AdaptationMediator initAdapationMediator(VisualizationWebsocket websocket, GP3Socket gp3Socket, KerasServerCore kerasServerCore, GazeWindow window) {
        return new AdaptationMediator(websocket, gp3Socket, kerasServerCore, window, ServerMain.numSequencesForClassification);
    }

}
