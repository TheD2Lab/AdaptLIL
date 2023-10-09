package data_classes;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import server.serialization_helpers.IntToBooleanDeserializer;

public class Fixation {
    @JacksonXmlProperty(isAttribute = true, localName = "FPOGX")
    private double x;
    @JacksonXmlProperty(isAttribute = true, localName = "FPOGY")
    private double y;
    @JacksonXmlProperty(isAttribute = true, localName = "FPOGS")
    private double startTime;
    @JacksonXmlProperty(isAttribute = true, localName = "FPOGD")
    private double duration;
    @JacksonXmlProperty(isAttribute = true, localName = "FPOGID")
    private int id;

    @JsonDeserialize(using = IntToBooleanDeserializer.class)
    @JacksonXmlProperty(isAttribute = true, localName = "FPOGV")

    private boolean isValid;


    public Fixation() {} //Default constructor for Jackson

    public Fixation(double x, double y, double startTime, double duration, boolean isValid, int id) {
        this.x = x;
        this.y = y;
        this.startTime = startTime;
        this.duration = duration;
        this.isValid = isValid;
        this.id = id;
    }

    public Fixation(double x, double y, double startTime, double duration, boolean isValid) {
        this.x = x;
        this.y = y;
        this.startTime = startTime;
        this.duration = duration;
        this.isValid = isValid;
        this.id = -1;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
    public double getStartTime() {
        return startTime;
    }

    public double getDuration() {
        return duration;
    }

    public int getId() {
        return id;
    }

    public Boolean getValid() {
        return isValid;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setStartTime(double startTime) {
        this.startTime = startTime;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setValid(Boolean valid) {
        isValid = valid;
    }
}
