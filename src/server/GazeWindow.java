package server;

import server.gazepoint.api.recv.RecXmlObject;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

import java.lang.reflect.Field;
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
    private int pollingRateInHz;
    private int bufferIndex;

    private RecXmlObject[] gazeBuffer;

//    private ArrayList<RecXmlObject> gazeData;


    //TODO
    //If we want to use fixation duration as baseline
    //we can add a max val here.

    /**
     *
     * @param overlapping Does nothing lol
     * @param windowSizeInMilliseconds
     */
    public GazeWindow(boolean overlapping, float windowSizeInMilliseconds) {
        this.overlapping = overlapping;

        this.pollingRateInHz = 150; // default is 150hz
        this.setWindowSizeInMilliseconds(windowSizeInMilliseconds);
        this.gazeBuffer = new RecXmlObject[this.windowSize];
    }

    public void setGazeBuffer(RecXmlObject[] gazeBuffer) {
        this.gazeBuffer = gazeBuffer;
    }

    public void setOverlapping(boolean overlapping) {
        this.overlapping = overlapping;
    }

    /**
     * Sets the # of packets that should belong in the GazeData (non-enforcing)
     * @param windowSizeInMilliseconds
     */
    public void setWindowSizeInMilliseconds(float windowSizeInMilliseconds) {
        this.windowSizeInMilliseconds = windowSizeInMilliseconds;
        //do some calculation, set real window size
        this.windowSize = (int) ((windowSizeInMilliseconds/1000) * pollingRateInHz);
    }

    public void setWindowSize(int windowSize) {
        this.windowSize = windowSize;

        //Do some calcuation, set the windowSuize in milliseconds.
        this.windowSizeInMilliseconds = (float) windowSize / pollingRateInHz;
    }

    /**
     * Adds a RecXmlObject to the buffer.
     * @param recXmlObject
     * @return The index it was inserted into is returned. -1 is returned if the buffer is full.
     */
    public int add(RecXmlObject recXmlObject) {
        if (this.bufferIndex < this.windowSize) {
            this.gazeBuffer[this.bufferIndex] = recXmlObject;
            ++this.bufferIndex;
            return this.bufferIndex - 1;
        } else {
            return -1;
        }
    }

    public int getInternalIndex() {
        return this.bufferIndex;
    }

    /**
     * Clears the gazeBuffer and resets the internal position.
     */
    public void flush() {
        this.gazeBuffer = new RecXmlObject[this.windowSize];
        this.bufferIndex = 0;
    }
    public RecXmlObject[] getGazeBuffer() {
        return gazeBuffer;
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
     *
     * @return returns true if the buffer is full (internal index is equal to the window size)
     */
    public boolean isFull() {
        return this.bufferIndex == this.windowSize;
    }

    /**
     * https://weka.sourceforge.io/doc.dev/weka/core/DenseInstance.html
     * Converts the window into an instance than can be used for Weka ML
     * Works by going over each declared field/attribute in each RecXmlObject of each
     * gazeData
     * @return
     */
    public Instance toDenseInstance(boolean reduceAttributeNames) {

        //Uses Java.Lang
        /**
         * Uses Java.Lang reflection to access the RecXMLObject fields in the current gazeData queue
         * For each public field in the RecXMLObject, it is set as an attribute in the Instance object as
         * fieldName_{i} where i is the index it belongs to in the gazeData queue.
         * Currently only public fields are converted. If it is private it is not viewed. This may be changed
         * later via the use of Custom annotations.
         *  https://www.baeldung.com/java-custom-annotation (see field level annotations)
         *
         *
         * After the attributes are set, the method loops over the RecXMLObjects again and constructs an Instance
         * which contains the data.
         */
        //Specify attributes list
        ArrayList<Attribute> attributeList = this.getAttributeList(reduceAttributeNames);
        return getInstancesFromAttributeList(attributeList);
    }

    public ArrayList<Attribute> getAttributeList(boolean reduceAttributeNames) {
        ArrayList<Attribute> attributeList = new ArrayList<>();
        for (int i = 0; i < gazeBuffer.length; ++i) {
            RecXmlObject recXmlObject = gazeBuffer[i];
            for (int j = 0; j < recXmlObject.getClass().getDeclaredFields().length; ++j) {
                Field field = recXmlObject.getClass().getDeclaredFields()[j];
                if (field.canAccess(recXmlObject)) {
                    try {
                        if (field.get(recXmlObject) != null && field.getType() != String.class) {
                            String attributeName = "";
                            if (reduceAttributeNames)
                                attributeName = i +"_"+ j;
                            else
                                attributeName = field.getName() + "_" + i;

                            attributeList.add(new Attribute(attributeName));
                        }

                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return attributeList;
    }

    public DenseInstance getInstancesFromAttributeList(ArrayList<Attribute> attributeList) {

        //TODO
        //Revisit data. I understand why it's exponential now
        //
        DenseInstance instance = new DenseInstance(attributeList.size());

        int attrIndex = 0;

        for (int i = 0; i < gazeBuffer.length; ++i) {

            RecXmlObject recXmlObject = gazeBuffer[i];
            for (Field field : recXmlObject.getClass().getDeclaredFields()) {
                Object val = new Object();

                try {
                    val = field.get(recXmlObject);

                    if (val != null && field.getType() != String.class) {

                        //Check types and cast appropriately.
                        //If there is a primitive type
                        //use field.getDouble(recXmlObject)
                        if (field.getType() == Double.class) {
                            System.out.println(val);
                            instance.setValue(attrIndex, (Double) val);
                        }
                        else if (field.getType() == Float.class)
                            instance.setValue(attrIndex, (Double) val);
                        else if (field.getType() == String.class)
                            instance.setValue(attrIndex, (String) val);
                        ++attrIndex;

                    }
                } catch (IllegalAccessException e) { //Field is private, cannot access, ignore in dense instance construciton
                    continue;
                }

            }
        }
        System.out.println(instance.toString());
        return instance;
    }

}
