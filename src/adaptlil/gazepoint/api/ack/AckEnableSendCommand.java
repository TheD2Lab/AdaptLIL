package adaptlil.gazepoint.api.ack;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import adaptlil.serialization_helpers.IntToBooleanDeserializer;

@JacksonXmlRootElement(localName = "ACK")
/**
 * Serialization class to convert Gazepoint <ACK></ACK> to plain old java objects.
 */
public class AckEnableSendCommand  extends AckXml {

    @JsonDeserialize(using = IntToBooleanDeserializer.class)
    @JacksonXmlProperty(isAttribute = true, localName ="STATE")
    public Boolean state;

    public AckEnableSendCommand(String id, boolean state) {
        super(id);
        this.state = state;
    }

    public Boolean getState() {
        return state;
    }
}
