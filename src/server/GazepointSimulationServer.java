package server;

import server.gazepoint.api.XmlObject;
import server.gazepoint.api.ack.AckXmlObject;
import server.gazepoint.api.recv.RecXmlObject;

import java.net.Socket;
import java.util.List;

public class GazepointSimulationServer {
    private String url;
    private int port;
    private List<RecXmlObject> recXmlPackets;
    private List<AckXmlObject> ackXmlPackets;
    private List<XmlObject> packets;
    private Socket socket;
    public GazepointSimulationServer(String url, int port) {
        this.url = url;
        this.port = port;
    }

    public String getUrl() {
        return url;
    }

    public int getPort() {
        return port;
    }
}
