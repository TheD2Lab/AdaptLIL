package server.gazepoint.api.recv;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import data_classes.*;
import server.gazepoint.api.XmlObject;
import server.serialization_helpers.IntToBooleanDeserializer;
import interpolation.*;
import wekaext.annotations.IgnoreWekaAttribute;

import java.util.List;

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
    public Double FPOGX;

    @JacksonXmlProperty(isAttribute = true, localName = "FPOGY")
    public Double FPOGY;

    @JacksonXmlProperty(isAttribute = true, localName = "FPOGS")
    public Double FPOGS;

    @JacksonXmlProperty(isAttribute = true, localName = "FPOGD")
    public Double FPOGD;

    @IgnoreWekaAttribute
    @JacksonXmlProperty(isAttribute = true, localName = "FPOGID")
    public Integer FPOGID;

    @IgnoreWekaAttribute
    @JsonDeserialize(using = IntToBooleanDeserializer.class)
    @JacksonXmlProperty(isAttribute = true, localName = "FPOGV")
    public Boolean FPOGV;

    //------ENABLE_SEND_POG_BEST-------------------
    @JacksonXmlProperty(isAttribute = true, localName = "BPOGX")
    public Double BPOGX;

    @JacksonXmlProperty(isAttribute = true, localName = "BPOGY")
    public Double BPOGY;

    @IgnoreWekaAttribute
    @JsonDeserialize(using = IntToBooleanDeserializer.class)
    @JacksonXmlProperty(isAttribute = true, localName = "BPOGV")
    public Boolean BPOGV;

    //------------ENABLE_SEND_POG_LEFT_---------------

    @JacksonXmlProperty(isAttribute = true, localName = "LPOGX")
    public Double LPOGX;

    @JacksonXmlProperty(isAttribute = true, localName = "LPOGY")
    public Double LPOGY;

    @IgnoreWekaAttribute
    @JsonDeserialize(using = IntToBooleanDeserializer.class)
    @JacksonXmlProperty(isAttribute = true, localName = "LPOGV")
    public Boolean LPOGV;

    //----------ENABLE_SEND_POG_RIGHT-------------------

    @JacksonXmlProperty(isAttribute = true, localName = "RPOGX")
    public Double RPOGX;

    @JacksonXmlProperty(isAttribute = true, localName = "RPOGY")
    public Double RPOGY;

    @IgnoreWekaAttribute
    @JsonDeserialize(using = IntToBooleanDeserializer.class)
    @JacksonXmlProperty(isAttribute = true, localName = "RPOGV")
    public Boolean RPOGV;

    //------------ENABLE_SEND_PUPIL_LEFT--------------------
    @IgnoreWekaAttribute
    @JacksonXmlProperty(isAttribute = true, localName = "LPCX")
    public Double LPCX;

    @IgnoreWekaAttribute
    @JacksonXmlProperty(isAttribute = true, localName = "LPCY")
    public Double LPCY;

    @JacksonXmlProperty(isAttribute = true, localName = "LPD")
    public Double LPD;

    @IgnoreWekaAttribute
    @JacksonXmlProperty(isAttribute = true, localName = "LPS")
    public Double LPS;

    @IgnoreWekaAttribute
    @JsonDeserialize(using = IntToBooleanDeserializer.class)
    @JacksonXmlProperty(isAttribute = true, localName = "LPV")
    public Boolean LPV;

    //------------ENABLE_SEND_PUPIL_RIGHT----------------
    @IgnoreWekaAttribute
    @JacksonXmlProperty(isAttribute = true, localName = "RPCX")
    public Double RPCX;

    @IgnoreWekaAttribute
    @JacksonXmlProperty(isAttribute = true, localName = "RPCY")
    public Double RPCY;

    @JacksonXmlProperty(isAttribute = true, localName = "RPD")
    public Double RPD;

    @JacksonXmlProperty(isAttribute = true, localName = "RPS")
    public Double RPS;

    @IgnoreWekaAttribute
    @JsonDeserialize(using = IntToBooleanDeserializer.class)
    @JacksonXmlProperty(isAttribute = true, localName = "RPV")
    public Boolean RPV;

    //-------------ENABLE_SEND_PUPILMM--------------------
    @JacksonXmlProperty(isAttribute = true, localName = "LPMM")
    public Double LPMM;

    @IgnoreWekaAttribute
    @JsonDeserialize(using = IntToBooleanDeserializer.class)
    @JacksonXmlProperty(isAttribute = true, localName = "LPMMV")
    public Boolean LPMMV;

    @IgnoreWekaAttribute
    @JsonDeserialize(using = IntToBooleanDeserializer.class)
    @JacksonXmlProperty(isAttribute = true, localName = "RPMMV")
    public Boolean RPMMV;

    @JacksonXmlProperty(isAttribute = true, localName = "RPMM")
    public Double RPMM;

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
    public Double CX;
    @JacksonXmlProperty(isAttribute = true, localName = "CY")
    public Double CY;
    @JacksonXmlProperty(isAttribute = true,localName = "CS")
    public Integer CS;


    //-----------ENABLE_SEND_COUNTER-----------------
    @IgnoreWekaAttribute
    @JacksonXmlProperty(isAttribute = true, localName = "CNT")
    public Integer counter;

    //----------Enable_Send_Time---------
    @JacksonXmlProperty(isAttribute = true, localName = "TIME")
    private Double time;
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

    public Double getFPOGX() {
        return FPOGX;
    }

    public Double getFPOGY() {
        return FPOGY;
    }

    public Double getFPOGS() {
        return FPOGS;
    }

    public Double getFPOGD() {
        return FPOGD;
    }

    public Integer getFPOGID() {
        return FPOGID;
    }

    public Boolean getFPOGV() {
        return FPOGV;
    }

    public Double getCX() {
        return CX;
    }

    public Double getCY() {
        return CY;
    }

    public Integer getCS() {
        return CS;
    }

    public Integer getCounter() {
        return counter;
    }

    public Double getTime() {
        return time;
    }

    public Double getBPOGX() {
        return BPOGX;
    }

    public Double getBPOGY() {
        return BPOGY;
    }

    public Boolean getBPOGV() {
        return BPOGV;
    }

    public Double getLPOGX() {
        return LPOGX;
    }

    public Double getLPOGY() {
        return LPOGY;
    }

    public Boolean getLPOGV() {
        return LPOGV;
    }

    public Double getRPOGX() {
        return RPOGX;
    }

    public Double getRPOGY() {
        return RPOGY;
    }

    public Boolean getRPOGV() {
        return RPOGV;
    }

    public Double getLPCX() {
        return LPCX;
    }

    public Double getLPCY() {
        return LPCY;
    }

    public Double getLPD() {
        return LPD;
    }

    public Double getLPS() {
        return LPS;
    }

    public Boolean getLPV() {
        return LPV;
    }

    public Double getRPCX() {
        return RPCX;
    }

    public Double getRPCY() {
        return RPCY;
    }

    public Double getRPD() {
        return RPD;
    }

    public Double getRPS() {
        return RPS;
    }

    public Boolean getRPV() {
        return RPV;
    }

    public Double getLPMM() {
        return LPMM;
    }

    public Boolean getLPMMV() {
        return LPMMV;
    }

    public Boolean getRPMMV() {
        return RPMMV;
    }

    public Double getRPMM() {
        return RPMM;
    }
    public Long getTimeTick() {
        return timeTick;
    }

    /**
     * Sets the fixation object and the relative GazePoint Data metric names [e.g. FPOGX, FPOGY]
     * @param fixation
     */
    public void setFixation(Fixation fixation) {
        this.fixation = fixation;
        this.FPOGX = fixation.getX();
        this.FPOGY = fixation.getY();
        this.FPOGD = fixation.getDuration();
        this.FPOGS = fixation.getStartTime();
        this.FPOGID = fixation.getId();
        this.FPOGV = fixation.isValid();

    }

    /**
     * Sets the bestPointOfGaze attribute and its relative GazePoitn data metrics [e.g. BPOGX, BPOGY]
     * @param bestPointOfGaze The object to set the RecXmlObject bestPointOfGaze attribute to.
     */
    public void setBestPointOfGaze(BestPointOfGaze bestPointOfGaze) {
        this.bestPointOfGaze = bestPointOfGaze;
        this.BPOGX = bestPointOfGaze.getX();
        this.BPOGY = bestPointOfGaze.getY();
        this.BPOGV = bestPointOfGaze.isValid();
    }

    /**
     * Sets the leftEyePointOfGaze attribute and its relative GazePoint data metrics [e.g. LPOGX, LPOGY]
     * @param leftEyePointOfGaze The object to set the RecXmlObject leftEyePointOfGaze attribute to.
     */
    public void setLeftEyePointOfGaze(LeftEyePointOfGaze leftEyePointOfGaze) {
        this.leftEyePointOfGaze = leftEyePointOfGaze;
        this.LPOGX = leftEyePointOfGaze.getX();
        this.LPOGY = leftEyePointOfGaze.getY();
        this.LPOGV = leftEyePointOfGaze.isValid();
    }

    /**
     * Sets the rightEyePointOfGaze attribute and its relative GazePoint data metrics [e.g. RPOGX, RPOGY]
     * @param rightEyePointOfGaze The actual param to set the RecXmlObject.rightEyePointOfGaze to.
     */
    public void setRightEyePointOfGaze(RightEyePointOfGaze rightEyePointOfGaze) {
        this.rightEyePointOfGaze = rightEyePointOfGaze;
        this.RPOGX = rightEyePointOfGaze.getX();
        this.RPOGY = rightEyePointOfGaze.getY();
        this.RPOGV = rightEyePointOfGaze.isValid();
    }

    /**
     * Sets the LeftEyePupil attribute and its relative GazePoint data metrics [e.g. LPV, LPCX, LPCY, LPD]
     * @param leftEyePupil the actual param to set the RecXmlObject.leftEyePupil to.
     */
    public void setLeftEyePupil(LeftEyePupil leftEyePupil) {
        this.leftEyePupil = leftEyePupil;
        this.LPCX = leftEyePupil.getX();
        this.LPCY = leftEyePupil.getY();
        this.LPD = leftEyePupil.getDiameter();
        this.LPS = leftEyePupil.getScale();
        this.LPV = leftEyePupil.isValid();
    }

    /**
     * Sets the RightEyePupil attribute and its relative GazePoint data metrics [e.g. RPCX, RPCY, RPV, RPD]
     * @param rightEyePupil the actual param to set the RecXmlObject.rightEyePupil to.
     */
    public void setRightEyePupil(RightEyePupil rightEyePupil) {
        this.rightEyePupil = rightEyePupil;
        this.RPCX = rightEyePupil.getX();
        this.RPCY = rightEyePupil.getY();
        this.RPV = rightEyePupil.isValid();
        this.RPD = rightEyePupil.getDiameter();
        this.RPS = rightEyePupil.getScale();
    }

    /**
     * Sets the PupilDiameter attribute of this class and its relative GazePoint data metrics [e.g. RPMM, RPMMV, LPMM, LPMMV]
     * @param pupilDiameter The actual param to set the RecXmlObject.pupilDiameter to.
     */
    public void setPupilDiameter(PupilDiameter pupilDiameter) {
        this.pupilDiameter = pupilDiameter;
        this.RPMM = pupilDiameter.getDiameterOfRightEyeInMM();
        this.RPMMV = pupilDiameter.isRightEyeValid();
        this.LPMM = pupilDiameter.getDiameterOfLeftEyeInMM();
        this.LPMMV = pupilDiameter.isLeftEyeValid();
    }

    public void setTime(Double time) {
        this.time = time;
    }
	
    public RecXmlObject[] interpolate(RecXmlObject first, RecXmlObject last, List<RecXmlObject> unmodified, int steps){
        RecXmlObject[] interpolationObjs = new RecXmlObject[steps];
        BestPointOfGaze[] bestPointOfGazes;
        if (first.getBestPointOfGaze() != null && last.getBestPointOfGaze() != null)
            bestPointOfGazes = first.getBestPointOfGaze().interpolate(first.getBestPointOfGaze(), last.getBestPointOfGaze(), steps);
        else
            bestPointOfGazes = null;

        LeftEyePointOfGaze[] leftEyePointOfGazes;
        if (first.getLeftEyePointOfGaze() != null && last.getLeftEyePointOfGaze() != null)
            leftEyePointOfGazes = first.getLeftEyePointOfGaze().interpolate(first.getLeftEyePointOfGaze(), last.getLeftEyePointOfGaze(), steps);
        else
            leftEyePointOfGazes = null;

        RightEyePointOfGaze[] rightEyePointOfGazes;

        if (first.getRightEyePointOfGaze() != null && last.getRightEyePointOfGaze() != null)
            rightEyePointOfGazes = first.getRightEyePointOfGaze().interpolate(first.getRightEyePointOfGaze(), last.getRightEyePointOfGaze(), steps);
        else
            rightEyePointOfGazes = null;


        PupilDiameter[] pupilDiameters;
        if (first.getPupilDiameter() != null && last.getPupilDiameter() != null)
            pupilDiameters = first.getPupilDiameter().interpolate(first.getPupilDiameter(), last.getPupilDiameter(), steps);
        else
            pupilDiameters = null;

        Fixation[] fixations;
        if (first.getFixation() != null && last.getFixation() != null)
            fixations = first.getFixation().interpolate(first.getFixation(), last.getFixation(), steps);
        else
            fixations = null;

        LeftEyePupil[] leftEyePupils;
        if (first.getLeftEyePupil() != null && last.getLeftEyePupil() != null)
            leftEyePupils = first.getLeftEyePupil().interpolate(first.getLeftEyePupil(), last.getLeftEyePupil(), steps);
        else
            leftEyePupils = null;

        RightEyePupil[] rightEyePupils;
        if (first.getRightEyePupil() != null && last.getRightEyePupil() != null)
            rightEyePupils = first.getRightEyePupil().interpolate(first.getRightEyePupil(), last.getRightEyePupil(), steps);
        else
            rightEyePupils = null;


        //Only interpolate on data attributes that are not valid
        for (int i = 0; i < steps; ++i) {
            RecXmlObject interpolatedRec = unmodified.get(i);

            if ((unmodified.get(i).getFixation() == null || !unmodified.get(i).getFixation().isValid()) && fixations != null) {
                interpolatedRec.setFixation(fixations[i]);
            }

            if ((unmodified.get(i).getBestPointOfGaze() == null || !unmodified.get(i).getBestPointOfGaze().isValid()) && bestPointOfGazes != null) {
                interpolatedRec.setBestPointOfGaze(bestPointOfGazes[i]);
            }

            if ((unmodified.get(i).getLeftEyePupil() == null || !unmodified.get(i).getLeftEyePupil().isValid()) && leftEyePupils != null) {
                interpolatedRec.setLeftEyePupil(leftEyePupils[i]);
            }

            if ((unmodified.get(i).getRightEyePupil() == null || !unmodified.get(i).getRightEyePupil().isValid()) && rightEyePupils != null) {
                interpolatedRec.setRightEyePupil(rightEyePupils[i]);
            }

            if ((unmodified.get(i).getLeftEyePointOfGaze() == null || !unmodified.get(i).getLeftEyePointOfGaze().isValid() )&& leftEyePointOfGazes != null) {
                interpolatedRec.setLeftEyePointOfGaze(leftEyePointOfGazes[i]);
            }

            if ((unmodified.get(i).getRightEyePointOfGaze() == null || !unmodified.get(i).getRightEyePointOfGaze().isValid()) && rightEyePointOfGazes != null) {
                interpolatedRec.setRightEyePointOfGaze(rightEyePointOfGazes[i]);
            }

            //This one will lead to reduced accuracy(though we don't use it in the current application)
            //If someone stumbles upon this code, the ideal solution is to go into th einterpolation and have it only interpolate on left eye/right eye valid.
            if (pupilDiameters != null  && ( unmodified.get(i).getPupilDiameter()== null || !unmodified.get(i).getPupilDiameter().isLeftEyeValid() || !unmodified.get(i).getPupilDiameter().isRightEyeValid())) {
                interpolatedRec.setPupilDiameter(pupilDiameters[i]);
            }
            
            interpolationObjs[i] = interpolatedRec;
        }

        return interpolationObjs;
	
	}

    public boolean hasInvalidAttributes() {
        if (this.getFixation() != null && !this.getFixation().isValid())
            return true;
        
        if (this.getBestPointOfGaze() != null && !this.getBestPointOfGaze().isValid())
            return true;
        
        if (this.getLeftEyePupil() != null && !this.getLeftEyePupil().isValid())
            return true;
        
        if (this.getRightEyePupil() != null && !this.getRightEyePupil().isValid())
            return true;
        
//        if (this.getRightEyePointOfGaze() != null && !this.getRightEyePointOfGaze().isValid())
//            return true;
        
//        if (this.getLeftEyePointOfGaze() != null && !this.getLeftEyePointOfGaze().isValid())
//            return true;
        
        return false;
    }

}
