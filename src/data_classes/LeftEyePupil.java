package data_classes;

import adaptlil.annotations.IgnoreWekaAttribute;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import interpolation.Interpolation;

public class LeftEyePupil {
    @JacksonXmlProperty(isAttribute = true, localName = "LPCX")
    private double x;
    @JacksonXmlProperty(isAttribute = true, localName = "LPCY")
    private double y;
    @JacksonXmlProperty(isAttribute = true, localName = "LPD")
    private double diameter;
    @JacksonXmlProperty(isAttribute = true, localName = "LPS")
    private double scale;

    @IgnoreWekaAttribute
    @JacksonXmlProperty(isAttribute = true, localName = "LPV")
    private boolean isValid;

    public LeftEyePupil() {} //Default constructor for Jackson

    /**
     *
     * @param x fraction of camera image size
     * @param y fraction of camera image size
     * @param diameter in pixels
     * @param scale depth of how far a user's eye is from the tracker
     * @param isValid flag to determine if this object data is valid (determiend by tracker)
     */
    public LeftEyePupil(double x, double y, double diameter, double scale, boolean isValid) {
        this.x = x;
        this.y = y;
        this.diameter = diameter;
        this.scale = scale;
        this.isValid = isValid;
    }


    /**
     * Reads LeftEyePupil data from the csv generated via gazepoint.
     * @param lpcxIndex
     * @param lpcyIndex
     * @param lpsIndex
     * @param lpdIndex
     * @param lpvIndex
     * @param cells
     * @return
     */
    public static LeftEyePupil getLeftEyePupilFromCsvLine(int lpcxIndex, int lpcyIndex, int lpsIndex, int lpdIndex, int lpvIndex, String[] cells) {
        return new LeftEyePupil(Double.parseDouble(cells[lpcxIndex]), Double.parseDouble(cells[lpcyIndex]),
                Double.parseDouble(cells[lpdIndex]), Double.parseDouble(cells[lpsIndex]), cells[lpvIndex].equals("1"));
    }

    /**
     * TODO, implement more accurate interpolation, right now we will do a simple linear inteprolation n diameters
     * https://ieeexplore.ieee.org/document/9129915
     * https://www.mathworks.com/help/matlab/ref/pchip.html
     * @param a
     * @param b
     * @param steps
     * @return
     */
    public LeftEyePupil[] interpolate(LeftEyePupil a, LeftEyePupil b, int steps) {
        LeftEyePupil[] leftPupilInterpols = new LeftEyePupil[steps];
        Interpolation interpolation = new Interpolation();
        double[] aCoords = new double[]{a.getX(), a.getY()};
        double[] bCoords = new double[]{b.getX(), b.getY()};
        double[][] interpolCoords = interpolation.interpolate(aCoords, bCoords, steps);
        double[] diameters = interpolation.interpolate(a.getDiameter(), b.getDiameter(), steps);
        double[] scales = interpolation.interpolate(a.getScale(), b.getScale(), steps);
        for (int i = 0; i < steps; ++i) {
            LeftEyePupil c = new LeftEyePupil();
            c.setX(interpolCoords[i][0]);
            c.setY(interpolCoords[i][1]);
            c.setDiameter(diameters[i]);
            c.setScale(scales[i]);
            c.setIsValid(true);
            leftPupilInterpols[i] = c;
        }

        return leftPupilInterpols;
    }

    /**
     * X-Coordinate of left eye
     * @param x fraction of camera image size
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Y-Coordinate of left eye
     * @param y fraction of camera image size
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * The diameter of the left eye pupil in pixels.
     * @param diameter in pixels
     */
    public void setDiameter(double diameter) {
        this.diameter = diameter;
    }

    /**
     * The scale factor of the left eye pupil (unitless). Value equals 1 at calibration
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
     * The X-coordinate of the left eye pupil in the camera image, as a fraction
     * of the camera image size
     * @return double
     */
    public double getX() {
        return x;
    }

    /**
     * The Y-coordinate of the left eye pupil in the camera image, as a fraction
     * of the camera image size
     * @return double
     */
    public double getY() {
        return y;
    }

    /**
     * The diameter of the left eye pupil in pixels.
     * @return double
     */
    public double getDiameter() {
        return diameter;
    }

    /**
     * The scale factor of the left eye pupil (unitless). Value equals 1 at calibration
     * depth, is less than 1 when user is closer to the eye tracker and greater than 1 when user is further away
     * @return double
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
