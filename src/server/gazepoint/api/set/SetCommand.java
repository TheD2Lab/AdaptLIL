package server.gazepoint.api.set;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "SET")
public class SetCommand {

    @JacksonXmlProperty(isAttribute=true, localName = "ID")
    public String id;

    public SetCommand() {}

    public SetCommand(String id) {
        this.id = id;
    }
}
