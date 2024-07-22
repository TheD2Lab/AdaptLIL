package adaptlil.adaptations;

import java.util.AbstractMap;
import java.util.Map;

/**
 * ColorAdaptation is an example of an Adaptation with no set strength value. Instead it relies on styleConfigs to invoke
 * two set CSS styles
 */
public class ColorAdaptation extends Adaptation{
    public ColorAdaptation(boolean state, Map<String, String> styleConfig) {
        super("color", state, styleConfig, 0.0);
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
