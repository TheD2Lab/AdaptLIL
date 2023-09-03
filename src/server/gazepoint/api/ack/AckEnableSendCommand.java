package server.gazepoint.api.ack;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import server.serialization_helpers.IntToBooleanDeserializer;

@JacksonXmlRootElement(localName = "ACK")
public class AckEnableSendCommand  extends AckXmlObject {

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
