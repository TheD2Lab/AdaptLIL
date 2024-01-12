package server.websocket.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION) @JsonSubTypes({
        @JsonSubTypes.Type(value = MapWorldDataResponseModelWs.class, name = "mapWorldDataResponse"),
        @JsonSubTypes.Type(value = CellCoordinateDataResponseModelWs.class, name = "cellCoordinateResponse")
})
public class DataResponseModelWs extends ResponseModelWs {

    @JsonProperty("name")
    public String name;
    public DataResponseModelWs(String name) {
        super("data");
        this.name = name;
    }

    public DataResponseModelWs() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
