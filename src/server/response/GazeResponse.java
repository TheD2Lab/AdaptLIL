package server.response;


import com.fasterxml.jackson.annotation.JsonProperty;

public class GazeResponse {
    @JsonProperty("type")
    public String type;

    @JsonProperty("data")
    //TODO
    public String xmlString; //will replace later

    public GazeResponse(String type, String xmlString) {
        this.type = type;
        this.xmlString = xmlString;
    }
    public GazeResponse() {

    }
}
