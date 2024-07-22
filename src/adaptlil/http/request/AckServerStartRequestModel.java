package adaptlil.http.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AckServerStartRequestModel extends RequestModelHttp{
    @JsonProperty("message")
    protected String message;
    @JsonProperty("resultCode")
    protected int resultCode;

    public AckServerStartRequestModel() {
    }

    public AckServerStartRequestModel(String message, int resultCode) {
        this.message = message;
        this.resultCode = resultCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }
}
