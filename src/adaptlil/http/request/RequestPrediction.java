package adaptlil.http.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.nd4j.linalg.api.ndarray.INDArray;
import adaptlil.serialization_helpers.INDArrayToDoubleArraySerializer;

public class RequestPrediction extends  RequestModelHttp{
    @JsonProperty("data")
    @JsonSerialize(using = INDArrayToDoubleArraySerializer.class)
    public INDArray data;

    @JsonProperty("shape")
    public long[] shape;
    @JsonProperty("encoding")
    public String encoding;

    public RequestPrediction() {}
    public RequestPrediction(INDArray data, long[] shape, String encoding) {
        this.data = data;
        this.shape = shape;
        this.encoding = encoding;
    }


}

