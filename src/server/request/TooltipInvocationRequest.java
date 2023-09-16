package server.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TooltipInvocationRequest extends InvocationRequest {
    @JsonProperty("elementIds")
    public String[] elementIds;

    public TooltipInvocationRequest(String[] elementIds, String name) {
        super(name);
        this.elementIds = elementIds;
    }
}
