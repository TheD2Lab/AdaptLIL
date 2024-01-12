package server;

import org.glassfish.grizzly.http.HttpBrokenContentException;
import org.nd4j.linalg.api.ndarray.INDArray;
import server.http.HttpRequestCore;
import server.http.request.RequestLoadModel;
import server.http.request.RequestPrediction;
import server.http.response.ResponseLoadModel;
import server.http.response.ResponsePrediction;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.net.URISyntaxException;

public class KerasServerCore {

    private final String loadModelEndpoint = "loadModel";
    private final String predictEndpoint = "predict";
    private String pythonServerURL;
    private Integer pythonServerPort;
    private String modelName;
    private UriBuilder uriBuilder;

    public KerasServerCore(String pythonServerURL, Integer pythonServerPort) {
        this.pythonServerURL = pythonServerURL;
        this.pythonServerPort = pythonServerPort;
        this.uriBuilder = UriBuilder.fromUri("http://" +pythonServerURL + ":" + pythonServerPort);
    }

    public void loadKerasModel(String modelName) throws URISyntaxException {
        this.modelName = modelName;
        //Uses resource directory below. Using hardcoding for now and will revisit when I clean this up.
        //String simpleMlp = new ClassPathResource("simple_mlp.h5").getFile().getPath();
        URI uri = uriBuilder.path(loadModelEndpoint).build();
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
        URI uri = uriBuilder.path(predictEndpoint).build();
        RequestPrediction requestPrediction = new RequestPrediction(input, input.shape(), "byte_array");
        Response response = HttpRequestCore.POST(uri.toString(), Entity.entity(requestPrediction, MediaType.APPLICATION_JSON));
        if (response.getStatus() == 200) {
            ResponsePrediction prediction = response.readEntity(ResponsePrediction.class);
            return prediction;
        } else {
            System.err.println("Error calling KerasServer predict endpoint: " + uri.toString());
            return null;
        }
    }


}
