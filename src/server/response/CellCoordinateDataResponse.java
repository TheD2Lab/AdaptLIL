package server.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import geometry.Shape;

import java.util.Map;

public class CellCoordinateDataResponse extends DataResponse {
    @JsonProperty("elementType")
    public String elementType;

    @JsonProperty("shapes")
    public Map<String, Shape> shapes;

    public CellCoordinateDataResponse(String elementType, Map<String, Shape> shapes) {
        super("cellCoordinates");
        this.elementType = elementType;
        this.shapes = shapes;
    }
    public CellCoordinateDataResponse() {

    }

    public String getElementType() {
        return elementType;
    }

    public Map<String, Shape> getShapesById() {
        return shapes;
    }

    public void setElementType(String elementType) {
        this.elementType = elementType;
    }

    public void setShapes(Map<String, Shape> shapes) {
        this.shapes = shapes;
    }
}
