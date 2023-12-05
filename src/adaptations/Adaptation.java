package adaptations;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.tuple.MutableTriple;

import java.util.Map;

public abstract class Adaptation {

    private String type;
    private boolean state;
    private double timeStarted;
    private double timeModified;
    private double timeStopped;
    private Map<String, String> styleConfig;
    private boolean isBeingObservedByMediator;
    private double score;
    private double strength;
    private boolean hasFlipped = false;

    private MutableTriple<String, Integer, Double> lastStyleChangePair;

    public Adaptation(String type, boolean state, double timeStarted, double timeModified, double timeStopped, Map<String, String> styleConfig) {
        this.type = type;
        this.state = state;
        this.timeStarted = timeStarted;
        this.timeModified = timeModified;
        this.timeStopped = timeStopped;
        if (styleConfig == null)
            this.styleConfig = this.getDefaultStyleConfig();
        else
            this.styleConfig = styleConfig;
        this.score = 0;
        this.lastStyleChangePair = new MutableTriple<>();
    }

    public void setBeingObservedByMediator(boolean beingObservedByMediator) {
        isBeingObservedByMediator = beingObservedByMediator;
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

    public void setLastStyleChangePair(String styleName, int direction, double stepAmount) {
        this.lastStyleChangePair = new MutableTriple<String, Integer, Double>(styleName, direction, stepAmount);
    }



    public void setLastStyleChangePair(MutableTriple<String, Integer, Double> lastStyleChangePair) {
        this.lastStyleChangePair = lastStyleChangePair;
    }

    public double getScore() {
        return score;
    }

    public MutableTriple<String, Integer, Double> getLastStyleChange() {
        return lastStyleChangePair;
    }


    public MutableTriple<String, Integer, Double> getLastStyleChangePair() {
        return lastStyleChangePair;
    }

    public abstract Map<String, String> getDefaultStyleConfig();

    @JsonProperty("isBeingObservedByMediator")
    public boolean isBeingObservedByMediator() {
        return isBeingObservedByMediator;
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
    public Map<String, String> getStyleConfig() {
        return styleConfig;
    }

    public abstract void applyStyleChange(double stepAmount);

    public double getStrength() {
        return strength;
    }

    public void setStrength(double strength) {
        this.strength = strength;
    }

    public boolean hasFlipped() {
        return hasFlipped;
    }

    public void setHasFlipped(boolean hasFlipped) {
        this.hasFlipped = hasFlipped;
    }

    public void flipDirection() {
        this.hasFlipped = !this.hasFlipped;
    }
}
