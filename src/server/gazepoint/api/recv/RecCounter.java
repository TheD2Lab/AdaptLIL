package server.gazepoint.api.recv;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;


public class RecCounter extends RecXmlObject{
    @JacksonXmlProperty(isAttribute = true, localName = "CNT")
    public Integer count;

    public String name() {
        return "RecCounter";
    }
}
