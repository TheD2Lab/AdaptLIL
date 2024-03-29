package adaptovis.gazepoint.api.set;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import adaptovis.gazepoint.api.XmlObject;

@JacksonXmlRootElement(localName = "SET")
public class SetCommand extends XmlObject {

    @JacksonXmlProperty(isAttribute=true, localName = "ID")
    public String id;

    public SetCommand() {}

    public SetCommand(String id) {
        this.id = id;
    }
}
