package server.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Response {

    @JsonProperty("type")
    public String type;

    public Response(String type) {
        this.type = type;
    }
}
