package fx.app.hopitaldirectorysystem;

import javafx.application.Platform;
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
import java.util.*;

public class DoctorHomeContoller implements Initializable {

    @FXML
    private BorderPane bp;

    @FXML
    private Button btnHome;



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
    private TableColumn<Directory, String> tcRegDate;

    @FXML
    private TableColumn<Directory, String> tcPatientCategory;



    @FXML
    private ProgressIndicator progressIn;

    @FXML
    private TableView<Directory> tvTable;

    @FXML
    private Text txt;

    @FXML
    private Button btnReload;

    @FXML
    private Button btnSearch;

    @FXML
    private Button btnProfile;

    @FXML
    private TextField tfSearch;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox vbox;

    @FXML
    private Button btnLogout;

    @FXML
    private Button btnImport;

    @FXML
    private Button btnExport;

    private final ObservableList<Directory> list = FXCollections.observableArrayList();

    private final FileChooser fileChooser = new FileChooser();

    private final List<Directory> searchList = new ArrayList<>();



    private List<Directory> importDirectories = new ArrayList<>();
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


        tcDocComments.setCellFactory(TextFieldTableCell.forTableColumn());

        tvTable.setItems(list);

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


        btnImport.setOnAction(e ->{

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    fileChooser.setTitle("Import Directory");
                    fileChooser.getExtensionFilters().clear();
                    fileChooser.getExtensionFilters().addAll(
                            new FileChooser.ExtensionFilter("directory Files", "*.dir")
                    );
                    File selectedFile1 = fileChooser.showOpenDialog(tfSearch.getScene().getWindow());


                    if(selectedFile1 != null) {


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

                        for (int i = 0; i < exportDirectory.getCategory().size(); i++) {
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


                        loadData(importDirectories);

                    }
                }
            });
        });

        //loadData();

        btnSearch.setOnAction(e ->{

            if(tfSearch.getText().isEmpty()){
                txt.setFill(Color.RED);
                txt.setText("Empty Search Field");
                return;
            }


            list.clear();
            searchList.clear();

            if(importDirectories !=null){
                for(Directory d : importDirectories){
                    list.add(d);
                }
            }else{
                txt.setFill(Color.RED);
                txt.setText("No imported directory");
                return;
            }



            for(Directory d : list){
                if(d.getName().contains(tfSearch.getText())){

                    searchList.add(d);
                }
            }

            list.clear();
            if(searchList.size() > 0){
                loadData(searchList);

                txt.setFill(Color.GREEN);
                txt.setText(searchList.size()+" result(s) found");
            }else{
                txt.setFill(Color.RED);
                txt.setText("No result found");
            }


        });




        btnProfile.setOnAction(e ->{
            loadPage("profile");
        });

        btnHome.setOnAction(e ->{
            bp.setCenter(scrollPane);
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


        tcDocComments.setOnEditCommit( e ->{
            TablePosition<Directory, String> pos = e.getTablePosition();

            String newComment = e.getNewValue();
            int row = pos.getRow();

            Directory d = e.getTableView().getItems().get(row);
            d.setDoc_comment(newComment);

        });

        btnReload.setOnAction( e->{
            if(importDirectories != null){
                list.clear();
                loadData(importDirectories);
            }else{
                txt.setFill(Color.RED);
                txt.setText("No file imported");
            }
        });

    }


    public void loadData(List<Directory> lists){

        list.clear();


        if(lists.size() > 0){
            for (Directory directory : lists) {
                list.add(directory);
            }
        }

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
}
