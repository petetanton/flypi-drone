package uk.flypi.drone.ctrl;

public class CmdRequest {

    private final String key;
    private final String value;

    public CmdRequest(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
