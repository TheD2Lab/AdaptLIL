package adaptlil.adaptations;

import java.util.HashMap;
import java.util.Map;

/**
 * The Deemphasis adaptation reduces opacity of surrounding elements. As such, the only 'style' it has in its config is the
 * CSS attribute, opacity.
 */
public class DeemphasisAdaptation extends Adaptation {
    public DeemphasisAdaptation(boolean state, Map<String, String> styleConfig, double strength) {
        super("deemphasis", state, styleConfig, strength);
    }

    @Override
    public void applyStyleChange(double stepAmount) {
        if (!this.hasFlipped())
            this.setStrength(this.getStrength() + stepAmount);
        else
            this.setStrength(this.getStrength() - stepAmount);
    }

    public Map<String, String> getDefaultStyleConfig() {
        Map<String, String> defaultStyleConfig = new HashMap<>();
        defaultStyleConfig.put("opacity", "0.25");

        return defaultStyleConfig;
    }


}
