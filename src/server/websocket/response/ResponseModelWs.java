package server.websocket.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY, property = "type") @JsonSubTypes({
        @JsonSubTypes.Type(value = CommandResponseModelWs.class, name = "commandResponse"),
        @JsonSubTypes.Type(value = DataResponseModelWs.class, name = "dataResponse"),
})
public class ResponseModelWs {

    @JsonProperty("type")
    public String type;

    public ResponseModelWs(String type) {
        this.type = type;
    }
    public ResponseModelWs() {

    }
}
