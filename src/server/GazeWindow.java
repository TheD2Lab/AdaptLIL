package server;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import server.gazepoint.api.recv.RecXmlObject;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import wekaext.annotations.IgnoreWekaAttribute;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Intended use of this file is to implement a 'window' in which gaze can be analyzed and conclusions/predictions can be drawn.
 * In terms of the real-time visualization prototype, it is used to make invokes adaptations to the charts and analyze if they are working
 * based of the user's gaze data.
 */
public class GazeWindow implements Component {
    private boolean overlapping;
    private float windowSizeInMilliseconds;
    private int windowSize;
    private int pollingRateInHz;
    private int bufferIndex;

    private RecXmlObject[] gazeBuffer;
    private AdaptationMediator mediator;

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

    public void interpolateMissingValues() {
        int invalidIndex = -1;
        int lastValidIndex = 0;
        List<RecXmlObject> invalidObjects = new ArrayList<RecXmlObject>();
        for (int i = 0; i < this.gazeBuffer.length; ++i) {
            boolean isValid = !this.gazeBuffer[i].hasInvalidAttributes();

            //First encounter of an invalid index after encountering good segments
            if (isValid) {

                //interpolate, a new valid segment has been found after the first.
                if (invalidIndex > -1) {
                    RecXmlObject firstValidObj = this.gazeBuffer[lastValidIndex];
                    RecXmlObject lastValidObj = this.gazeBuffer[i];
                    int steps = i - lastValidIndex - 1;
                    RecXmlObject[] interpolatedRecXmlObjects = this.gazeBuffer[i].interpolate(firstValidObj, lastValidObj, invalidObjects, steps);

                    //Reassign interpolated objects
                    System.arraycopy(interpolatedRecXmlObjects, 0, this.gazeBuffer, invalidIndex, interpolatedRecXmlObjects.length);

                    invalidObjects.clear();
                }

                lastValidIndex = i;

            } else {

                if (invalidIndex < 0 ) { //Invalid object was not yet encountered, mark current position
                    invalidIndex = i;
                }

                //Add to list of invalid objects for interpolation
                invalidObjects.add(this.gazeBuffer[i]);
            }


        }
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

    public INDArray toINDArray() {
        List<Attribute> attributeList = this.getAttributeList(false);
        int numAttributes = attributeList.size();

        double[][] x = new double[this.windowSize][numAttributes];

        for (int i = 0; i < gazeBuffer.length; ++i) {
            int attrIndex = 0;

            RecXmlObject recXmlObject = gazeBuffer[i];
            for (Field field : recXmlObject.getClass().getDeclaredFields()) {
                Object val = new Object();

                try {

                    //Only set values for
                    if (!field.isAnnotationPresent(IgnoreWekaAttribute.class) && field.getType() != String.class) {
                        val = field.get(recXmlObject);

                        //Check types and cast appropriately.
                        //If there is a primitive type
                        //use field.getDouble(recXmlObject)
                        if (field.getType() == Double.class) {
                            if (val != null)
                                x[i][attrIndex] = (Double) val;
                            else
                                x[i][attrIndex] = 0;
                        }
                        else if (field.getType() == Float.class) {
                            if (val != null)
                                x[i][attrIndex] = (Double) val;
                            else
                                x[i][attrIndex] = 0;
                        }
//                        else if (field.getType() == String.class) {
//                            if (val != null)
//                                instance.setValue(attrIndex, (String) val);
//                            else
//                                instance.setValue(attrIndex, "");
//                        }

                        //TODO, not sure how to do nulls for instance so it's left out.
                        ++attrIndex;

                    }
                } catch (IllegalAccessException e) { //Field is private, cannot access, ignore in dense instance construciton
                    continue;
                }

            }
        }

        //TODO
        //DONT FORGET NORMALIZATION
        this.normalize(x);
        //apply interpolation, if needed.
        return Nd4j.create(x);
    }

    public double[][] normalize(double[][] x) {
        //TODO get normalization values
        double[] normalizations = new double[]{};

        return x;
    }

    public ArrayList<Attribute> getAttributeList(boolean reduceAttributeNames) {
        ArrayList<Attribute> attributeList = new ArrayList<>();
        for (int i = 0; i < gazeBuffer.length; ++i) {
            RecXmlObject recXmlObject = gazeBuffer[i];
            for (int j = 0; j < recXmlObject.getClass().getDeclaredFields().length; ++j) {
                Field field = recXmlObject.getClass().getDeclaredFields()[j];
                if (field.canAccess(recXmlObject)) {
                    //Skip strings and those with the IgnoreWekaAttribute annotation
                    if (!field.isAnnotationPresent(IgnoreWekaAttribute.class) && field.getType() != String.class) {
                        String attributeName = "";
                        if (reduceAttributeNames)
                            attributeName = i +"_"+ j;
                        else
                            attributeName = field.getName() + "_" + i;
//                                if (i == 299 && field.getName().equals("RPD"))
//                                    System.out.println(attributeName);
                        attributeList.add(new Attribute(attributeName));
                    }

                }
            }
        }
        return attributeList;
    }

    public DenseInstance getInstancesFromAttributeList(ArrayList<Attribute> attributeList) {


        DenseInstance instance = new DenseInstance(attributeList.size());
        int attrIndex = 0;

        for (int i = 0; i < gazeBuffer.length; ++i) {

            RecXmlObject recXmlObject = gazeBuffer[i];
            for (Field field : recXmlObject.getClass().getDeclaredFields()) {
                Object val = new Object();

                try {

                    //Only set values for
                    if (!field.isAnnotationPresent(IgnoreWekaAttribute.class) && field.getType() != String.class) {
                        val = field.get(recXmlObject);

                        //Check types and cast appropriately.
                        //If there is a primitive type
                        //use field.getDouble(recXmlObject)
                        if (field.getType() == Double.class) {
                            if (val != null)
                                instance.setValue(attrIndex, (Double) val);
                            else
                                instance.setValue(attrIndex, 0.0);
                        }
                        else if (field.getType() == Float.class) {
                            if (val != null)
                                instance.setValue(attrIndex, (Float) val);
                            else
                                instance.setValue(attrIndex, 0.0);
                        }
                        else if (field.getType() == String.class) {
                            if (val != null)
                                instance.setValue(attrIndex, (String) val);
                            else
                                instance.setValue(attrIndex, "");
                        }

                        //TODO, not sure how to do nulls for instance so it's left out.
                        ++attrIndex;

                    }
                } catch (IllegalAccessException e) { //Field is private, cannot access, ignore in dense instance construciton
                    continue;
                }

            }
        }

        return instance;
    }

    /**
     * Calculates the cognitive load from the current gaze window
     * TODO: Ask Dr. Fu how to generate a 'cognitive load' score.
     *
     * @return
     */
    public Float getCognitiveLoadScore() {
        return 1.0F;
    }

    @Override
    public void setMediator(Mediator mediator) {
        this.mediator = (AdaptationMediator) mediator;
    }
}
