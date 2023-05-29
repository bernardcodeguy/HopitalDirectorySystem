package fx.app.hopitaldirectorysystem;

import javafx.concurrent.Task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

public class UpdateDBTask extends Task<Integer> {

    private final List<Directory> directories;

    public UpdateDBTask(List<Directory> directories) {
        this.directories = directories;
    }

    @Override
    protected Integer call() throws Exception {

        Connection con = ConnectionManager.getConnection();

        int updated = 0;

        for(Directory d : directories){
            PreparedStatement ps = con.prepareStatement("UPDATE directory SET patient_name=?,phone=?,date_registration=?,clinicLoc=?,category=?,doc_comment=? WHERE id="+d.getNumber()+
                    " AND patient_name=?");

            ps.setString(1,d.getName());
            ps.setString(2,d.getPhone());
            ps.setString(3,d.getDate_reg());
            ps.setString(4,d.getClinic_loc());
            ps.setString(5,d.getCategory());
            ps.setString(6,d.getDoc_comment());
            ps.setString(7,d.getName());

            int row = ps.executeUpdate();
            if(row > 0){
                updated = updated + row;
            }else{
                Controller.notUpdatedList.add(d);
            }


        }

        return updated;
    }
}
