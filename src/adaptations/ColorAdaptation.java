package adaptations;

import java.util.Map;

public class ColorAdaptation extends Adaptation{
    public ColorAdaptation(boolean state, double timeStarted, double timeModified, double timeStopped, Map<String, String> styleConfig) {
        super("color", state, timeStarted, timeModified, timeStopped, styleConfig);
    }

    @Override
    public Map<String, String> getDefaultStyleConfig() {
        return null;
    }

    @Override
    public void applyStyleChange(int direction, double stepAmount) {

        //Step amount likely to result in a color wheel shift.
    }
}
