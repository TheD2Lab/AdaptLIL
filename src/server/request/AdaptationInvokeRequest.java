package server.request;

import adaptations.Adaptation;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

public class AdaptationInvokeRequest extends InvokeRequest{

    @JsonUnwrapped
    public Adaptation adaptation;
    public AdaptationInvokeRequest(Adaptation adaptation) {
        super("adaptation");
        this.adaptation = adaptation;
    }
}
