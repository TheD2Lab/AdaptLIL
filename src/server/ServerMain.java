package server;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.websockets.WebSocketAddOn;
import org.glassfish.grizzly.websockets.WebSocketApplication;
import org.glassfish.grizzly.websockets.WebSocketEngine;
import server.gazepoint.api.XmlObject;
import data_classes.Fixation;
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
        try {
            gp3Socket.connect();
            System.out.println("Connected to GP3");
            System.out.println("Starting Data Stream via thread");
            gp3Socket.startGazeDataStream();
            //gp3Socket.write((mapper.writeValueAsString(new SetEnableSendCommand(GazeApiCommands.ENABLE_SEND_POG_FIX, true))));
            //gp3Socket.write((mapper.writeValueAsString(new SetEnableSendCommand(GazeApiCommands.ENABLE_SEND_BLINK, true))));
            //gp3Socket.write((mapper.writeValueAsString(new SetEnableSendCommand(GazeApiCommands.ENABLE_SEND_COUNTER, true))));
            //
            //gp3Socket.write((mapper.writeValueAsString(new SetEnableSendCommand(GazeApiCommands.ENABLE_SEND_DIAL, true))));
            //gp3Socket.write((mapper.writeValueAsString(new SetEnableSendCommand(GazeApiCommands.ENABLE_SEND_HR, true))));
            //gp3Socket.writeSetCommand(new SetEnableSendCommand(GazeApiCommands.ENABLE_SEND_TIME_TICK, true));
            //gp3Socket.writeSetCommand(new SetEnableSendCommand(GazeApiCommands.ENABLE_SEND_TIME, true));


            //gp3Socket.writeSetCommand(new SetEnableSendCommand(GazeApiCommands.ENABLE_SEND_CURSOR, true));

            Thread.sleep(500);
            //By this point, the data should have stopped writing.

            //We will now test the data class mapping for fixations
            RecXmlObject recObject = gp3Socket.readGazeDataFromBuffer();

            //TODO
            //Check gazepoint for a stop command
            while ( recObject  != null) {
                Fixation fixation = recObject.getFixation();
                System.out.println("buffer size: " + gp3Socket.getGazeDataQueue().size());
                System.out.println("Fixation ("+fixation.getX() + "," + fixation.getY() + ")");
                recObject = gp3Socket.readGazeDataFromBuffer();
            }

//            System.out.println("Started gaze data stream.");

//            System.out.println("Starting websocket...");
//            server = initWebSocket();
////
//            // All
////            System.out.println("Sever stays alive by waiting for input so type anything to exit");
////
//            System.in.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            //server.shutdown();
        }
    }

    /**
     * https://javaee.github.io/grizzly/websockets.html
     */
    public static HttpServer initWebSocket() {
        final HttpServer server = HttpServer.createSimpleServer("/var/www", port);
        final WebSocketAddOn addon = new WebSocketAddOn();
        server.getListener("grizzly").registerAddOn(addon);
        WebSocketApplication adaptiveOntoMapp = new AdaptiveOntoMapApp();
        WebSocketEngine.getEngine().register("", "/gaze", adaptiveOntoMapp);

        try {
            server.start();
            System.out.println("Websocket started on  " + url + ":" + port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return server;
    }

}
