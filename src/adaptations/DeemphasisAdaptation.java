package adaptations;

import org.apache.commons.lang3.tuple.MutablePair;

import java.util.HashMap;
import java.util.Map;

public class DeemphasisAdaptation extends Adaptation {
    public DeemphasisAdaptation(boolean state, double timeStarted, double timeModified, double timeStopped, Map<String, String> styleConfig) {
        super("deemphasis", state, timeStarted, timeModified, timeStopped, styleConfig);
    }

    public Map<String, String> getDefaultStyleConfig() {
        Map<String, String> defaultStyleConfig = new HashMap<>();
        defaultStyleConfig.put("opacity", "0.25");

        return defaultStyleConfig;
    }

    @Override
    public void applyStyleChange(int direction, double stepAmount) {
        float currentOpacity = Float.parseFloat(this.getStyleConfig().get("opacity"));
        float maxOpacity = 1;
        float minOpacity = 0;
        if (direction > 0) {
            currentOpacity += (float) (stepAmount * maxOpacity);
            if (currentOpacity > maxOpacity)
                currentOpacity = maxOpacity;
        } else {
            currentOpacity -= (float) (stepAmount * maxOpacity);
            if (currentOpacity < minOpacity)
                currentOpacity = minOpacity;
        }

        this.getDefaultStyleConfig().put("opacity", String.valueOf(currentOpacity));
        this.setLastStyleChange(new MutablePair<>("opacity", String.valueOf(currentOpacity)));
    }


}
