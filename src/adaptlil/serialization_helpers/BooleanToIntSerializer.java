package adaptlil.serialization_helpers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * Quick Serialiation class that converts true/false to 1/0 for JSON serialization
 */
public class BooleanToIntSerializer extends JsonSerializer<Boolean> {

    @Override
    public void serialize(Boolean aBoolean, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        int intValue = aBoolean ? 1 : 0;
        jsonGenerator.writeNumber(intValue);

    }
}
