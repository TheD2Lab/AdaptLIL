package adaptlil.gazepoint.api.get;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import adaptlil.gazepoint.api.XmlObject;

@JacksonXmlRootElement(localName = "GET")
public class GetCommand extends XmlObject{

    @JacksonXmlProperty(isAttribute = true, localName = "ID")
    public String id;

    public GetCommand() {}
    public GetCommand(String id) {
        this.id = id;
    }

}
