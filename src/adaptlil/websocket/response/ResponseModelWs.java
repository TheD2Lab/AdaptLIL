package adaptlil.websocket.response;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Unused but left for folder structure and future bi-directional communication with visualization and java server.
 * In other words, make a response model and specify json types if you wish to send data back to java server (such as a user finishing a task)
 */
public class ResponseModelWs {

    @JsonProperty("type")
    public String type;

    public ResponseModelWs(String type) {
        this.type = type;
    }
    public ResponseModelWs() {

    }
}
