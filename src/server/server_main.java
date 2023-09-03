package server;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import server.recvcommands.RecCounter;
import server.recvcommands.RecTime;
import server.recvcommands.RecXmlObject;

import javax.xml.rpc.encoding.XMLType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class server_main {

    public static void serializationTest() {
        XmlMapper mapper = new XmlMapper();
        String serialString = "<REC CNT=\"34\"/>";
        String serialString2 = "<REC TIME=\"3434344\"/>";
        RecXmlObject someXmlObj = null;
        PolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator.builder()
                .allowIfSubType("com.baeldung.jackson.inheritance")
                .build();
        mapper.activateDefaultTyping(mapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL);
        List<Class<? extends RecXmlObject>> recXmlObjects = new ArrayList<>();
        recXmlObjects.add(RecCounter.class);
        recXmlObjects.add(RecTime.class);

        for (Class<? extends RecXmlObject> xmlClass : recXmlObjects) {
            try {
                someXmlObj = mapper.readValue(serialString, xmlClass);
                System.out.println(someXmlObj.name());
                break;
            } catch (JsonProcessingException e) {
                //couldnt find, ignore and keep trying
                System.out.println(xmlClass);
            }
        }
        for (Class<? extends RecXmlObject> xmlClass2 : recXmlObjects) {
            try {
                someXmlObj = mapper.readValue(serialString2, xmlClass2);
                System.out.println(someXmlObj.name());
                break;
            } catch (JsonProcessingException e) {
                //couldnt find, ignore and keep trying
            }
        }

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
