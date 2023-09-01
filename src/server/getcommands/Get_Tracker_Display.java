package server.sendcommands;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "GET")
public class Get_Tracker_Display {
    @JacksonXmlProperty(isAttribute = true, localName = "ID")
    public String id = "TRACKER_DISPLAY";
}
