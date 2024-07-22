class VisualizationAdaptations {

    constructor(visualization, baselineMap) {
        this.visualization = visualization;
        this.baselineMap = baselineMap;
        this.deemphasisAdaptation = new Adaptation('deemphaasis', false, {}, 0.5);
        this.highlightAdaptation = new Adaptation('highlight', false, {}, 0.5);
        this.colorSchemeAdaptation= new Adaptation('colorScheme', false, {
            'map_to_hidden_color': '#FF0000',
            'map_to_not_hidden_color' : '#00FFFF',
        });
        this.annotationAdaptation = new Adaptation('annotation', false, {});
    }

    /**
     * Sets flags for adaptations. Will likely need to be expanded to allow for more customization as needed.
     * @param adaptationType
     * @param state
     * @param styleConfig
     * @param strength
     */
    toggleAdaptation(adaptationType, state, styleConfig, strength) {

        const _this = this;
        _this.deemphasisAdaptation.state = false;
        _this.highlightAdaptation.state = false;
        _this.colorSchemeAdaptation.state = false;
        restoreToBaselineAdaptations(d3.select('#baseline-svg'));
        if (adaptationType === 'deemphasis') {
            _this.deemphasisAdaptation.state = state;
            _this.deemphasisAdaptation.styleConfig = styleConfig;
            _this.deemphasisAdaptation.strength = strength;
        } else if (adaptationType === 'highlighting') {
            _this.highlightAdaptation.state = state;
            _this.highlightAdaptation.styleConfig = styleConfig;
            _this.highlightAdaptation.strength = strength;
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
            _this.colorSchemeAdaptation.styleConfig = styleConfig;
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