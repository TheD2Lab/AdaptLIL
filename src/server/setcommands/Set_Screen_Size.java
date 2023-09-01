package server.setcommands;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "SET")
public class Set_Screen_Size {
    @JacksonXmlProperty(isAttribute = true, localName = "ID")
    public String id = "SCREEN_SIZE";
}
