package adaptlil.data_classes;

import adaptlil.annotations.IgnoreWekaAttribute;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import adaptlil.interpolation.Interpolation;

public class PupilDiameter {

    @JacksonXmlProperty(isAttribute = true, localName = "LPMM")
    private double diameterOfLeftEyeInMM;

    @IgnoreWekaAttribute
    @JacksonXmlProperty(isAttribute = true, localName = "LPMMV")
    private boolean isLeftEyeValid;

    @IgnoreWekaAttribute
    @JacksonXmlProperty(isAttribute = true, localName = "RPMMV")
    private boolean isRightEyeValid;

    @JacksonXmlProperty(isAttribute = true, localName = "RPMM")
    private double diameterOfRightEyeInMM;


    /**
     * Empty constructor for Jackson Serialization
     */
    public PupilDiameter() {}


    /**
     *
     * @param diameterOfLeftEyeInMM
     * @param isLeftEyeValid
     * @param diameterOfRightEyeInMM
     * @param isRightEyeValid
     */
    public PupilDiameter(double diameterOfLeftEyeInMM, boolean isLeftEyeValid, double diameterOfRightEyeInMM, boolean isRightEyeValid) {
        this.diameterOfLeftEyeInMM = diameterOfLeftEyeInMM;
        this.isLeftEyeValid = isLeftEyeValid;
        this.diameterOfRightEyeInMM = diameterOfRightEyeInMM;
        this.isRightEyeValid = isRightEyeValid;

    }


    /**
     *
     * @param firstPupilDiameter
     * @param nextPupilDiameter
     * @param nSteps
     * @return
     */
    public PupilDiameter[] interpolate(PupilDiameter firstPupilDiameter, PupilDiameter nextPupilDiameter, int nSteps) {
        PupilDiameter[] pupilDiamsIterpols = new PupilDiameter[nSteps];
        Interpolation interpolation = new Interpolation();
        double[] leftEyeDiams = interpolation.interpolate(firstPupilDiameter.getDiameterOfLeftEyeInMM(), nextPupilDiameter.getDiameterOfLeftEyeInMM(), nSteps);
        double[] rightEyeDiams = interpolation.interpolate(firstPupilDiameter.getDiameterOfRightEyeInMM(), nextPupilDiameter.getDiameterOfRightEyeInMM(), nSteps);
        for (int i = 0; i < nSteps; ++i) {
            PupilDiameter c = new PupilDiameter();
            c.setDiameterOfLeftEyeInMM(leftEyeDiams[i]);
            c.setDiameterOfRightEyeInMM(rightEyeDiams[i]);
            c.setLeftEyeValid(true);
            c.setRightEyeValid(true);
            pupilDiamsIterpols[i] = c;
        }

        return pupilDiamsIterpols;
    }


    /**
     * The diameter of the left eye pupil in millimeters.
     * @param diameterOfLeftEyeInMM double
     */
    public void setDiameterOfLeftEyeInMM(double diameterOfLeftEyeInMM) {
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
    public void setDiameterOfRightEyeInMM(double diameterOfRightEyeInMM) {
        this.diameterOfRightEyeInMM = diameterOfRightEyeInMM;
    }


    /**
     * The diameter of the left eye pupil in millimeters.
     * @return double in mm
     */
    public double getDiameterOfLeftEyeInMM() {
        return diameterOfLeftEyeInMM;
    }


    /**
     * The diameter of the right eye pupil in millimeters.
     * @return double in mm
     */
    public double getDiameterOfRightEyeInMM() {
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
