package fx.app.hopitaldirectorysystem;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

public class Controller implements Initializable {

    @FXML
    private BorderPane bp;
    @FXML
    private Button btnAdd;

    @FXML
    private Button btnClear;

    @FXML
    private DatePicker dpRegDate;
    @FXML
    private Button btnHome;

    @FXML
    private Button btnExport;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private TextArea taDocsComments;

    @FXML
    private TableColumn<Directory, String> tcClinicLoc;

    @FXML
    private TableColumn<Directory, String> tcDocComments;

    @FXML
    private TableColumn<Directory, Integer> tcNumber;

    @FXML
    private TableColumn<Directory, String> tcPatientName;

    @FXML
    private TableColumn<Directory, String> tcPhone;

    @FXML
    private TableColumn<Directory, String> tcPatientCategory;


    @FXML
    private TableColumn<Directory, String> tcRegDate;

    @FXML
    private ToggleGroup rdCat;

    @FXML
    private RadioButton rdFree;

    @FXML
    private ToggleGroup rdLoc;

    @FXML
    private RadioButton rdPaid;

    @FXML
    private RadioButton rdPortHarcourt;

    @FXML
    private RadioButton rdUyo;


    @FXML
    private TextField tfPatientName;

    @FXML
    private TextField tfPhone;
    @FXML
    private ProgressIndicator progressIn;

    @FXML
    private TableView<Directory> tvTable;

    @FXML
    private Text txt;

    @FXML
    private Button btnReminder;

    @FXML
    private Button btnReload;

    @FXML
    private Button btnImport;

    @FXML
    private Button btnSearch;

    @FXML
    private Button btnProfile;

    @FXML
    private TextField tfSearch;

    @FXML
    private VBox vbox;

    @FXML
    private Button btnLogout;

    private final ObservableList<Directory> list = FXCollections.observableArrayList();

    private final FileChooser fileChooser = new FileChooser();

    private List<Directory> importDirectories = new ArrayList<>();

    private String location, cat =null;

    public static List<Directory> notUpdatedList = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        progressIn.setVisible(false);


        tcNumber.setCellValueFactory(new PropertyValueFactory<>("number"));
        tcPatientName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tcPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        tcRegDate.setCellValueFactory(new PropertyValueFactory<>("date_reg"));
        tcClinicLoc.setCellValueFactory(new PropertyValueFactory<>("clinic_loc"));
        tcPatientCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        tcDocComments.setCellValueFactory(new PropertyValueFactory<>("doc_comment"));

        rdFree.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                if(t1){
                    cat = "Free";
                }
            }
        });

        rdPaid.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                if(t1){
                    cat = "Paid";
                }
            }
        });

        rdUyo.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                if(t1){
                    location = "Uyo";
                }
            }
        });

        rdPortHarcourt.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                if(t1){
                    location = "Portharcourt";
                }
            }
        });
        


       /* Platform.runLater(new Runnable() {
            @Override
            public void run() {
                fileChooser.setTitle("Import Directory");
                fileChooser.getExtensionFilters().clear();
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("Excel Files", "*.xlsx")
                );
                File selectedFile1 = fileChooser.showOpenDialog(tfClinicLoc.getScene().getWindow());


                if(selectedFile1 != null){
                    List<Directory> d = readDirectoryDataFromExcel(selectedFile1.getAbsolutePath());

                    System.out.println(d.size());
                }
            }
        });*/

        tcPatientName.setCellFactory(TextFieldTableCell.forTableColumn());
        tcPhone.setCellFactory(TextFieldTableCell.forTableColumn());
        tcPatientCategory.setCellFactory(TextFieldTableCell.forTableColumn());
        tcRegDate.setCellFactory(TextFieldTableCell.forTableColumn());
        tcClinicLoc.setCellFactory(TextFieldTableCell.forTableColumn());

        tvTable.setItems(list);


        btnImport.setOnAction(e ->{

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    fileChooser.setTitle("Import Directory");
                    fileChooser.getExtensionFilters().clear();
                    fileChooser.getExtensionFilters().addAll(
                            new FileChooser.ExtensionFilter("directory Files", "*.dir")
                          /*  new FileChooser.ExtensionFilter("Excel Files", "*.xlsx")*/
                    );
                    File selectedFile1 = fileChooser.showOpenDialog(tfSearch.getScene().getWindow());


                    if(selectedFile1 != null){


                        FileInputStream fileIn = null;
                        ObjectInputStream in = null;
                        ExportDirectory exportDirectory = null;
                        try {
                            fileIn = new FileInputStream(selectedFile1.getAbsolutePath());
                            in = new ObjectInputStream(fileIn);
                            exportDirectory = (ExportDirectory) in.readObject();
                            // access the lists property
                            in.close();
                            fileIn.close();
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }

                        importDirectories.clear();
                        for(int i=0;i<exportDirectory.getCategory().size(); i++){
                            Directory d = new Directory();

                            d.setNumber(exportDirectory.getNumber().get(i));
                            d.setName(exportDirectory.getName().get(i));
                            d.setPhone(exportDirectory.getPhone().get(i));
                            d.setDate_reg(exportDirectory.getDate_reg().get(i));
                            d.setClinic_loc(exportDirectory.getClinic_loc().get(i));
                            d.setCategory(exportDirectory.getCategory().get(i));
                            d.setDoc_comment(exportDirectory.getDoc_comment().get(i));

                            importDirectories.add(d);
                        }



                        UpdateDBTask updateDBTask = new UpdateDBTask(importDirectories);

                        txt.setFill(Color.BLACK);
                        txt.setText("Updating directories.........Please Wait");
                        progressIn.progressProperty().bind(updateDBTask.progressProperty());


                        updateDBTask.setOnRunning(ex ->{
                            progressIn.setVisible(true);
                        });

                        updateDBTask.setOnSucceeded(ex -> {
                                    progressIn.setVisible(false);
                                    int updates = updateDBTask.getValue();

                                    if (updates < importDirectories.size()) {

                                        try {
                                            ConnectionManager.insertImportData(notUpdatedList);
                                        } catch (SQLException exc) {
                                            throw new RuntimeException(exc);
                                        }

                                    }

                            txt.setFill(Color.GREEN);
                            txt.setText(updates+" row(s) updated");

                        });


                        updateDBTask.setOnFailed((event) -> {
                            progressIn.setVisible(false);
                            txt.setFill(Color.RED);
                            txt.setText("Error Occurred");

                        });

                        Thread taskThread = new Thread(updateDBTask);
                        taskThread.setDaemon(true);
                        taskThread.start();

                    }
                }
            });
        });

        btnLogout.setOnAction(e ->{

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Stage stage1 = (Stage) btnLogout.getScene().getWindow();

                    Parent loginScene = null;
                    try {
                        loginScene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("acces.fxml")));
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    Scene mainScene = new Scene(loginScene);

                    // Switch to the second scene

                    stage1.setScene(mainScene);
                    stage1.setTitle("Login Access");
                }
            });

        });

        loadData();

        btnReminder.setOnAction(e ->{
            String[] command = {"cmd", "/c", "start", "outlookcal:"};
            try {
                ProcessBuilder processBuilder = new ProcessBuilder(command);
                processBuilder.start();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        btnSearch.setOnAction(e ->{
            list.clear();
            if(tfSearch.getText().isEmpty()){
                txt.setFill(Color.RED);
                txt.setText("Search field empty");
                return;
            }

            SearchTask searchTask = new SearchTask(tfSearch.getText());

            searchTask.setOnRunning(ex ->{
                progressIn.setVisible(true);
                txt.setFill(Color.BLACK);
                txt.setText("Searching record");

            });


            searchTask.setOnSucceeded(ex ->{

                progressIn.setVisible(false);
                List<Directory> directories = searchTask.getValue();


                if(directories != null){
                    for (Directory directory : directories) {
                        list.add(directory);
                    }

                    txt.setFill(Color.GREEN);
                    txt.setText(list.size()+" result(s) found");
                }else{
                    txt.setFill(Color.RED);
                    txt.setText("No result found");
                }

            });

            searchTask.setOnFailed((event) -> {
                progressIn.setVisible(false);
                txt.setFill(Color.RED);
                txt.setText("Error Occurred");

            });


            Thread taskThread2 = new Thread(searchTask);
            taskThread2.setDaemon(true);
            taskThread2.start();


        });



        tfPhone.setTextFormatter(new TextFormatter<>(change -> {
            if (!change.getControlNewText().matches("[+\\d]*")) {
                return null;
            }
            return change;
        }));


        btnProfile.setOnAction(e ->{
            loadPage("profile");

        });

        btnHome.setOnAction(e ->{
            bp.setCenter(scrollPane);
        });


        btnAdd.setOnAction(e ->{

            if(tfPatientName.getText().isEmpty() || tfPhone.getText().isEmpty()|| location == null
                    || dpRegDate.getValue() == null || taDocsComments.getText().isEmpty() || cat == null){
                txt.setFill(Color.RED);
                txt.setText("Empty field detected");
                return;
            }

            String name = tfPatientName.getText();
            String phone = tfPhone.getText();
            String regDate = dpRegDate.getValue().toString();
            String clinicLoc = location;
            String category = cat;
            String docComments = taDocsComments.getText();


            InsertDBTask insertDBTask = new InsertDBTask(name,phone,regDate,clinicLoc, category, docComments);
            progressIn.progressProperty().bind(insertDBTask.progressProperty());

            insertDBTask.setOnRunning(ex ->{
                progressIn.setVisible(true);
                txt.setFill(Color.BLACK);
                txt.setText("Inserting data to database");

            });


            insertDBTask.setOnSucceeded(ex ->{
                progressIn.setVisible(false);
                int row = insertDBTask.getValue();
                txt.setFill(Color.GREEN);
                txt.setText("Success: "+row+" row(s) affected");

            });

            insertDBTask.setOnFailed((event) -> {
                progressIn.setVisible(false);
                txt.setFill(Color.RED);
                txt.setText("Error Occurred");

            });



            Thread taskThread2 = new Thread(insertDBTask);
            taskThread2.setDaemon(true);
            taskThread2.start();

        });

        btnClear.setOnAction(ex ->{
            tfPhone.clear();
            taDocsComments.clear();
            dpRegDate.setValue(null);
            tfPatientName.clear();
            rdPaid.setSelected(false);
            rdFree.setSelected(false);
            rdUyo.setSelected(false);
            rdPortHarcourt.setSelected(false);
        });


        tcDocComments.setOnEditStart( e ->{
           TablePosition<Directory, String> pos = e.getTablePosition();

           int row = pos.getRow();

           Directory d = e.getTableView().getItems().get(row);

            // Show a success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Doctor's Comments");
            alert.setHeaderText(null);
            alert.setContentText(d.getDoc_comment());
            alert.showAndWait();

        });

        btnReload.setOnAction( e->{
            list.clear();
            loadData();
        });

        btnExport.setOnAction(e ->{
            if (tvTable.getItems().isEmpty()) {
                txt.setFill(Color.RED);
                txt.setText("No data present to export now");
            }else{
                fileChooser.setTitle("Export Directory");
                fileChooser.getExtensionFilters().clear();
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("Directory Files", "*.dir")
                );

                // Show the dialog to choose file location and name
                File selectedFile = fileChooser.showSaveDialog(null);



                if (selectedFile != null) {


                     List<Integer> number = new ArrayList<>();
                     List<String> name = new ArrayList<>();
                     List<String> phone = new ArrayList<>();
                     List<String> date_reg = new ArrayList<>();
                     List<String> clinic_loc = new ArrayList<>();

                     List<String> category = new ArrayList<>();
                     List<String> doc_comment = new ArrayList<>();

                    // Write data rows
                    for (Directory directory : list) {
                        number.add(directory.getNumber());
                        name.add(directory.getName());
                        phone.add(directory.getPhone());
                        date_reg.add(directory.getDate_reg());
                        clinic_loc.add(directory.getClinic_loc());
                        category.add(directory.getCategory());
                        doc_comment.add(directory.getDoc_comment());

                    }

                    ExportDirectory exportDirectory = new ExportDirectory(number,name,phone,date_reg,clinic_loc,category,doc_comment);
                    FileOutputStream fileOut = null;
                    ObjectOutputStream out = null;
                    try {
                        fileOut = new FileOutputStream(selectedFile.getAbsolutePath());
                        out = new ObjectOutputStream(fileOut);
                        out.writeObject(exportDirectory);
                        out.close();
                        fileOut.close();

                         //Show a success message
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Save Successful");
                        alert.setHeaderText(null);
                        alert.setContentText("Directory saved to " + selectedFile.getAbsolutePath());
                        alert.showAndWait();
                    } catch (Exception ex) {
                        // Show an error message if the file could not be saved
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Save Error");
                        alert.setHeaderText(null);
                        alert.setContentText("An error occurred while saving the file.");
                        alert.showAndWait();
                        ex.printStackTrace();
                        throw new RuntimeException(ex);
                    }

                }
            }
        });

    }


    public void loadData(){
        DatabaseQueryTask databaseQueryTask = new DatabaseQueryTask();

        txt.setFill(Color.BLACK);
        txt.setText("Loading directories.........Please Wait");
        progressIn.progressProperty().bind(databaseQueryTask.progressProperty());


        databaseQueryTask.setOnRunning(ex ->{
            progressIn.setVisible(true);
        });

        databaseQueryTask.setOnSucceeded(ex ->{
            progressIn.setVisible(false);
            List<Directory> directories = databaseQueryTask.getValue();

            if(directories.size() > 0){
                for (Directory directory : directories) {
                    list.add(directory);
                }

                txt.setFill(Color.GREEN);
                txt.setText("");


            }else{
                txt.setFill(Color.GREEN);
                txt.setText("Empty directory");
            }


        });


        databaseQueryTask.setOnFailed((event) -> {
            progressIn.setVisible(false);
            txt.setFill(Color.RED);
            txt.setText("Error Occurred");

        });

        Thread taskThread = new Thread(databaseQueryTask);
        taskThread.setDaemon(true);
        taskThread.start();
    }

    private void loadPage(String page){
        Parent root = null;

        try{
            root = FXMLLoader.load(getClass().getResource(page+".fxml"));
        }catch (Exception e){

        }

        bp.setCenter(root);
    }

    public static List<Directory> readDirectoryDataFromExcel(String excelFilePath) {
        List<Directory> directoryList = new ArrayList<>();
        try (FileInputStream inputStream = new FileInputStream(new File(excelFilePath));
             Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            // Skip header row
            rowIterator.next();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Directory directory = new Directory();
                Iterator<Cell> cellIterator = row.cellIterator();

                directory.setNumber((int) cellIterator.next().getNumericCellValue());
                directory.setName(cellIterator.next().getStringCellValue());
                directory.setPhone(cellIterator.next().getStringCellValue());
                directory.setDate_reg(cellIterator.next().getStringCellValue());
                directory.setClinic_loc(cellIterator.next().getStringCellValue());
                directory.setCategory(cellIterator.next().getStringCellValue());
                directory.setDoc_comment(cellIterator.next().getStringCellValue());

                directoryList.add(directory);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return directoryList;
    }

    @FXML
    void editDirectory(TableColumn.CellEditEvent<Directory, String> e) {
        TablePosition<Directory, String> pos = e.getTablePosition();

        String newVal = e.getNewValue();
        int row = pos.getRow();
        int col = pos.getColumn();
        Directory d = e.getTableView().getItems().get(row);

        if(col == 1){
            d.setName(newVal);
            System.out.println(col);
        }else if(col == 2){
            d.setPhone(newVal);
            System.out.println(col);
        }else if(col == 3){
            d.setDate_reg(newVal);
            System.out.println(col);
        }else if(col == 4){
            d.setClinic_loc(newVal);
            System.out.println(col);
        }else if(col == 5){
            d.setCategory(newVal);
            System.out.println(col);
        }


        List<Directory> newList = new ArrayList<>();
        newList.add(d);

        UpdateDBTask updateDBTask = new UpdateDBTask(newList);

        txt.setFill(Color.BLACK);
        txt.setText("Loading directories.........Please Wait");
        progressIn.progressProperty().bind(updateDBTask.progressProperty());


        updateDBTask.setOnRunning(ex ->{
            progressIn.setVisible(true);
        });

        updateDBTask.setOnSucceeded(ex ->{
            progressIn.setVisible(false);
            int updates = updateDBTask.getValue();

            txt.setFill(Color.GREEN);
            txt.setText(updates+" row(s) updated");
        });


        updateDBTask.setOnFailed((event) -> {
            progressIn.setVisible(false);
            txt.setFill(Color.RED);
            txt.setText("Error Occurred");

        });

        Thread taskThread = new Thread(updateDBTask);
        taskThread.setDaemon(true);
        taskThread.start();

        tvTable.getItems().clear();
        list.clear();
        loadData();

    }


}
