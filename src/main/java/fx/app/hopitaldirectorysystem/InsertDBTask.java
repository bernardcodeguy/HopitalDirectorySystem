package fx.app.hopitaldirectorysystem;

import javafx.concurrent.Task;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class InsertDBTask extends Task<Integer> {
    private final String name,phone,regDate,clinicLoc,category,docComments;

    public InsertDBTask(String name, String phone, String regDate, String clinicLoc, String category, String docComments) {
        this.name = name;
        this.phone = phone;
        this.regDate = regDate;
        this.clinicLoc = clinicLoc;
        this.category = category;
        this.docComments = docComments;
    }

    @Override
    protected Integer call() throws Exception {

        Connection con = ConnectionManager.getConnection();

        String sql = "INSERT INTO directory(patient_name,phone,date_registration,clinicLoc,category,doc_comment) VALUES(?,?,?,?,?,?)";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setString(1,name);
        ps.setString(2,phone);
        ps.setString(3,regDate);
        ps.setString(4,clinicLoc);
        ps.setString(5,category);
        ps.setString(6,docComments);


        int row = ps.executeUpdate();

        return row;
    }
}
