package server;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import data_classes.DomElement;
import geometry.Cartesian2D;
import org.glassfish.grizzly.websockets.DataFrame;
import org.glassfish.grizzly.websockets.WebSocket;
import org.glassfish.grizzly.websockets.WebSocketApplication;
import server.gazepoint.api.recv.RecXmlObject;
import server.request.DataRequest;
import server.request.InvokeRequest;
import server.request.TooltipInvokeRequest;
import server.response.*;


import java.util.*;

/**
 * Temporarily test file to connect OntoMapVis with gazepoint analytics
 */
public class AdaptiveOntoMapApp extends WebSocketApplication {


    public List<String> responses = new LinkedList<>();
    private ObjectMapper objectMapper;
    protected boolean hasResponded = false;

    public AdaptiveOntoMapApp() {
        objectMapper = new ObjectMapper();
    }

    public void onConnect(WebSocket socket) {
        socket.send("Hello, client, this is server.");
    }

    /**
     * Listener/method invoked when socket receives a message.
     *
     * @param socket
     * @param msg
     */
    public void onMessage(WebSocket socket, String msg) {
        try {
            Response response = objectMapper.readValue(msg, Response.class);
            if (response.type.equals("data")) {
                this.handleDataResponse((DataResponse) response);
            }
        } catch (Exception e) {
            System.err.println("JSON failed to parse via websocket");
            System.out.println(msg);
        } finally {
            this.hasResponded = true;
        }
    }

    public void requestDataResponse(WebSocket socket, String requestName) {
        this.hasResponded = false;
        DataRequest dataRequest = new DataRequest(requestName);
        try {
            socket.send(this.objectMapper.writeValueAsString(dataRequest));

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    public void invoke(WebSocket socket, InvokeRequest invokeRequest) {
        if (invokeRequest.name.equals("tooltip")) {
            invokeTooltip(socket, (TooltipInvokeRequest) invokeRequest);
        }
    }

    public void invokeTooltip(WebSocket socket, TooltipInvokeRequest tooltipInvokeRequest) {
        try {
            socket.send(this.objectMapper.writeValueAsString(tooltipInvokeRequest));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendGazeData(WebSocket socket, RecXmlObject recXmlObject) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        XmlMapper xmlMapper = new XmlMapper();
        GazeResponse gazeResponse = new GazeResponse("gaze", xmlMapper.writeValueAsString(recXmlObject));
        socket.send(objectMapper.writeValueAsString(gazeResponse));

    }

    public void handleDataResponse(DataResponse response) {
        if (response.name.equals("cellCoordinates")) {
            this.handleCellCoordinatesResponse((CellCoordinateDataResponse) response);
        } else if (response.name.equals("mapWorld")) {
            this.handleMapWorldResponse((MapWorldDataResponse) response);
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

    public void handleMapWorldResponse(MapWorldDataResponse response) {
        MapWorld.setScreenWidth(response.screenWidth);
        MapWorld.setScreenHeight(response.screenHeight);
        MapWorld.setVisMapShape(response.visMapShape);

        MapWorld.setVisMapOffset(new Cartesian2D(response.xOffset, response.yOffset));
        //This might not be needed but we will include just in case.
        //MapWorld.setVisMapXAbsolute(response.visMapShape.x + response.xOffset);
        //MapWorld.setVisMapYAbsolute(response.visMapShape.y + response.yOffset);
    }

    public Set<WebSocket> getWebSockets() {
        return super.getWebSockets();
    }
    public void onClose(WebSocket socket, DataFrame frame) {

        System.out.println("Closing session...");
    }

    public boolean hasResponded() {
        return hasResponded;
    }
}
