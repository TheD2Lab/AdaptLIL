package adaptlil.gazepoint.api.ack;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import adaptlil.gazepoint.api.XmlObject;

/**
 * Serialization class to convert Gazepoint specified XML <ACK></ACK> to plain old java objects.
 */
@JacksonXmlRootElement(localName = "ACK")
public class AckXml extends XmlObject {

    @JacksonXmlProperty(isAttribute = true, localName = "ID")
    public String id;


    /**
     * Default constructor for Jackson Serialization
     */
    public AckXml() {}

    /**
     * ID as specified by gazepoint documentaiton
     * @param id
     */
    public AckXml(String id) {
        this.id = id;
    }
}
