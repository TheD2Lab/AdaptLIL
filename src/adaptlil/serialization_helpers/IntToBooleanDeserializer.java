package adaptlil.serialization_helpers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

/**
 * Serializes 1 -> true and 0 -> false
 */
public class IntToBooleanDeserializer extends JsonDeserializer<Boolean> {
    @Override
    public Boolean deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        int intValue = jsonParser.getValueAsInt();
        return intValue == 1;
    }
}