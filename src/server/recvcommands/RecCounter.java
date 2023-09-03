package server.recvcommands;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;


public class RecCounter extends RecXmlObject{
    @JacksonXmlProperty(isAttribute = true, localName = "CNT")
    public Integer count;

    public String name() {
        return "RecCounter";
    }
}
