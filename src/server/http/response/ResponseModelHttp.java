package server.http.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseModelHttp {

    @JsonProperty("resultCode")
    protected Integer resultCode;

    public ResponseModelHttp() {}
    public Integer getResultCode() {
        return resultCode;
    }

    public void setResultCode(Integer resultCode) {
        this.resultCode = resultCode;
    }
}
