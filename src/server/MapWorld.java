package server;

import geometry.Cartesian2D;
import geometry.Shape;
import data_classes.DomElement;

import java.util.List;
import java.util.Map;

public class MapWorld {


    /**
     * Absolute coordinates of the visualization in correspondence with the screen.
     * Putting these in temporarily so that we have some reference in case all of our coordinates need to be offset.
     */
    private float visMapXAbsolute;
    private float visMapYAbsolute;
    private float screenHeight;
    private float screenWidth;

    private Shape visMapShape;

    private Map<String, DomElement> domElements;

    public MapWorld(float screenHeight, float screenWidth, Shape visMapShape, Map<String, DomElement> domElements) {
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
        this.visMapShape = visMapShape;
        this.domElements = domElements;
    }

    public float getScreenHeight() {
        return screenHeight;
    }

    public float getScreenWidth() {
        return screenWidth;
    }

    public Map<String, DomElement> getDomElements() {
        return domElements;
    }
    public DomElement getDomElement(String id) {
        return this.domElements.get(id);
    }
    public void putDomElement(String id, DomElement domElement) {
        this.domElements.put(id, domElement);
    }

    public void setScreenHeight(float screenHeight) {
        this.screenHeight = screenHeight;
    }

    public void setScreenWidth(float screenWidth) {
        this.screenWidth = screenWidth;
    }

    public void setDomElements(Map<String, DomElement> domElements) {
        this.domElements = domElements;
    }

    public boolean isInBounds(float x, float y, float x1, float y1, float width, float height) {
        return (x >= x1 && x <= (x1 + width)) && (y >= y1 && y <= (y1 + height));
    }

    public boolean isInBounds(float x, float y, Shape shape) {
        return this.isInBounds(x, y, shape.getX(), shape.getY(), shape.getWidth(), shape.getHeight());
    }

    public Cartesian2D translateCoordinatesToScreenCoordinates(Cartesian2D coordinate) {
        //Right now, just return as is until we figure out math for calculating.
        return coordinate;
    }
    public DomElement getIntersection(Cartesian2D fixationCoords, List<DomElement> elements) {
        // Translate fixation coordinates to SVG viewpoint if needed
        fixationCoords = this.translateCoordinatesToScreenCoordinates(fixationCoords);

        // Check if fixation is within the SVG bounds
        if (isInBounds(fixationCoords.getX(), fixationCoords.getY(), this.visMapShape)) {
            // Iterate through elements and check bounds
            for (DomElement element : elements) {
                // Check if fixation intersects with the current element
                if (this.isInBounds(fixationCoords.getX(), fixationCoords.getY(),  element.getShape())) {
                    return element;
                }
            }
        }

        // Return the intersected element, or null if none
        return null;
    }

}
