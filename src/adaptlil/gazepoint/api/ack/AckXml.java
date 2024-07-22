package adaptlil.gazepoint.api.ack;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import adaptlil.gazepoint.api.XmlObject;

@JacksonXmlRootElement(localName = "ACK")
public class AckXml extends XmlObject {


    @JacksonXmlProperty(isAttribute = true, localName = "ID")
    public String id;
    public AckXml() {}
    public AckXml(String id) {
        this.id = id;
    }
}
