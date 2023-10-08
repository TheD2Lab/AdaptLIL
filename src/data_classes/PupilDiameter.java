package data_classes;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class PupilDiameter {
    @JacksonXmlProperty(isAttribute = true, localName = "LPMM")
    private float diameterOfLeftEyeInMM;
    @JacksonXmlProperty(isAttribute = true, localName = "LPMMV")
    private boolean isLeftEyeValid;
    @JacksonXmlProperty(isAttribute = true, localName = "RPMMV")
    private boolean isRightEyeValid;
    @JacksonXmlProperty(isAttribute = true, localName = "RPMM")
    private float diameterOfRightEyeInMM;

    /**
     * The diameter of the left eye pupil in millimeters.
     * @param diameterOfLeftEyeInMM float
     */
    public void setDiameterOfLeftEyeInMM(float diameterOfLeftEyeInMM) {
        this.diameterOfLeftEyeInMM = diameterOfLeftEyeInMM;
    }

    /**
     * The valid flag with value of true if the data is valid, and false if it is not.
     * @param leftEyeValid The valid flag with value of true if the data is valid, and false if it is not.
     */
    public void setLeftEyeValid(boolean leftEyeValid) {
        isLeftEyeValid = leftEyeValid;
    }

    /**
     * The valid flag with value of true if the data is valid, and false if it is not.
     * @param rightEyeValid The valid flag with value of true if the data is valid, and false if it is not.
     */
    public void setRightEyeValid(boolean rightEyeValid) {
        isRightEyeValid = rightEyeValid;
    }

    /**
     * The diameter of the right eye pupil in millimeters.
     * @param diameterOfRightEyeInMM The diameter of the right eye pupil in millimeters.
     */
    public void setDiameterOfRightEyeInMM(float diameterOfRightEyeInMM) {
        this.diameterOfRightEyeInMM = diameterOfRightEyeInMM;
    }

    /**
     * The diameter of the left eye pupil in millimeters.
     * @return float in mm
     */
    public float getDiameterOfLeftEyeInMM() {
        return diameterOfLeftEyeInMM;
    }

    /**
     * The diameter of the right eye pupil in millimeters.
     * @return float in mm
     */
    public float getDiameterOfRightEyeInMM() {
        return diameterOfRightEyeInMM;
    }

    /**
     * Determines validity of left eye pupil diameter data
     * @return true, false
     */
    public boolean isLeftEyeValid() {
        return isLeftEyeValid;
    }

    /**
     * Determines validity of right eye pupil diameter data
     * @return true, false
     */
    public boolean isRightEyeValid() {
        return isRightEyeValid;
    }



}
