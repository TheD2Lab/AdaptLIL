class LinkIndentedListAdaptations {





    constructor() {
        this.deemphasisEnabled = false;
        this.highlightingEnabled = false;
        this.colorSchemeEnabled = false;
        this.annotationsEnabled = false;
        this.alphabeticalSortEnabled = false;
    }

    /**
     * Sets flags for adaptations. Will likely need to be expanded to allow for more customization as needed.
     * @param adaptationType
     * @param state
     */
    toggleAdaptation(adaptationType, state) {
        const _this = this;
        if (adaptationType === 'deemphasis') {
            _this.deemphasisEnabled = state;
        } else if (adaptationType === 'highlighting') {
            _this.highlightingEnabled = state;
        } else if (adaptationType === 'colorScheme') {
            _this.colorSchemeEnabled = state;
        } else if (adaptationType === 'annotations') {
            _this.annotationsEnabled = state;
        } else if (adaptationType === 'alphabeticalSort') {
            _this.alphabeticalSortEnabled = state;
        }

    }

}