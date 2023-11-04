class LinkIndentedListAdaptations {

    constructor() {
        this.deemphasisAdaptation = new Adaptation('deemphaasis', false, {});

        this.highlightAdaptation = new Adaptation('highlight', false, {});
        this.colorSchemeAdaptation= new Adaptation('colorScheme', false, {});
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
        } else if (adaptationType === 'highlighting') {
            _this.highlightAdaptation.state = state;
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