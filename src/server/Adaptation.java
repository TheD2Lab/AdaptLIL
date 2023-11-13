package server;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class Adaptation {

    private String type;
    private boolean state;
    private double timeStarted;
    private double timeModified;
    private double timeStopped;
    private Map<String, String> styleConfig;

    private double score;

    public Adaptation(String type, boolean state, double timeStarted, double timeModified, double timeStopped, Map<String, String> styleConfig) {
        this.type = type;
        this.state = state;
        this.timeStarted = timeStarted;
        this.timeModified = timeModified;
        this.timeStopped = timeStopped;
        this.styleConfig = styleConfig;
        this.score = 0;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public void setTimeStarted(double timeStarted) {
        this.timeStarted = timeStarted;
    }

    public void setStyleConfig(Map<String, String> styleConfig) {
        this.styleConfig = styleConfig;
    }

    public void setTimeStopped(double timeStopped) {
        this.timeStopped = timeStopped;
    }

    public void setTimeModified(double timeModified) {
        this.timeModified = timeModified;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public double getScore() {
        return score;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("state")
    public boolean isState() {
        return state;
    }

    @JsonProperty("timeStarted")
    public double getTimeStarted() {
        return timeStarted;
    }

    @JsonProperty("timeModified")
    public double getTimeModified() {
        return timeModified;
    }

    @JsonProperty("timeStopped")
    public double getTimeStopped() {
        return timeStopped;
    }

    @JsonProperty("styleConfig")
    @JsonAnyGetter
    public Map<String, String> getStyleConfig() {
        return styleConfig;
    }
}
