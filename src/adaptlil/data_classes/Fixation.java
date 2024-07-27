package adaptlil.data_classes;


import adaptlil.annotations.IgnoreWekaAttribute;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import adaptlil.interpolation.Interpolation;
import adaptlil.serialization_helpers.IntToBooleanDeserializer;

import java.awt.geom.Point2D;

public class Fixation {
    @JacksonXmlProperty(isAttribute = true, localName = "FPOGX")
    private double x;
    @JacksonXmlProperty(isAttribute = true, localName = "FPOGY")
    private double y;
    @JacksonXmlProperty(isAttribute = true, localName = "FPOGS")
    private double startTime;
    @JacksonXmlProperty(isAttribute = true, localName = "FPOGD")
    private double duration;

    @IgnoreWekaAttribute
    @JacksonXmlProperty(isAttribute = true, localName = "FPOGID")
    private int id;

    @IgnoreWekaAttribute
    @JsonDeserialize(using = IntToBooleanDeserializer.class)
    @JacksonXmlProperty(isAttribute = true, localName = "FPOGV")
    private boolean isValid;

    public Fixation() {} //Default constructor for Jackson

    public Fixation(double x, double y, double startTime, double duration, boolean isValid, int id) {
        this.x = x;
        this.y = y;
        this.startTime = startTime;
        this.duration = duration;
        this.isValid = isValid;
        this.id = id;
    }

    public Fixation(double x, double y, double startTime, double duration, boolean isValid) {
        this.x = x;
        this.y = y;
        this.startTime = startTime;
        this.duration = duration;
        this.isValid = isValid;
        this.id = -1;
    }

    /**
     *
     * @param x The x coordinate of the fixation (fraction of screen width)
     * @param y The y coordinate of the fixation (fraction of screen height)
     * @param duration Duration of the fixation
     */
    public Fixation(double x, double y, double duration) {
        this.x = x;
        this.y = y;
        this.duration = duration;
    }

    /**
     * Constructs a fixation object through specification of the array indices of the fixationLine argument
     * @param durationColIndex
     * @param xColIndex
     * @param yColIndex
     * @param idColIndex
     * @param timestampColIndex
     * @param fixationLine Array of strings representing a single row in a CSV file
     * @return
     */
    public static Fixation getFixationFromCSVLine(int durationColIndex, int xColIndex, int yColIndex, int idColIndex, int timestampColIndex, String[] fixationLine) {
        float duration = Float.parseFloat(fixationLine[durationColIndex]);
        float x = Float.parseFloat(fixationLine[xColIndex]);
        float y = Float.parseFloat(fixationLine[yColIndex]);
        int id = Integer.parseInt(fixationLine[idColIndex]);
        float timestamp = Float.parseFloat(fixationLine[timestampColIndex]);
        return new Fixation(x, y, timestamp, duration, true, id);
    }

    /**
     * Performs Linear interpolation nSteps Fixation elements between the first Fixation and the next Fixation
     * @param firstFixation
     * @param nextFixation
     * @param nSteps
     * @return
     */
    public Fixation[] interpolate(Fixation firstFixation, Fixation nextFixation, int nSteps) {
        Fixation[] interpolFixations = new Fixation[nSteps];
        Interpolation interpolation = new Interpolation();
        double[] aCoords = new double[]{firstFixation.getX(), firstFixation.getY()};
        double[] bCoords = new double[]{nextFixation.getX(), nextFixation.getY()};
        double[][] interpolCoords = interpolation.interpolate(aCoords, bCoords, nSteps);
        double[] startTimes = interpolation.interpolate(firstFixation.getStartTime(), nextFixation.getStartTime(), nSteps);
        double[] durations = interpolation.interpolate(firstFixation.getDuration(), nextFixation.getDuration(), nSteps);
        for (int i = 0; i < nSteps; ++i) {
            Fixation c = new Fixation();
            double[] cCoord = interpolCoords[i];
            c.setX(cCoord[0]);
            c.setY(cCoord[1]);
            c.setId(firstFixation.getId() + 1);
            c.setStartTime(startTimes[i]);
            c.setDuration(durations[i]);
            interpolFixations[i] = c;
        }
        return interpolFixations;
    }


    /**
     * Get the fixation in terms of a 2D-Cartesian point
     * @return
     */
    @JsonIgnore
    public Point2D.Double getPoint() {
        return new Point2D.Double(this.getX(), this.getY());
    }

    /**
     * Get x-coordinate of the fixation in terms of point on the screen.
     * @return Returns x coordinate in range [0-1] where 0.5 is the middle of the screen on the x-axis
     */
    public double getX() {
        return x;
    }


    /**
     * Get y-coordinate of the fixation in terms of point on the screen.
     * @return Returns y coordinate in range [0-1] where 0.5 is the middle of the screen on the y-axis
     */
    public double getY() {
        return y;
    }


    /**
     * Returns the startTime of the fixation
     * @return
     */
    public double getStartTime() {
        return startTime;
    }


    /**
     * Returns the duration of the fixation
     * @return
     */
    public double getDuration() {
        return duration;
    }


    /**
     * Returns the id of the fixation (default -1)
     * @return
     */
    public int getId() {
        return id;
    }


    /**
     * Flag set by gazepoint to signify that this Fixation packet may be corrupt.
     * @return
     */
    public boolean isValid() {
        return isValid;
    }


    /**
     * Set X coordinate (fraction of screen-width)
     * @param x
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Set y coordinate (fraction of screen-height)
     * @param y
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Setter for the StartTime of the fixation
     * @param startTime
     */
    public void setStartTime(double startTime) {
        this.startTime = startTime;
    }


    /**
     * Setter for the duration of the fixation
     * @param duration
     */
    public void setDuration(double duration) {
        this.duration = duration;
    }

    /**
     * Setter for the id of the fixation
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }


    /**
     * Setter for the isValid flag (signifies if the packet is corrupt)
     * @param valid
     */
    public void setValid(Boolean valid) {
        isValid = valid;
    }

}
