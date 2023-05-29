package fx.app.hopitaldirectorysystem;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ExportDirectory implements Serializable {
    private List<Integer> number;
    private List<String> name;
    private List<String> phone;
    private List<String> date_reg;
    private List<String> clinic_loc;

    private List<String> category;
    private List<String> doc_comment;

    public ExportDirectory(List<Integer> number, List<String> name, List<String> phone, List<String> date_reg, List<String> clinic_loc, List<String> category, List<String> doc_comment) {
        this.number = number;
        this.name = name;
        this.phone = phone;
        this.date_reg = date_reg;
        this.clinic_loc = clinic_loc;
        this.category = category;
        this.doc_comment = doc_comment;
    }

    public List<Integer> getNumber() {
        return number;
    }

    public void setNumber(List<Integer> number) {
        this.number = number;
    }

    public List<String> getName() {
        return name;
    }

    public void setName(List<String> name) {
        this.name = name;
    }

    public List<String> getPhone() {
        return phone;
    }

    public void setPhone(List<String> phone) {
        this.phone = phone;
    }

    public List<String> getDate_reg() {
        return date_reg;
    }

    public void setDate_reg(List<String> date_reg) {
        this.date_reg = date_reg;
    }

    public List<String> getClinic_loc() {
        return clinic_loc;
    }

    public void setClinic_loc(List<String> clinic_loc) {
        this.clinic_loc = clinic_loc;
    }

    public List<String> getCategory() {
        return category;
    }

    public void setCategory(List<String> category) {
        this.category = category;
    }

    public List<String> getDoc_comment() {
        return doc_comment;
    }

    public void setDoc_comment(List<String> doc_comment) {
        this.doc_comment = doc_comment;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
    }
}
