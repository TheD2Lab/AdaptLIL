package adaptlil.data_classes;

import adaptlil.annotations.IgnoreWekaAttribute;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import adaptlil.interpolation.Interpolation;
import adaptlil.serialization_helpers.IntToBooleanDeserializer;

public class LeftEyePointOfGaze {

    @JacksonXmlProperty(isAttribute = true, localName = "LPOGX")
    private double x;
    @JacksonXmlProperty(isAttribute = true, localName = "LPOGY")
    private double y;
    @IgnoreWekaAttribute
    @JsonDeserialize(using = IntToBooleanDeserializer.class)
    @JacksonXmlProperty(isAttribute = true, localName = "LPOGV")
    private boolean isValid;

    /**
     *
     */
    public LeftEyePointOfGaze() {}  //Default constructor for jackson

    /**
     * LeftEyePointOfGaze is where the left eye is currently looking on the screen.
     * @param x The X-coordinate of the left eye POG, as a fraction of the screen size.
     * @param y The Y-coordinate of the left eye POG, as a fraction of the screen size.
     * @param isValid Flag that details if the tracker data is valid or not
     */
    public LeftEyePointOfGaze(double x, double y, boolean isValid) {
        this.x = x;
        this.y = y;
        this.isValid = isValid;
    }

    /**
     * The X-coordinate of the left eye POG, as a fraction of the screen size.
     * @param x X-coordinate of the left eye POG, as a fraction of the screen size.
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * The Y-coordinate of the left eye POG, as a fraction of the screen size.
     * @param y Y-coordinate of the left eye POG, as a fraction of the screen size.
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
     * The X-coordinate of the left eye POG, as a fraction of the screen size.
     * @return double
     */
    public double getX() {
        return x;
    }

    /**
     * The Y-coordinate of the left eye POG, as a fraction of the screen size.
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
     * @param firstBestPointOfGaze
     * @param nextBestPointOfGaze
     * @param nSteps Number of BestPointofGaze packets to interpolate between.
     * @return
     */
    /**
     * Performs Linear interpolation nSteps LeftEyePointOfGaze elements between the first LeftEyePointOfGaze and the next LeftEyePointOfGaze
     * @param firstLeftEyePointOfGaze
     * @param nextLeftEyePointOfGaze
     * @param nSteps
     * @return
     */
    public LeftEyePointOfGaze[] interpolate(LeftEyePointOfGaze firstLeftEyePointOfGaze, LeftEyePointOfGaze nextLeftEyePointOfGaze, int nSteps) {
        Interpolation interpolation = new Interpolation();
        LeftEyePointOfGaze[] leftEyeInterpols = new LeftEyePointOfGaze[nSteps];
        double[] aCoords = new double[]{firstLeftEyePointOfGaze.getX(), firstLeftEyePointOfGaze.getY()};
        double[] bCoords = new double[]{nextLeftEyePointOfGaze.getX(), nextLeftEyePointOfGaze.getY()};
        double[][] interpolCoords = interpolation.interpolate(aCoords, bCoords, nSteps);

        for (int i = 0; i < nSteps; ++i) {
            LeftEyePointOfGaze c = new LeftEyePointOfGaze();
            c.setX(interpolCoords[i][0]);
            c.setY(interpolCoords[i][1]);
            c.setValid(true);
            leftEyeInterpols[i] = c;
        }
        return leftEyeInterpols;
    }
}
