package adaptlil.http.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RequestClosePythonServer extends RequestModelHttp {

    public RequestClosePythonServer(int sessionId) {
        this.sessionId = sessionId;
    }

    @JsonProperty
    public int sessionId;
}
