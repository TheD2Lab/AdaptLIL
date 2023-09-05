package server;
import org.glassfish.grizzly.websockets.DataFrame;
import org.glassfish.grizzly.websockets.WebSocket;
import org.glassfish.grizzly.websockets.WebSocketApplication;


import java.io.IOException;

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



    public void onClose(WebSocket socket, DataFrame frame) {

        System.out.println("Closing session...");
    }
}
