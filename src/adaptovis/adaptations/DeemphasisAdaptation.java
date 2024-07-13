package adaptovis.adaptations;

import java.util.HashMap;
import java.util.Map;

public class DeemphasisAdaptation extends Adaptation {
    public DeemphasisAdaptation(boolean state, Map<String, String> styleConfig, double strength) {
        super("deemphasis", state, styleConfig, strength);
    }

    public Map<String, String> getDefaultStyleConfig() {
        Map<String, String> defaultStyleConfig = new HashMap<>();
        defaultStyleConfig.put("opacity", "0.25");

        return defaultStyleConfig;
    }

}
