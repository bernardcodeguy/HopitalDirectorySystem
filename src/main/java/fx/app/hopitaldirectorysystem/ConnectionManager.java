package fx.app.hopitaldirectorysystem;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConnectionManager {

    public static Connection getConnection() throws SQLException {
        String dbName = "app.db";
        File theDir = new File(System.getProperty("user.home") + File.separator + "Documents" + File.separator + "Hospital_Directory" + File.separator);
        if (!theDir.exists()){
            theDir.mkdirs();
        }
        String path = System.getProperty("user.home") + File.separator + "Documents" + File.separator + "Hospital_Directory" + File.separator;
        String url = "jdbc:sqlite:" +path+dbName;
        Connection connection = DriverManager.getConnection(url);
        return connection;
    }


    public static String testConnection() throws SQLException {
        Connection connection = null;
        String message = "";
        try {
            connection = ConnectionManager.getConnection();

            if(!connection.isClosed()){

                message = "Connection Success";
                connection.close();
            }

        } catch (Exception e) {
            message = "Connection Success";
            //connection.close();
        }

        return message;
    }

    public static int createTable() throws SQLException, ClassNotFoundException {
        Connection con = ConnectionManager.getConnection();

        String sql1 = "CREATE TABLE IF NOT EXISTS directory(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,patient_name TEXT,phone TEXT,date_registration TEXT,clinicLoc TEXT,category TEXT,doc_comment TEXT)";
        PreparedStatement ps = con.prepareStatement(sql1);

        int row = ps.executeUpdate();
        //con.close();
        return row;
    }

    public static int createAccess() throws SQLException, ClassNotFoundException {


        Connection con = ConnectionManager.getConnection();

        String sql1 = "CREATE TABLE IF NOT EXISTS access(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,user_type TEXT,pin TEXT)";
        PreparedStatement ps = con.prepareStatement(sql1);

        int row = ps.executeUpdate();
        //con.close();
        return row;
    }


    public static List<Directory> searchData(String patient_name) throws SQLException, ClassNotFoundException {

        Connection con = ConnectionManager.getConnection();

        String sql = "SELECT * FROM directory";

        PreparedStatement ps = con.prepareStatement(sql);


        ResultSet rs = ps.executeQuery();

        List<Directory> list = new ArrayList<>();

        while(rs.next()){

            Directory d = new Directory();


                d.setNumber(rs.getInt(1));
                d.setName(rs.getString(2));
                d.setPhone(rs.getString(3));
                d.setDate_reg(rs.getString(4));
                d.setClinic_loc(rs.getString(5));
                d.setDoc_comment(rs.getString(6));

                if(d.getName().equals(patient_name)){
                    list.add(d);
                }



        }

        return list;
    }


    public static int insertImportData(List<Directory> list) throws SQLException {
        int quantiy = 0;
        for(Directory d : list){
            Connection con = ConnectionManager.getConnection();
            String sql = "INSERT INTO directory(patient_name,phone,date_registration,clinicLoc,category,doc_comment) VALUES(?,?,?,?,?,?)";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1,d.getName());
            ps.setString(2,d.getPhone());
            ps.setString(3,d.getDate_reg());
            ps.setString(4,d.getClinic_loc());
            ps.setString(5,d.getCategory());
            ps.setString(6,d.getDoc_comment());


            int row = ps.executeUpdate();

            quantiy = quantiy + row;

            con.close();

        }

        return quantiy;

    }


}
