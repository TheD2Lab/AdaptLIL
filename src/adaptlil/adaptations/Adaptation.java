package adaptlil.adaptations;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 * Abstract class for Adaptations. When making an adaptation in the AdaptLIL Architecture, following this abstract
 * class structure allows for an easier implementation with the rest of the system.
 */
public abstract class Adaptation {

    private String type;
    private boolean state;

    private Map<String, String> styleConfig;
    private boolean isBeingObservedByMediator;
    private double score;
    private double strength;
    private boolean hasFlipped = false;


    /**
     *
     * @param type
     * @param state
     * @param styleConfig A map containing CSS style name and it's corresponding string value.
     * @param strength
     */
    public Adaptation(String type, boolean state, Map<String, String> styleConfig, double strength) {
        this.type = type;
        this.state = state;
        if (styleConfig == null)
            this.styleConfig = this.getDefaultStyleConfig();
        else
            this.styleConfig = styleConfig;
        this.score = 0;
        this.strength = strength;
    }

    /**
     * Used by mediator to flag that the Adaptation is currently selected and under observation for changes
     * @param beingObservedByMediator
     */
    public void setBeingObservedByMediator(boolean beingObservedByMediator) {
        isBeingObservedByMediator = beingObservedByMediator;
    }

    public void flipDirection() {
        this.hasFlipped = !this.hasFlipped;
    }

    /**
     * Applies style change as specified by the stepAmount
     * @param stepAmount
     */
    public abstract void applyStyleChange(double stepAmount);

    public void setType(String type) {
        this.type = type;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public double getScore() {
        return score;
    }

    public abstract Map<String, String> getDefaultStyleConfig();

    /**
     *
     * @return
     */
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

    @JsonProperty("styleConfig")
    public Map<String, String> getStyleConfig() {
        return styleConfig;
    }

    @JsonProperty("strength")
    public double getStrength() {
        return strength;
    }

    public void setStrength(double strength) {
        this.strength = strength;
    }

    public boolean hasFlipped() {
        return hasFlipped;
    }


}
