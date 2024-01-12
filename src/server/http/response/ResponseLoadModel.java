package server.http.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseLoadModel extends ResponseModelHttp{

    @JsonProperty("message")
    protected String message;

    public ResponseLoadModel() {}


    public ResponseLoadModel(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
