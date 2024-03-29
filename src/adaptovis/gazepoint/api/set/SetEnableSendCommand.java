package adaptovis.gazepoint.api.set;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import adaptovis.serialization_helpers.BooleanToIntSerializer;

@JacksonXmlRootElement(localName = "SET")
/**
 * Used to tell gazepoint device to send specified data.
 */
public class SetEnableSendCommand extends SetCommand {

    @JsonSerialize(using = BooleanToIntSerializer.class)
    @JacksonXmlProperty(isAttribute = true, localName = "STATE")
    public Boolean state;

    public SetEnableSendCommand() {}

    public SetEnableSendCommand(String id, Boolean state) {
        super(id);
        this.state = state;
    }
}
