package server;

import geometry.Cartesian2D;
import geometry.Shape;
import data_classes.DomElement;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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

    private static Cartesian2D visMapOffset;

    public static FileWriter debugFile;
    private static boolean debug = true;

    public static void initMapWorld(float screenHeight, float screenWidth, Shape visMapShape, Map<String, DomElement> domElements) {
        MapWorld.screenHeight = screenHeight;
        MapWorld.screenWidth = screenWidth;
        MapWorld.visMapShape = visMapShape;
        if (domElements != null)
            MapWorld.domElements = domElements;
        else
            MapWorld.domElements = new HashMap<String, DomElement>();

        if (debug) {
            try {
                debugFile = new FileWriter("debug.txt", false);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public static boolean isInBounds(float x, float y, float x1, float y1, float width, float height) {
        if (debug) {
//            try {
//                debugFile.write("(" + x + " >= " + x1 + " && " + x + " <= " + (x1 + width) + ") && " +
//                        "(" + y + " <= " + " (" + y1 + " + " + height + " ))\n");
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
        }
        return (x >= x1 && x <= (x1 + width)) && (y >= y1 && y <= (y1 + height));
    }

    public static boolean isInBounds(float x, float y, Shape shape) {
        return MapWorld.isInBounds(x, y, shape.getX(), shape.getY(), shape.getWidth(), shape.getHeight());
    }

    public static Cartesian2D translateCoordinatesToScreenCoordinates(Cartesian2D coordinate) {
        //Right now, just return as is until we figure out math for calculating.
        return coordinate;
    }
    public static Cartesian2D fixationCoordsToScreen(Cartesian2D fixationCoords) {
        return new Cartesian2D(fixationCoords.getX() * MapWorld.screenWidth, fixationCoords.getY() * MapWorld.screenHeight);
    }
    public static DomElement getIntersection(Cartesian2D fixationCoords, List<DomElement> elements) {
        // Translate fixation coordinates to SVG viewpoint if needed
        fixationCoords = MapWorld.fixationCoordsToScreen(fixationCoords);

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


    public static float getVisMapXAbsolute() {
        return visMapXAbsolute;
    }

    public static float getVisMapYAbsolute() {
        return visMapYAbsolute;
    }

    public static Shape getVisMapShape() {
        return visMapShape;
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

    public static Cartesian2D getVisMapOffset() {
        return MapWorld.visMapOffset;
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

    public static void setVisMapXAbsolute(float visMapXAbsolute) {
        MapWorld.visMapXAbsolute = visMapXAbsolute;
    }

    public static void setVisMapYAbsolute(float visMapYAbsolute) {
        MapWorld.visMapYAbsolute = visMapYAbsolute;
    }

    public static void setVisMapShape(Shape visMapShape) {
        MapWorld.visMapShape = visMapShape;
    }

    public static void setVisMapOffset(Cartesian2D visMapOffset) {
        MapWorld.visMapOffset = visMapOffset;
    }
}
