package adaptlil.data_classes;

import adaptlil.annotations.IgnoreWekaAttribute;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import adaptlil.interpolation.Interpolation;

@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.ANY)
public class BestPointOfGaze {

    @JacksonXmlProperty(isAttribute = true, localName = "BPOGX")
    private double x;
    @JacksonXmlProperty(isAttribute = true, localName = "BPOGY")
    private double y;

    @IgnoreWekaAttribute
    @JacksonXmlProperty(isAttribute = true, localName = "BPOGV")
    private boolean isValid;

    public BestPointOfGaze() {} //Empty default constructor is for Jackson

    /**
     * Construct BestPointofGaze data class object.
     * Best point of gaze is the area between where the left and right eye are looking. If only the left eye is available
     * this object will represent the left eye and vice versa for the right eye.
     * @param x
     * @param y
     * @param isValid
     */
    public BestPointOfGaze(double x, double y, boolean isValid) {
        this.x = x;
        this.y = y;
        this.isValid = isValid;
    }

    /**
     * Constructs BestPointOfGaze Object through the strings of an array. In this case, an excel line.
     * @param bpogxIndex
     * @param bpogyIndex
     * @param bpogvIndex
     * @param cells
     * @return
     */
    public static BestPointOfGaze getBestPointOfGaze(int bpogxIndex, int bpogyIndex, int bpogvIndex, String[] cells) {
        return new BestPointOfGaze(Double.parseDouble(cells[bpogxIndex]), Double.parseDouble(cells[bpogyIndex]), cells[bpogvIndex].equals("1"));
    }


    /**
     * Performs Linear interpolation nSteps BestPointOfGaze elements between the first BestPointofGaze and the next BestPointofGaze
     * @param firstBestPointOfGaze
     * @param nextBestPointOfGaze
     * @param nSteps Number of BestPointofGaze packets to interpolate between.
     * @return
     */
    public BestPointOfGaze[] interpolate(BestPointOfGaze firstBestPointOfGaze, BestPointOfGaze nextBestPointOfGaze, int nSteps) {
        Interpolation interpolation = new Interpolation();
        BestPointOfGaze[] bPogsInterpols = new BestPointOfGaze[nSteps];
        double[] aCoords = new double[]{firstBestPointOfGaze.getX(), firstBestPointOfGaze.getY()};
        double[] bCoords = new double[]{nextBestPointOfGaze.getX(), nextBestPointOfGaze.getY()};
        double[][] interpolCoords = interpolation.interpolate(aCoords, bCoords, nSteps);
        for (int i = 0; i < nSteps; ++i) {
            BestPointOfGaze c = new BestPointOfGaze();
            c.setX(interpolCoords[i][0]);
            c.setY(interpolCoords[i][1]);
            c.setValid(true);
            bPogsInterpols[i] = c;
        }

        return bPogsInterpols;
    }


    /**
    * Gets the X coordinate for the best point of gaze (where the user is looking on the screen)
    * @return returns scale [0-1] where 0.5 represents the middle of the screen on the x axis
    */
    public double getX() {
        return x;
    }


    /**
     * Set the x coordinate of best point of gaze.
     * @param x Keep within range 0-1.
     */
    public void setX(double x) {
        this.x = x;
    }


    /**
     * Gets the Y coordinate for the best point of gaze (where the user is looking on the screen)
     * @return returns scale [0-1] where 0.5 represents the middle of the screen on the Y axis.
     */
    public double getY() {
        return y;
    }


    /**
     * Set the Y coordinate of best point of gaze.
     * @param y Keep within range 0-1.
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Flag set by gazepoint to signal the BestPointOfGaze packet is corrupted.
     * @return
     */
    public boolean isValid() {
        return isValid;
    }


    /**
     * Set to true to signal there is an issue with BestPointOfGaze packet.
     * @return
     */
    public void setValid(boolean valid) {
        isValid = valid;
    }

}
