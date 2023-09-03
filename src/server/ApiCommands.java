package server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import server.ackcommands.AckXmlObject;
import server.recvcommands.*;
import weka.Run;

import java.util.ArrayList;
import java.util.List;

/**
 * Static grabber file to obtain Class types for Xml deserialization.
 */
public class ApiCommands {

    private static XmlMapper xmlMapper = new XmlMapper();

    /**
     * Contains a list of classes that extend RecXmlObject.
     * These contain the class reference so the xmlMapper can try it against many types when we don't know exactly what
     * the command will be.
     * @return
     */
    public static List<Class<? extends RecXmlObject>> getRecCommands() {
        ArrayList<Class<? extends RecXmlObject>> recCommandList = new ArrayList<>();
        recCommandList.add(RecFixationPOG.class);

        recCommandList.add(RecCounter.class);
        recCommandList.add(RecTime.class);
        recCommandList.add(RecTimeTick.class);
        return recCommandList;
    }

    public static List<Class<? extends AckXmlObject>> getAckCommands() {
        ArrayList<Class<? extends AckXmlObject>> ackCommandList = new ArrayList<>();
        ackCommandList.add(null);
        return ackCommandList;
    }

    /**
     * Attempts to map a REC xml from Gaze. If one is found, it is returned, otherwise null.
     * NOTE: this downcasts the object so to get the proper object, do an instanceof check on the type to properly deal
     * with the correct xml object.
     *      (e.g.) -> if (xmlObject instance of RecFixationPOG) ((RecFixationPOG) xmlObject))
     *          now you can call the correct methods and handle the data properly.
     * @param xml
     * @return
     */
    public static RecXmlObject mapRecCommandToXmlObject(String xml) {
        RecXmlObject recXmlObject;
        for (Class<? extends RecXmlObject> recXmlClass : ApiCommands.getRecCommands()) {
            try {
                recXmlObject = xmlMapper.readValue(xml, recXmlClass);
                //If we reach here, no exception throw, return object as it found a match
                return recXmlObject;
            } catch (JsonProcessingException e) {
                //Do nothing, continue trying to parse
                //Not optimal but hey, let's just get it working
            }
        }
        return null;
    }

    /**
     * Attempts to map the ACK commands from Gaze to a coresponding XmlObject.
     * If one is found, it will be returned, otherwise null.
     * @param xml
     * @return
     */
    public static AckXmlObject mapAckStringToXmlObject(String xml) {
        AckXmlObject ackXmlObject;

        for (Class<? extends AckXmlObject> ackXmlClass : ApiCommands.getAckCommands()) {
            try {
                ackXmlObject = xmlMapper.readValue(xml, ackXmlClass);
                //Finally found w.o exception being thrown
                return ackXmlObject;
            } catch (JsonProcessingException e) {
                //Do nothing continue searching for a matching ackXml Object
            }
        }
        return null;
    }

}
