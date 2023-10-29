package data_classes;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import interpolation.Interpolation;

public class PupilDiameter {

    public PupilDiameter() {} //Empty constructor for Jackson

    public PupilDiameter(double diameterOfLeftEyeInMM, boolean isLeftEyeValid, double diameterOfRightEyeInMM, boolean isRightEyeValid) {
        this.diameterOfLeftEyeInMM = diameterOfLeftEyeInMM;
        this.isLeftEyeValid = isLeftEyeValid;
        this.diameterOfRightEyeInMM = diameterOfRightEyeInMM;
        this.isRightEyeValid = isRightEyeValid;

    }

    @JacksonXmlProperty(isAttribute = true, localName = "LPMM")
    private double diameterOfLeftEyeInMM;
    @JacksonXmlProperty(isAttribute = true, localName = "LPMMV")
    private boolean isLeftEyeValid;
    @JacksonXmlProperty(isAttribute = true, localName = "RPMMV")
    private boolean isRightEyeValid;
    @JacksonXmlProperty(isAttribute = true, localName = "RPMM")
    private double diameterOfRightEyeInMM;

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

    /**
     *
     * @param a
     * @param b
     * @param steps
     * @return
     */
    public PupilDiameter[] interpolate(PupilDiameter a, PupilDiameter b, int steps) {
        PupilDiameter[] pupilDiamsIterpols = new PupilDiameter[steps];
        Interpolation interpolation = new Interpolation();
        double[] leftEyeDiams = interpolation.interpolate(a.getDiameterOfLeftEyeInMM(), b.getDiameterOfLeftEyeInMM(), steps);
        double[] rightEyeDiams = interpolation.interpolate(a.getDiameterOfRightEyeInMM(), b.getDiameterOfRightEyeInMM(), steps);
        for (int i = 0; i < steps; ++i) {
            PupilDiameter c = new PupilDiameter();
            c.setDiameterOfLeftEyeInMM(leftEyeDiams[i]);
            c.setDiameterOfRightEyeInMM(rightEyeDiams[i]);
            c.setLeftEyeValid(true);
            c.setRightEyeValid(true);
            pupilDiamsIterpols[i] = c;
        }

        return pupilDiamsIterpols;
    }

}
