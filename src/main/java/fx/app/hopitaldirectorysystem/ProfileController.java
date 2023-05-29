package fx.app.hopitaldirectorysystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.text.Text;

import java.io.*;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {
    @FXML
    private Text txtBigName;

    @FXML
    private Text txtEmail;

    @FXML
    private Text txtGender;

    @FXML
    private Text txtName;

    @FXML
    private Text txtType;
    String infoFileDir = System.getProperty("user.home") + File.separator + "Documents" + File.separator + "Hospital_Directory" + File.separator;


    String aFilename = "attendant.user";
    String dFileName = "doctor.user";
    String fileName = "profile.me";

    String filePath = infoFileDir+fileName;
    String aFilePath = infoFileDir+aFilename;
    String dFilePath = infoFileDir+dFileName;

    public File directory = new File( System.getProperty("user.home") + File.separator + "Documents" + File.separator + "Hospital_Directory");

    public File file = new File(infoFileDir,fileName);

    public File aFile = new File(infoFileDir,aFilename);
    public File dFile = new File(infoFileDir,dFileName);
    @FXML
    private Text txtPIN;

    private UserProfile userInfo = new UserProfile();

    private AccessType accessType = AccessType.getInstance();

    private LoginInfo loginInfo = new LoginInfo();
    @FXML
    void editClicked(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        String buttonId = clickedButton.getId();




        if(buttonId.equals("name")){
            TextInputDialog textInputDialog1 = new TextInputDialog();
            textInputDialog1.setTitle("User Full Name");
            textInputDialog1.setHeaderText("Enter your name");
            textInputDialog1.setContentText("Full Name: ");

            Optional<String> result1 = textInputDialog1.showAndWait();
            if(result1.isPresent()){
                txtBigName.setText(result1.get());

                txtName.setText("Name: "+result1.get());
                if (!file.exists()){
                    file.mkdirs();
                }
                userInfo.setName(result1.get());
                try {
                    writeObj(userInfo,filePath);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                //dob.put("d_b", result1.get());

            }
        }else if(buttonId.equals("gender")){
            List<String> choices = Arrays.asList("Male", "Female","Other");
            ChoiceDialog<String> dialog = new ChoiceDialog<>("Male", choices);
            dialog.setTitle("Gender Input");
            dialog.setHeaderText("Please select your gender:");
            dialog.setContentText("Gender:");

            // Show the dialog and wait for a response.
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                String gender = result.get();
                txtGender.setText("Gender: " + gender);
                if (!file.exists()){
                    file.mkdirs();
                }
                userInfo.setGender(gender);
                try {
                    writeObj(userInfo,filePath);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

            }

        }else if(buttonId.equals("pin")){
            TextInputDialog textInputDialog1 = new TextInputDialog();
            textInputDialog1.setTitle("Acess PIN");
            textInputDialog1.setHeaderText("Enter your new pin");
            textInputDialog1.setContentText("PIN: ");


            Optional<String> result1 = textInputDialog1.showAndWait();


            if(result1.isPresent()) {

                txtPIN.setText("PIN: " + result1.get());



                if(accessType.getAccess_type().equals("Doctor")){
                    if (!dFile.exists()) {
                        dFile.mkdirs();
                    }
                    loginInfo.setPin(result1.get());
                    try {
                        writeObjPIN(loginInfo, dFilePath);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }else{
                    if (!aFile.exists()) {
                        aFile.mkdirs();
                    }
                    loginInfo.setPin(result1.get());
                    try {
                        writeObjPIN(loginInfo, aFilePath);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }


            }
        }else{
            TextInputDialog textInputDialog1 = new TextInputDialog();
            textInputDialog1.setTitle("User Email");
            textInputDialog1.setHeaderText("Enter your email");
            textInputDialog1.setContentText("Email: ");


            Optional<String> result1 = textInputDialog1.showAndWait();
            if(result1.isPresent()){

                txtEmail.setText("Email: "+result1.get());
                if (!file.exists()){
                    file.mkdirs();
                }
                userInfo.setEmail(result1.get());
                try {
                    writeObj(userInfo,filePath);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                //dob.put("d_b", result1.get());

            }
        }



    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        txtType.setText(accessType.getAccess_type());
        if (!directory.exists()){
            directory.mkdirs();
        }


        if(!aFile.exists() || !dFile.exists()){
            try {
                aFile.createNewFile();
                loginInfo.setAccess_type("record");
                loginInfo.setPin("5353");
                writeObjPIN(loginInfo,aFilePath);

                dFile.createNewFile();
                loginInfo.setAccess_type("doctor");
                loginInfo.setPin("9030");
                writeObjPIN(loginInfo,dFilePath);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }else{


            if(accessType.getAccess_type().equals("Doctor")){
                try {
                    loginInfo = readObjPIN(dFilePath);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                txtPIN.setText("PIN: "+loginInfo.getPin());
            }else{
                try {
                    loginInfo = readObjPIN(aFilePath);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                txtPIN.setText("PIN: "+loginInfo.getPin());
            }


        }



        if(file.exists()){


            long fileSize = file.length();
            if(fileSize > 0){
                try {
                    userInfo = readObj(filePath);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                txtBigName.setText(userInfo.getName());
                txtName.setText("Name: "+userInfo.getName());
                txtGender.setText("Gender: "+userInfo.getGender());
                txtEmail.setText("Email: "+userInfo.getEmail());
            }


            //imageView.setImage(new Image(userInfo.getDp()));
        }else{
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void writeObj(UserProfile info, String filename) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(info);
        }
    }

    public void writeObjPIN(LoginInfo info, String filename) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(info);
        }
    }

    public LoginInfo readObjPIN(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (LoginInfo) ois.readObject();
        }
    }

    public UserProfile readObj(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (UserProfile) ois.readObject();
        }
    }
}
