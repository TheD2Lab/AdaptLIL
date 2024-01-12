package server.websocket.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RequestModelWs {
    @JsonProperty("type")
    public String type;

    public RequestModelWs(String type) {
        this.type = type;
    }
}
