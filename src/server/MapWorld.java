package server;

import geometry.Cartesian2D;
import geometry.Shape;
import data_classes.DomElement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Singleton design pattern
 */

public class MapWorld {


    /**
     * Absolute coordinates of the visualization in correspondence with the screen.
     * Putting these in temporarily so that we have some reference in case all of our coordinates need to be offset.
     */
    private static float visMapXAbsolute;
    private static float visMapYAbsolute;
    private static float screenHeight;
    private static float screenWidth;
    private static Shape visMapShape;
    private static Map<String, DomElement> domElements;

    public static void initMapWorld(float screenHeight, float screenWidth, Shape visMapShape, Map<String, DomElement> domElements) {
        MapWorld.screenHeight = screenHeight;
        MapWorld.screenWidth = screenWidth;
        MapWorld.visMapShape = visMapShape;
        if (domElements != null)
            MapWorld.domElements = domElements;
        else
            MapWorld.domElements = new HashMap<String, DomElement>();
    }

    public static float getScreenHeight() {
        return screenHeight;
    }

    public static float getScreenWidth() {
        return screenWidth;
    }

    public static Map<String, DomElement> getDomElements() {
        return domElements;
    }
    public static DomElement getDomElement(String id) {
        return MapWorld.domElements.get(id);
    }
    public static void putDomElement(String id, DomElement domElement) {
        MapWorld.domElements.put(id, domElement);
    }

    public static void setScreenHeight(float screenHeight) {
        MapWorld.screenHeight = screenHeight;
    }

    public static void setScreenWidth(float screenWidth) {
        MapWorld.screenWidth = screenWidth;
    }

    public static void setDomElements(Map<String, DomElement> domElements) {
        MapWorld.domElements = domElements;
    }

    public static boolean isInBounds(float x, float y, float x1, float y1, float width, float height) {
        return (x >= x1 && x <= (x1 + width)) && (y >= y1 && y <= (y1 + height));
    }

    public static boolean isInBounds(float x, float y, Shape shape) {
        return MapWorld.isInBounds(x, y, shape.getX(), shape.getY(), shape.getWidth(), shape.getHeight());
    }

    public static Cartesian2D translateCoordinatesToScreenCoordinates(Cartesian2D coordinate) {
        //Right now, just return as is until we figure out math for calculating.
        return coordinate;
    }
    public static DomElement getIntersection(Cartesian2D fixationCoords, List<DomElement> elements) {
        // Translate fixation coordinates to SVG viewpoint if needed
        fixationCoords = MapWorld.translateCoordinatesToScreenCoordinates(fixationCoords);

        // Check if fixation is within the SVG bounds
        if (isInBounds(fixationCoords.getX(), fixationCoords.getY(), MapWorld.visMapShape)) {
            // Iterate through elements and check bounds
            for (DomElement element : elements) {
                // Check if fixation intersects with the current element
                if (MapWorld.isInBounds(fixationCoords.getX(), fixationCoords.getY(),  element.getShape())) {
                    return element;
                }
            }
        }

        // Return the intersected element, or null if none
        return null;
    }

}
