package data_classes;

import adaptlil.annotations.IgnoreWekaAttribute;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import interpolation.Interpolation;

@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.ANY)
public class BestPointOfGaze {

    @JacksonXmlProperty(isAttribute = true, localName = "BPOGX")
    private double x;
    @JacksonXmlProperty(isAttribute = true, localName = "BPOGY")
    private double y;

    @IgnoreWekaAttribute
    @JacksonXmlProperty(isAttribute = true, localName = "BPOGV")
    private boolean isValid;


    //Default constructor for Jackson
    public BestPointOfGaze() {}

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


    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }


    public BestPointOfGaze[] interpolate(BestPointOfGaze a, BestPointOfGaze b, int steps) {
        Interpolation interpolation = new Interpolation();
        BestPointOfGaze[] bPogsInterpols = new BestPointOfGaze[steps];
        double[] aCoords = new double[]{a.getX(), a.getY()};
        double[] bCoords = new double[]{b.getX(), b.getY()};
        double[][] interpolCoords = interpolation.interpolate(aCoords, bCoords, steps);
        for (int i = 0; i < steps; ++i) {
            BestPointOfGaze c = new BestPointOfGaze();
            c.setX(interpolCoords[i][0]);
            c.setY(interpolCoords[i][1]);
            c.setValid(true);
            bPogsInterpols[i] = c;
        }

        return bPogsInterpols;
    }
}
