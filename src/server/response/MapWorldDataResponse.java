package server.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import geometry.Shape;

public class MapWorldDataResponse extends DataResponse{
      @JsonProperty("visMapShape")
      public Shape visMapShape;

      @JsonProperty("xOffset")
      public float xOffset;

      @JsonProperty("yOffset")
      public float yOffset;

      @JsonProperty("screenHeight")
      public float screenHeight;
      @JsonProperty("screenWidth")
      public float screenWidth;
    public MapWorldDataResponse(String name) {
        super("mapWorld");

    }
}
