package server.setcommands;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "SET")
public class Set_Calibrate_AddPoint {
    @JacksonXmlProperty(isAttribute = true, localName = "ID")
    public String id = "CALIBRATE_ADDPOINT";

    @JacksonXmlProperty(isAttribute = true, localName = "X")
    public double x;
    @JacksonXmlProperty(isAttribute = true, localName = "Y")
    public double y;

    public Set_Calibrate_AddPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }
}
