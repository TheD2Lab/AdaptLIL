package server.getcommands;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "GET")
public class Get_Time_Tick_Frequency {
    @JacksonXmlProperty(isAttribute = true, localName = "ID")
    public String id = "TIME_TICK_FREQUENCY";
}
