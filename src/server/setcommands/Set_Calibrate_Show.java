package server.setcommands;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "SET")
public class Set_Calibrate_Show {
    @JacksonXmlProperty(isAttribute = true, localName = "ID")
    public String id = "CALIBRATE_SHOW";
    @JacksonXmlProperty(isAttribute = true, localName = "STATE")
    public boolean state;

    public Set_Calibrate_Show(boolean state) {
        this.state = state;
    }
}
