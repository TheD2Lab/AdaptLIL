package server.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DataRequest extends Request {
    @JsonProperty("name")
    public String name;
    public DataRequest(String name) {
        super("data");
        this.name = name;
    }


}
