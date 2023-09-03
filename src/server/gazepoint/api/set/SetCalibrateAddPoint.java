package server.gazepoint.api.set;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import server.GazeApiCommands;

@JacksonXmlRootElement(localName = "SET")
public class SetCalibrateAddPoint extends SetCommand {

    @JacksonXmlProperty(isAttribute = true, localName = "X")
    public double x;
    @JacksonXmlProperty(isAttribute = true, localName = "Y")
    public double y;

    public SetCalibrateAddPoint(double x, double y) {
        super(GazeApiCommands.CALIBRATE_ADDPOINT);
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
