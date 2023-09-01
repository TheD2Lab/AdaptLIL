package server.setcommands;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "SET")
public class Set_Enable_Send_User_Data {
    @JacksonXmlProperty(isAttribute = true, localName = "ID")
    public String id = "ENABLE_SEND_USER_DATA";
}
