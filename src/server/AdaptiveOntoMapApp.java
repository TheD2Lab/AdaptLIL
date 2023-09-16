package server;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import data_classes.DomElement;
import geometry.Shape;
import org.glassfish.grizzly.websockets.DataFrame;
import org.glassfish.grizzly.websockets.WebSocket;
import org.glassfish.grizzly.websockets.WebSocketApplication;
import org.nd4j.shade.guava.collect.Table;
import server.gazepoint.api.recv.RecXmlObject;
import server.response.CellCoordinateDataResponse;
import server.response.DataResponse;
import server.response.GazeResponse;
import server.response.Response;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Temporarily test file to connect OntoMapVis with gazepoint analytics
 */
public class AdaptiveOntoMapApp extends WebSocketApplication {


    private ObjectMapper objectMapper;
    public AdaptiveOntoMapApp() {
        objectMapper = new ObjectMapper();
    }

    public void onConnect(WebSocket socket) {
        socket.send("Hello, client, this is server.");
    }

    public void onMessage(WebSocket socket, String msg) {

        try {
            Response response = objectMapper.readValue(msg, Response.class);
            if (response.type.equals("data")) {
                this.handleDataResponse((DataResponse) response);
            }
        } catch (Exception e) {
            System.err.println("JSON failed to parse via websocket");
            System.out.println(msg);
        }
    }

    public void sendGazeData(WebSocket socket, RecXmlObject recXmlObject) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        XmlMapper xmlMapper = new XmlMapper();
        GazeResponse gazeResponse = new GazeResponse("gaze", xmlMapper.writeValueAsString(recXmlObject));
        socket.send(objectMapper.writeValueAsString(gazeResponse));

    }

    public void handleDataResponse(DataResponse response) {
        if (response.type.equals("cellCoordinates")) {
            this.handleCellCoordinatesResponse((CellCoordinateDataResponse) response);
        }
    }

    public void handleCellCoordinatesResponse(CellCoordinateDataResponse response) {
        Map<String, DomElement> domElementMap = MapWorld.getDomElements();
        for (String domId : response.getShapesById().keySet()) {
            DomElement element = new DomElement(domId, response.getElementType(), response.getShapesById().get(domId));
            MapWorld.putDomElement(domId, element);
        }
        MapWorld.setDomElements(domElementMap);
    }

    public Set<WebSocket> getWebSockets() {
        return super.getWebSockets();
    }
    public void onClose(WebSocket socket, DataFrame frame) {

        System.out.println("Closing session...");
    }
}
