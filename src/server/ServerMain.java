package server;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.deeplearning4j.nn.modelimport.keras.KerasModelImport;
import org.deeplearning4j.nn.modelimport.keras.exceptions.InvalidKerasConfigurationException;
import org.deeplearning4j.nn.modelimport.keras.exceptions.UnsupportedKerasConfigurationException;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.websockets.WebSocketAddOn;
import org.glassfish.grizzly.websockets.WebSocketEngine;
import server.gazepoint.api.XmlObject;
import server.gazepoint.api.recv.RecXmlObject;
import server.gazepoint.api.set.SetEnableSendCommand;

import java.io.IOException;

public class ServerMain {

//    public static ServerMain serverMain;
    public static final String url = "localhost";
    public static final int port = 8080;
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

        XmlMapper mapper = new XmlMapper();
        HttpServer server = null;
        System.out.println("Beginning GP3 Real-Time Prototype Stream");
//        MachineLearningExperiments.classificationExperiment();
//        serializationTest();
        GP3Socket gp3Socket = new GP3Socket();
        //screenheight && width should be an ack for consistentcy. see gazepoint documentation
        float screenHeight = 1920;
        float screenWidth = 1080;
        try {
            gp3Socket.connect();
            System.out.println("Connected to GP3");
            System.out.println("Starting Data Stream via thread");
            gp3Socket.startGazeDataStream();
            gp3Socket.write((mapper.writeValueAsString(new SetEnableSendCommand(GazeApiCommands.ENABLE_SEND_POG_FIX, true))));
            //gp3Socket.write((mapper.writeValueAsString(new SetEnableSendCommand(GazeApiCommands.ENABLE_SEND_BLINK, true))));
            //gp3Socket.write((mapper.writeValueAsString(new SetEnableSendCommand(GazeApiCommands.ENABLE_SEND_COUNTER, true))));
            //
            //gp3Socket.write((mapper.writeValueAsString(new SetEnableSendCommand(GazeApiCommands.ENABLE_SEND_DIAL, true))));
            //gp3Socket.write((mapper.writeValueAsString(new SetEnableSendCommand(GazeApiCommands.ENABLE_SEND_HR, true))));
            //gp3Socket.writeSetCommand(new SetEnableSendCommand(GazeApiCommands.ENABLE_SEND_TIME_TICK, true));
            //gp3Socket.writeSetCommand(new SetEnableSendCommand(GazeApiCommands.ENABLE_SEND_TIME, true));


            //gp3Socket.writeSetCommand(new SetEnableSendCommand(GazeApiCommands.ENABLE_SEND_CURSOR, true));

            System.out.println("Started gaze data stream.");

            server = initWebSocket(gp3Socket);
            AdaptiveVisualizationComposer adaptiveVisualizationComposer = initAdaptiveVisualizationConductor();
            System.out.println("Sever stays alive by waiting for input so type anything to exit");

            System.in.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            //assert server != null;
            //server.shutdown();
        }
    }

    /**
     * https://javaee.github.io/grizzly/websockets.html
     */
    public static HttpServer initWebSocket(GP3Socket gp3Socket) {
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

    public static AdaptiveVisualizationComposer initAdaptiveVisualizationConductor(GP3Socket gp3Socket) {
        //Uses resource directory below. Using hardcoding for now and will revisit when I clean this up.
        //String simpleMlp = new ClassPathResource("simple_mlp.h5").getFile().getPath();
        String modelConfigPath = "C:\\Users\\LeaseCalcs\\Desktop\\d2 lab\\gp3 tracking\\models";
        modelConfigPath += "\\stacked_lstm-Adam0,0014_10-30 20_31_55.h5";
        MultiLayerNetwork classifier = null;
        try {
            classifier = KerasModelImport.importKerasSequentialModelAndWeights(modelConfigPath);
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
        AdaptiveVisualizationComposer adaptiveOntoMap = new AdaptiveVisualizationComposer(gp3Socket, classifier);
        WebSocketEngine.getEngine().register("", "/gaze", adaptiveOntoMap);
        return adaptiveOntoMap;
    }

}
