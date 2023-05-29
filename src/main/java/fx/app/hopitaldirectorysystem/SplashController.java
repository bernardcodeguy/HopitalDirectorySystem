package fx.app.hopitaldirectorysystem;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class SplashController implements Initializable {
    @FXML
    private ProgressIndicator piLoading;

    @FXML
    private Text txtDaysLeft;

    private int daysLeft = 0;
    public void startLoading(){
        piLoading.setVisible(true);
    }

    public void stopLoading(){
        piLoading.setVisible(false);
    }

    public Text getTxtDaysLeft() {
        return txtDaysLeft;
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        piLoading.setProgress(-1);
        startLoading();


    }


    public void setDaysLeft(int daysLeft){
        this.daysLeft = daysLeft;
    }
}
