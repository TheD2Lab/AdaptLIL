package server.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TooltipInvokeRequest extends InvokeRequest {
    @JsonProperty("elementIds")
    public String[] elementIds;

    public TooltipInvokeRequest(String[] elementIds) {
        super("tooltip");
        this.elementIds = elementIds;
    }
}
