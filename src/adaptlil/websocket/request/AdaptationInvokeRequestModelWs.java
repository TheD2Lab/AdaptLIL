package adaptlil.websocket.request;

import adaptlil.adaptations.Adaptation;

public class AdaptationInvokeRequestModelWs extends InvokeRequestModelWs {

    public Adaptation adaptation;
    public AdaptationInvokeRequestModelWs(Adaptation adaptation) {
        super("adaptation");
        this.adaptation = adaptation;
    }
}
