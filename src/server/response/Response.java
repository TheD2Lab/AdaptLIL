package server.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY, property = "type") @JsonSubTypes({

        @JsonSubTypes.Type(value = DataResponse.class, name = "dataResponse"),
})
public class Response {

    @JsonProperty("type")
    public String type;

    public Response(String type) {
        this.type = type;
    }
    public Response() {

    }
}
