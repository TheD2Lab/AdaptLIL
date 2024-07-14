package adaptovis;
import adaptovis.websocket.VisualizationWebsocket;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.opencsv.exceptions.CsvValidationException;
import jakarta.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.websockets.WebSocketAddOn;
import org.glassfish.grizzly.websockets.WebSocketEngine;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.nd4j.linalg.factory.Nd4j;
import adaptovis.gazepoint.api.GazeApiCommands;
import adaptovis.gazepoint.api.GazepointSimulationServer;
import adaptovis.gazepoint.api.GazepointSocket;
import adaptovis.gazepoint.api.XmlObject;
import adaptovis.gazepoint.api.recv.RecXmlObject;
import adaptovis.gazepoint.api.set.SetEnableSendCommand;
import adaptovis.http.KerasServerCore;
import adaptovis.mediator.AdaptationMediator;


import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    public static Main serverMain; //Singleton, keeps server alive

    public static final String url = "localhost";
    public static final int port = 8080;

    public static final String pythonServerURL = "localhost";
    public static final int pythonServerPort = 5000;
    public static float gazeWindowSizeInMilliseconds = 1000;
    public static int numSequencesForClassification = 2;
    static boolean simulateGazepointServer = true;

    public static SimpleLogger logFile = new SimpleLogger(new File("AdaptationOutput_" +System.currentTimeMillis()+".txt"));
    public static boolean hasKerasServerAckd = false;
    public static String modelName = "transformer_model_channels.h5";


    //Used to block main thread from making a keras server load_modal before receiving an ACK
    public static final ReentrantLock mainThreadLock = new ReentrantLock();

    public static void main(String[] args) {
        preloadND4J();
        Thread printingHook = new Thread(() -> Main.ShutDownFunction());
        Runtime.getRuntime().addShutdownHook(printingHook);

        logFile.logLine("App Ran at: " + System.currentTimeMillis() + ", " + (new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").format((new Date(System.currentTimeMillis())))));
        System.out.println("Beginning GP3 Real-Time Prototype Stream");
        serverMain = new Main();

        //Give adaptation mediator the keras endpoint
        System.out.println("Loaded keras model : " + modelName);
        System.out.println("Starting grizzly HTTP server");
        serverMain.initHttpServerAndWebSocketPort();

        System.out.println("returned back to main thread after init http --- locking thread");

        try {

            //Throw onto new thread
            Runnable runnable = ()-> {
                long pid = 0;
                try {
                    System.out.println("starting keras server....");
                    pid = startKerasServer();
                    System.out.println("pid: " + pid);
                    Runtime.getRuntime().addShutdownHook(new KillPidThread(pid));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            };
            Thread cmdKerasThread = new Thread(runnable);
            cmdKerasThread.start();

            //Block main thread acess until /init/kerasAckServer is called.
            synchronized (Main.mainThreadLock) {
                while (!Main.hasKerasServerAckd) {
                    Main.mainThreadLock.wait();
                }
                System.out.println("retained access");
                execKerasServerAck();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    

    public static void execKerasServerAck() {

        try {

            System.out.println("Started python server.");
            System.out.println("Loading Classification Model: " + modelName);
            KerasServerCore kerasServerCore = new KerasServerCore(pythonServerURL, pythonServerPort);

            //Make POST request to keras endpoint
            kerasServerCore.loadKerasModel(modelName);

            System.out.println("Initialized HTTP Server & WebSocket port");
            System.out.println("Starting gp3 socket");
            GazepointSocket gazepointSocket = initGP3Socket(simulateGazepointServer);
            System.out.println("Initializing VisualizationWebsocket");
            VisualizationWebsocket visualizationWebsocket = initVisualizationWebsocket();
            System.out.println(" initialized VisualizationWebsocket");
            logFile.logLine("Websocket Connected at: " + System.currentTimeMillis() + ", " + (new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").format((new Date(System.currentTimeMillis())))));


            System.out.println("Starting gaze window");
            GazeWindow gazeWindow = initGazeWindow(gazeWindowSizeInMilliseconds);
            System.out.println("Initialized gaze window");
            System.out.println("Building AdaptationMediator");
            AdaptationMediator adaptationMediator = initAdapationMediator(visualizationWebsocket, gazepointSocket, kerasServerCore, gazeWindow);
            logFile.logLine("Mediator started at: " + System.currentTimeMillis() + ", " + (new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").format((new Date(System.currentTimeMillis())))));

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

    public List<String> readProcessOutput(Process p) throws IOException { return readProcessOutput(p, false);}

    public static List<String> readProcessOutput(Process p, boolean debug) throws IOException {
        ArrayList<String> resultStrings = new ArrayList<>();
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        System.out.println("hi");
        String line = "";
        while (true) {
            line = r.readLine();
            resultStrings.add(line);
            if (line == null) { break; }
            System.out.println(line);
        }
        return resultStrings;
    }


    /**
     * Starts the keras server via a batch script.
     * @return
     * @throws IOException
     */
    public static long startKerasServer() throws IOException {
        //Start keras server from python script and wait for ACK from python that the server started.
        System.out.println("Starting python server..");

        //Launch batch script to start Python server.
        ProcessBuilder initPythonPBuilder = new ProcessBuilder( "scripts/start_flask.bat");

        initPythonPBuilder.redirectErrorStream(true);

        Process p = initPythonPBuilder.start();
        serverMain.readProcessOutput(p);
        return p.pid();
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

    public static VisualizationWebsocket initVisualizationWebsocket() {

        VisualizationWebsocket visWebSocket = new VisualizationWebsocket();
        WebSocketEngine.getEngine().register("", "/gaze", visWebSocket);
        return visWebSocket;

    }

    public static GazepointSocket initGP3Socket(boolean simulateGazepointServer) throws IOException, CsvValidationException {
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
        GazepointSocket gazepointSocket = new GazepointSocket(gp3Hostname, gp3Port);
        gazepointSocket.connect();
        System.out.println("Connected to GP3");
        System.out.println("Starting Data Stream via thread");
        gazepointSocket.startGazeDataStream();
        gazepointSocket.write((mapper.writeValueAsString(new SetEnableSendCommand(GazeApiCommands.ENABLE_SEND_POG_BEST, true))));

        return gazepointSocket;

    }


    public static GazeWindow initGazeWindow(float windowSizeInMilliseconds) {
        return new GazeWindow(windowSizeInMilliseconds);
    }

    public static AdaptationMediator initAdapationMediator(VisualizationWebsocket websocket, GazepointSocket gazepointSocket, KerasServerCore kerasServerCore, GazeWindow window) {
        return new AdaptationMediator(websocket, gazepointSocket, kerasServerCore, window, Main.numSequencesForClassification);
    }

    /**
     * Makes a simple call to create a 1,1 nd4j array to preload libs.
     */
    public static void preloadND4J() {
        Nd4j.create(1,1); //Used to preload ND4j backend and prevent inference delay
    }

    public static void ShutDownFunction() {

    }
}
