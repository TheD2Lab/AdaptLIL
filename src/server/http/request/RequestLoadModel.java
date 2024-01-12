package server.http.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RequestLoadModel extends RequestModelHttp{

    public RequestLoadModel() {}

    public String modelName;

    @JsonCreator
    public RequestLoadModel(@JsonProperty(value="modelName", required = true) String modelName) {
        this.modelName = modelName;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }
}
