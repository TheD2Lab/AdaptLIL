package server.gazepoint.api.ack;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "ACK")
public class AckXmlObject {


    @JacksonXmlProperty(isAttribute = true, localName = "ID")
    public String id;
    public AckXmlObject() {}
    public AckXmlObject(String id) {
        this.id = id;
    }
}
