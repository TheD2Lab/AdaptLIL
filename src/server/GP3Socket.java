package server;

import javax.websocket.server.ServerEndpoint;

/**
 *
 * Connects to GP3 and reads data.
 */
@ServerEndpoint("/gp3connection")
public class GP3Socket {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    public void start() {

    }
}
