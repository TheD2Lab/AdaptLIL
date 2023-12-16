class Websocket {
    hostName = "localhost";
    port = 8080;
    constructor(visualizationMap) {
        this.websocket = new WebSocket("ws://"+this.hostName+":"+this.port+"/gaze");
        this.visualizationMap = visualizationMap;
        //TODO, replace map with the matrixmap.
        this.map = {width: 3000,
                    height: 3000,
                    viewportWidth: 500,
                    viewportHeight: 600
        };
    }

    openConnection() {
        const _this = this;
        this.websocket.addEventListener("open", (event) => {

            _this.closeSocketOnWindowClose();
        });
    }


    listenForMessages() {
        const _this = this;
        //Message types:
        //invoke -> Backend wants to invoke an interruption or change to the ontolgoy
        //request -> Backend is requesting some data from the ontology.
        //
        this.websocket.addEventListener("message", (event) => {
            console.log("Message from server ", event.data);
            try {
                const response = JSON.parse(event.data);
                if (response.type === "invoke") {
                    //Likely some chart invokation will happen.
                    _this.handleInvokeRequest(response);
                } else if (response.type === "data") {
                    _this.handleDataRequest(response);
                    //Backend is requesting some sort of data.
                    //respond back
                } else if (response.type === "gaze") {
                    //Handle some live gaze data.
                    _this.handleGazeResponse(response);
                }
            } catch(e) {
                console.error(e);
            }
        });
    }

    closeSocketOnWindowClose() {
        const _this = this;
        window.addEventListener('beforeunload', (event) => {
            _this.websocket.close();
        });

    }

    /**
     * Protocol description:
     * response : {type: 'invoke', 'adaptationType' : 'highlighting'|'deemphasis'|'colorScheme'| 'annotations', 'adaptationState': true|false}
     * @param response
     */
    handleInvokeRequest(response) {
        const _this = this;

        if (response.name === 'adaptation') {
            this.visualizationMap.adaptations.toggleAdaptation(response.adaptation.type, response.adaptation.state, response.adaptation.styleConfig);

        }
        if (response.name === "tooltip") {
          
            const elementIds = response.elementIds;
            for (let elementId of elementIds) {
                this.visualizationMap.showTooltip(d3.select('#'+elementId), 0, 0);
            }
        }

    }

    handleGazeResponse(response) {
        console.log("shouldnt get here because this code is outdated")
        // const recXmlStr = response.data;
        // //Super lazily rn but instead of parsing the response.data gaze to json, we are parsing it as xml
        // const parser = new DOMParser();
        // const xmlDoc = parser.parseFromString(recXmlStr, "text/xml");
        // //Also hardcoded for the time being but backend should configure the FPOGX and FPOGY to the proper
        // //values as they are intiially encoded as a fraction/
        // const screenHeight = 1920;
        // const screenWidth = 1080;
        // const fixation = {x: parseFloat(xmlDoc.getAttribute('FPOGX')) * screenWidth, y: parseFloat(xmlDoc.getAttribute('FPOGY')) * screenHeight};
        // mapWorld.getIntersection(fixation, d3.selectAll('.mapCell'));
    }

    handleDataRequest(response) {
        const _this = this;
        if (response.name === "cellCoordinates") {
            const nodes = d3.selectAll('.mapCell').nodes();
            _this.sendElementCoordinates(nodes, '.mapCell');
        } else if (response.name === "mapWorld") {
            const visMapRect = this.visualizationMap.svg.node().getBoundingClientRect();
            const json_data = {
                'type' : 'data',
                'name': 'mapWorld',
                'visMapShape': {
                    x: visMapRect.x,
                    y: visMapRect.y,
                    width: visMapRect.width,
                    height: visMapRect.height
                },
                xOffset: window.outerWidth - window.innerWidth,
                yOffset: window.outerHeight - window.innerHeight,
                screenHeight: window.outerHeight,
                screenWidth: window.outerWidth
            };
            _this.websocket.send(JSON.stringify(json_data));
        } else if (response.name === 'mapLines') {
            const json_data = {
                'type' : 'data',
                'name' : 'mapLines',
                'mapLines': this.visualizationMap.mapLines
            }
        } else if (response.name === 'ontologyEntities') {

            const json_data = {
                'type' : 'data',
                'name': 'ontologyEntities',
                'entities': this.visualizationMap.entities
            }
        }
    }

    sendElementCoordinates(elements, elementType) {

        const elementShapes = this.visualizationMap.getShapes(elements);
        const jsonBody = {
            'type': 'data',
            'name': 'cellCoordinates',
            'elementType' : elementType,
            'shapes': elementShapes
        }

        console.log('sendingggg...')
        console.log(jsonBody);
        this.websocket.send(JSON.stringify(jsonBody));

    }
}