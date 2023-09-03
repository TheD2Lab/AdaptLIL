package server.recvcommands;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "REC")
public class RecFixationPOG extends RecXmlObject {

    @JacksonXmlProperty(isAttribute = true, localName = "FPOGX")
    public Float x;
    @JacksonXmlProperty(isAttribute = true, localName = "FPOGY")
    public Float y;
    @JacksonXmlProperty(isAttribute = true, localName = "FPOGD")
    public Float duration;
    @JacksonXmlProperty(isAttribute = true, localName = "FPOGV")
    public Integer id;

    public RecFixationPOG(Float x, Float y, Float duration, Integer id) {
        this.x = x;
        this.y = y;
        this.duration = duration;
        this.id = id;
    }

    public RecFixationPOG(Float x, Float y, Float duration) {
        this.x = x;
        this.y = y;
        this.duration = duration;
        this.id = -1;
    }

    public String name() {
        return "RecFIXATIONPOG";
    }
}
