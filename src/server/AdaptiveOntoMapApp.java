package server;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.glassfish.grizzly.websockets.DataFrame;
import org.glassfish.grizzly.websockets.WebSocket;
import org.glassfish.grizzly.websockets.WebSocketApplication;
import server.gazepoint.api.recv.RecXmlObject;
import server.response.GazeResponse;


import java.io.IOException;
import java.util.Set;

/**
 * Temporarily test file to connect OntoMapVis with gazepoint analytics
 */
public class AdaptiveOntoMapApp extends WebSocketApplication {
    public void onConnect(WebSocket socket) {
        socket.send("Hello, client, this is server.");
    }

    public void onMessage(WebSocket socket, String msg) {
        System.out.println("Msg from client socket: " + msg);
    }

    public void sendGazeData(WebSocket socket, RecXmlObject recXmlObject) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        XmlMapper xmlMapper = new XmlMapper();
        GazeResponse gazeResponse = new GazeResponse("gaze", xmlMapper.writeValueAsString(recXmlObject));
        socket.send(objectMapper.writeValueAsString(gazeResponse));

    }

    public Set<WebSocket> getWebSockets() {
        return this.getWebSockets();
    }
    public void onClose(WebSocket socket, DataFrame frame) {

        System.out.println("Closing session...");
    }
}
