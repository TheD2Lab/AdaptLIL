package adaptovis.websocket.response;

import com.fasterxml.jackson.annotation.JsonProperty;


public class ResponseModelWs {

    @JsonProperty("type")
    public String type;

    public ResponseModelWs(String type) {
        this.type = type;
    }
    public ResponseModelWs() {

    }
}
