package server.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DataResponse extends Response{

    @JsonProperty("name")
    public String name;
    public DataResponse(String name) {
        super("data");
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
