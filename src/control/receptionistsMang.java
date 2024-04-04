package control;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
//import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import dao.ReceptionistDao;
import model.Receptionist;
import org.apache.poi.ss.usermodel.*;

public class receptionistsMang implements Initializable {

    @FXML
    TextField nameIn;
    @FXML
    TextField salaryIn;
    @FXML
    TextField emailIn;
    @FXML
     TextField updateEmail;
    @FXML
    TableColumn ID;
    @FXML
    TableColumn NAME;
    @FXML
    TableColumn SALAIRE;
    @FXML
    TableColumn EMAIL;
    @FXML
     TableView TABLE;
    @FXML
     Label errmsg;


    ReceptionistDao recdao = new ReceptionistDao();
    public void find(Event e){
        //resetBtns();
        if(emailIn.getText().equals("")){
            System.out.println("Donner l'email à chercher");
            errmsg.setText("Donner l'email à chercher");
            return;
        }

        try{
            Receptionist rec = null;
            rec = recdao.getEmployeeByEmail(emailIn.getText());
            if(rec != null){
                salaryIn.setText(String.valueOf(rec.getSalary()));
                nameIn.setText(rec.getName());
            }else{
                System.out.println("Cet email n'existe pas");
                errmsg.setText("Cet email n'existe pas");
            }
        }catch (SQLException | ClassNotFoundException err ){
            err.printStackTrace();
        }

    }

    public void add(Event e){
        if (!isRemplir()){
            System.out.println("Remplir tous les champs");
            errmsg.setText("Remplir tous les champs");
            return;
        }
        Receptionist receptionist = new Receptionist();
        receptionist.setEmail(emailIn.getText());
        receptionist.setPassword(emailIn.getText());
        receptionist.setName(nameIn.getText());
        receptionist.setSalary(Double.valueOf(salaryIn.getText()));
        try{
            int statu = recdao.createEmployee(receptionist);
            if(statu == -2){
                System.out.println("client avec cet email existe déjà");
                errmsg.setText("client avec cet email existe déjà");
            }else if(statu == 1){
                System.out.println("donnnne");
                errmsg.setText("done");
                resetBtns();
                refrech();
            }
        }catch(SQLException | ClassNotFoundException err){
            err.printStackTrace();
        }
    }

    public void delete(Event e){
        //resetBtns();
        if(emailIn.getText().equals("")){
            System.out.println("Donner l'email à supprimer");
            errmsg.setText("Donner l'email à supprimer");
            return;
        }

        try{
            int statu = recdao.deleteEmployee(emailIn.getText());
            if(statu == 1){
                System.out.println("dooonne");
                errmsg.setText("done");
                resetBtns();
                refrech();
            }else if(statu == -2){
                System.out.println("Cet email n'existe pas");
                errmsg.setText("Cet email n'existe pas");
            }
        }catch (SQLException | ClassNotFoundException err ){
            err.printStackTrace();
        }

    }

    public void update(Event e){
        if(updateEmail.getText().equals("")){
            System.out.println("Donner l'email à mettre à jour");
            errmsg.setText("Donner l'email à mettre à jour");
            return;
        }
        Receptionist receptionist = null;
        try{
            receptionist = recdao.getEmployeeByEmail(updateEmail.getText());
            if(receptionist == null){
                System.out.println("Cet email n'existe pas");
                errmsg.setText("Cet email n'existe pas");
            }else{
                receptionist.setEmail(emailIn.getText());
                receptionist.setName(nameIn.getText());
                receptionist.setSalary(Double.valueOf(salaryIn.getText()));
                int statu = recdao.updateEmployee(receptionist);
                if(statu == -2){
                    System.out.println("client avec cet email existe déjà");
                    errmsg.setText("client avec cet email existe déjà");
                }else if(statu == 1){
                    System.out.println("donnnne");
                    errmsg.setText("done");
                    resetBtns();
                    refrech();
                }
            }
        }catch (SQLException | ClassNotFoundException err){
            err.printStackTrace();
        }
    }


/*****************************************************************************************************************************************/

    public void exportToExcel(Event e) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
        fileChooser.setTitle("Save Excel File");

        File file = fileChooser.showSaveDialog(((Node) e.getSource()).getScene().getWindow());

        if (file != null) {
            String filePath = file.getAbsolutePath();

            exportToExcel(TABLE, filePath);

            System.out.println("Excel file created successfully at: " + filePath);
            errmsg.setText("Excel file created successfully at: " + filePath);
        }
    }

    private void exportToExcel(TableView<?> tableView, String filePath) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet1");

        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < tableView.getColumns().size(); i++) {
            headerRow.createCell(i).setCellValue(((TableColumn<?, ?>) tableView.getColumns().get(i)).getText());
        }


        for (int i = 0; i < tableView.getItems().size(); i++) {
            Row dataRow = sheet.createRow(i + 1);
            for (int j = 0; j < tableView.getColumns().size(); j++) {
                TableColumn<?, ?> column = (TableColumn<?, ?>) tableView.getColumns().get(j);
                Object cellValue = column.getCellData(i);
                if (cellValue != null) {
                    dataRow.createCell(j).setCellValue(cellValue.toString());
                }
            }
        }


        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    public void exportToCSV(Event e) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        fileChooser.setTitle("Save CSV File");


        File file = fileChooser.showSaveDialog(((Node) e.getSource()).getScene().getWindow());

        if (file != null) {
            String filePath = file.getAbsolutePath();


            exportToCSV(TABLE, filePath);

            System.out.println("CSV file created successfully at: " + filePath);
            errmsg.setText("CSV file created successfully at: " + filePath);
        }
    }

    private void exportToCSV(TableView<?> tableView, String filePath) {
        try (PrintWriter writer = new PrintWriter(new File(filePath))) {

            for (int i = 0; i < tableView.getColumns().size(); i++) {
                writer.print(((TableColumn<?, ?>) tableView.getColumns().get(i)).getText());
                if (i < tableView.getColumns().size() - 1) {
                    writer.print(",");
                }
            }
            writer.println();

            // Write data rows
            for (int i = 0; i < tableView.getItems().size(); i++) {
                for (int j = 0; j < tableView.getColumns().size(); j++) {
                    TableColumn<?, ?> column = (TableColumn<?, ?>) tableView.getColumns().get(j);
                    Object cellValue = column.getCellData(i);
                    if (cellValue != null) {
                        writer.print(cellValue.toString());
                    }
                    if (j < tableView.getColumns().size() - 1) {
                        writer.print(",");
                    }
                }
                writer.println(); // Move to the next line after writing a data row
            }

            System.out.println("CSV data exported successfully.");
            errmsg.setText("CSV data exported successfully.");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }






    public void importFromCSV(Event event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        fileChooser.setTitle("Choose CSV File");

        File file = fileChooser.showOpenDialog(((Node) event.getSource()).getScene().getWindow());

        if (file != null) {
            String filePath = file.getAbsolutePath();

            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;
                reader.readLine();

                while ((line = reader.readLine()) != null) {
                    String[] data = line.split(",");
                    String name = data[1].trim();
                    double salaire = Double.parseDouble(data[2].trim());
                    String email = data[3].trim();

                    Receptionist rec = new Receptionist();
                    rec.setName(name);
                    rec.setSalary(salaire);
                    rec.setEmail(email);
                    rec.setPassword(rec.getEmail());
                    recdao.createEmployee(rec);
                }

                System.out.println("CSV data imported to the database successfully.");
                errmsg.setText("CSV data imported to the database successfully");
                refrech();
            } catch (IOException | SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }






    public void importFromExcel(Event event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
        fileChooser.setTitle("Choose Excel File");

        // Show open file dialog
        File file = fileChooser.showOpenDialog(((Node) event.getSource()).getScene().getWindow());

        if (file != null) {
            String filePath = file.getAbsolutePath();

            try (Workbook workbook = new XSSFWorkbook(new FileInputStream(filePath))) {
                Sheet sheet = workbook.getSheetAt(0);

                Row headerRow = sheet.getRow(0);

                for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                    Row row = sheet.getRow(rowIndex);

                    String name = row.getCell(1).getStringCellValue().trim();
                    double salaire = row.getCell(2).getNumericCellValue();
                    String email = row.getCell(3).getStringCellValue().trim();

                    Receptionist rec = new Receptionist();
                    rec.setName(name);
                    rec.setSalary(salaire);
                    rec.setEmail(email);
                    rec.setPassword(rec.getEmail());
                    recdao.createEmployee(rec);
                }

                System.out.println("Excel data imported to the database successfully.");
                errmsg.setText("Excel data imported to the database successfully.");
                refrech();
            } catch (IOException | SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


    /********************************************************************************************************/


    public void resetBtns(){
        nameIn.setText("");
        salaryIn.setText("");
        emailIn.setText("");
        errmsg.setText("");
    }

    public boolean isRemplir(){
        if(nameIn.getText().equals("") | emailIn.getText().equals("") | salaryIn.getText().equals("")){
            return false;
        }
        return true;
    }
    public void rtn(Event e){
        Node node = (Node) e.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        //stage.close();
        try{
        Parent root = FXMLLoader.load(getClass().getResource("/view/AdminPage.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        } catch (IOException ex) {
        ex.printStackTrace();
        }
    }

    public void logout(Event e){
        Node node = (Node) e.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        //stage.close();

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/LoginForm.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        refrech();
    }

    public  void refrech(){
        ID.setCellValueFactory((new PropertyValueFactory<>("id")));
        NAME.setCellValueFactory((new PropertyValueFactory<>("name")));
        SALAIRE.setCellValueFactory((new PropertyValueFactory<>("salary")));
        EMAIL.setCellValueFactory((new PropertyValueFactory<>("email")));
        try {
            TABLE.setItems(recdao.getAllEmployeesObservable());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        errmsg.setText("");
    }
}
