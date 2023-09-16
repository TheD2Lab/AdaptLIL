package server.request;


import com.fasterxml.jackson.annotation.JsonProperty;

public class InvokeRequest extends Request {
    @JsonProperty("name")
    public String name;

    public InvokeRequest(String name) {
        super("invoke");
        this.name = name;
    }
}
