package adaptations;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

public class ColorAdaptation extends Adaptation{
    public ColorAdaptation(boolean state, double timeStarted, double timeModified, double timeStopped, Map<String, String> styleConfig) {
        super("color", state, timeStarted, timeModified, timeStopped, styleConfig, 0.0);
    }

    @Override
    public Map<String, String> getDefaultStyleConfig() {
        //  'map_to_hidden_color': '#FF0000',
        //            'map_to_not_hidden_color' : '#00FFFF',
        return Map.ofEntries(
                new AbstractMap.SimpleEntry<>("map_to_hidden_color", "#FF0000"),
                new AbstractMap.SimpleEntry<>("map_to_not_hidden_Color", "#00FFFF")
        );
    }



}
