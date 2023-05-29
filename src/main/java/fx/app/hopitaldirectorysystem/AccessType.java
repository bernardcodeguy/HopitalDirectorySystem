package fx.app.hopitaldirectorysystem;

public class AccessType {
    private String access_type;

    private static final AccessType instance = new AccessType();


    public static AccessType getInstance(){
        return instance;
    }

    public String getAccess_type() {
        return access_type;
    }

    public void setAccess_type(String access_type) {
        this.access_type = access_type;
    }
}
