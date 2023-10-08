package data_classes;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.ANY)
public class BestPointOfGaze {

    //Default constructor for Jackson
    public BestPointOfGaze() {}

    public BestPointOfGaze(float x, float y, boolean isValid) {
        this.x = x;
        this.y = y;
        this.isValid = isValid;
    }

    @JacksonXmlProperty(isAttribute = true, localName = "BPOGX")
    private float x;
    @JacksonXmlProperty(isAttribute = true, localName = "BPOGY")
    private float y;
    @JacksonXmlProperty(isAttribute = true, localName = "BPOGV")
    private boolean isValid;

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }
}
