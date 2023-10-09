package server.gazepoint.api.recv;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import data_classes.*;
import server.gazepoint.api.XmlObject;
import server.serialization_helpers.IntToBooleanDeserializer;

/**
 * Implements GazeAPI for real time processing of gaze data.
 * public class attributes are used to convert this object to a WEKA instance for machine learning.
 * private attributes are generally data_classes that mirror the GazeAPI for a more 'best-practice' development experience
 * <REC CNT="238465" FPOGX="0.18760" FPOGY="0.78100" FPOGS="1603.46423" FPOGD="0.08008" FPOGID="2078" FPOGV="1" BKID="0" BKDUR="0.00000" BKPMIN="15" DIAL="0.00000" DIALV="0" HR="0.00000" HRV="0" />
 * Looks ike gaze api doesnt send different rec templates where we have to map.
 * It sends one single rec element with unique attribute ids for each command set.
 */
@JacksonXmlRootElement(localName = "REC")

public class RecXmlObject extends XmlObject {
    private Fixation fixation;
    private Cursor cursor;
    private BestPointOfGaze bestPointOfGaze;
    private LeftEyePointOfGaze leftEyePointOfGaze;
    private RightEyePointOfGaze rightEyePointOfGaze;
    private LeftEyePupil leftEyePupil;
    private RightEyePupil rightEyePupil;
    private PupilDiameter pupilDiameter;

    //-----------------ENABLE_SEND_POG_FIX----------------
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

    //------ENABLE_SEND_POG_BEST-------------------
    @JacksonXmlProperty(isAttribute = true, localName = "BPOGX")
    public Float BPOGX;

    @JacksonXmlProperty(isAttribute = true, localName = "BPOGY")
    public Float BPOGY;

    @JsonDeserialize(using = IntToBooleanDeserializer.class)
    @JacksonXmlProperty(isAttribute = true, localName = "BPOGV")
    public Boolean BPOGV;

    //------------ENABLE_SEND_POG_LEFT_---------------

    @JacksonXmlProperty(isAttribute = true, localName = "LPOGX")
    public Float LPOGX;

    @JacksonXmlProperty(isAttribute = true, localName = "LPOGY")
    public Float LPOGY;

    @JsonDeserialize(using = IntToBooleanDeserializer.class)
    @JacksonXmlProperty(isAttribute = true, localName = "LPOGV")
    public Boolean LPOGV;

    //----------ENABLE_SEND_POG_RIGHT-------------------

    @JacksonXmlProperty(isAttribute = true, localName = "RPOGX")
    public Float RPOGX;

    @JacksonXmlProperty(isAttribute = true, localName = "RPOGY")
    public Float RPOGY;

    @JsonDeserialize(using = IntToBooleanDeserializer.class)
    @JacksonXmlProperty(isAttribute = true, localName = "RPOGV")
    public Boolean RPOGV;

    //------------ENABLE_SEND_PUPIL_LEFT--------------------
    @JacksonXmlProperty(isAttribute = true, localName = "LPCX")
    public Float LPCX;

    @JacksonXmlProperty(isAttribute = true, localName = "LPCY")
    public Float LPCY;

    @JacksonXmlProperty(isAttribute = true, localName = "LPD")
    public Float LPD;

    @JacksonXmlProperty(isAttribute = true, localName = "LPS")
    public Float LPS;

    @JsonDeserialize(using = IntToBooleanDeserializer.class)
    @JacksonXmlProperty(isAttribute = true, localName = "LPV")
    public Boolean LPV;

    //------------ENABLE_SEND_PUPIL_RIGHT----------------

    @JacksonXmlProperty(isAttribute = true, localName = "RPCX")
    public Float RPCX;

    @JacksonXmlProperty(isAttribute = true, localName = "RPCY")
    public Float RPCY;

    @JacksonXmlProperty(isAttribute = true, localName = "RPD")
    public Float RPD;

    @JacksonXmlProperty(isAttribute = true, localName = "RPS")
    public Float RPS;

    @JsonDeserialize(using = IntToBooleanDeserializer.class)
    @JacksonXmlProperty(isAttribute = true, localName = "RPV")
    public Boolean RPV;

    //-------------ENABLE_SEND_PUPILMM--------------------
    @JacksonXmlProperty(isAttribute = true, localName = "LPMM")
    public Float LPMM;

    @JsonDeserialize(using = IntToBooleanDeserializer.class)
    @JacksonXmlProperty(isAttribute = true, localName = "LPMMV")
    public Boolean LPMMV;

    @JsonDeserialize(using = IntToBooleanDeserializer.class)
    @JacksonXmlProperty(isAttribute = true, localName = "RPMMV")
    public Boolean RPMMV;

    @JacksonXmlProperty(isAttribute = true, localName = "RPMM")
    public Float RPMM;

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

    /**
     * Returns the data class holder for the Fixation attributes contained in the <REC></REC> packet.
     * @return Fixation
     */
    public Fixation getFixation() {
        //if (fixation == null && this.getFPOGV() != null)
        if (fixation == null && this.getFPOGV() != null)
            this.fixation = new Fixation(this.getFPOGX(), this.getFPOGY(), this.getFPOGS(), this.getFPOGD(), this.getFPOGV(), this.getFPOGID());

        return this.fixation;
    }

    /**
     * Returns the data class holder for BestPointOfGaze attributes contained in this <REC></REC> data packet.
     * @return BestPointOfGaze
     */
    public BestPointOfGaze getBestPointOfGaze() {

        if (bestPointOfGaze == null && this.getBPOGV() != null) {
            this.bestPointOfGaze = new BestPointOfGaze(this.BPOGX, this.BPOGY, this.BPOGV);
        }

        return this.bestPointOfGaze;
    }

    /**
     * Returns the data class holder for the LeftPointOfGaze attributes contained in this <Rec></Rec>
     * @return LeftEyePointOfGaze
     */
    public LeftEyePointOfGaze getLeftEyePointOfGaze() {
        if (leftEyePointOfGaze == null && this.getLPOGV() != null) {
            this.leftEyePointOfGaze = new LeftEyePointOfGaze(this.LPOGX, this.LPOGY, this.LPOGV);
        }
        return this.leftEyePointOfGaze;
    }

    /**
     * Returns the data class holder for the RightEyePointOfGaze attributes contained in this <Rec></Rec>
     * @return RightEyePointOfGaze
     */
    public RightEyePointOfGaze getRightEyePointOfGaze() {
        if (this.rightEyePointOfGaze == null && this.RPOGV != null) {
            this.rightEyePointOfGaze = new RightEyePointOfGaze(this.RPOGX, this.RPOGY, this.RPOGV);
        }
        return this.rightEyePointOfGaze;
    }

    /**
     * Returns the data class holder for the LeftEyePupil attributes contained <Rec></Rec>
     * @return LeftEyePupil
     */
    public LeftEyePupil getLeftEyePupil() {
        if (leftEyePointOfGaze == null && this.LPV != null) {
            this.leftEyePupil = new LeftEyePupil(this.LPCX, this.LPCY, this.LPD, this.LPS, this.LPV);
        }

        return this.leftEyePupil;
    }

    /**
     * Returns the RightEyePupil data class for the attributes contained in the <Rec></Rec> (this)
     * @return RightEyePupil
     */
    public RightEyePupil getRightEyePupil() {
        if (this.rightEyePupil == null && this.RPV != null) {
            this.rightEyePupil = new RightEyePupil(this.RPCX, this.RPCY, this.RPD, this.RPS, this.RPV);
        }

        return this.rightEyePupil;
    }

    /**
     * Data class holder for PupilDiameter attributes of the <REC></REC> (This)
     * @return
     */
    public PupilDiameter getPupilDiameter() {
        if (this.pupilDiameter == null && this.RPMMV != null)
            this.pupilDiameter = new PupilDiameter(this.LPMM, this.LPMMV, this.RPMM, this.RPMMV);

        return this.pupilDiameter;
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

    public Float getBPOGX() {
        return BPOGX;
    }

    public Float getBPOGY() {
        return BPOGY;
    }

    public Boolean getBPOGV() {
        return BPOGV;
    }

    public Float getLPOGX() {
        return LPOGX;
    }

    public Float getLPOGY() {
        return LPOGY;
    }

    public Boolean getLPOGV() {
        return LPOGV;
    }

    public Float getRPOGX() {
        return RPOGX;
    }

    public Float getRPOGY() {
        return RPOGY;
    }

    public Boolean getRPOGV() {
        return RPOGV;
    }

    public Float getLPCX() {
        return LPCX;
    }

    public Float getLPCY() {
        return LPCY;
    }

    public Float getLPD() {
        return LPD;
    }

    public Float getLPS() {
        return LPS;
    }

    public Boolean getLPV() {
        return LPV;
    }

    public Float getRPCX() {
        return RPCX;
    }

    public Float getRPCY() {
        return RPCY;
    }

    public Float getRPD() {
        return RPD;
    }

    public Float getRPS() {
        return RPS;
    }

    public Boolean getRPV() {
        return RPV;
    }

    public Float getLPMM() {
        return LPMM;
    }

    public Boolean getLPMMV() {
        return LPMMV;
    }

    public Boolean getRPMMV() {
        return RPMMV;
    }

    public Float getRPMM() {
        return RPMM;
    }
    public Long getTimeTick() {
        return timeTick;
    }
}
