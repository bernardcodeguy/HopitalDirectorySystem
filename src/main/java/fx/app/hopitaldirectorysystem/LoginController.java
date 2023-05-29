package fx.app.hopitaldirectorysystem;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private ComboBox<String> cbxAccessType;

    @FXML
    private TextField tfPin;


    @FXML
    private Button btnLogIn;

    @FXML
    private Text txt;

    @FXML
    private ProgressIndicator progressIn;
    String access;

    private final String types [] = {"Record Attendant","Doctor"};


    String infoFileDir = System.getProperty("user.home") + File.separator + "Documents" + File.separator + "Hospital_Directory" + File.separator;

    String fileName = "attendant.user";
    String fileName1 = "doctor.user";
    String filePath = infoFileDir+fileName;
    String filePath1 = infoFileDir+fileName1;

    public File directory = new File( System.getProperty("user.home") + File.separator + "Documents" + File.separator + "Hospital_Directory");

    public File file = new File(infoFileDir,fileName);
    public File file1 = new File(infoFileDir,fileName1);
    private final ObservableList<String> accessTypes = FXCollections.observableArrayList(types);

    private AccessType accessType = AccessType.getInstance();
    private LoginInfo loginInfo = new LoginInfo();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ConnectionManager.createTable();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        if(!file.exists() || !file1.exists()){

            try {
                file.createNewFile();
                loginInfo.setAccess_type("record");
                loginInfo.setPin("5353");
                writeObj(loginInfo,filePath);

                file1.createNewFile();
                loginInfo.setAccess_type("doctor");
                loginInfo.setPin("9030");
                writeObj(loginInfo,filePath1);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }

        cbxAccessType.setItems(accessTypes);


        btnLogIn.setOnAction(e->{
            if(tfPin.getText().isEmpty() || cbxAccessType.getSelectionModel().getSelectedItem() == null){
                txt.setFill(Color.RED);
                txt.setText("Incorrect Pin");
                return;
            }


            if(cbxAccessType.getSelectionModel().getSelectedIndex() == 0){
                if(file.exists()){

                    try {
                        loginInfo = readObj(filePath);


                        if(loginInfo.getPin().equals(tfPin.getText())){

                            accessType.setAccess_type("Record Attendant");
                            // Help all the fxml controls to load before triggering the code in it
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    // Getting the stage from the textfield control
                                    Stage stage = (Stage) tfPin.getScene().getWindow();


                                    try {

                                        Parent root = FXMLLoader.load(getClass().getResource("home.fxml"));

                                        stage.setTitle("Home Page");

                                        Scene scene = new Scene(root);

                                        stage.setScene(scene);


                                    } catch (IOException ex) {
                                        throw new RuntimeException(ex);
                                    }

                                }
                            });
                        }else{
                            txt.setFill(Color.RED);
                            txt.setText("PIN Incorrect. Try Again");
                        }

                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    } catch (ClassNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }




                }
            }else{
                if(file1.exists()){

                    try {
                        loginInfo = readObj(filePath1);


                        if(loginInfo.getPin().equals(tfPin.getText())){

                            accessType.setAccess_type("Doctor");
                            // Help all the fxml controls to load before triggering the code in it
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    // Getting the stage from the textfield control
                                    Stage stage = (Stage) tfPin.getScene().getWindow();


                                    try {

                                        Parent root = FXMLLoader.load(getClass().getResource("doctorhome.fxml"));

                                        stage.setTitle("Home Page");

                                        Scene scene = new Scene(root);

                                        stage.setScene(scene);


                                    } catch (IOException ex) {
                                        throw new RuntimeException(ex);
                                    }

                                }
                            });
                        }else{
                            txt.setFill(Color.RED);
                            txt.setText("PIN Incorrect. Try Again");
                        }


                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    } catch (ClassNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }




                }
            }


        });



    }

    public void writeObj(LoginInfo info, String filename) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(info);
        }
    }

    public LoginInfo readObj(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (LoginInfo) ois.readObject();
        }
    }
}
