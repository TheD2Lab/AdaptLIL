package server;

import server.gazepoint.api.recv.RecXmlObject;

import java.util.ArrayList;

/**
 * Intended use of this file is to implement a 'window' in which gaze can be analyzed and conclusions/predictions can be drawn.
 * In terms of the real-time visualization prototype, it is used to make invokes adaptations to the charts and analyze if they are working
 * based of the user's gaze data.
 */
public class GazeWindow {
    private boolean overlapping;
    private float windowSizeInMilliseconds;
    private int windowSize;

    private ArrayList<RecXmlObject> gazeData;

    public GazeWindow(boolean overlapping, float windowSizeInMilliseconds) {
        this.overlapping = overlapping;
        this.windowSizeInMilliseconds = windowSizeInMilliseconds;
        this.gazeData = new ArrayList<>();
    }

    public void setGazeData(ArrayList<RecXmlObject> gazeData) {
        this.gazeData = gazeData;
    }

    public void setOverlapping(boolean overlapping) {
        this.overlapping = overlapping;
    }
    public void setWindowSizeInMilliseconds(float windowSizeInMilliseconds) {
        this.windowSizeInMilliseconds = windowSizeInMilliseconds;

        //do some calculation, set real window size
        int windowSizeInPackets = (int) windowSizeInMilliseconds;
        this.windowSize = windowSizeInPackets;
    }

    public void setWindowSize(int windowSize) {
        this.windowSize = windowSize;

        //Do some calcuation, set the windowSuize in milliseconds.
        this.windowSizeInMilliseconds = windowSize;
    }

    public ArrayList<RecXmlObject> getGazeData() {
        return gazeData;
    }

    public int getWindowSize() {
        return windowSize;
    }

    public float getWindowSizeInMilliseconds() {
        return windowSizeInMilliseconds;
    }

    public boolean isOverlapping() {
        return overlapping;
    }


}
