package adaptovis.websocket;

import adaptovis.Main;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.glassfish.grizzly.websockets.DataFrame;
import org.glassfish.grizzly.websockets.WebSocket;
import org.glassfish.grizzly.websockets.WebSocketApplication;
import adaptovis.mediator.AdaptationMediator;
import adaptovis.mediator.Component;
import adaptovis.mediator.Mediator;
import adaptovis.websocket.request.AdaptationInvokeRequestModelWs;
import adaptovis.websocket.request.InvokeRequestModelWs;
import adaptovis.websocket.response.DataResponseModelWs;

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
        try {
            DataResponseModelWs response = objectMapper.readValue(msg, DataResponseModelWs.class);

        } catch (JsonMappingException e) {
            System.out.println("JSON MAPPING EXCEPTION");
            System.out.println(e.getMessage());
        } catch (JsonProcessingException e) {
            System.out.println("JSON PROCESSING EXCEPTION");
            System.out.println(e.getMessage());
        } finally {
            this.hasResponded = true;
        }
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
              Main.logFile.logLine("adaptation invoke," +adaptationInvokeRequest.adaptation.getType()+ "," + adaptationInvokeRequest.adaptation.getStrength()+","+ System.currentTimeMillis());
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
