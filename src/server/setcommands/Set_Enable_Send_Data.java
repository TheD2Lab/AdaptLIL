package server.setcommands;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "SET")
public class Set_Enable_Send_Data {
    /**
     *
     * @param state
     */
    public Set_Enable_Send_Data(boolean state) {
        this.state = state;
    }

    @JacksonXmlProperty(isAttribute = true, localName = "ID")
    public String id = "ENABLE_SEND_DATA";

    @JacksonXmlProperty(isAttribute = true, localName = "STATE")
    public boolean state;
}
