package adaptlil.websocket.response;


import com.fasterxml.jackson.annotation.JsonProperty;

public class GazeResponseModelWs {
    @JsonProperty("type")
    public String type;

    @JsonProperty("data")
    //TODO
    public String xmlString; //will replace later

    public GazeResponseModelWs(String type, String xmlString) {
        this.type = type;
        this.xmlString = xmlString;
    }
    public GazeResponseModelWs() {

    }
}
