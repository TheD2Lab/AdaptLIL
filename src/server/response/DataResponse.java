package server.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import geometry.Shape;

public class DataResponse extends Response {
    @JsonProperty("elementType")
    public String elementType;

    @JsonProperty("coordsAndDimens")
    public Shape[] coordsAndDimen;
}
