
# About
This repository contains both the backend and frontend of an intelligent adaptive ontological visualization (AdaptLIL). Integrated with Gazepoint Api, the visualization applies deep learning techniques to intelligently adapt a visualization to a user's gaze profile. The current adaptations are aimed to reduce clutter, improve readability, and improve task success amon ontology mapping visualizations.

# Java Docs
See: [https://thed2lab.github.io/adaptLIL](https://thed2lab.github.io/adaptLIL)

# Requirements
Eye Tracker w/ Gazepoint API implementation (or override the GazepointSocket with your own protocol)

Java 11 or greater

Python 3.9
- pip
- Poetry

CUDA 11+
* Python server is setup with tensorflow and can use CUDA for GPU accelerated inference

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
java -jar target/adaptlil-*.0-bin.jar
```
To run the backend with a pre-recorded gaze file (simulation), run the command
```
java -jar target/adaptlil-*.0-bin.jar -useSimulation="SimulationFile.csv"
```
5. Navigate to python directory and install poetry
```
python -m pip install poetry
python -m poetry install
```
6. Launch and calibrate gazepoint (Skip if using pre-recorded gaze file
7. Open the frontend visualization located in web/index.html
8. Continue to the link indented list (to be altered when study commences)
9. At this point the websocket socket should now be connected

# Gazepoint API
How to realtime gaze
1) Connect to the eye tracker using GazepointSocket object
     ```
    import com.fasterxml.jackson.dataformat.xml.XmlMapper;
    XmlMapper mapper = new XmlMapper();
    GazepointSocket gazepointSocket = new GazepointSocket(EYETRACKER_HOST, EYETRACKER_PORT);
    gazepointSocket.connect();
    gazepointSocket.start(); //As per documentation sends the ENABLE_SEND_DATA and the transmission of eye gaze <REC> packets initiates
    SetCommand enableDataCommand = new SetEnableSendCommand(GazeApiCommands.ENABLE_SEND_POG_BEST, true); //Use GazeApiCommands consts to get the gazepoint API commands
    String gazeCommandBody = mapper.writeValueAsString(enableDataCommand);
    gazepointSocket.write(gazeCommandBody); 
   ```
     Note: A good chunk of Gazepoint API commands are prewritten for XML serialization in the GazeApiCommands class.
3) To read real-time data, GazepointSocket has a `gazeDataBuffer` method that you may pull from (thread safe)
   a) Please note, the it is a FIFO, if you need to get the most recent data, flush the buffer
   ```
   gazepointSocket.getGazeDataBuffer().flush()
   ```

There are a whole bunch of commands in the GazeApiCommands object, all serializable by a jackson mapper.


See: https://www.gazept.com/dl/Gazepoint_API_v2.0.pdf for reference.

# Websocket
The goal of implementing websocket is to invoke adaptive visualization changes into the OntoMap Visualization.
There is both an implementation of the ES6 WebSocket in the userstudy javascript files in this repo and a Java object.

To create and connect the backend to the frontend, first use the grizzly WebSocket object

```
import org.glassfish.grizzly.websockets.WebSocketEngine;
VisualizationWebsocket visWebSocket = new VisualizationWebsocket();
WebSocketEngine.getEngine().register("", "/gaze", visWebSocket);
```
The VisualizationWebSocket object extends import org.glassfish.grizzly.websockets.WebSocketApplication and thus inherits from it.
For a comprehensive overview, visit the grizzly documentation 

https://javadoc.io/doc/org.glassfish.grizzly/grizzly-http-all/3.0.1/org/glassfish/grizzly/websockets/WebSocketApplication.html

In the case of VisWebSocket, the main concerns are: connecting to the frontend and sending an adaptation to it.

To send an adaptation to the frontend over this websocket, first use jackson mapper to serialize an Adaptation Object. In this example we will be using the DeemphasisAdaptation
```
DeemphasisAdaptation adaptation = new DeemphasisAdaptation(boolean state, double timeStarted, double timeModified, double timeStopped, Map<String, String> styleConfig, double strength)
String adaptationBody = mapper.writeValueAsString(adaptation);
visWebSocket.send(adaptationBody);
```

The front end (in our case, the user study) will then read the serialized adaptation as JSON in the format:
```
message body: {    
    "type": "invoke", 
    "name": "adaptation", 
    "adaptation": { 
        "type": "deemphasis" | "highlighting", 
        "state": true | false, //On/off 
        "strength": [0-1] 
    } 
}
```

# Rendering Adaptation
Adaptations are received in JSON in the format:
```
message body: {    
    "type": "invoke", 
    "name": "adaptation", 
    "adaptation": { 
        "type": "deemphasis" | "highlighting", 
        "state": true | false, //On/off 
        "strength": [0-1] 
    } 
}
```
```
function highlightNode(g, node, adaptation) {
    let adaptiveFontWeight = Math.ceil(900 * adaptation.strength);
    if (adaptiveFontWeight < 500)
        adaptiveFontWeight = 500;

    g.select('#n'+node.id)
        .style('opacity', 1)
        .select('text')
        .style('font-weight', adaptiveFontWeight);

}
```
When new messages are received over the websocket they get thrown through a control-branch based on the 'type' attribute. This could be used to handle different types of messages but for the sake of the prototype, it's limited to invoke which triggers adaptations.

Next it gets sent through another control statement based on the name attribute.
If it is adaptation then 
```
this.visualizationMap.adaptations.toggleAdaptation(response.adaptation.type, response.adaptation.state, response.adaptation.styleConfig, response.adaptation.strength);
```

Where the visualizationMap is the current visualization (in our case, link-indented-list or LinkIndentedList.js for the maplines and BaselineMap.js for the ontologies.
Since it is built on d3.js, elements are DOM. Therefore, to reflect adaptation updates, hover and click events must also be updated based on these values.
# Loading a deep learning model
 ###
Add your model to python/deep_learning_models 
 
Navigate to env.yml and change model_name to your model name.

Change EYETRACKER_REFRESH_RATE to the refresh rate you trained your model on

Change the data shape in src/adaptlil/mediator/AdaptationMediator.java->formatGazeWindowsToModelInput
to match the data shape of the input on your model. This shape is communicated to the python server so this is the only place
you will need to update it.
```
    /**
     * Formats collected gazewindows into the deep learning model's input format. Uses INDArray for better performance.
     * @param gazeWindows
     * @return
     */
    public INDArray formatGazeWindowsToModelInput(List<INDArray> gazeWindows) {
        INDArray unshapedData = Nd4j.stack(0, gazeWindows.get(0), gazeWindows.get(1));

        return unshapedData.reshape(
                new int[] {
                        1, //Only feed 1 block of sequences at a time.
                        this.numSequencesForClassification, // Num sequences to feed\
                        (int) gazeWindows.get(0).shape()[0], //Num attributes per sequence
                }
            );
    }
```
# Adding a new adaptation
 ### Backend
1) Navigate to src/adaptations
2) Create a subclass of Adaptation
   4) What does this do?
   5) 
4) Establish JSON structure and elements to
 ### Frontend
1) Navigate to web/scripts/VisualizationAdapation.js
2) Navigate to web/scripts/BaselineMap.js
3) Navigate to web/scripts/script-alignment.js
# Replacing Rule-Based Selection Process
 ### Overview
 ### Replacement Procedure


# Using a Different Eye Tracker
#### HOWTO

# Adaptation Mediator, Controlling the flow and adjusting behavior

# Python Server
ACK, Load, Predict

# Limitations
1. Access to eye tracking technology
