/**
 * It represents the "World" or space of the Ontology Map. It is used to relay and represent the map so that the backend can
 * understand and tie in the gaze data to the ontology.
 */

class MapWorld {

    constructor(visualizationMap, baselineMap) {
        this.baselineMap = baselineMap;
        this.visualizationMap = visualizationMap;
        this.svgCoords = this.visualizationMap.svg.node().getBoundingClientRect();
        this.svgSize = {width: this.visualizationMap.svg.node().width, height: this.visualizationMap.svg.node().height };
        this.adaptations = new VisualizationAdaptations(visualizationMap, baselineMap);

    }
    getVisualizationViewportHeight() {
        return this.visualizationMap.svg.attr('height');
    }
    getVisualizationViewportWidth() {
        return this.visualizationMap.svg.attr('width');
    }

    /**
     * Translates a raw X value that represents a coordinate on the screen to the SVG.
     */
    translateXToViewpointX() {
        //Screen offset
        //the difference between the search bar, bookmarks, etc. and the viewport/document page
        const screenXOffset = window.outerWidth - window.innerWidth;
        return screenXOffset + this.visualizationMap.svg.node().getBoundingClientRect().x;
    }

    /**
     * Translates a raw Y value that represents a coordinate on the screen to the SVG
     */
    translateYToViewpointY() {
        const screenYOffset = window.outerHeight - window.innerHeight;
        return screenYOffset + this.visualizationMap.svg.node().getBoundingClientRect().y;
    }

    /**
     * Translates a raw X value that represents a coordinate on the screen to the SVG.
     */
    translateViewpointToX() {
        //Screen offset
        //the difference between the search bar, bookmarks, etc. and the viewport/document page
        const screenXOffset = window.outerWidth - window.innerWidth;
        return screenXOffset + this.visualizationMap.svg.node().getBoundingClientRect().x;
    }

    /**
     * Translates a raw Y value that represents a coordinate on the screen to the SVG
     */
    translateViewpointToY() {
        const screenYOffset = window.outerHeight - window.innerHeight;
        return screenYOffset + this.visualizationMap.svg.node().getBoundingClientRect().y;
    }


    getScreenHeight() {
        return window.innerHeight;
    }

    getScreenWidth() {
        return window.innerWidth;
    }

    isInBounds(x, y, x1, y1, width, height) {
        console.log('x: ' + x + ', y: ' + y + ' x1 : ' + x1 + ' y1: ' + y1 + ' width: ' + width + ' height: ' + height)
       return (x >= x1 && ( x <= (x1 + width)))
            && (y >= y1 && ( y <= (y1 + height)));
    }

    /**
     * NOTE: I am concerned with polling rates and calculations- we may have to move this to backend or
     * reduce the polling rate which we calculate if an intersection occurred with the fixation and the elements
     * Returns which element the fixation intersects with (if it does, otherwise undefined is returned)
     * @param fixationCoords : {x, y}
     * @param elements
     */
    getIntersection(fixationCoords, elements) {
        const _this = this;
        const fixationInSvg = fixationCoords;//_this.translateCoordToViewpoint(fixationCoords);
        this.visualizationMap.hideAllTooltips();
        if (this.isInBounds(fixationInSvg.x, fixationInSvg.y, this.svgCoords.x, this.svgCoords.y, this.svgCoords.width, this.svgCoords.height)) {
            //if fixation coords are negative or out of bounds, just return null. user isn't looking in the graph
            for (let element of elements) {
                //Calculate bound
                const elementCoords = element.getBoundingClientRect();
                const elementBBox = element.getBBox();
                if (this.isInBounds(fixationInSvg.x, fixationInSvg.y,
                    elementCoords.x, elementCoords.y, elementBBox.width, elementBBox.height)) {
                    console.log('true');
                    console.log(element.classList);
                    if (element.classList.contains('mapCell')) {
                        //hardcode row and col
                        //but it would be optimal to have this in the gNode element
                        _this.visualizationMap.showTooltip(d3.select(element), 0, 0);
                    }
                } else {
                    _this.visualizationMap.hideTooltip(d3.select(element), 0, 0);
                }
            }
        }
    }

    /**
     * From some list of elements, we will return their x,y position, width, and height. They
     * are indexed by their id (hopefully they have one).
     * @param elements
     * @returns {{}}
     */
    getShapes(elements) {
        const shapes = {};
        for (let element of elements) {
            const elementCoords = element.getBoundingClientRect();
            const elementBBox = element.getBBox();
            shapes[element.getAttribute('id')] =  {
                x : elementCoords.x,
                y : elementCoords.y,
                width : elementBBox.width,
                height : elementBBox.height
            };
        }

        return shapes;
    }


}
