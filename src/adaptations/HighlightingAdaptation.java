package adaptations;

import java.util.Map;

public class HighlightingAdaptation extends Adaptation {

    public HighlightingAdaptation(boolean state, double timeStarted, double timeModified, double timeStopped, Map<String, String> styleConfig) {
        super("highlighting", state, timeStarted, timeModified, timeStopped, styleConfig);
    }

    @Override
    public Map<String, String> getDefaultStyleConfig() {
        return null;
    }

    @Override
    public void applyStyleChange(double stepAmount) {

    }


}
