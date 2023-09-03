package server.getcommands;


import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * https://dzone.com/articles/jaxb-and-inhertiance-using
 */
@JacksonXmlRootElement(localName = "GET")

public class Enable_Send_Command {
    @JacksonXmlProperty(isAttribute = true, localName = "ID")
    public String id;
    @JacksonXmlProperty(isAttribute = true, localName ="STATE")
    public boolean state;


    public Enable_Send_Command(String id, boolean state) {
        this.id = id;
        this.state = state;
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

}
