package adaptlil.adaptations;

import java.util.Map;

/**
 * The highlighting adaptation emphasizes and increases visual prominence of elements. As such it only needs
 * a strength value and the frontend visualization is responsible for its behavior
 */
public class HighlightingAdaptation extends Adaptation {

    public HighlightingAdaptation(boolean state, Map<String, String> styleConfig, double strength) {
        super("highlighting", state, styleConfig, strength);
    }

    @Override
    public Map<String, String> getDefaultStyleConfig() {
        //.highlight text: font-weigh max=900, min == baseline == 10, initial = 400
        return null;
    }

    @Override
    public void applyStyleChange(double stepAmount) {
        if (!this.hasFlipped())
            this.setStrength(this.getStrength() + stepAmount);
        else
            this.setStrength(this.getStrength() - stepAmount);
    }


}
