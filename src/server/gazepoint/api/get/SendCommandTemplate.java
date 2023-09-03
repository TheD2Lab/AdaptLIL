package server.gazepoint.api.get;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "GET")
public class SendCommandTemplate {
    @JacksonXmlProperty(isAttribute = true, localName = "ID")
    public String id;
}
