package data_classes;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class RightEyePupil {

    @JacksonXmlProperty(isAttribute = true, localName = "RPCX")
    private double x;
    @JacksonXmlProperty(isAttribute = true, localName = "RPCY")
    private double y;
    @JacksonXmlProperty(isAttribute = true, localName = "RPD")
    private double diameter;
    @JacksonXmlProperty(isAttribute = true, localName = "RPS")
    private double scale;
    @JacksonXmlProperty(isAttribute = true, localName = "RPV")
    private boolean isValid;

    public RightEyePupil() {} //Default constructor for Jackson
    /**
     *
     * @param x fraction of camera image size
     * @param y fraction of camera image size
     * @param diameter in pixels
     * @param scale depth of how far a user's eye is from the tracker
     * @param isValid flag to determine if this object data is valid (determiend by tracker)
     */
    public RightEyePupil(double x, double y, double diameter, double scale, boolean isValid) {
        this.x = x;
        this.y = y;
        this.diameter = diameter;
        this.scale = scale;
        this.isValid = isValid;
    }

    /**
     * X-Coordinate of right eye
     * @param x fraction of camera image size
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Y-Coordinate of right eye
     * @param y fraction of camera image size
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * The diameter of the right eye pupil in pixels.
     * @param diameter in pixels
     */
    public void setDiameter(double diameter) {
        this.diameter = diameter;
    }

    /**
     * The scale factor of the right eye pupil (unitless). Value equals 1 at calibration
     * depth, is less than 1 when user is closer to the eye tracker and greater than 1 when user is further away.
     * @param scale [1,inf]
     */
    public void setScale(double scale) {
        this.scale = scale;
    }

    public void setIsValid(boolean isValid) {
        this.isValid = isValid;
    }

    /**
     * The X-coordinate of the right eye pupil in the camera image, as a fraction
     * of the camera image size
     * @return float
     */
    public double getX() {
        return x;
    }

    /**
     * The Y-coordinate of the right eye pupil in the camera image, as a fraction
     * of the camera image size
     * @return float
     */
    public double getY() {
        return y;
    }

    /**
     * The diameter of the right eye pupil in pixels.
     * @return float
     */
    public double getDiameter() {
        return diameter;
    }

    /**
     * The scale factor of the right eye pupil (unitless). Value equals 1 at calibration
     * depth, is less than 1 when user is closer to the eye tracker and greater than 1 when user is further away
     * @return float
     */
    public double getScale() {
        return scale;
    }

    /**
     * The valid flag with value of 1 if the data is valid, and 0 if it is not.
     * @return
     */
    public boolean isValid() {
        return isValid;
    }

}