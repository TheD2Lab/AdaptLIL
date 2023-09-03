package server;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import server.gazepoint.api.recv.RecFixationPOG;
import server.gazepoint.api.recv.RecXmlObject;

public class server_main {

    public static void serializationTest() {
        XmlMapper mapper = new XmlMapper();
        String serialString = "<REC CNT=\"34\"/>";
        String serialString2 = "<REC TIME_TICK=\"3434344\"/>";
        String fixationString = "<REC FPOGX=\"0.48439\" FPOGY=\"0.50313\" FPOGS=\"1891.86768\" FPOGD=\"0.49280\" FPOGID=\"1599\" FPOGV=\"1\" />";
        RecXmlObject someXmlObj = null;

        someXmlObj =  GazeApiCommands.mapRecCommandToXmlObject(fixationString);

        if (someXmlObj instanceof RecFixationPOG)
            System.out.println(((RecFixationPOG) someXmlObj).getX());


    }
    public static void main(String[] args) {

        System.out.println("Beginning GP3 Real-Time Prototype Stream");
        serializationTest();
//        GP3Socket gp3Socket = new GP3Socket();
//        try {
//            gp3Socket.connect();
//            System.out.println("Connected to GP3");
//            System.out.println("Starting Data Stream via thread");
//            gp3Socket.startGazeDataStream();
//            System.out.println("Started gaze data stream.");
//
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }

}
