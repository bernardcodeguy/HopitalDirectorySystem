package fx.app.hopitaldirectorysystem;

public class Directory {
    private int number;
    private String name;
    private String phone;
    private String date_reg;
    private String clinic_loc;

    private String category;
    private String doc_comment;

    public Directory(){

    }

    public Directory(int number, String name, String phone, String date_reg, String clinic_loc, String doc_comment) {
        this.number = number;
        this.name = name;
        this.phone = phone;
        this.date_reg = date_reg;
        this.clinic_loc = clinic_loc;
        this.doc_comment = doc_comment;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDate_reg() {
        return date_reg;
    }

    public void setDate_reg(String date_reg) {
        this.date_reg = date_reg;
    }

    public String getClinic_loc() {
        return clinic_loc;
    }

    public void setClinic_loc(String clinic_loc) {
        this.clinic_loc = clinic_loc;
    }

    public String getDoc_comment() {
        return doc_comment;
    }

    public void setDoc_comment(String doc_comment) {
        this.doc_comment = doc_comment;
    }
}
