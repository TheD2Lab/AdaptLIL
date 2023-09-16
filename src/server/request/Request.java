package server.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Request {
    @JsonProperty("type")
    public String type;

    public Request(String type) {
        this.type = type;
    }
}
