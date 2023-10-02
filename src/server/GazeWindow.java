package server;

import server.gazepoint.api.recv.RecXmlObject;
import weka.core.Attribute;
import weka.core.DenseInstance;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Intended use of this file is to implement a 'window' in which gaze can be analyzed and conclusions/predictions can be drawn.
 * In terms of the real-time visualization prototype, it is used to make invokes adaptations to the charts and analyze if they are working
 * based of the user's gaze data.
 */
public class GazeWindow {
    private boolean overlapping;
    private float windowSizeInMilliseconds;
    private int windowSize;
    private int pollingRateInHz;

    private ArrayList<RecXmlObject> gazeData;

    public GazeWindow(boolean overlapping, float windowSizeInMilliseconds) {
        this.overlapping = overlapping;
        this.windowSizeInMilliseconds = windowSizeInMilliseconds;
        this.gazeData = new ArrayList<>();
        this.pollingRateInHz = 150; // default is 150hz
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
        int windowSizeInPackets = (int) windowSizeInMilliseconds * pollingRateInHz;
        this.windowSize = windowSizeInPackets;
    }

    public void setWindowSize(int windowSize) {
        this.windowSize = windowSize;

        //Do some calcuation, set the windowSuize in milliseconds.
        this.windowSizeInMilliseconds = (float) windowSize / pollingRateInHz;
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

    /**
     * Converts the window into an instance than can be used for Weka ML
     * Works by going over each declared field/attribute in each RecXmlObject of each
     * gazeData
     * @return
     */
    public List<DenseInstance> toDenseInstance() throws IllegalAccessException {
        List<DenseInstance> instances = new ArrayList<>();

        for (int i = 0; i < gazeData.size(); ++i) {
            RecXmlObject recXmlObject = gazeData.get(i);
            DenseInstance instance = new DenseInstance(gazeData.size() * recXmlObject.getClass().getDeclaredFields().length);
            for (Field field : recXmlObject.getClass().getDeclaredFields()) {
                Object val = new Object();
                    val = field.get(val);

                if (val != null) {
                    Attribute attr = new Attribute(field.getName());
                    //figure out.
                    if (field.getType() == Double.class)
                        instance.setValue(attr, field.getDouble(val));
                    else if (field.getType() == String.class)
                        instance.setValue(attr, (String) val);
                }
            }
            instances.add(instance);
        }
        return instances;
    }

}
