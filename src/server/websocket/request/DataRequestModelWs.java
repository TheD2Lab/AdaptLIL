package server.websocket.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DataRequestModelWs extends RequestModelWs {
    @JsonProperty("name")
    public String name;
    public DataRequestModelWs(String name) {
        super("data");
        this.name = name;
    }


}
