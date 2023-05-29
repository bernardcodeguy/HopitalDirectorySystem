package fx.app.hopitaldirectorysystem;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.ComputerSystem;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.prefs.Preferences;
import java.io.IOException;
import java.util.Objects;

public class AppManager extends Application {
    /*Preferences prefs;
    Text t;*/
    private final FileChooser fileChooser = new FileChooser();

    public int daysBetween;
    @Override
    public void start(Stage stage) throws IOException {
        /*prefs = Preferences.userNodeForPackage(AppManager.class);*/
        // Loading the fxml file
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("splash.fxml"));
        Parent root = fxmlLoader.load();
        SplashController controller = (SplashController) fxmlLoader.getController();
        // Creating the scene
        Scene scene = new Scene(root);
        stage.initStyle(StageStyle.UNDECORATED);
        //stage.setTitle("Hospital Directory System");
        stage.setScene(scene);

      /*  // Get current date
        LocalDate currentDate = LocalDate.now();



        // Check if preference date is present and compare it with current date
        String prefDateStr = prefs.get("oneMonthLater", null);
        Boolean shown = prefs.getBoolean("shown", false);

        if (prefDateStr != null) {
            LocalDate prefDate = LocalDate.parse(prefDateStr);
            System.out.println("Preference date: " + prefDate);
            if (currentDate.isAfter(prefDate)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("License Info");
                alert.setHeaderText(null);
                alert.setContentText("Free trial expired\nStore your serial number and send to software manufacturer to purchase license\n" +
                        "Your serial number: "+generateLicenseKey()+"\n" +
                        "Cancel the next dialog if license has not been acquired yet");
                alert.showAndWait();

                fileChooser.setTitle("Import License");
                fileChooser.getExtensionFilters().clear();
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("directory Files", "*.license")

                );
                File selectedFile1 = fileChooser.showOpenDialog(stage);


                if(selectedFile1 != null){

                    FileInputStream fileIn = null;
                    ObjectInputStream in = null;
                    License license = null;
                    try {
                        fileIn = new FileInputStream(selectedFile1.getAbsolutePath());
                        in = new ObjectInputStream(fileIn);
                        license = (License) in.readObject();
                        // access the lists property
                        in.close();
                        fileIn.close();
                    } catch (Exception ex) {
                        Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                        alert1.setTitle("License Info");
                        alert1.setHeaderText(null);
                        alert1.setContentText("License expired or invalid");
                        alert1.showAndWait();
                        return;

                    }
                     if(license != null){

                         String serial = license.getSerialNumber();

                         if(serial.equals(generateLicenseKey())){
                             LocalDate oneMonthLater = currentDate.plus(1, ChronoUnit.YEARS);
                             prefs.put("oneMonthLater", oneMonthLater.toString());
                             daysBetween = (int) ChronoUnit.DAYS.between(currentDate, oneMonthLater);
                             Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                             alert1.setTitle("License Info");
                             alert1.setHeaderText(null);
                             alert1.setContentText("License valid and days extended successfully\n"+daysBetween+" day(s) left");
                             alert1.showAndWait();

                         }else{
                             Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                             alert1.setTitle("License Info");
                             alert1.setHeaderText(null);
                             alert1.setContentText("License expired or invalid");
                             alert1.showAndWait();
                             return;
                         }
                     }else{
                         Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                         alert1.setTitle("License Info");
                         alert1.setHeaderText(null);
                         alert1.setContentText("License expired or invalid");
                         alert1.showAndWait();
                         return;
                     }
                }else{
                    return;
                }

            } else {
                daysBetween = (int) ChronoUnit.DAYS.between(currentDate, prefDate);


                if(daysBetween < 30 && shown){
                    prefs.putBoolean("shown", false);
                }



                if(daysBetween == 31 || daysBetween == 0){

                    if(!shown || shown == null){

                        prefs.putBoolean("shown", true);
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("License Info");
                        alert.setHeaderText(null);
                        alert.setContentText(daysBetween+" day(s) left");
                        alert.showAndWait();


                    }

                }


            }
        } else {
            // Add one month to current date
            LocalDate oneMonthLater = currentDate.plus(7, ChronoUnit.DAYS);
            prefs.put("oneMonthLater", oneMonthLater.toString());

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("License Info");
            alert.setHeaderText(null);
            alert.setContentText("Free trial began, Expires on "+oneMonthLater);
            alert.showAndWait();
        }


        controller.setDaysLeft(daysBetween);*/


        // Create a Task to wait for 5 seconds
        Task<Void> sleeper = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    Thread.sleep(5000);

                } catch (InterruptedException e) {
                    // Do nothing
                }
                return null;
            }
        };



        // Set the action to be performed after the Task completes
        sleeper.setOnSucceeded(event -> {
            controller.stopLoading();
            stage.close();




            // Load the second FXML scene
            Parent mainRoot = null;
            try {
                mainRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("acces.fxml")));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Scene mainScene = new Scene(mainRoot);

            // Switch to the second scene
            Stage stage1 = new Stage();
            stage1.setScene(mainScene);
            stage1.setTitle("Login Access");
            stage1.getIcons().addAll(new Image(getClass().getResourceAsStream("icon.png")));
            stage1.show();

        });

        // Start the Task on a background thread
        new Thread(sleeper).start();
        stage.getIcons().addAll(new Image(getClass().getResourceAsStream("icon.png")));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    static String generateLicenseKey()
    {
        SystemInfo systemInfo = new SystemInfo();
        OperatingSystem operatingSystem = systemInfo.getOperatingSystem();
        HardwareAbstractionLayer hardwareAbstractionLayer = systemInfo.getHardware();
        CentralProcessor centralProcessor = hardwareAbstractionLayer.getProcessor();
        ComputerSystem computerSystem = hardwareAbstractionLayer.getComputerSystem();


        String processorSerialNumber = computerSystem.getSerialNumber();


        return processorSerialNumber;
    }
}