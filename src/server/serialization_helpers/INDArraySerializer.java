package server.serialization_helpers;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.nd4j.common.util.SerializationUtils;
import org.nd4j.linalg.api.ndarray.INDArray;

import java.io.IOException;
import java.io.OutputStream;

public class INDArraySerializer extends JsonSerializer<INDArray> {

    @Override
    public void serialize(INDArray indArray, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        byte[] byteArray = SerializationUtils.toByteArray(indArray);
        jsonGenerator.writeObject(byteArray);
    }
}
