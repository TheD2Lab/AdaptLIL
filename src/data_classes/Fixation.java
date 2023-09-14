package data_classes;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import server.serialization_helpers.IntToBooleanDeserializer;

public class Fixation {
    @JacksonXmlProperty(isAttribute = true, localName = "FPOGX")
    public Float x;
    @JacksonXmlProperty(isAttribute = true, localName = "FPOGY")
    public Float y;
    @JacksonXmlProperty(isAttribute = true, localName = "FPOGS")
    public Float startTime;
    @JacksonXmlProperty(isAttribute = true, localName = "FPOGD")
    public Float duration;
    @JacksonXmlProperty(isAttribute = true, localName = "FPOGID")
    public Integer id;

    @JsonDeserialize(using = IntToBooleanDeserializer.class)
    @JacksonXmlProperty(isAttribute = true, localName = "FPOGV")

    public Boolean isValid;


    public Fixation() {
    }

    public Fixation(Float x, Float y, Float startTime, Float duration, Boolean isValid, Integer id) {
        this.x = x;
        this.y = y;
        this.startTime = startTime;
        this.duration = duration;
        this.isValid = isValid;
        this.id = id;
    }

    public Fixation(Float x, Float y, Float startTime, Float duration, Boolean isValid) {
        this.x = x;
        this.y = y;
        this.startTime = startTime;
        this.duration = duration;
        this.isValid = isValid;
        this.id = -1;
    }

    public Float getX() {
        return x;
    }

    public Float getY() {
        return y;
    }

    public Float getStartTime() {
        return startTime;
    }

    public Float getDuration() {
        return duration;
    }

    public Integer getId() {
        return id;
    }

    public Boolean getValid() {
        return isValid;
    }
}
