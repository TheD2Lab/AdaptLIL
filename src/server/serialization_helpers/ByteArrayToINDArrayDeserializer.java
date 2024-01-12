package server.serialization_helpers;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.nd4j.common.util.SerializationUtils;
import org.nd4j.linalg.api.ndarray.INDArray;

import java.io.IOException;

public class ByteArrayToINDArrayDeserializer extends JsonDeserializer<INDArray> {
    @Override
    public INDArray deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        return SerializationUtils.fromByteArray(jsonParser.getBinaryValue());
    }
}
