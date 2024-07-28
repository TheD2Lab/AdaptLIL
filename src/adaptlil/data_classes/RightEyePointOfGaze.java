package adaptlil.data_classes;

import adaptlil.annotations.IgnoreWekaAttribute;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import adaptlil.interpolation.Interpolation;

public class RightEyePointOfGaze {

    @JacksonXmlProperty(isAttribute = true, localName = "RPOGX")
    private double x;
    @JacksonXmlProperty(isAttribute = true, localName = "RPOGY")
    private double y;

    @IgnoreWekaAttribute
    @JacksonXmlProperty(isAttribute = true, localName = "RPOGV")
    private boolean isValid;

    //Default constructor for jackson
    public RightEyePointOfGaze() {}

    /**
     *
     * @param x The X-coordinate of the right eye POG, as a fraction of the screen size.
     * @param y The Y-coordinate of the right eye POG, as a fraction of the screen size.
     * @param isValid Flag that details if the tracker data is valid or not
     */
    public RightEyePointOfGaze(double x, double y, boolean isValid) {
        this.x = x;
        this.y = y;
        this.isValid = isValid;
    }

    /**
     * Performs Linear interpolation nSteps RightEyePointOfGaze elements between the first RightEyePointOfGaze and the next RightEyePointOfGaze
     * @param firstRightEyePointOfGaze
     * @param nextRightEyePointOfGaze
     * @param nSteps
     * @return
     */
    public RightEyePointOfGaze[] interpolate(RightEyePointOfGaze firstRightEyePointOfGaze, RightEyePointOfGaze nextRightEyePointOfGaze, int nSteps) {
        Interpolation interpolation = new Interpolation();
        RightEyePointOfGaze[] rightEyeInterpols = new RightEyePointOfGaze[nSteps];
        double[] aCoords = new double[]{firstRightEyePointOfGaze.getX(), firstRightEyePointOfGaze.getY()};
        double[] bCoords = new double[]{nextRightEyePointOfGaze.getX(), nextRightEyePointOfGaze.getY()};
        double[][] interpolCoords = interpolation.interpolate(aCoords, bCoords, nSteps);

        for (int i = 0; i < nSteps; ++i) {
            RightEyePointOfGaze c = new RightEyePointOfGaze();
            c.setX(interpolCoords[i][0]);
            c.setY(interpolCoords[i][1]);
            c.setValid(true);
            rightEyeInterpols[i] = c;
        }
        return rightEyeInterpols;
    }

    /**
     * The X-coordinate of the right eye POG, as a fraction of the screen size.
     * @param x X-coordinate of the right eye POG, as a fraction of the screen size.
     */
    public void setX(double x) {
        this.x = x;
    }


    /**
     * The Y-coordinate of the right eye POG, as a fraction of the screen size.
     * @param y Y-coordinate of the right eye POG, as a fraction of the screen size.
     */
    public void setY(double y) {
        this.y = y;
    }


    /**
     * The valid flag if the data is valid
     * @param valid
     */
    public void setValid(boolean valid) {
        isValid = valid;
    }


    /**
     * The X-coordinate of the right eye POG, as a fraction of the screen size.
     * @return double
     */
    public double getX() {
        return x;
    }


    /**
     * The Y-coordinate of the right eye POG, as a fraction of the screen size.
     * @return double
     */
    public double getY() {
        return y;
    }


    /**
     * Valid flag
     * @return true if data is valid, false if not.
     */
    public boolean isValid() {
        return isValid;
    }

}
