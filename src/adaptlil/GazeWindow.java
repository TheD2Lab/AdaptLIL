package adaptlil;

import adaptlil.annotations.IgnoreWekaAttribute;
import adaptlil.gazepoint.excel.GazeMetrics;
import adaptlil.data_classes.Fixation;
import adaptlil.data_classes.Saccade;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import adaptlil.gazepoint.api.recv.RecXml;
import adaptlil.mediator.AdaptationMediator;
import adaptlil.mediator.Component;
import adaptlil.mediator.Mediator;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Intended use of this file is to implement a 'window' in which gaze can be analyzed and conclusions/predictions can be drawn.
 * In terms of the real-time visualization prototype, it is used to make invokes adaptovis.adaptations to the charts and analyze if they are working
 * based of the user's gaze data.
 */
public class GazeWindow implements Component {

    private float windowSizeInMilliseconds;
    private int windowSize;
    private int pollingRateInHz;
    private int bufferIndex;

    private RecXml[] gazeBuffer;
    private AdaptationMediator mediator;

    private GazeMetrics gazeMetrics;


    /**
     *
     * @param windowSizeInMilliseconds
     */
    public GazeWindow(float windowSizeInMilliseconds, int pollingRateInHz) {

        this.pollingRateInHz = 150; // default is 150hz
        this.setWindowSizeInMilliseconds(windowSizeInMilliseconds);
        this.gazeBuffer = new RecXml[this.windowSize];
    }


    /**
     * Linear adaptlil.interpolation to fill in any values that may be flagged as invalid by the eyetracker. This applies to all attributes in the RecXmlObject
     */
    public void interpolateMissingValues() {
        int invalidIndex = -1;
        int lastValidIndex = 0;
        List<RecXml> invalidObjects = new ArrayList<RecXml>();
        for (int i = 0; i < this.gazeBuffer.length; ++i) {
            boolean isValid = !this.gazeBuffer[i].hasInvalidAttributes();

            //First encounter of an invalid index after encountering good segments
            if (isValid) {

                //interpolate, a new valid segment has been found after the first.
                if (invalidIndex > -1) {
                    RecXml firstValidObj = this.gazeBuffer[lastValidIndex];
                    RecXml lastValidObj = this.gazeBuffer[i];
                    int steps = i - lastValidIndex - 1;
                    RecXml[] interpolatedRecXmls = this.gazeBuffer[i].interpolate(firstValidObj, lastValidObj, invalidObjects, steps);

                    //Reassign interpolated objects
                    System.arraycopy(interpolatedRecXmls, 0, this.gazeBuffer, invalidIndex, interpolatedRecXmls.length);

                    invalidObjects.clear();
                }

                lastValidIndex = i;

            } else {

                if (invalidIndex < 0 ) { //Invalid object was not yet encountered, mark current position
                    invalidIndex = i;
                }

                //Add to list of invalid objects for adaptlil.interpolation
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
    public Instance toDenseInstance(boolean reduceAttributeNames, boolean useAdditionalGazeMetrics) {
        if (useAdditionalGazeMetrics)
            this.calculateAdditionalGazeMetrics();
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
        ArrayList<Attribute> attributeList = this.getAttributeList(reduceAttributeNames, useAdditionalGazeMetrics);
        return getInstancesFromAttributeList(attributeList, useAdditionalGazeMetrics);
    }

    /**
     * Converts each gaze data point (RecXmlObject) into an element for the INDArray.
     * The # of elements in the array is a factor of number of non-null protected/public attributes in the class.
     * E.g. RecXmlOBjects have 3 non-null public attributes and there is a gaze window of size 10. The shape of the INDArray will be 30
     * @param useAdditionalGazeMetrics
     * @return
     */
    public INDArray toINDArray(boolean useAdditionalGazeMetrics) {

        if (useAdditionalGazeMetrics)
            this.calculateAdditionalGazeMetrics();

        List<Attribute> attributeList = this.getAttributeList(false, useAdditionalGazeMetrics);

        int numAttributes = attributeList.size();

        double[] x = new double[numAttributes];
        int attrIndex = 0;

        if (!useAdditionalGazeMetrics) { //Raw Gaze for the INDArray
            for (int i = 0; i < gazeBuffer.length; ++i) {

                RecXml recXml = gazeBuffer[i];
                for (Field field : recXml.getClass().getDeclaredFields()) {
                    Object val = new Object();

                    try {

                        //Only set values for
                        if (!field.isAnnotationPresent(IgnoreWekaAttribute.class) && field.getType() != String.class) {
                            val = field.get(recXml);

                            //Check types and cast appropriately.
                            //If there is a primitive type
                            //use field.getDouble(recXmlObject)
                            if (field.getType() == Double.class || field.getType() == Integer.class || field.getType() == Float.class) {
                                if (val != null)
                                    x[attrIndex] = (double) val;
                                else
                                    x[attrIndex] = 0;
                            }


                            ++attrIndex;

                        }
                    } catch (IllegalAccessException e) { //Field is private, cannot access, ignore in dense instance construction
                    }

                }
            }
        }


        //Fill remaining with gaze metrics (saccades, fixations etc.)
        if (useAdditionalGazeMetrics) {
            for (int i = attrIndex; i < numAttributes; ++i) {
                for (Field field : gazeMetrics.getClass().getDeclaredFields()) {
                    Object val = new Object();

                    try {

                        //Only set values for
                        if (!field.isAnnotationPresent(IgnoreWekaAttribute.class) && field.getType() != String.class) {
                            val = field.get(gazeMetrics);

                            //Check types and cast appropriately.
                            //If there is a primitive type
                            //use field.getDouble(recXmlObject)
                            if (field.getType() == Double.class || field.getType() == Integer.class || field.getType() == Float.class) {
                                if (val != null)
                                    x[attrIndex] = (double) val;
                                else
                                    x[attrIndex] = 0;
                            }
                        }
                    } catch (IllegalAccessException e) { //Field is private, cannot access, ignore in dense instance construction
                    }
                }
            }
        }

        return Nd4j.create(x);
    }


    /**
     * Gets Attributes used for machine learning classification
     * @param reduceAttributeNames
     * @param useAdditionalGazeMetrics
     * @return
     */
    public ArrayList<Attribute> getAttributeList(boolean reduceAttributeNames, boolean useAdditionalGazeMetrics) {
        ArrayList<Attribute> attributeList = new ArrayList<>();
        if (!useAdditionalGazeMetrics) {
            for (int i = 0; i < gazeBuffer.length; ++i) {
                RecXml recXml = gazeBuffer[i];
                for (int j = 0; j < recXml.getClass().getDeclaredFields().length; ++j) {
                    Field field = recXml.getClass().getDeclaredFields()[j];
                    if (field.canAccess(recXml)) {
                        //Skip strings and those with the IgnoreWekaAttribute annotation
                        if (!field.isAnnotationPresent(IgnoreWekaAttribute.class) && field.getType() != String.class) {
                            String attributeName = "";
                            if (reduceAttributeNames)
                                attributeName = i + "_" + j;
                            else
                                attributeName = field.getName() + "_" + i;

                            attributeList.add(new Attribute(attributeName));
                        }

                    }
                }
            }
        }

        if (useAdditionalGazeMetrics) {
            for (int i = 0; i < this.gazeMetrics.getClass().getDeclaredFields().length; ++i) {
                Field field = this.gazeMetrics.getClass().getDeclaredFields()[i];
                if (field.canAccess(this.gazeMetrics)) {
                    //Skip strings and those with the IgnoreWekaAttribute annotation
                    if (!field.isAnnotationPresent(IgnoreWekaAttribute.class) && field.getType() != String.class) {
                        String attributeName = "";
                        if (reduceAttributeNames)
                            attributeName = i + "_" + i;
                        else
                            attributeName = field.getName() + "_" + i;

                        attributeList.add(new Attribute(attributeName));
                    }

                }
            }
        }

        return attributeList;
    }

    /**
     * Creates a DenseInstanceList of all gaze data points (RecXmlObjects) in the window. The DenseInstance list can be used for WEKA
     * @param attributeList
     * @param useAdditionalGazeMetrics
     * @return
     */
    public DenseInstance getInstancesFromAttributeList(ArrayList<Attribute> attributeList, boolean useAdditionalGazeMetrics) {

        DenseInstance instance = new DenseInstance(attributeList.size());
        int attrIndex = 0;

        if (!useAdditionalGazeMetrics) {
            for (int i = 0; i < gazeBuffer.length; ++i) {

                RecXml recXml = gazeBuffer[i];
                for (Field field : recXml.getClass().getDeclaredFields()) {
                    Object val = new Object();

                    try {

                        //Only set values for
                        if (!field.isAnnotationPresent(IgnoreWekaAttribute.class) && field.getType() != String.class) {
                            val = field.get(recXml);

                            //Check types and cast appropriately.
                            //If there is a primitive type
                            //use field.getDouble(recXmlObject)
                            if (field.getType() == Double.class) {
                                if (val != null)
                                    instance.setValue(attrIndex, (Double) val);
                                else
                                    instance.setValue(attrIndex, 0.0);
                            }

                            if (field.getType() == Integer.class) {
                                if (val != null)
                                    instance.setValue(attrIndex, (Double) val);
                                else
                                    instance.setValue(attrIndex, 0.0);
                            } else if (field.getType() == Float.class) {
                                if (val != null)
                                    instance.setValue(attrIndex, (Float) val);
                                else
                                    instance.setValue(attrIndex, 0.0);
                            } else if (field.getType() == String.class) {
                                if (val != null)
                                    instance.setValue(attrIndex, (String) val);
                                else
                                    instance.setValue(attrIndex, "");
                            }

                            ++attrIndex;

                        }
                    } catch (IllegalAccessException e) { //Field is private, cannot access, ignore in dense instance construction
                        continue;
                    }
                }
            }
        }

        if (useAdditionalGazeMetrics) {
            for (Field field : this.gazeMetrics.getClass().getDeclaredFields()) {
                Object val = new Object();

                try {

                    //Only set values for
                    if (!field.isAnnotationPresent(IgnoreWekaAttribute.class) && field.getType() != String.class) {
                        val = field.get(this.gazeMetrics);

                        //Check types and cast appropriately.
                        //If there is a primitive type
                        //use field.getDouble(recXmlObject)
                        if (field.getType() == Double.class) {
                            if (val != null)
                                instance.setValue(attrIndex, (Double) val);
                            else
                                instance.setValue(attrIndex, 0.0);
                        }

                        if (field.getType() == Integer.class) {
                            if (val != null)
                                instance.setValue(attrIndex, (Integer) val);
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

                        ++attrIndex;

                    }
                } catch (IllegalAccessException e) { //Field is private, cannot access, ignore in dense instance construction
                    continue;
                }
            }
        }

        return instance;
    }

    /**
     * Calculates additional metrics useful for classification and ml training.
     */
    public void calculateAdditionalGazeMetrics() {
        //Go through gaze window
        //Go through each packet
        //calculate stats
        //Try to keep to O(m*n), m = windowSize, n = number of fields in the packets
        int numFixations = 0;
        int numSaccades = 0;
        Set<Integer> fixationIds = new HashSet<>();
        List<Saccade> saccadeList = new ArrayList<>();
        List<Fixation> fixations = new ArrayList<>();
        List<Double> fixationDurations = new ArrayList<>();
        Fixation lastFixation = null;
        for (int i = 0; i < gazeBuffer.length; ++ i) {

            //Process fixation
            if (gazeBuffer[i].getFixation() != null && gazeBuffer[i].getFixation().isValid()) {

                Fixation curFixation = gazeBuffer[i].getFixation();

                //increase fixation count if it is unique
                if (!fixationIds.contains(curFixation.getId())) {
                    fixationIds.add(curFixation.getId());
                    ++numFixations;
                    fixations.add(curFixation);
                    fixationDurations.add(curFixation.getDuration());

                    if (lastFixation != null) {
                        ++numSaccades;
                        double durationOfSaccade = curFixation.getDuration() - lastFixation.getDuration();
                        Saccade saccade = new Saccade(lastFixation.getPoint(), curFixation.getPoint(), durationOfSaccade);
                        saccadeList.add(saccade);
                    }

                    lastFixation = curFixation;

                }
            }

            //Process Saccades
        }

        this.gazeMetrics = new GazeMetrics(fixations, saccadeList);
    }

    /**
     * Adds a RecXmlObject to the buffer.
     * @param recXml
     * @return The index it was inserted into is returned. -1 is returned if the buffer is full.
     */
    public int add(RecXml recXml) {
        if (this.bufferIndex < this.windowSize) {
            this.gazeBuffer[this.bufferIndex] = recXml;
            ++this.bufferIndex;
            return this.bufferIndex - 1;
        } else {
            return -1;
        }
    }

    /**
     * Internal index pointer for the array holding the gaze data
     * @return
     */
    public int getInternalIndex() {
        return this.bufferIndex;
    }

    /**
     * Clears the gazeBuffer and resets the internal position.
     */
    public void flush() {
        this.gazeBuffer = new RecXml[this.windowSize];
        this.bufferIndex = 0;
    }

    public void setGazeBuffer(RecXml[] gazeBuffer) {
        this.gazeBuffer = gazeBuffer;
    }


    /**
     * Sets the # of packets that should belong in the GazeData (non-enforcing)
     * @param windowSizeInMilliseconds
     */
    public void setWindowSizeInMilliseconds(float windowSizeInMilliseconds) {
        this.windowSizeInMilliseconds = windowSizeInMilliseconds;
        this.windowSize = (int) ((windowSizeInMilliseconds/1000) * pollingRateInHz);
    }

    public void setWindowSize(int windowSize) {
        this.windowSize = windowSize;
        this.windowSizeInMilliseconds = (float) windowSize / pollingRateInHz;
    }

    public RecXml[] getGazeBuffer() {
        return gazeBuffer;
    }

    /**
     * Returns the number of individual data points the window can hold
     * @return
     */
    public int getWindowSize() {
        return windowSize;
    }

    /**
     *
     * @return Returns size of the window in terms of milliseconds
     */
    public float getWindowSizeInMilliseconds() {
        return windowSizeInMilliseconds;
    }



    /**
     *
     * @return returns true if the buffer is full (internal index is equal to the window size)
     */
    public boolean isFull() {
        return this.bufferIndex == this.windowSize;
    }

    @Override
    public void setMediator(Mediator mediator) {
        this.mediator = (AdaptationMediator) mediator;
    }
}
