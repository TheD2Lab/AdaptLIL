package server.gazepoint.api.recv;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "REC")

public class RecXmlObject {

    public String name() {
        return "RecXMLOBJECt";
    }
}