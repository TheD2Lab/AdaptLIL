package adaptovis.websocket.request;

import adaptovis.adaptations.Adaptation;

public class AdaptationInvokeRequestModelWs extends InvokeRequestModelWs {

    public Adaptation adaptation;
    public AdaptationInvokeRequestModelWs(Adaptation adaptation) {
        super("adaptation");
        this.adaptation = adaptation;
    }
}
