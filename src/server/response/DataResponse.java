package server.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION) @JsonSubTypes({
        @JsonSubTypes.Type(value = MapWorldDataResponse.class, name = "mapWorldDataResponse"),
        @JsonSubTypes.Type(value = CellCoordinateDataResponse.class, name = "cellCoordinateResponse")
})
public class DataResponse extends Response{

    @JsonProperty("name")
    public String name;
    public DataResponse(String name) {
        super("data");
        this.name = name;
    }

    public DataResponse() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
