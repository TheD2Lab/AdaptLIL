package server.setcommands;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "SET")
public class Set_Tracker_Display {
    @JacksonXmlProperty(isAttribute = true, localName = "ID")
    public String id = "TRACKER_DISPLAY";
}
