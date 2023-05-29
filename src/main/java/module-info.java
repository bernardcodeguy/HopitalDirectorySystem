module fx.app.hopitaldirectorysystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires java.prefs;
    requires com.github.oshi;


    opens fx.app.hopitaldirectorysystem to javafx.fxml;
    exports fx.app.hopitaldirectorysystem;
}