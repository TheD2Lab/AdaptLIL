package adaptovis.gazepoint.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import adaptovis.gazepoint.api.ack.AckXmlObject;
import adaptovis.gazepoint.api.recv.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Static grabber file to obtain Class types for Xml deserialization.
 */
public class GazeApiCommands {

    private static XmlMapper xmlMapper = new XmlMapper();

    /**
     * Static initialization block
     */
    static {
        //Without this configuration, the mapper will fail to map a RecXmlObject because an attribute wasn't annotated in.
        //Furthermore, it allows for the RecXmlObject mega class to exist to encompass all variations of the xml packet.
        xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * Contains a list of classes that extend RecXmlObject.
     * These contain the class reference so the xmlMapper can try it against many types when we don't know exactly what
     * the command will be.
     * @return
     */
    public static List<Class<? extends RecXmlObject>> getRecCommands() {
        ArrayList<Class<? extends RecXmlObject>> recCommandList = new ArrayList<>();
        recCommandList.add(RecXmlObject.class);
        return recCommandList;
    }

    public static List<Class<? extends AckXmlObject>> getAckCommands() {
        ArrayList<Class<? extends AckXmlObject>> ackCommandList = new ArrayList<>();
        ackCommandList.add(AckXmlObject.class);
        return ackCommandList;
    }


    /**
     * Attempts to map an xml from Gaze. If one is found, it is returned, otherwise null.
     * NOTE: this downcasts the object so to get the proper object, do an instanceof check on the type to properly deal
     * with the correct xml object.
     *      (e.g.) -> if (xmlObject instance of RecXmlCommand) ((RecFixationPOG) xmlObject))
     *          now you can call the correct methods and handle the data properly.
     * @param xml
     * @return
     */
    public static XmlObject mapToXmlObject(String xml) {
        RecXmlObject recXmlObject;

        for (Class<? extends RecXmlObject> recXmlClass : GazeApiCommands.getRecCommands()) {
            try {
                recXmlObject = xmlMapper.readValue(xml, recXmlClass);
                //If we reach here, no exception throw, return object as it found a match
                return recXmlObject;
            } catch (JsonProcessingException e) {
                //Do nothing, continue trying to parse
                //Not optimal but hey, let's just get it working
            }
        }

        for (Class<? extends AckXmlObject> ackXmlClass : GazeApiCommands.getAckCommands()) {
            try {
                AckXmlObject ackXmlObject = xmlMapper.readValue(xml, ackXmlClass);
                //Finally found w.o exception being thrown
                return ackXmlObject;
            } catch (JsonProcessingException e) {
                //Do nothing continue searching for a matching ackXml Object
            }
        }

        return null;
    }


    public static final String ENABLE_SEND_DATA = "ENABLE_SEND_DATA";
    public static final String ENABLE_SEND_COUNTER = "ENABLE_SEND_COUNTER";
    public static final String ENABLE_SEND_TIME = "ENABLE_SEND_TIME";
    public static final String ENABLE_SEND_TIME_TICK = "ENABLE_SEND_TIME_TICK";
    public static final String ENABLE_SEND_POG_FIX = "ENABLE_SEND_POG_FIX";
    public static final String ENABLE_SEND_POG_LEFT = "ENABLE_SEND_POG_LEFT";
    public static final String ENABLE_SEND_POG_RIGHT = "ENABLE_SEND_POG_RIGHT";
    public static final String ENABLE_SEND_POG_BEST = "ENABLE_SEND_POG_BEST";
    public static final String ENABLE_SEND_POG_AAC = "ENABLE_SEND_POG_AAC";
    public static final String ENABLE_SEND_PUPIL_LEFT = "ENABLE_SEND_PUPIL_LEFT";
    public static final String ENABLE_SEND_PUPIL_RIGHT = "ENABLE_SEND_PUPIL_RIGHT";
    public static final String ENABLE_SEND_EYE_LEFT = "ENABLE_SEND_EYE_LEFT";
    public static final String ENABLE_SEND_EYE_RIGHT = "ENABLE_SEND_EYE_RIGHT";
    public static final String ENABLE_SEND_CURSOR = "ENABLE_SEND_CURSOR";
    public static final String ENABLE_SEND_BLINK = "ENABLE_SEND_BLINK";
    public static final String ENABLE_SEND_PUPILMM = "ENABLE_SEND_PUPILMM";
    public static final String ENABLE_SEND_DIAL = "ENABLE_SEND_DIAL";
    public static final String ENABLE_SEND_GSR = "ENABLE_SEND_GSR";
    public static final String ENABLE_SEND_HR = "ENABLE_SEND_HR";
    public static final String ENABLE_SEND_HR_PULSE = "ENABLE_SEND_HR_PULSE";
    public static final String ENABLE_SEND_TTL = "ENABLE_SEND_TTL";
    public static final String ENABLE_SEND_PIX = "ENABLE_SEND_PIX";
    public static final String ENABLE_SEND_USER_DATA = "ENABLE_SEND_USER_DATA";
    public static final String CALIBRATE_START = "CALIBRATE_START";
    public static final String CALIBRATE_SHOW = "CALIBRATE_SHOW";
    public static final String CALIBRATE_TIMEOUT = "CALIBRATE_TIMEOUT";
    public static final String CALIBRATE_DELAY = "CALIBRATE_DELAY";
    public static final String CALIBRATE_RESULT_SUMMARY = "CALIBRATE_RESULT_SUMMARY";
    public static final String CALIBRATE_CLEAR = "CALIBRATE_CLEAR";
    public static final String CALIBRATE_RESET = "CALIBRATE_RESET";
    public static final String CALIBRATE_ADDPOINT = "CALIBRATE_ADDPOINT";
    public static final String USER_DATA = "USER_DATA";
    public static final String TRACKER_DISPLAY = "TRACKER_DISPLAY";
    public static final String TIME_TICK_FREQUENCY = "TIME_TICK_FREQUENCY";
    public static final String SCREEN_SIZE = "SCREEN_SIZE";
    public static final String CAMERA_SIZE = "CAMERA_SIZE";
    public static final String PRODUCT_ID = "PRODUCT_ID";
    public static final String SERIAL_ID = "SERIAL_ID";
    public static final String COMPANY_ID = "COMPANY _ID";
    public static final String API_ID = "API_ID";
    public static final String TRACKER_ID = "TRACKER_ID";
    public static final String MARKER_PIX = "MARKER_PIX";
    public static final String AAC_FILTER = "AAC_FILTER";

}
