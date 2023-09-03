package server.recvcommands;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import server.deserializers.IntToBooleanDeserializer;

import javax.xml.bind.annotation.XmlElement;

@JacksonXmlRootElement(localName = "REC")
public class RecFixationPOG extends RecXmlObject {

    /**
     * Description: The Fixation POG data provides the user’s point-of-gaze as determined by the
     * internal fixation filter.
     * Parameter ID: FPOGX, FPOGY
     * Parameter type: float
     * Parameter description: The X- and Y-coordinates of the fixation POG, as a fraction of the screen size.
     * (0,0) is top left, (0.5,0.5) is the screen center, and (1.0,1.0) is bottom right.
     * Parameter ID: FPOGS
     * Parameter type: float
     * Parameter description: The starting time of the fixation POG in seconds since the system initialization or
     * calibration.
     * Parameter ID: FPOGD
     * Parameter type: float
     * Parameter description: The duration of the fixation POG in seconds.
     * Parameter ID: FPOGID
     * Parameter type: integer
     * Parameter description: The fixation POG ID number
     * Parameter ID: FPOGV
     * Parameter type: boolean
     * Parameter description: The valid flag with value of 1 (TRUE) if the fixation POG data is valid, and 0
     * (FALSE) if it is not. FPOGV valid is TRUE ONLY when either one, or both, of the eyes are detected AND a
     * fixation is detected. FPOGV is FALSE all other times, for example when the subject blinks, when there is
     * no face in the field of view, when the eyes move to the next fixation (i.e. a saccade).
     * Enable: ENABLE_SEND_POG_FIX
     */
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


    public RecFixationPOG() {
    }

    public RecFixationPOG(Float x, Float y, Float startTime, Float duration, Boolean isValid, Integer id) {
        this.x = x;
        this.y = y;
        this.startTime = startTime;
        this.duration = duration;
        this.isValid = isValid;
        this.id = id;
    }

    public RecFixationPOG(Float x, Float y, Float startTime, Float duration, Boolean isValid) {
        this.x = x;
        this.y = y;
        this.startTime = startTime;
        this.duration = duration;
        this.isValid = isValid;
        this.id = -1;
    }

    public String name() {
        return "RecFIXATIONPOG";
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
