package adaptlil.http;

import org.glassfish.grizzly.http.HttpBrokenContentException;
import org.nd4j.linalg.api.ndarray.INDArray;
import adaptlil.http.request.RequestLoadModel;
import adaptlil.http.request.RequestPrediction;
import adaptlil.http.response.ResponseLoadModel;
import adaptlil.http.response.ResponsePrediction;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import java.net.URI;
import java.net.URISyntaxException;

public class KerasServerCore {

    private final String loadModelEndpoint = "loadModel";
    private final String predictEndpoint = "predict";
    private String pythonServerURL; //TODO  Env var load
    private Integer pythonServerPort; //TODO  Env var load
    private String modelName; //TODO Env var load
    private String baseUri;

    public KerasServerCore(String pythonServerURL, Integer pythonServerPort) {
        this.pythonServerURL = pythonServerURL;
        this.pythonServerPort = pythonServerPort;
        this.baseUri = UriBuilder.fromUri("http://" +pythonServerURL + ":" + pythonServerPort).toString();
    }

    public void loadKerasModel(String modelName) throws URISyntaxException {

        this.modelName = modelName;
        URI uri = URI.create(baseUri + "/" + (loadModelEndpoint));
        RequestLoadModel requestModel = new RequestLoadModel(modelName);
        Response response = HttpRequestCore.POST(uri.toString(), Entity.entity(requestModel, MediaType.APPLICATION_JSON));

        if (response.getStatus() != 200) {
            System.err.println("ERROR LOADING MODEL");
            throw new HttpBrokenContentException();
        }
        ResponseLoadModel responseLoadModel = response.readEntity(ResponseLoadModel.class);

        if (responseLoadModel.getResultCode() >= 1500) {
            System.err.println("ERROR LOADING MODEL");
            throw new HttpBrokenContentException("ResultCode: " + responseLoadModel.getResultCode() + "\n message: " + responseLoadModel.getMessage());
        } else {
            System.out.println("KerasServerCore successfully loaded model.");
        }
    }


    public ResponsePrediction predict(INDArray input) {
        URI uri = URI.create(baseUri + "/" + predictEndpoint);
        RequestPrediction requestPrediction = new RequestPrediction(input, input.shape(), "byte_array");
        Response response = HttpRequestCore.POST(uri.toString(), Entity.entity(requestPrediction, MediaType.APPLICATION_JSON));
        if (response.getStatus() == 200) {
            return response.readEntity(ResponsePrediction.class);
        } else {
            System.err.println("Error calling KerasServer predict endpoint: " + uri);
            return null;
        }
    }


}
