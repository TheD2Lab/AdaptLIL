package server.serialization_helpers;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.nd4j.common.util.SerializationUtils;
import org.nd4j.linalg.api.buffer.DataType;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.cpu.nativecpu.NDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.factory.Nd4jBackend;
import org.nd4j.nativeblas.Nd4jBlas;
import org.nd4j.serde.base64.Nd4jBase64;

import java.io.IOException;

public class ByteArrayToINDArrayDeserializer extends JsonDeserializer<INDArray> {
    @Override
    public INDArray deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {

        return Nd4j.create(Nd4j.createTypedBuffer(jsonParser.getBinaryValue(), DataType.DOUBLE));
    }
}
