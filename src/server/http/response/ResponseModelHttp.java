package server.http.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseModelHttp {

    @JsonProperty("resultCode")
    protected Integer resultCode;

    @JsonProperty("message")
    protected String message;

    public ResponseModelHttp() {}

    public ResponseModelHttp(Integer resultCode) {
        this.resultCode = resultCode;
    }

    public ResponseModelHttp(Integer resultCode, String message) {
        this.resultCode = resultCode;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getResultCode() {
        return resultCode;
    }

    public void setResultCode(Integer resultCode) {
        this.resultCode = resultCode;
    }
}
