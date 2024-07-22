package adaptlil.gazepoint.api.get;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import adaptlil.serialization_helpers.BooleanToIntSerializer;

@JacksonXmlRootElement(localName = "GET")

public class GetEnableSendCommand {
    @JacksonXmlProperty(isAttribute = true, localName = "ID")
    public String id;
    @JsonSerialize(using = BooleanToIntSerializer.class)
    @JacksonXmlProperty(isAttribute = true, localName ="STATE")
    public Boolean state;


    public GetEnableSendCommand(String id, boolean state) {
        this.id = id;
        this.state = state;
    }

}
