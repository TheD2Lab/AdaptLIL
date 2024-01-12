package server.websocket.request;


import com.fasterxml.jackson.annotation.JsonProperty;

public class InvokeRequestModelWs extends RequestModelWs {
    @JsonProperty("name")
    public String name;

    public InvokeRequestModelWs(String name) {
        super("invoke");
        this.name = name;
    }
}
