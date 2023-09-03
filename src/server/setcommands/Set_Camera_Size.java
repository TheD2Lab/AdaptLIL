package server.setcommands;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "SET")
public class Set_Camera_Size {
    @JacksonXmlProperty(isAttribute = true, localName = "ID")
    public String id = "CAMERA_SIZE";
}
