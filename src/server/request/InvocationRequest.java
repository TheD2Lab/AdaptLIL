package server.request;


import com.fasterxml.jackson.annotation.JsonProperty;

public class InvocationRequest extends Request {
    @JsonProperty("name")
    public String name;

    public InvocationRequest(String name) {
        super("invocation");
        this.name = name;
    }
}
