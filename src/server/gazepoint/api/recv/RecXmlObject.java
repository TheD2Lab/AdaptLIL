package server.gazepoint.api.recv;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import server.gazepoint.api.XmlObject;
import data_classes.Cursor;
import data_classes.Fixation;
import server.serialization_helpers.IntToBooleanDeserializer;

/**
 * <REC CNT="238465" FPOGX="0.18760" FPOGY="0.78100" FPOGS="1603.46423" FPOGD="0.08008" FPOGID="2078" FPOGV="1" BKID="0" BKDUR="0.00000" BKPMIN="15" DIAL="0.00000" DIALV="0" HR="0.00000" HRV="0" />
 * Looks ike gaze api doesnt send different rec templates where we have to map.
 * It sends one single rec element with unique attribute ids for each command set.
 */
@JacksonXmlRootElement(localName = "REC")

public class RecXmlObject extends XmlObject {
    private Fixation fixation;
    private Cursor cursor;

    //-----------------ENABLE_SEND_POG_FIX----------------

    /**
     * Description: The Fixation POG data provides the user’s point-of-gaze as determined by the
     * internal fixation filter.
     * Parameter ID: FPOGX, FPOGY
     * Parameter type: float
     * Parameter description: The X- and Y-coordinates of the fixation POG, as a fraction of the screen size.
     * (0,0) is top left, (0.5,0.5) is the screen center, and (1.0,1.0) is bottom right.
     * Parameter ID: FPOGS
     * Parameter type: float
     * Parameter description: The starting time of the fixation POG in seconds since the system initialization or
     * calibration.
     * Parameter ID: FPOGD
     * Parameter type: float
     * Parameter description: The duration of the fixation POG in seconds.
     * Parameter ID: FPOGID
     * Parameter type: integer
     * Parameter description: The fixation POG ID number
     * Parameter ID: FPOGV
     * Parameter type: boolean
     * Parameter description: The valid flag with value of 1 (TRUE) if the fixation POG data is valid, and 0
     * (FALSE) if it is not. FPOGV valid is TRUE ONLY when either one, or both, of the eyes are detected AND a
     * fixation is detected. FPOGV is FALSE all other times, for example when the subject blinks, when there is
     * no face in the field of view, when the eyes move to the next fixation (i.e. a saccade).
     * Enable: ENABLE_SEND_POG_FIX
     */
    @JacksonXmlProperty(isAttribute = true, localName = "FPOGX")
    public Float FPOGX;
    @JacksonXmlProperty(isAttribute = true, localName = "FPOGY")
    public Float FPOGY;
    @JacksonXmlProperty(isAttribute = true, localName = "FPOGS")
    public Float FPOGS;
    @JacksonXmlProperty(isAttribute = true, localName = "FPOGD")
    public Float FPOGD;
    @JacksonXmlProperty(isAttribute = true, localName = "FPOGID")
    public Integer FPOGID;
    @JsonDeserialize(using = IntToBooleanDeserializer.class)
    @JacksonXmlProperty(isAttribute = true, localName = "FPOGV")
    public Boolean FPOGV;


    //-------------Enable_Send_Cursor--------------------
    /**
     * 5.13 Cursor position
     * Description: The position of the mouse cursor.
     * Parameter ID: CX, CY
     * Parameter type: float
     * Parameter description: The X- and Y-coordinates of the mouse cursor, as percentage of the screen size.
     * Parameter ID: CS (0 for idle, 1 for left mouse button down, 2 for right button down, 3 for left
     * button up, 4 for right button up.
     * Parameter type: integer
     * Parameter description: Mouse cursor state, 0 for steady state, 1 for left button down, 2 for right button
     * down, 3 for left button up, 4 for right button up
     */
    @JacksonXmlProperty(isAttribute = true, localName = "CX")
    public Float CX;
    @JacksonXmlProperty(isAttribute = true, localName = "CY")
    public Float CY;
    @JacksonXmlProperty(isAttribute = true,localName = "CS")
    public Integer CS;


    //-----------ENABLE_SEND_COUNTER-----------------
    @JacksonXmlProperty(isAttribute = true, localName = "CNT")
    public Integer counter;

    //----------Enable_Send_Time---------
    @JacksonXmlProperty(isAttribute = true, localName = "TIME")
    public Float time;
    //------------Enable_Send_Time_Tick
    @JacksonXmlProperty(isAttribute = true, localName = "TIME_TICK")
    public Long timeTick;


    public Fixation getFixation() {
        if (fixation == null && this.getFPOGV() != null)
            return new Fixation(this.getFPOGX(), this.getFPOGY(), this.getFPOGS(), this.getFPOGD(), this.getFPOGV(), this.getFPOGID());

        return null;
    }

    public Cursor getCursor() {
        return cursor;
    }

    public Float getFPOGX() {
        return FPOGX;
    }

    public Float getFPOGY() {
        return FPOGY;
    }

    public Float getFPOGS() {
        return FPOGS;
    }

    public Float getFPOGD() {
        return FPOGD;
    }

    public Integer getFPOGID() {
        return FPOGID;
    }

    public Boolean getFPOGV() {
        return FPOGV;
    }

    public Float getCX() {
        return CX;
    }

    public Float getCY() {
        return CY;
    }

    public Integer getCS() {
        return CS;
    }

    public Integer getCounter() {
        return counter;
    }

    public Float getTime() {
        return time;
    }

    public Long getTimeTick() {
        return timeTick;
    }
}
