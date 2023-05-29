package fx.app.hopitaldirectorysystem;

import java.io.Serializable;

public class LoginInfo implements Serializable {
    private String access_type;
    private String pin;

    public String getAccess_type() {
        return access_type;
    }

    public void setAccess_type(String access_type) {
        this.access_type = access_type;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }
}
