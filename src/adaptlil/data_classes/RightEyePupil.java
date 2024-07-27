package adaptlil.data_classes;

import adaptlil.annotations.IgnoreWekaAttribute;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import adaptlil.interpolation.Interpolation;

public class RightEyePupil {

    @JacksonXmlProperty(isAttribute = true, localName = "RPCX")
    private double x;
    @JacksonXmlProperty(isAttribute = true, localName = "RPCY")
    private double y;
    @JacksonXmlProperty(isAttribute = true, localName = "RPD")
    private double diameter;
    @JacksonXmlProperty(isAttribute = true, localName = "RPS")
    private double scale;

    @IgnoreWekaAttribute
    @JacksonXmlProperty(isAttribute = true, localName = "RPV")
    private boolean isValid;


    /**
     * Default constructor for Jackson Serialization
     */
    public RightEyePupil() {}


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
     * Constructs a RightEyePupil Object from an excel row as specified by gazepoint api. The index arguments correspond to the
     * column rows of cells argument.
     * @param rpcxIndex
     * @param rpcyIndex
     * @param rpsIndex
     * @param rpdIndex
     * @param rpvIndex
     * @param cells
     * @return
     */
    public static RightEyePupil getRightEyePupilFromCsvLine(int rpcxIndex, int rpcyIndex, int rpsIndex, int rpdIndex, int rpvIndex, String[] cells) {
        return new RightEyePupil(Double.parseDouble(cells[rpcxIndex]),
                Double.parseDouble(cells[rpcyIndex]),
                Double.parseDouble(cells[rpdIndex]),
                Double.parseDouble(cells[rpsIndex]),
                cells[rpvIndex].equals("1"));
    }

    /**
     * Performs Linear interpolation nSteps RightEyePupil elements between the first RightEyePupil and the next RightEyePupil
     * @param firstRightEyePupil
     * @param nextRightEyePupil
     * @param nSteps
     * @return
     */
    public RightEyePupil[] interpolate(RightEyePupil firstRightEyePupil, RightEyePupil nextRightEyePupil, int nSteps) {
        RightEyePupil[] rightPupilInterpols = new RightEyePupil[nSteps];
        Interpolation interpolation = new Interpolation();
        double[] aCoords = new double[]{firstRightEyePupil.getX(), firstRightEyePupil.getY()};
        double[] bCoords = new double[]{nextRightEyePupil.getX(), nextRightEyePupil.getY()};
        double[][] interpolCoords = interpolation.interpolate(aCoords, bCoords, nSteps);
        double[] diameters = interpolation.interpolate(firstRightEyePupil.getDiameter(), nextRightEyePupil.getDiameter(), nSteps);
        double[] scales = interpolation.interpolate(firstRightEyePupil.getScale(), nextRightEyePupil.getScale(), nSteps);
        for (int i = 0; i < nSteps; ++i) {
            RightEyePupil c = new RightEyePupil();
            c.setX(interpolCoords[i][0]);
            c.setY(interpolCoords[i][1]);
            c.setDiameter(diameters[i]);
            c.setScale(scales[i]);
            c.setIsValid(true);
            rightPupilInterpols[i] = c;
        }

        return rightPupilInterpols;
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

    /**
     * Set the validation flag to signify packet corruption.
     * @param isValid
     */
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
