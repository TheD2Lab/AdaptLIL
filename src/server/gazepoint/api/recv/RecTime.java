package server.gazepoint.api.recv;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "REC")
public class RecTime extends RecXmlObject {

    @JacksonXmlProperty(isAttribute = true, localName = "TIME")
    public Float time;
}
