package server;

import org.deeplearning4j.nn.conf.graph.ElementWiseVertex;
import org.deeplearning4j.nn.modelimport.keras.exceptions.InvalidKerasConfigurationException;
import org.deeplearning4j.nn.modelimport.keras.exceptions.UnsupportedKerasConfigurationException;
import org.deeplearning4j.nn.modelimport.keras.layers.core.KerasMerge;

import java.util.Map;

public class KerasAddLayer extends KerasMerge {
    public KerasAddLayer(Map<String, Object> layerConfig) throws InvalidKerasConfigurationException, UnsupportedKerasConfigurationException {
        super(layerConfig, ElementWiseVertex.Op.Add, false);
    }
}
