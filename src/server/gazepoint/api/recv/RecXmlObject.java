package server.gazepoint.api.recv;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * <REC CNT="238465" FPOGX="0.18760" FPOGY="0.78100" FPOGS="1603.46423" FPOGD="0.08008" FPOGID="2078" FPOGV="1" BKID="0" BKDUR="0.00000" BKPMIN="15" DIAL="0.00000" DIALV="0" HR="0.00000" HRV="0" />
 * Looks ike gaze api doesnt send different rec templates where we have to map.
 * It sends one single rec element with unique attribute ids for each command set.
 */
@JacksonXmlRootElement(localName = "REC")

public class RecXmlObject {

    private RecCounter recCounter;

    private RecFixationPOG recFixationPOG;
    private RecTime recTime;
    private RecTimeTick rectimeTick;
}
