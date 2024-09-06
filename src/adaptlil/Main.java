package adaptlil;
import adaptlil.websocket.VisualizationWebsocket;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
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
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class Main {

    //TODO replace killPID with kill Python Server
    public static SimpleLogger adaptationLogFile;
    public static SimpleLogger runTimeLogfile;
    public static boolean hasKerasServerAckd = false;
    public static EnvironmentConfig EnvironmentConfig;
    //Used to block main thread from making a keras server load_modal before receiving an ACK
    public static final ReentrantLock mainThreadLock = new ReentrantLock();

    public static void main(String[] args) throws IOException {
        preloadND4J();
        Main.initLoggers();
        Main.loadEnvConfig();

        //TODO
        //Replace with python kill command.
        Thread printingHook = new Thread(() -> Main.ShutDownFunction());
        Runtime.getRuntime().addShutdownHook(printingHook);

        adaptationLogFile.logLine("App Ran at," + adaptationLogFile.getDateTimeStamp() + "," + System.currentTimeMillis());
        runTimeLogfile.printAndLog("Beginning GP3 Real-Time Prototype Stream");

        //Give adaptation mediator the keras endpoint
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

    private static void loadEnvConfig() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        Main.EnvironmentConfig = objectMapper.readValue(new File("src/main/resources/customer.yaml"), EnvironmentConfig.class);
    }

    /**
     * Creates folder for loggers and intializes them.
     * resulting directory /logs/System.currentTimeMillis()
     */
    private static void initLoggers() {
        Long currentTime = System.currentTimeMillis();
        File logDir = new File(Paths.get("logs", currentTime.toString()).toString());


        if (logDir.mkdirs()) {
            Main.adaptationLogFile = new SimpleLogger(new File(Paths.get(logDir.toString(), "AdaptationOutput_" + currentTime).toString()));
            Main.runTimeLogfile = new SimpleLogger(new File(Paths.get(logDir.toString(), "RunTime" + currentTime).toString()));
        } else {
            throw new RuntimeException("Exception occured making the directory for logging");
        }
    }
    

    public static void execKerasServerAck() {

        try {

            runTimeLogfile.printAndLog("Started python server.");
            runTimeLogfile.printAndLog("Loading Classification Model: " + EnvironmentConfig.DEEP_LEARNING_MODEL_NAME);
            KerasServerCore kerasServerCore = new KerasServerCore(EnvironmentConfig.PYTHON_SERVER_URL, EnvironmentConfig.PYTHON_SERVER_PORT);

            //Make POST request to keras endpoint
            kerasServerCore.loadKerasModel(EnvironmentConfig.DEEP_LEARNING_MODEL_NAME);

            GazepointSocket gazepointSocket = initGazepointSocket(EnvironmentConfig.SIMULATE_GAZE_SERVER);
            VisualizationWebsocket visualizationWebsocket = initVisualizationWebsocket();
            adaptationLogFile.logLine("Websocket Connected at," + adaptationLogFile.getDateTimeStamp() + "," + System.currentTimeMillis());


            runTimeLogfile.printAndLog("Starting gaze window");
            GazeWindow gazeWindow = initGazeWindow(EnvironmentConfig.GAZE_WINDOW_SIZE_IN_MILLISECONDS);
            runTimeLogfile.printAndLog("Initialized gaze window");
            runTimeLogfile.printAndLog("Building AdaptationMediator");
            AdaptationMediator adaptationMediator = initAdaptationMediator(visualizationWebsocket, gazepointSocket, kerasServerCore, gazeWindow);
            adaptationLogFile.logLine("Mediator started at: " + adaptationLogFile.getDateTimeStamp() + "," + System.currentTimeMillis());

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
        runTimeLogfile.printAndLog("Starting grizzly HTTP server");

        URI uri = UriBuilder.fromUri("http://" + EnvironmentConfig.JAVA_URL).port(EnvironmentConfig.JAVA_PORT).build();

        ResourceConfig rc = new ResourceConfig().packages("adaptlil.http.endpoints");
        rc.register(JacksonFeature.class);

        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(uri, rc, false);

        final WebSocketAddOn addon = new WebSocketAddOn();

        server.getListener("grizzly").registerAddOn(addon);

        try {
            server.start();
            runTimeLogfile.printAndLog("Http Server started on  " + EnvironmentConfig.JAVA_URL + ":" + EnvironmentConfig.JAVA_PORT);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return server;
    }

    public static VisualizationWebsocket initVisualizationWebsocket() {
        runTimeLogfile.printAndLog("Initializing VisualizationWebsocket");

        VisualizationWebsocket visWebSocket = new VisualizationWebsocket();
        WebSocketEngine.getEngine().register("", "/gaze", visWebSocket);

        runTimeLogfile.printAndLog(" initialized VisualizationWebsocket");

        return visWebSocket;

    }

    public static GazepointSocket initGazepointSocket(boolean simulateGazepointServer) throws IOException, CsvValidationException {
        runTimeLogfile.printAndLog("Starting Socket to connect to Gazepoint eye tracker");

        XmlMapper mapper = new XmlMapper();

        if (simulateGazepointServer) {
            Path curPath = Paths.get("");

            File gazeFile = new File(curPath.resolve("User 0_all_gaze.csv").toAbsolutePath().toString());

            GazepointSimulationServer simulationServer = new GazepointSimulationServer(EnvironmentConfig.EYETRACKER_URL, EnvironmentConfig.EYETRACKER_PORT, gazeFile, true);
            Thread serverSimThread = new Thread(simulationServer);
            serverSimThread.start();
        }

        GazepointSocket gazepointSocket = new GazepointSocket(EnvironmentConfig.EYETRACKER_URL, EnvironmentConfig.EYETRACKER_PORT);
        gazepointSocket.connect();
        runTimeLogfile.printAndLog("Connected to gazepoint eye tracker");

        gazepointSocket.startGazeDataStream();
        gazepointSocket.writeToGazepointSocket((mapper.writeValueAsString(new SetEnableSendCommand(GazeApiCommands.ENABLE_SEND_POG_BEST, true))));

        return gazepointSocket;

    }


    public static GazeWindow initGazeWindow(float windowSizeInMilliseconds) {
        return new GazeWindow(windowSizeInMilliseconds, Main.EnvironmentConfig.EYETRACKER_REFRESH_RATE);
    }

    public static AdaptationMediator initAdaptationMediator(VisualizationWebsocket websocket, GazepointSocket gazepointSocket, KerasServerCore kerasServerCore, GazeWindow window) {
        return new AdaptationMediator(websocket, gazepointSocket, kerasServerCore, window, EnvironmentConfig.GAZE_CHUNKS_FOR_TIME_SERIES_INPUT);
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
