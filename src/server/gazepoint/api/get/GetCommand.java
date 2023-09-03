package server.gazepoint.api.get;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "GET")
public class GetCommand {

    @JacksonXmlProperty(isAttribute = true, localName = "ID")
    public String id;

    public GetCommand() {}
    public GetCommand(String id) {
        this.id = id;
    }

}
