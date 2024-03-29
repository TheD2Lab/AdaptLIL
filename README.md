
# About
This repository contains both the backend and frontend of an intelligent adaptive ontological visualization. Integrated with Gazepoint Api, the visualization applies deep learning techniques to intelligently adapt a visualization to a user's gaze profile. The current adaptations are aimed to reduce clutter, improve readability, and improve understanding of the ontology.
Bear in mind, there may be a lot of why in the heck is it written this way code as this was a prototype on a 6-month crunch
# Differences in original gazepoint-data-analysis repo.
This repository implements the Gazepoint API for real-time gaze data analysis. It also builds the foundation for using gaze data to construct adaptive ontology visualizations (see OntoMapVisAdpative: https://github.com/TheD2Lab/OntoMapVisAdaptive). Lastly, there are data classes for gaze data metrics to enhance reusability of D2Lab experiments

# Requirements
Eye Tracker compatabile with gazepoint
Java 11 or greater
Python 3.X
    - Flask
    - gevent
    - TensorFlow

# Setup
1. Ensure you have maven installed (https://maven.apache.org/download.cgi)
2. Create a directory and clone this repository into it
3. Run the following inside the base directory of the repository:
```
mvn compile
mvn package
```
4. The backend is now compiled and can be ran with:
```
java -jar target/iav-ontology-*.0-bin.jar
```
5. Launch and calibrate gazepoint
6. Open the frontend visualization located in web/index.html
7. Continue to the link indented list (to be altered when study commences)
8. At this point the websocket socket should now be connected

# Gazepoint API
How to realtime gaze
1) Connect to the eye tracker using GP3Socket (To be renamed so it's more obvious)
     `
        import com.fasterxml.jackson.dataformat.xml.XmlMapper;
        XmlMapper mapper = new XmlMapper();
        GP3Socket gp3Socket = new GP3Socket(gp3Hostname, gp3Port);
        gp3Socket.connect();
        gp3Socket.startGazeDataStream(); //As per documentation sends the ENABLE_SEND_DATA and the transmission of eye gaze <REC> packets initiates
        gp3Socket.write((mapper.writeValueAsString(new SetEnableSendCommand(GazeApiCommands.ENABLE_SEND_POG_BEST, true)))); //Use GazeApiCommands consts to get the gazepoint API commands
       `
3) To read real-time data, GP3Socket has a `gazeDataBuffer` method that you may pull from.
   a) Please note, the it is a FIFO, if you need to get the most recent data, flush the buffer
   `
   gp3Socket.getGazeDataBuffer().flush()
   `
4) Profit
There are a whole bunch of commands in the GazeApiCommands object, all seriazable by a jackson mapper

# Websocket
The goal of implementing websocket is to invoke adaptive visualization changes into the OntoMap Visualization.
There is both an implementation of the ES6 WebSocket in the userstudy javascript files in this repo and a Java object.

To create and connect the backend to the frontend, first use the grizzly WebSocket object

`
//Note, passing in gp3Socket will be deprecated as it was initially used for a very early prototype and never removed.
 VisualizationWebsocket visWebSocket = new VisualizationWebsocket(gp3Socket);
WebSocketEngine.getEngine().register("", "/gaze", visWebSocket);
`
The VisualizationWebSocket object extends import org.glassfish.grizzly.websockets.WebSocketApplication and thus inherits from it.
For a comprehensive overview, visit the grizzly documentation https://javadoc.io/doc/org.glassfish.grizzly/grizzly-http-all/3.0.1/org/glassfish/grizzly/websockets/WebSocketApplication.html

In the case of VisWebSocket, the main concerns are: connecting to the frontend and sending an adaptation to it.

To send an adaptation to the frontend over this websocket, first use jackson mapper to serialize an Adaptation Object. In this example we will be using the DeemphasisAdaptation
`
DeemphasisAdaptation adaptation = new DeemphasisAdaptation(boolean state, double timeStarted, double timeModified, double timeStopped, Map<String, String> styleConfig, double strength)
String adaptationBody = mapper.writeValueAsString(adaptation);
visWebSocket.send(adaptationBody);
`

The front end (in our case, the user study) will then read the serialized adaptation as JSON in the format:
`
message body: {    
    "type": "invoke", 
    "name": "adaptation", 
    "adaptation": { 
        "type": "deemphasis" | "highlighting", 
        "state": true | false, //On/off 
        "strength": [0-1] 
    } 
}
`

# Rendering Adaptation
Rendering gets a bit hacky.

Adaptations are received in JSON in the format:
`
message body: {    
    "type": "invoke", 
    "name": "adaptation", 
    "adaptation": { 
        "type": "deemphasis" | "highlighting", 
        "state": true | false, //On/off 
        "strength": [0-1] 
    } 
}
`
When new messages are received over the websocket they get thrown through a control-branch based on the 'type' attribute. This could be used to handle different types of messages but for the sake of the prototype, it's limited to invoke which triggers adaptations.

Next it gets sent through another control statement based on the name attribute.
If it is adaptation then 
`
this.visualizationMap.adaptations.toggleAdaptation(response.adaptation.type, response.adaptation.state, response.adaptation.styleConfig, response.adaptation.strength);
`

Where the visualizationMap is the current visualization (in our case, link-indented-list or LinkIndentedList.js for the maplines and BaselineMap.js for the ontologies.
Since it is built on d3.js, elements are DOM. Therefore, to reflect adaptation updates, hover and click events must also be updated based on these values.

# Adaptation Mediator, Controlling the flow and adjusting behavior

# Python Server
ACK, Load, Predict

# Limitations
1. Access to eye tracking technology
