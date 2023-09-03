package server.setcommands;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "SET")
public class Set_Calibrate_Start {
    @JacksonXmlProperty(isAttribute = true, localName = "ID")
    public String id = "CALIBRATE_START";

    @JacksonXmlProperty(isAttribute = true, localName = "STATE")
    public boolean state;

    public Set_Calibrate_Start(boolean state) {
        this.state = state;
    }
}
