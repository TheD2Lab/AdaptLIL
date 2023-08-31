package server;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

/**
 * Temporarily test file to connect OntoMapVis with gazepoint analytics
 */
@ServerEndpoint("/gaze")
public class GazeEndpoint {
    @OnOpen
    public void onOpen(Session session, EndpointConfig conf) {
        try {
            //Blocking
            session.getBasicRemote().sendText("Hello world");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @OnMessage
    public void OnMessage(Session session, String msg) {
        try {
            //Returns message back.
            session.getBasicRemote().sendText(msg);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @OnError
    public void OnError(Session session, Throwable error) {

    }

    @OnClose
    public void OnClose(Session session, CloseReason reason) {

    }
}
