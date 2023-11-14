class VisualizationAdaptations {

    constructor(visualization, baselineMap) {
        this.visualization = visualization;
        this.baselineMap = baselineMap;
        this.deemphasisAdaptation = new Adaptation('deemphaasis', false, {});

        this.highlightAdaptation = new Adaptation('highlight', true, {});
        this.colorSchemeAdaptation= new Adaptation('colorScheme', true, {
            'map_to_hidden_color': '#FF0000',
            'map_to_not_hidden_color' : '#00FFFF',
        });
        this.annotationAdaptation = new Adaptation('annotation', false, {});
    }

    /**
     * Sets flags for adaptations. Will likely need to be expanded to allow for more customization as needed.
     * @param adaptationType
     * @param state
     * @param adaptiveSettings
     */
    toggleAdaptation(adaptationType, state, adaptiveSettings) {
        const _this = this;
        if (adaptationType === 'deemphasis') {
            _this.deemphasisAdaptation.state = state;
            _this.deemphasisAdaptation.settings = adaptiveSettings;
        } else if (adaptationType === 'highlighting') {
            _this.highlightAdaptation.state = state;
            _this.highlightAdaptation.settings = adaptiveSettings;
        } else if (adaptationType === 'colorScheme') { //I am moving to having a 'automated' selection color scheme. Looking for academic papers.
            //Could also have depth based colorschemeing
            _this.colorSchemeAdaptation.state = state;
            //Adaptive Settings:
            /**
             * {
             *     'mapLines' : {id: {'color',..},... n}
             *     'entities': {id: {'color',..},... n}
             * }
             */
            _this.colorSchemeAdaptation.settings = adaptiveSettings;
            _this.baselineMap.refreshMapLineColors();
        } else if (adaptationType === 'annotations') { //https://d3-annotation.susielu.com/
            _this.annotationAdaptation.state = state;
        }
    }



    get deemphasisEnabled() {
        return this.deemphasisAdaptation.state;
    }

    get highlightingEnabled() {
        return this.highlightAdaptation.state;
    }

    get colorSchemeEnabled() {
        return this.colorSchemeAdaptation.state;
    }

    get annotationsEnabled() {
        return this.annotationAdaptation.state;
    }

}