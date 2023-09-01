package server.getcommands;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "GET")
public class Get_Camera_Size {
    @JacksonXmlProperty(isAttribute = true, localName = "ID")
    public String id = "CAMERA_SIZE";
}
