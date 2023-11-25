package data_classes;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import interpolation.Interpolation;
import server.serialization_helpers.IntToBooleanDeserializer;
import wekaext.annotations.IgnoreWekaAttribute;

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

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
    public double getStartTime() {
        return startTime;
    }

    public double getDuration() {
        return duration;
    }

    public int getId() {
        return id;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setStartTime(double startTime) {
        this.startTime = startTime;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setValid(Boolean valid) {
        isValid = valid;
    }

    /**
     * Right now this is simple linear interpolation, view academic research on most accurate estimations

     * @param a
     * @param b
     * @param steps
     * @return
     */
    public Fixation[] interpolate(Fixation a, Fixation b, int steps) {
	    Fixation[] interpolFixations = new Fixation[steps];
        Interpolation interpolation = new Interpolation();
        double[] aCoords = new double[]{a.getX(), a.getY()};
        double[] bCoords = new double[]{b.getX(), b.getY()};
        double[][] interpolCoords = interpolation.interpolate(aCoords, bCoords, steps);
        double[] startTimes = interpolation.interpolate(a.getStartTime(), b.getStartTime(), steps);
        double[] durations = interpolation.interpolate(a.getDuration(), b.getDuration(), steps);
	    for (int i = 0; i < steps; ++i) {
    	    Fixation c = new Fixation();
            double[] cCoord = interpolCoords[i];
            c.setX(cCoord[0]);
            c.setY(cCoord[1]);
            c.setId(a.getId() + 1);
            c.setStartTime(startTimes[i]);
            c.setDuration(durations[i]);
            interpolFixations[i] = c;
	    }
        return interpolFixations;
    }

    @JsonIgnore
    public Point2D.Double getPoint() {
        return new Point2D.Double(this.getX(), this.getY());
    }
}
