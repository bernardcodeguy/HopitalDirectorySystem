package fx.app.hopitaldirectorysystem;

import javafx.concurrent.Task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AccessTask extends Task<Boolean> {
    private final String pin;
    private final String accesstype;

    public AccessTask(String pin, String accesstype) {
        this.pin = pin;
        this.accesstype = accesstype;
    }

    @Override
    protected Boolean call() throws Exception {


        Connection con = ConnectionManager.getConnection();

        String sql = "SELECT * FROM access WHERE user_type=? AND pin=?";
        PreparedStatement ps = con.prepareStatement(sql);

        ps.setString(1,accesstype);
        ps.setString(2,pin);

        ResultSet rs = ps.executeQuery();

        while(rs.next()){
            return true;
        }

        return false;
    }
}
