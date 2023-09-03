package server.recvcommands;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import server.getcommands.Get_Enable_Send_Data;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

@JacksonXmlRootElement(localName = "REC")

public class RecXmlObject {

    public String name() {
        return "RecXMLOBJECt";
    }
}
