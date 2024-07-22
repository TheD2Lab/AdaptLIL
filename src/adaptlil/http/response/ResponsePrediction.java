package adaptlil.http.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

public class ResponsePrediction extends ResponseModelHttp {
    @JsonProperty("output")
    protected double[] output;

    @JsonProperty("outputShape")
    protected long[] outputShape;

    public ResponsePrediction() {}
    public ResponsePrediction(double[] output) {
        this.output = output;
    }

    public INDArray getOutput() {
        return Nd4j.create(output).reshape(outputShape);
    }

    public void setOutput(double[] output) {
        this.output = output;
    }
}
