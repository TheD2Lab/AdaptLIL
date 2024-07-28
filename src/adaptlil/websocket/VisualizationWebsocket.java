package adaptlil.websocket;

import adaptlil.Main;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.glassfish.grizzly.websockets.DataFrame;
import org.glassfish.grizzly.websockets.WebSocket;
import org.glassfish.grizzly.websockets.WebSocketApplication;
import adaptlil.mediator.AdaptationMediator;
import adaptlil.mediator.Component;
import adaptlil.mediator.Mediator;
import adaptlil.websocket.request.AdaptationInvokeRequestModelWs;
import adaptlil.websocket.request.InvokeRequestModelWs;

import java.util.Set;

/**
 * The composer handles analyzing and processing of gaze data and invocating adaptive changes. It keeps track
 * of current adaptovis.adaptations, classification results, and so on.
 */
public class VisualizationWebsocket extends WebSocketApplication implements Component {


    private ObjectMapper objectMapper;
    protected boolean hasResponded = false;
    private AdaptationMediator mediator;


    public VisualizationWebsocket() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    }

    /**
     * Calls parent method onConnect
     * @param socket the new {@link WebSocket} connection.
     */
    public void onConnect(WebSocket socket) {
        super.onConnect(socket);
    }

    /**
     * Listener/method invoked when socket receives a message.
     * When frontend sends a message over the socket, this method is invoked
     * @param socket
     * @param msg
     */
    public void onMessage(WebSocket socket, String msg) {
        this.hasResponded = true;
    }

    /**
     * Use this to send adaptovis.adaptations to the frontend
     * Send an Invoke Adaptation Request model to the frontend.
     * @param invokeRequest
     */
    public void invoke(InvokeRequestModelWs invokeRequest) {
      try {
          if (invokeRequest.name.equals("adaptation")) {
              AdaptationInvokeRequestModelWs adaptationInvokeRequest = (AdaptationInvokeRequestModelWs) invokeRequest;
              Main.adaptationLogFile.logLine("adaptation invoke," +adaptationInvokeRequest.adaptation.getType()+ "," + adaptationInvokeRequest.adaptation.getStrength()+","+ System.currentTimeMillis());
              this.send(this.objectMapper.writeValueAsString(adaptationInvokeRequest));
          }
      } catch (JsonProcessingException e) {
          throw new RuntimeException(e);
      }
    }


    /**
     * Sends a message to all connected websockets.
     * @param msg
     */
    public void send(String msg) {
        for (WebSocket socket : this.getWebSockets()) {
            socket.send(msg);
        }
    }

    public Set<WebSocket> getWebSockets() {
        return super.getWebSockets();
    }
    public void onClose(WebSocket socket, DataFrame frame) {
        super.onClose(socket, frame);
        System.out.println("Closing session...");
    }

    @Override
    public void setMediator(Mediator mediator) {
        this.mediator = (AdaptationMediator) mediator;
    }
}
