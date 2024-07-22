package adaptlil.gazepoint.api.recv;

import adaptlil.annotations.IgnoreWekaAttribute;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import data_classes.*;
import adaptlil.gazepoint.api.XmlObject;
import adaptlil.serialization_helpers.IntToBooleanDeserializer;

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

public class RecXml extends XmlObject {
    @JsonIgnore
    private Fixation fixation;
    @JsonIgnore
    private Cursor cursor;
    @JsonIgnore
    private BestPointOfGaze bestPointOfGaze;
    @JsonIgnore
    private LeftEyePointOfGaze leftEyePointOfGaze;
    @JsonIgnore
    private RightEyePointOfGaze rightEyePointOfGaze;
    @JsonIgnore
    private LeftEyePupil leftEyePupil;
    @JsonIgnore
    private RightEyePupil rightEyePupil;
    @JsonIgnore
    private PupilDiameter pupilDiameter;

    //-----------------ENABLE_SEND_POG_FIX----------------
    @IgnoreWekaAttribute
    @JacksonXmlProperty(isAttribute = true, localName = "FPOGX")
    public Double FPOGX;

    @IgnoreWekaAttribute
    @JacksonXmlProperty(isAttribute = true, localName = "FPOGY")
    public Double FPOGY;

    @IgnoreWekaAttribute
    @JacksonXmlProperty(isAttribute = true, localName = "FPOGS")
    public Double FPOGS;

    @IgnoreWekaAttribute
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
    @IgnoreWekaAttribute
    @JacksonXmlProperty(isAttribute = true, localName = "LPOGX")
    public Double LPOGX;
    @IgnoreWekaAttribute
    @JacksonXmlProperty(isAttribute = true, localName = "LPOGY")
    public Double LPOGY;

    @IgnoreWekaAttribute
    @JsonDeserialize(using = IntToBooleanDeserializer.class)
    @JacksonXmlProperty(isAttribute = true, localName = "LPOGV")
    public Boolean LPOGV;

    //----------ENABLE_SEND_POG_RIGHT-------------------
    @IgnoreWekaAttribute
    @JacksonXmlProperty(isAttribute = true, localName = "RPOGX")
    public Double RPOGX;
    @IgnoreWekaAttribute
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

    @IgnoreWekaAttribute
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

    @IgnoreWekaAttribute
    @JacksonXmlProperty(isAttribute = true, localName = "RPD")
    public Double RPD;

    @IgnoreWekaAttribute
    @JacksonXmlProperty(isAttribute = true, localName = "RPS")
    public Double RPS;

    @IgnoreWekaAttribute
    @JsonDeserialize(using = IntToBooleanDeserializer.class)
    @JacksonXmlProperty(isAttribute = true, localName = "RPV")
    public Boolean RPV;

    //-------------ENABLE_SEND_PUPILMM--------------------
    @IgnoreWekaAttribute

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

    @IgnoreWekaAttribute
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
    @IgnoreWekaAttribute
    @JacksonXmlProperty(isAttribute = true, localName = "CX")
    public Double CX;

    @IgnoreWekaAttribute
    @JacksonXmlProperty(isAttribute = true, localName = "CY")
    public Double CY;

    @IgnoreWekaAttribute
    @JacksonXmlProperty(isAttribute = true,localName = "CS")
    public Integer CS;


    //-----------ENABLE_SEND_COUNTER-----------------
    @IgnoreWekaAttribute
    @JacksonXmlProperty(isAttribute = true, localName = "CNT")
    public Integer counter;

    //----------Enable_Send_Time---------
    @IgnoreWekaAttribute
    @JacksonXmlProperty(isAttribute = true, localName = "TIME")
    private Double time;
    //------------Enable_Send_Time_Tick
    @IgnoreWekaAttribute
    @JacksonXmlProperty(isAttribute = true, localName = "TIME_TICK")
    public Long timeTick;

    /**
     * Returns the data class holder for the Fixation attributes contained in the <REC></REC> packet.
     * @return Fixation
     */
    @JsonIgnore
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
    @JsonIgnore
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
    @JsonIgnore
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
    @JsonIgnore
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
    @JsonIgnore
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
    @JsonIgnore
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
    @JsonIgnore
    public PupilDiameter getPupilDiameter() {
        if (this.pupilDiameter == null && this.RPMMV != null)
            this.pupilDiameter = new PupilDiameter(this.LPMM, this.LPMMV, this.RPMM, this.RPMMV);

        return this.pupilDiameter;
    }
    @JsonIgnore
    public Cursor getCursor() {
        return cursor;
    }

    @JsonIgnore
    public Double getFPOGX() {
        return FPOGX;
    }

    @JsonIgnore
    public Double getFPOGY() {
        return FPOGY;
    }

    @JsonIgnore
    public Double getFPOGS() {
        return FPOGS;
    }

    @JsonIgnore
    public Double getFPOGD() {
        return FPOGD;
    }

    @JsonIgnore
    public Integer getFPOGID() {
        return FPOGID;
    }

    @JsonIgnore
    public Boolean getFPOGV() {
        return FPOGV;
    }

    @JsonIgnore
    public Double getCX() {
        return CX;
    }

    @JsonIgnore
    public Double getCY() {
        return CY;
    }

    @JsonIgnore
    public Integer getCS() {
        return CS;
    }

    @JsonIgnore
    public Integer getCounter() {
        return counter;
    }

    @JsonIgnore
    public Double getTime() {
        return time;
    }

    @JsonIgnore
    public Double getBPOGX() {
        return BPOGX;
    }

    @JsonIgnore
    public Double getBPOGY() {
        return BPOGY;
    }

    @JsonIgnore
    public Boolean getBPOGV() {
        return BPOGV;
    }

    @JsonIgnore
    public Double getLPOGX() {
        return LPOGX;
    }

    @JsonIgnore
    public Double getLPOGY() {
        return LPOGY;
    }

    @JsonIgnore
    public Boolean getLPOGV() {
        return LPOGV;
    }

    @JsonIgnore
    public Double getRPOGX() {
        return RPOGX;
    }

    @JsonIgnore
    public Double getRPOGY() {
        return RPOGY;
    }

    @JsonIgnore
    public Boolean getRPOGV() {
        return RPOGV;
    }

    @JsonIgnore
    public Double getLPCX() {
        return LPCX;
    }

    @JsonIgnore
    public Double getLPCY() {
        return LPCY;
    }

    @JsonIgnore
    public Double getLPD() {
        return LPD;
    }

    @JsonIgnore
    public Double getLPS() {
        return LPS;
    }

    @JsonIgnore
    public Boolean getLPV() {
        return LPV;
    }

    @JsonIgnore
    public Double getRPCX() {
        return RPCX;
    }

    @JsonIgnore
    public Double getRPCY() {
        return RPCY;
    }

    @JsonIgnore
    public Double getRPD() {
        return RPD;
    }

    @JsonIgnore
    public Double getRPS() {
        return RPS;
    }

    @JsonIgnore
    public Boolean getRPV() {
        return RPV;
    }

    @JsonIgnore
    public Double getLPMM() {
        return LPMM;
    }

    @JsonIgnore
    public Boolean getLPMMV() {
        return LPMMV;
    }

    @JsonIgnore
    public Boolean getRPMMV() {
        return RPMMV;
    }

    @JsonIgnore
    public Double getRPMM() {
        return RPMM;
    }

    @JsonIgnore
    public Long getTimeTick() {
        return timeTick;
    }

    /**
     * Sets the fixation object and the relative GazePoint Data metric names [e.g. FPOGX, FPOGY]
     * @param fixation
     */
    public void setFixation(Fixation fixation) {
        this.fixation = fixation;
        if (fixation != null) {
            this.FPOGX = fixation.getX();
            this.FPOGY = fixation.getY();
            this.FPOGD = fixation.getDuration();
            this.FPOGS = fixation.getStartTime();
            this.FPOGID = fixation.getId();
            this.FPOGV = fixation.isValid();
        } else {
            this.FPOGX = null;
            this.FPOGY = null;
            this.FPOGD = null;
            this.FPOGS = null;
            this.FPOGID = null;
            this.FPOGV = null;
        }

    }

    /**
     * Sets the bestPointOfGaze attribute and its relative GazePoitn data metrics [e.g. BPOGX, BPOGY]
     * @param bestPointOfGaze The object to set the RecXmlObject bestPointOfGaze attribute to.
     */
    public void setBestPointOfGaze(BestPointOfGaze bestPointOfGaze) {
        this.bestPointOfGaze = bestPointOfGaze;
        if (bestPointOfGaze != null) {
            this.BPOGX = bestPointOfGaze.getX();
            this.BPOGY = bestPointOfGaze.getY();
            this.BPOGV = bestPointOfGaze.isValid();
        } else {
            this.BPOGX = null;
            this.BPOGY = null;
            this.BPOGV = null;
        }
    }

    /**
     * Sets the leftEyePointOfGaze attribute and its relative GazePoint data metrics [e.g. LPOGX, LPOGY]
     * @param leftEyePointOfGaze The object to set the RecXmlObject leftEyePointOfGaze attribute to.
     */
    public void setLeftEyePointOfGaze(LeftEyePointOfGaze leftEyePointOfGaze) {
        this.leftEyePointOfGaze = leftEyePointOfGaze;
        if (leftEyePointOfGaze != null) {
            this.LPOGX = leftEyePointOfGaze.getX();
            this.LPOGY = leftEyePointOfGaze.getY();
            this.LPOGV = leftEyePointOfGaze.isValid();
        } else {
            this.LPOGX = null;
            this.LPOGY = null;
            this.LPOGV = null;
        }
    }

    /**
     * Sets the rightEyePointOfGaze attribute and its relative GazePoint data metrics [e.g. RPOGX, RPOGY]
     * @param rightEyePointOfGaze The actual param to set the RecXmlObject.rightEyePointOfGaze to.
     */
    public void setRightEyePointOfGaze(RightEyePointOfGaze rightEyePointOfGaze) {
        this.rightEyePointOfGaze = rightEyePointOfGaze;
        if (rightEyePointOfGaze != null) {
            this.RPOGX = rightEyePointOfGaze.getX();
            this.RPOGY = rightEyePointOfGaze.getY();
            this.RPOGV = rightEyePointOfGaze.isValid();
        } else {
            this.RPOGX = null;
            this.RPOGY = null;
            this.RPOGV = null;
        }
    }

    /**
     * Sets the LeftEyePupil attribute and its relative GazePoint data metrics [e.g. LPV, LPCX, LPCY, LPD]
     * @param leftEyePupil the actual param to set the RecXmlObject.leftEyePupil to.
     */
    public void setLeftEyePupil(LeftEyePupil leftEyePupil) {
        this.leftEyePupil = leftEyePupil;
        if (leftEyePupil != null) {
            this.LPCX = leftEyePupil.getX();
            this.LPCY = leftEyePupil.getY();
            this.LPD = leftEyePupil.getDiameter();
            this.LPS = leftEyePupil.getScale();
            this.LPV = leftEyePupil.isValid();
        } else {
            this.LPCX = null;
            this.LPCY = null;
            this.LPD = null;
            this.LPS = null;
            this.LPV = null;
        }
    }

    /**
     * Sets the RightEyePupil attribute and its relative GazePoint data metrics [e.g. RPCX, RPCY, RPV, RPD]
     * @param rightEyePupil the actual param to set the RecXmlObject.rightEyePupil to.
     */
    public void setRightEyePupil(RightEyePupil rightEyePupil) {
        this.rightEyePupil = rightEyePupil;
        if (rightEyePupil != null) {
            this.RPCX = rightEyePupil.getX();
            this.RPCY = rightEyePupil.getY();
            this.RPV = rightEyePupil.isValid();
            this.RPD = rightEyePupil.getDiameter();
            this.RPS = rightEyePupil.getScale();
        } else {
            this.RPCX = null;
            this.RPCY = null;
            this.RPV = null;
            this.RPD = null;
            this.RPS = null;
        }
    }

    /**
     * Sets the PupilDiameter attribute of this class and its relative GazePoint data metrics [e.g. RPMM, RPMMV, LPMM, LPMMV]
     * @param pupilDiameter The actual param to set the RecXmlObject.pupilDiameter to.
     */
    public void setPupilDiameter(PupilDiameter pupilDiameter) {
        this.pupilDiameter = pupilDiameter;
        if (pupilDiameter != null) {
            this.RPMM = pupilDiameter.getDiameterOfRightEyeInMM();
            this.RPMMV = pupilDiameter.isRightEyeValid();
            this.LPMM = pupilDiameter.getDiameterOfLeftEyeInMM();
            this.LPMMV = pupilDiameter.isLeftEyeValid();
        } else {
            this.RPMM = null;
            this.RPMMV = null;
            this.LPMM = null;
            this.LPMMV = null;
        }
    }

    public void setTime(Double time) {
        this.time = time;
    }
	
    public RecXml[] interpolate(RecXml first, RecXml last, List<RecXml> unmodified, int steps){
        RecXml[] interpolationObjs = new RecXml[steps];
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
            RecXml interpolatedRec = unmodified.get(i);

            if ((unmodified.get(i).getFixation() == null || !unmodified.get(i).getFixation().isValid())) {
                if (fixations != null)
                    interpolatedRec.setFixation(fixations[i]);
                else
                    interpolatedRec.setFixation(null);
            }

            if ((unmodified.get(i).getBestPointOfGaze() == null || !unmodified.get(i).getBestPointOfGaze().isValid())) {
                if (bestPointOfGazes != null)
                    interpolatedRec.setBestPointOfGaze(bestPointOfGazes[i]);
                else
                    interpolatedRec.setBestPointOfGaze(null);
            }

            if ((unmodified.get(i).getLeftEyePupil() == null || !unmodified.get(i).getLeftEyePupil().isValid())) {
                if (leftEyePupils != null)
                    interpolatedRec.setLeftEyePupil(leftEyePupils[i]);
                else
                    interpolatedRec.setLeftEyePupil(null);
            }

            if ((unmodified.get(i).getRightEyePupil() == null || !unmodified.get(i).getRightEyePupil().isValid())) {
                if (rightEyePupils != null)
                    interpolatedRec.setRightEyePupil(rightEyePupils[i]);
                else
                    interpolatedRec.setRightEyePupil(null);
            }

            if ((unmodified.get(i).getLeftEyePointOfGaze() == null || !unmodified.get(i).getLeftEyePointOfGaze().isValid())) {
                if (leftEyePointOfGazes != null)
                    interpolatedRec.setLeftEyePointOfGaze(leftEyePointOfGazes[i]);
                else
                    interpolatedRec.setLeftEyePupil(null);
            }

            if ((unmodified.get(i).getRightEyePointOfGaze() == null || !unmodified.get(i).getRightEyePointOfGaze().isValid())) {
                if (rightEyePointOfGazes != null)
                    interpolatedRec.setRightEyePointOfGaze(rightEyePointOfGazes[i]);
                else
                    interpolatedRec.setRightEyePointOfGaze(null);
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
        
        return false;
    }

}
