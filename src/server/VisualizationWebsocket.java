package server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import data_classes.DomElement;
import data_classes.Fixation;
import geometry.Cartesian2D;
import org.glassfish.grizzly.websockets.DataFrame;
import org.glassfish.grizzly.websockets.WebSocket;
import org.glassfish.grizzly.websockets.WebSocketApplication;
import server.gazepoint.api.recv.RecXmlObject;
import server.websocket.request.AdaptationInvokeRequestModelWs;
import server.websocket.request.DataRequestModelWs;
import server.websocket.request.InvokeRequestModelWs;
import server.websocket.request.TooltipInvokeRequestModelWs;
import server.websocket.response.*;


import java.util.*;

/**
 * The composer handles analyzing and processing of gaze data and invocating adaptive changes. It keeps track
 * of current adaptations, classification results, and so on.
 */
public class VisualizationWebsocket extends WebSocketApplication implements Component {


    public List<String> responses = new LinkedList<>();
    private ObjectMapper objectMapper;
    protected boolean hasResponded = false;
    private GP3Socket gp3Socket;

    private AdaptationMediator mediator;


    public VisualizationWebsocket(GP3Socket gp3Socket) {
        this.gp3Socket = gp3Socket;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    }

    public void onConnect(WebSocket socket) {

        super.onConnect(socket);
        System.out.println("Connected to client websocket");
        //Request map world dimensions
//        this.requestDataResponse(socket, "mapWorld");

        //MapWorld most likely intialized due to responded.
//        while (!this.hasResponded) {} //busy wait
//        System.out.println("MapWorld Response Received");
//
//        System.out.println("Requesting Cell Coordinates");
//        //Request cell coordinates
//        this.requestDataResponse(socket, "cellCoordinates");
//        while (!this.hasResponded) {} //busy wait
//
//        System.out.println("Cell Coordinates Recieved");
//
//        //Start reading gaze data from buffer and test for intersections.
//        RecXmlObject recObject = null;
//        do {
//            recObject = gp3Socket.readGazeDataFromBuffer();
//
//        } while (recObject == null || recObject.getFixation() == null);
//        XmlMapper xmlMapper = new XmlMapper();
//        try {
//            System.out.println(xmlMapper.writeValueAsString(recObject));
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//        while (recObject != null) {
//            Fixation fixation = recObject.getFixation();
//
//            Cartesian2D fixationCoords = new Cartesian2D(fixation.x, fixation.y);
//            DomElement intersectionElement = MapWorld.getIntersection(fixationCoords, new ArrayList<>(MapWorld.getDomElements().values()));
//            if (intersectionElement != null) {
//                //Fixation intersected with element, invoke tooltip
//                TooltipInvokeRequest invokeRequest = new TooltipInvokeRequest(
//                        new String[]{intersectionElement.getId()}
//                );
//                this.invokeTooltip(socket, invokeRequest);
//            }
//            recObject = gp3Socket.readGazeDataFromBuffer();
//
//        }
    }

    /**
     * Listener/method invoked when socket receives a message.
     *
     * @param socket
     * @param msg
     */
    public void onMessage(WebSocket socket, String msg) {
        try {
            DataResponseModelWs response = objectMapper.readValue(msg, DataResponseModelWs.class);
            if (response.type.equals("data")) {
                this.handleDataResponse(socket, response);

            }
        } catch (JsonMappingException e) {
            System.out.println("JSON MAPPING EXCEPTION");
            System.out.println(e.getMessage());
//            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            System.out.println("JSON PROCESSING EXCEPTION");
            System.out.println(e.getMessage());
//            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            System.out.println("INTERUPTEDEXCEPTION");
            System.out.println(e.getMessage());
//            throw new RuntimeException(e);
        } finally {
            this.hasResponded = true;
        }
    }

    public void requestDataResponse(WebSocket socket, String requestName) {
        this.hasResponded = false;
        DataRequestModelWs dataRequest = new DataRequestModelWs(requestName);
        try {
            socket.send(this.objectMapper.writeValueAsString(dataRequest));

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    public void invoke(InvokeRequestModelWs invokeRequest) {
      try {
          if (invokeRequest.name.equals("adaptation")) {
              AdaptationInvokeRequestModelWs adaptationInvokeRequest = (AdaptationInvokeRequestModelWs) invokeRequest;
              ServerMain.logFile.logLine("adaptation invoke," +adaptationInvokeRequest.adaptation.getType()+ "," + adaptationInvokeRequest.adaptation.getStrength()+","+ System.currentTimeMillis());
              this.send(this.objectMapper.writeValueAsString(adaptationInvokeRequest));
          }
      } catch (JsonProcessingException e) {
          throw new RuntimeException(e);
      }
    }

    public void invokeTooltip(WebSocket socket, TooltipInvokeRequestModelWs tooltipInvokeRequest) {
        try {
            socket.send(this.objectMapper.writeValueAsString(tooltipInvokeRequest));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendGazeData(WebSocket socket, RecXmlObject recXmlObject) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        XmlMapper xmlMapper = new XmlMapper();
        GazeResponseModelWs gazeResponseModelWs = new GazeResponseModelWs("gaze", xmlMapper.writeValueAsString(recXmlObject));
        socket.send(objectMapper.writeValueAsString(gazeResponseModelWs));

    }

    public void handleDataResponse(WebSocket socket, DataResponseModelWs response) throws InterruptedException {
        if (response.name.equals("cellCoordinates")) {
            this.handleCellCoordinatesResponse(socket, (CellCoordinateDataResponseModelWs) response);
        } else if (response.name.equals("mapWorld")) {
            this.handleMapWorldResponse(socket, (MapWorldDataResponseModelWs) response);
        }
    }

    public void handleCellCoordinatesResponse(WebSocket socket, CellCoordinateDataResponseModelWs response) throws InterruptedException {
        System.out.println("handling cellCoordinates");
        XmlMapper xmlMapper = new XmlMapper();
        Map<String, DomElement> domElementMap = MapWorld.getDomElements();
        for (String domId : response.getShapesById().keySet()) {
            DomElement element = new DomElement(domId, response.getElementType(), response.getShapesById().get(domId));
            MapWorld.putDomElement(domId, element);
        }
        MapWorld.setDomElements(domElementMap);

        RecXmlObject recObject = null;
        do {
            recObject = gp3Socket.readGazeDataFromBuffer();

        } while (recObject == null || recObject.getFixation() == null);

//        try {
//            System.out.println(xmlMapper.writeValueAsString(recObject));
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
        int count = 0;
        while (recObject != null) {
            ++count;
          while (recObject.getFixation() == null) {
              //keep reading and ignore the other fixations (only used for testing purposes)
              recObject = gp3Socket.readGazeDataFromBuffer();

          }
            try {
                System.out.println("interal cnt: " + count + "REC CNT: " + recObject.getCounter() + " buf size: " + gp3Socket.getGazeDataQueue().size() + "\r\n");
            } catch (Exception e) {
                System.out.println("erro occured, rec counter/recobject is null");
                throw new RuntimeException(e);
            }
            Fixation fixation = recObject.getFixation();
            Cartesian2D fixationCoords = new Cartesian2D((float) fixation.getX(), (float) fixation.getY());

            DomElement intersectionElement = MapWorld.getIntersection(fixationCoords, new ArrayList<>(MapWorld.getDomElements().values()));
            if (intersectionElement != null) {
                System.out.println(intersectionElement.getId());
                //Fixation intersected with element, invoke tooltip
                TooltipInvokeRequestModelWs invokeRequest = new TooltipInvokeRequestModelWs(
                        new String[]{intersectionElement.getId()}
                );
                this.invokeTooltip(socket, invokeRequest);
            }
            recObject = gp3Socket.readGazeDataFromBuffer();
        }

    }

    public void handleMapWorldResponse(WebSocket socket, MapWorldDataResponseModelWs response) {
        MapWorld.setScreenWidth(response.screenWidth);
        MapWorld.setScreenHeight(response.screenHeight);
        MapWorld.setVisMapShape(response.visMapShape);

        MapWorld.setVisMapOffset(new Cartesian2D(response.xOffset, response.yOffset));
        MapWorld.initMapWorld(response.screenHeight, response.screenWidth, response.visMapShape, new HashMap<>());
        //This might not be needed but we will include just in case.
        //MapWorld.setVisMapXAbsolute(response.visMapShape.x + response.xOffset);
        //MapWorld.setVisMapYAbsolute(response.visMapShape.y + response.yOffset);

        System.out.println("MapWorld Response Received");
//
//        System.out.println("Requesting Cell Coordinates");
//        //Request cell coordinates
        this.requestDataResponse(socket, "cellCoordinates");
//        while (!this.hasResponded) {} //busy wait
//
//        System.out.println("Cell Coordinates Recieved");
//
//        //Start reading gaze data from buffer and test for intersections.
//
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

    public boolean hasResponded() {
        return hasResponded;
    }

    @Override
    public void setMediator(Mediator mediator) {
        this.mediator = (AdaptationMediator) mediator;
    }
}
