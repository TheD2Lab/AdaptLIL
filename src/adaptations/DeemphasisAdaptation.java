package adaptations;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.MutableTriple;

import java.util.HashMap;
import java.util.Map;

public class DeemphasisAdaptation extends Adaptation {
    public DeemphasisAdaptation(boolean state, double timeStarted, double timeModified, double timeStopped, Map<String, String> styleConfig, double strength) {
        super("deemphasis", state, timeStarted, timeModified, timeStopped, styleConfig, strength);
    }

    public Map<String, String> getDefaultStyleConfig() {
        Map<String, String> defaultStyleConfig = new HashMap<>();
        defaultStyleConfig.put("opacity", "0.25");

        return defaultStyleConfig;
    }

//    @Override
//    public void applyStyleChange(double stepAmount) {
//        float currentOpacity = Float.parseFloat(this.getStyleConfig().get("opacity"));
//        float maxOpacity = 1;
//        float minOpacity = 0;
//        int direction = this.hasFlipped() ? -1 : 1;
//        if (direction > 0) {
//            currentOpacity += (float) (stepAmount * maxOpacity);
//            if (currentOpacity > maxOpacity)
//                currentOpacity = maxOpacity;
//        } else {
//            currentOpacity -= (float) (stepAmount * maxOpacity);
//            if (currentOpacity < minOpacity)
//                currentOpacity = minOpacity;
//        }
//
//        this.setStrength(currentOpacity);
//        this.getStyleConfig().put("opacity", String.valueOf(currentOpacity));
//        this.setLastStyleChangePair("opacity", direction, currentOpacity);
//    }



}
