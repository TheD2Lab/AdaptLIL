package adaptlil;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EnvironmentConfig {

    @JsonProperty
    public String PYTHON_SERVER_URL = "localhost";
    @JsonProperty
    public int PYTHON_SERVER_PORT = 5000;
    @JsonProperty
    public String JAVA_URL = "localhost";
    @JsonProperty
    public int JAVA_PORT = 8080;
    @JsonProperty
    public float GAZE_WINDOW_SIZE_IN_MILLISECONDS = 1000;
    @JsonProperty
    public int EYETRACKER_REFRESH_RATE;
    @JsonProperty
    public int GAZE_CHUNKS_FOR_TIME_SERIES_INPUT = 2;
    @JsonProperty
    public String DEEP_LEARNING_MODEL_NAME = "transformer_model_channels.h5";
    @JsonProperty
    public String EYETRACKER_URL = "localhost";
    @JsonProperty
    public int EYETRACKER_PORT = 4242;
    @JsonProperty
    public boolean SIMULATE_GAZE_SERVER = true;




}
