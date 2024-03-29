package adaptovis.serialization_helpers;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.nd4j.linalg.api.buffer.DataBuffer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class INDArraySerializer extends JsonSerializer<INDArray> {

    byte[] toByteArray(double val) {
        byte[] bytes = new byte[8];
        ByteBuffer.wrap(bytes).putDouble(val);
        return bytes;
    }
    byte[] toByteArray(double[] vals) {
        byte[] byteVals = new byte[vals.length * 8];
        int k = 0;
        for (int i = 0; i < vals.length; ++i) {
            byte[] valToBytes = toByteArray(vals[i]);
            for (int j = 0; j < valToBytes.length; ++j) {

                byteVals[k] = valToBytes[j];
                ++k;
            }
        }
        return byteVals;
    }
    /**
     * Converts an INDArray to a byte array for serialization by Jackson
     * @param indArray Value to serialize; can <b>not</b> be null.
     * @param jsonGenerator Generator used to output resulting Json content
     * @param serializerProvider Provider that can be used to get serializers for
     *   serializing Objects value contains, if any.
     * @throws IOException
     */
    @Override
    public void serialize(INDArray indArray, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

        //Flatten to send as 1D byte array
        indArray = Nd4j.toFlattened('C', indArray);
        //Grab internal data (hidden by JVM so must use ND4J utils)
        DataBuffer internalDataBuffer = indArray.data();
        System.out.println(internalDataBuffer.dataType().toString());
        System.out.println(internalDataBuffer.toString());
        //DAta mismatch, causing overflow. Verify the type
        //byte[] byteArray = internalDataBuffer.asBytes();
        double[] doubles = internalDataBuffer.asDouble();
        System.out.println(Arrays.toString(doubles));
        byte[] bytes = this.toByteArray(doubles);

        jsonGenerator.writeArray(doubles, 0, doubles.length);

    }
}
