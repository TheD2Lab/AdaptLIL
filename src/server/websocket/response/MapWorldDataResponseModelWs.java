package server.websocket.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import geometry.Shape;

public class MapWorldDataResponseModelWs extends DataResponseModelWs {
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
    public MapWorldDataResponseModelWs(String name) {
        super("mapWorld");
    }

    public MapWorldDataResponseModelWs() {

    }
}
