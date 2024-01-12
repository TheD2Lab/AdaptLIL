package server.websocket.request;

import adaptations.Adaptation;

public class AdaptationInvokeRequestModelWs extends InvokeRequestModelWs {

    public Adaptation adaptation;
    public AdaptationInvokeRequestModelWs(Adaptation adaptation) {
        super("adaptation");
        this.adaptation = adaptation;
    }
}
