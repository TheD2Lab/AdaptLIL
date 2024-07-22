package adaptlil.adaptations;

import java.util.Map;

public class HighlightingAdaptation extends Adaptation {

    public HighlightingAdaptation(boolean state, Map<String, String> styleConfig, double strength) {
        super("highlighting", state, styleConfig, strength);
    }

    @Override
    public Map<String, String> getDefaultStyleConfig() {
        //.highlight text: font-weigh max=900, min == baseline == 10, initial = 400
        return null;
    }




}
