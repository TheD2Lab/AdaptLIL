class Websocket {
    hostName = "localhost";
    port = 8080;
    constructor(visualizationMap) {
        this.websocket = new WebSocket("ws://"+this.hostName+":"+this.port+"/gaze");
        this.visualizationMap = visualizationMap;
    }

    /**
     * Adds event listener to wait for the websocket connection to open. Once open, the close event bind is also added to the websocket
     */
    openConnection() {
        const _this = this;
        this.websocket.addEventListener("open", (event) => {
            _this.closeSocketOnWindowClose();
        });
    }


    /**
     * Adds an event listener for messages from the backend. Deconstructs the message into JSON and calls the appropriate functions.
     */
    listenForMessages() {
        const _this = this;
        //Message types:
        //invoke -> Backend wants to invoke an interruption or change to the ontolgoy

        this.websocket.addEventListener("message", (event) => {
            console.log("Message from server ", event.data);
            try {
                const response = JSON.parse(event.data);
                if (response.type === "invoke")
                    _this.handleInvokeRequest(response);
            } catch(e) {
                console.error(e);
            }
        });
    }

    /**
     * Simple event listener to the websocket to close before the connection closes on the backend.
     */
    closeSocketOnWindowClose() {
        const _this = this;
        window.addEventListener('beforeunload', (event) => {
            _this.websocket.close();
        });

    }

    /**
     * Protocol description:
     * response : {type: 'invoke', 'adaptationType' : 'highlighting'|'deemphasis'|'colorScheme', 'adaptationState': true|false}
     * @param response
     */
    handleInvokeRequest(response) {
        const _this = this;
        if (response.name === 'adaptation')
            this.visualizationMap.adaptations.toggleAdaptation(response.adaptation.type, response.adaptation.state, response.adaptation.styleConfig, response.adaptation.strength);

    }


}