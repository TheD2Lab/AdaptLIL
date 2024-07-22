package data_classes;

import adaptlil.annotations.IgnoreWekaAttribute;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import interpolation.Interpolation;

@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.ANY)
public class BestPointOfGaze {

    //Default constructor for Jackson
    public BestPointOfGaze() {}

    public BestPointOfGaze(double x, double y, boolean isValid) {
        this.x = x;
        this.y = y;
        this.isValid = isValid;
    }

    @JacksonXmlProperty(isAttribute = true, localName = "BPOGX")
    private double x;
    @JacksonXmlProperty(isAttribute = true, localName = "BPOGY")
    private double y;

    @IgnoreWekaAttribute
    @JacksonXmlProperty(isAttribute = true, localName = "BPOGV")
    private boolean isValid;

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
