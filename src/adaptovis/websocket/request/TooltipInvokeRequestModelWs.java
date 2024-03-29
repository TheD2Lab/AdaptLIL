package adaptovis.websocket.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TooltipInvokeRequestModelWs extends InvokeRequestModelWs {
    @JsonProperty("elementIds")
    public String[] elementIds;

    public TooltipInvokeRequestModelWs(String[] elementIds) {
        super("tooltip");
        this.elementIds = elementIds;
    }
}
