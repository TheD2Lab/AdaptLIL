package adaptovis.gazepoint.api.set;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import adaptovis.gazepoint.api.GazeApiCommands;

@JacksonXmlRootElement(localName = "SET")
public class SetCalibrateDelay extends SetCommand {

    @JacksonXmlProperty(isAttribute = true, localName = "VALUE")
    public Float value;

    public SetCalibrateDelay(Float value) {
        super(GazeApiCommands.CALIBRATE_DELAY);
        this.value = value;
    }

    public Float getValue() {
        return value;
    }
}
