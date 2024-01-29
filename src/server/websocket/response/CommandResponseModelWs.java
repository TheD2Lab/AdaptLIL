package server.websocket.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CommandResponseModelWs extends ResponseModelWs {

    /**
     *      'type': 'command',
     *             'name': 'record',
     *             'time': (new Date()).getMilliseconds(),
     *             'action': action
     */
    @JsonProperty("name")
    protected String name;
    @JsonProperty("action")
    protected String action;
    @JsonProperty("time")
    protected long time;
    public CommandResponseModelWs() {}

    public CommandResponseModelWs(String type, String name, String action, long time) {
        super(type);
        this.name = name;
        this.action = action;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
