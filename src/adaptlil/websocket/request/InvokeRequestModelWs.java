package adaptlil.websocket.request;


import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * {
 *     'type': 'invoke,
 *     'name': this.name
 * }
 */
public class InvokeRequestModelWs extends RequestModelWs {
    @JsonProperty("name")
    public String name;

    public InvokeRequestModelWs(String name) {
        super("invoke");
        this.name = name;
    }
}
