package server.ackcommands;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "ACK")
public class Ack_Enable_Send_Data {
    @JacksonXmlProperty(isAttribute = true, localName = "ID")
    public String id = "ENABLE_SEND_DATA";
    @JacksonXmlProperty(isAttribute = true, localName ="STATE")
    public boolean state;

    public Ack_Enable_Send_Data(boolean state) {
        this.state = state;
    }
}
