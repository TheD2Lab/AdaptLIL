package data_classes;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

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
}
