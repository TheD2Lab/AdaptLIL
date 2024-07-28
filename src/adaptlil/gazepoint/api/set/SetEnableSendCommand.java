package adaptlil.gazepoint.api.set;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import adaptlil.serialization_helpers.BooleanToIntSerializer;

@JacksonXmlRootElement(localName = "SET")
/**
 * Used to tell gazepoint device to send specified data in the form
 * <SET id="SetEnableSendCommand.id" state=setEnableSendCommand.state"></SET>
 */
public class SetEnableSendCommand extends SetCommand {

    @JsonSerialize(using = BooleanToIntSerializer.class)
    @JacksonXmlProperty(isAttribute = true, localName = "STATE")
    public Boolean state;

    public SetEnableSendCommand() {}


    /**
     * Used to tell gazepoint device to send specified data in the form
     * <SET id="SetEnableSendCommand.id" state=setEnableSendCommand.state"></SET>
     * Using this command will enable the remaining <REC></REC> packets to contain the specified attribute via 'id' argument.
     * @param id
     * @param state
     */
    public SetEnableSendCommand(String id, Boolean state) {
        super(id);
        this.state = state;
    }
}
