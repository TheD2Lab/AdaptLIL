package adaptlil.websocket.request;

import adaptlil.adaptations.Adaptation;

/**
 * JSON Request model to send via websocket
 * {
 * 'type': 'invoke',
 * 'name': 'adaptation',
 * 'adaptation': {
 *      'state': 0/1 -> on/off
 *      'type': deemphasis/highlighting
 *      'strength': [0,1] (float)
 *  }
 * }
 */
public class AdaptationInvokeRequestModelWs extends InvokeRequestModelWs {

    public Adaptation adaptation;
    public AdaptationInvokeRequestModelWs(Adaptation adaptation) {
        super("adaptation");
        this.adaptation = adaptation;
    }
}
