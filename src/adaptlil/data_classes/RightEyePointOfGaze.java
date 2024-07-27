package adaptlil.data_classes;

import adaptlil.annotations.IgnoreWekaAttribute;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import adaptlil.interpolation.Interpolation;

public class RightEyePointOfGaze {

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

    @JacksonXmlProperty(isAttribute = true, localName = "RPOGX")
    private double x;
    @JacksonXmlProperty(isAttribute = true, localName = "RPOGY")
    private double y;

    @IgnoreWekaAttribute
    @JacksonXmlProperty(isAttribute = true, localName = "RPOGV")
    private boolean isValid;

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

    /**
     *
     * @param a
     * @param b
     * @param steps
     * @return
     */
    public RightEyePointOfGaze[] interpolate(RightEyePointOfGaze a, RightEyePointOfGaze b, int steps) {
        Interpolation interpolation = new Interpolation();
        RightEyePointOfGaze[] rightEyeInterpols = new RightEyePointOfGaze[steps];
        double[] aCoords = new double[]{a.getX(), a.getY()};
        double[] bCoords = new double[]{b.getX(), b.getY()};
        double[][] interpolCoords = interpolation.interpolate(aCoords, bCoords, steps);

        for (int i = 0; i < steps; ++i) {
            RightEyePointOfGaze c = new RightEyePointOfGaze();
            c.setX(interpolCoords[i][0]);
            c.setY(interpolCoords[i][1]);
            c.setValid(true);
            rightEyeInterpols[i] = c;
        }
        return rightEyeInterpols;
    }
}
