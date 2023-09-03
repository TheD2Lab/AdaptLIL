package server.getcommands;


import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * https://dzone.com/articles/jaxb-and-inhertiance-using
 */
@JacksonXmlRootElement(localName = "GET")

public class Enable_Send_Command {
    @JacksonXmlProperty(isAttribute = true, localName = "ID")
    public String id;
    @JacksonXmlProperty(isAttribute = true, localName ="STATE")
    public boolean state;


    public Enable_Send_Command(String id, boolean state) {
        this.id = id;
        this.state = state;
    }
}
