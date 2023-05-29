package fx.app.hopitaldirectorysystem;

import javafx.concurrent.Task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SearchTask extends Task<List<Directory>> {
    private final String patient_name;

    public SearchTask(String patient_name) {
        this.patient_name = patient_name;
    }

    @Override
    protected List<Directory> call() throws Exception {

        Connection con = ConnectionManager.getConnection();

        String sql = "SELECT * FROM directory WHERE patient_name LIKE ?";
        String pattern = "%" + patient_name + "%"; // add wildcards to search for any occurrence of the searchName string

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1,pattern);

        ResultSet rs = ps.executeQuery();

        List<Directory> list = new ArrayList<>();


        while(rs.next()){
            Directory d = new Directory();

            d.setNumber(rs.getInt(1));
            d.setName(rs.getString(2));
            d.setPhone(rs.getString(3));
            d.setDate_reg(rs.getString(4));
            d.setClinic_loc(rs.getString(5));
            d.setCategory(rs.getString(6));
            d.setDoc_comment(rs.getString(7));

            list.add(d);

        }

        return list;

    }
}
