package server.http.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.nd4j.linalg.api.ndarray.INDArray;
import server.serialization_helpers.ByteArrayToINDArrayDeserializer;

public class ResponsePrediction extends ResponseModelHttp {
    @JsonDeserialize(using= ByteArrayToINDArrayDeserializer.class)
    @JsonProperty("output")
    protected INDArray output;

    public ResponsePrediction() {}
    public ResponsePrediction(INDArray output) {
        this.output = output;
    }

    public INDArray getOutput() {
        return output;
    }

    public void setOutput(INDArray output) {
        this.output = output;
    }
}
