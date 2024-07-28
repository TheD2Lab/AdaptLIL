package adaptlil;
import adaptlil.websocket.VisualizationWebsocket;
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
import adaptlil.gazepoint.api.GazeApiCommands;
import adaptlil.gazepoint.api.GazepointSimulationServer;
import adaptlil.gazepoint.api.GazepointSocket;
import adaptlil.gazepoint.api.set.SetEnableSendCommand;
import adaptlil.http.KerasServerCore;
import adaptlil.mediator.AdaptationMediator;


import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class Main {

    public static final String url = "localhost";
    public static final int port = 8080;

    public static final String pythonServerURL = "localhost";
    public static final int pythonServerPort = 5000;
    public static float gazeWindowSizeInMilliseconds = 1000;
    public static int numSequencesForClassification = 2;
    static boolean simulateGazepointServer = true;
    public static double currentTime = System.currentTimeMillis();
    public static SimpleLogger adaptationLogFile = new SimpleLogger(new File("AdaptationOutput_" +currentTime));
    public static SimpleLogger runTimeLogfile = new SimpleLogger(new File("RunTime"+currentTime));
    public static boolean hasKerasServerAckd = false;
    public static String modelName = "transformer_model_channels.h5";
    public static String gazepointHostName = "localhost";
    public static int gazepointPort = 4242;

    //Used to block main thread from making a keras server load_modal before receiving an ACK
    public static final ReentrantLock mainThreadLock = new ReentrantLock();

    public static void main(String[] args) {
        preloadND4J();
        Thread printingHook = new Thread(() -> Main.ShutDownFunction());
        Runtime.getRuntime().addShutdownHook(printingHook);

        adaptationLogFile.logLine("App Ran at," + (new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").format((new Date(System.currentTimeMillis())))) + "," + System.currentTimeMillis());
        runTimeLogfile.printAndLog("Beginning GP3 Real-Time Prototype Stream");

        //Give adaptation mediator the keras endpoint
        runTimeLogfile.printAndLog("Loaded keras model : " + modelName);
        runTimeLogfile.printAndLog("Starting grizzly HTTP server");
        Main.initHttpServerAndWebSocketPort();

        runTimeLogfile.printAndLog("returned back to main thread after init http --- locking thread");

        try {

            //Throw onto new thread
            Runnable runnable = ()-> {
                long pid = 0;
                try {
                    runTimeLogfile.printAndLog("starting keras server....");
                    pid = startKerasServer();
                    runTimeLogfile.printAndLog("pid: " + pid);
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
                runTimeLogfile.printAndLog("retained access");
                execKerasServerAck();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    

    public static void execKerasServerAck() {

        try {

            runTimeLogfile.printAndLog("Started python server.");
            runTimeLogfile.printAndLog("Loading Classification Model: " + modelName);
            KerasServerCore kerasServerCore = new KerasServerCore(pythonServerURL, pythonServerPort);

            //Make POST request to keras endpoint
            kerasServerCore.loadKerasModel(modelName);

            runTimeLogfile.printAndLog("Initialized HTTP Server & WebSocket port");
            runTimeLogfile.printAndLog("Starting gp3 socket");
            GazepointSocket gazepointSocket = initGP3Socket(simulateGazepointServer);
            runTimeLogfile.printAndLog("Initializing VisualizationWebsocket");
            VisualizationWebsocket visualizationWebsocket = initVisualizationWebsocket();
            runTimeLogfile.printAndLog(" initialized VisualizationWebsocket");
            adaptationLogFile.logLine("Websocket Connected at," + Main.getDateTimeStamp() + "," + System.currentTimeMillis());


            runTimeLogfile.printAndLog("Starting gaze window");
            GazeWindow gazeWindow = initGazeWindow(gazeWindowSizeInMilliseconds);
            runTimeLogfile.printAndLog("Initialized gaze window");
            runTimeLogfile.printAndLog("Building AdaptationMediator");
            AdaptationMediator adaptationMediator = initAdapationMediator(visualizationWebsocket, gazepointSocket, kerasServerCore, gazeWindow);
            adaptationLogFile.logLine("Mediator started at: " + (new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").format((new Date(System.currentTimeMillis())))) + System.currentTimeMillis());

            runTimeLogfile.printAndLog("Constructed AdaptationMediator");
            runTimeLogfile.printAndLog("Main thread access goes to adaptationMediator.");
            runTimeLogfile.printAndLog("Sever stays alive by waiting for input so type anything to exit");
            boolean runAdaptations = true;
            adaptationMediator.start();
        } catch (IOException | CsvValidationException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }



    /**
     * TODO
     * This method and the next are confusing in terms of launch script.
     * Needs to be cleaned up and tested.
     * I believe this is used to output batch script strings
     * Which if that is the case, we can still use it.
     *
     * @param p
     * @throws IOException
     */
    public static void readProcessOutput(Process p) throws IOException {
        ArrayList<String> resultStrings = new ArrayList<>();
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line = "";
        while (line != null) {
            line = r.readLine();
            if (line != null) {
                resultStrings.add(line);
                runTimeLogfile.printAndLog(line);
            }
        }
    }


    /**
     * Starts the keras server via a batch script.
     * @return
     * @throws IOException
     */
    public static long startKerasServer() throws IOException {
        //Start keras server from python script and wait for ACK from python that the server started.
        runTimeLogfile.printAndLog("Starting python server..");

        //Launch batch script to start Python server.
        ProcessBuilder initPythonPBuilder = new ProcessBuilder( "scripts/start_flask.bat");

        initPythonPBuilder.redirectErrorStream(true);

        Process p = initPythonPBuilder.start();
        Main.readProcessOutput(p);
        return p.pid();
    }

    /**
     * https://javaee.github.io/grizzly/websockets.html
     */
    public static HttpServer initHttpServerAndWebSocketPort() {
        URI uri = UriBuilder.fromUri("http://" + url).port(port).build();

        ResourceConfig rc = new ResourceConfig().packages("adaptlil.http.endpoints");
        rc.register(JacksonFeature.class);

        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(uri, rc, false);

        final WebSocketAddOn addon = new WebSocketAddOn();


        server.getListener("grizzly").registerAddOn(addon);

        try {
            server.start();
            runTimeLogfile.printAndLog("Websocket started on  " + url + ":" + port);

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
        Path curPath = Paths.get("");

        File gazeFile = new File(curPath.resolve("User 0_all_gaze.csv").toAbsolutePath().toString());
        if (simulateGazepointServer) {
            GazepointSimulationServer simulationServer = new GazepointSimulationServer(gazepointHostName, gazepointPort, gazeFile, true);
            Thread serverSimThread = new Thread(simulationServer);
            serverSimThread.start();
        }
        GazepointSocket gazepointSocket = new GazepointSocket(gazepointHostName, gazepointPort);
        gazepointSocket.connect();
        runTimeLogfile.printAndLog("Connected to GP3");
        runTimeLogfile.printAndLog("Starting Data Stream via thread");
        gazepointSocket.startGazeDataStream();
        gazepointSocket.writeToGazepointSocket((mapper.writeValueAsString(new SetEnableSendCommand(GazeApiCommands.ENABLE_SEND_POG_BEST, true))));

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


    /**
     * Return the current date and time formatted to yyyy-mm-dd hh:mm:ss
     * @return
     */
    private static String getDateTimeStamp() {
        return (new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").format((new Date(System.currentTimeMillis()))));
    }
}
