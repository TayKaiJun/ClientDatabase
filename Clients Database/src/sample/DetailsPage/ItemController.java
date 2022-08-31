package sample.DetailsPage;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import sample.AccountsPage.ExpiringController;
import sample.Database.DisplayDatabase;
import sample.Database.GetDatabaseInfo;
import sample.Main;

import javax.xml.soap.Text;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.sql.Date;
import java.util.Scanner;

/**
 * Created by Kai Jun on 14/4/2017.
 */
public class ItemController {

    private Main main;
    private Stage dialogStage;
    private String table;
    private int function; //1 for new, 2 for edit, 3 for delete.
    private ArrayList<String> primaryKeys;
    private ArrayList<String> foreignKeys;
    private ArrayList<String> foreignTables;
    private ArrayList<String> fieldHeaders;
    private ArrayList<String> fieldType;
    private ArrayList<Label> fieldName;
    private ArrayList<Control> field;
    private ArrayList<String> fieldsData;
    private TableView dataTable;
    private boolean modify;
    private boolean next;

    @FXML
    private GridPane container;
    @FXML
    private Button cancelButton;
    @FXML
    private Button acceptButton;
    @FXML
    private Button secondaryButton;

    public ItemController(){
    }

    @FXML
    public void initialize() {
        fieldsData = new ArrayList<String>();
        fieldName = new ArrayList<Label>();
        field = new ArrayList<Control>();
        foreignTables = new ArrayList<String>();
        modify = false;
        next = false;

        acceptButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if(function == 1){
                    saveFunction();
                    confirmationMessage(1);
                } else{
                    if(modify){
                        saveFunction();
                        confirmationMessage(2);
                    } else {
                        if(!next) {
                            next = true;
                            disableFields();
                            acceptButton.setText("Confirm");
                            secondaryButton.setVisible(false);
                        } else {
                            saveFunction();
                            confirmationMessage(3);
                        }
                    }
                }
            }
        });

        secondaryButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                modify = true;
                next = true;
                acceptButton.setText("Save");
                secondaryButton.setVisible(false);
            }
        });

        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                dialogStage.close();
            }
        });
    }

    private void disableFields() {
        for(Control c:field){
            c.setDisable(true);
        }
    }

    public void setDataTable(TableView dataTable){
        this.dataTable = dataTable;
    }

    public void setMain(Main main){
        this.main = main;
    }

    public void setDialogStage(Stage dialogStage){
        this.dialogStage = dialogStage;
    }

    public void setFunction(int function) {
        this.function = function;
        if(function == 1) {
            acceptButton.setText("Save");
            secondaryButton.setVisible(false);
        } else{
            secondaryButton.setText("Modify");
            acceptButton.setText("Delete");
        }
    }

    public void setTable(String table){
        this.table = table;
    }

    public void retreiveData() {
        fieldHeaders = GetDatabaseInfo.obtainColumnInfo(table).get(0);
        fieldType = GetDatabaseInfo.obtainColumnInfo(table).get(1);
        primaryKeys = GetDatabaseInfo.obtainPrimaryKeys(table);
        for(int i =0; i < primaryKeys.size(); i++) {
            System.out.println("Primary Key: " + primaryKeys.get(i));
        }
        foreignKeys = GetDatabaseInfo.obtainForeignKeys(table);
        for(int i =0; i < foreignKeys.size(); i++) {
            System.out.println("Foreign Key: " + foreignKeys.get(i));
        }
        for(int i = 0; i < foreignKeys.size(); i++){
            switch (foreignKeys.get(i)){
                case "clientid": foreignTables.add("client"); break;
                case "referrerid": foreignTables.add("referrer"); break;
                case "insurerid": foreignTables.add("insurer"); break;
                case "rpaymenttype":
                case "myptype": foreignTables.add("mypaymentdetail"); break;
                case "ic": foreignTables.add("driver"); break;
            }
        }
    }

    public void populatePage(){

        for(int i = 0; i < fieldHeaders.size(); i++){
            String header = fieldHeaders.get(i);
            Label label = new Label();
            label.setText(header);
            fieldName.add(label);

            boolean setAsForeignKey = false;
            if(header.contains("id")){
                if(!foreignKeys.isEmpty()) {
                    for (int n = 0; n < foreignKeys.size(); n++) {
                        if (header.equals(foreignKeys.get(n))) {
                            ComboBox foreignKeyField = new ComboBox();
                            ArrayList<String> comboList = ItemProcesses.getFKvalues(foreignTables.get(n),foreignKeys.get(n));
                            for(int m = 0; m < comboList.size(); m++)
                                foreignKeyField.getItems().addAll(comboList.get(m));
                            field.add(foreignKeyField);
                            System.out.println("OVER HERE");
                            setAsForeignKey = true;
                        }
                    }
                }
                if(!primaryKeys.isEmpty() && !setAsForeignKey) {
                    for (int n = 0; n < primaryKeys.size(); n++) {
                        if (header.equals(primaryKeys.get(n))) {
                            TextField primaryKeyField = new TextField();
                            field.add(primaryKeyField);
                            System.out.println("IN HERE");
                        }
                    }
                }

            } else if(header.contains("date")){
                DatePicker datePicker = new DatePicker();
                field.add(datePicker);
                System.out.println("DATING HERE");
            } else {
                TextField attributeField = new TextField();
                field.add(attributeField);
                System.out.println("EVERYTHING ELSE HERE");
            }
        }

        if(field.size() == fieldName.size()){
            for(int i = 0; i < field.size(); i++){
                container.setConstraints(fieldName.get(i),0,i);
                container.setConstraints(field.get(i),1,i);
                container.getChildren().addAll(fieldName.get(i),field.get(i));
            }
        }
    }

    private void confirmationMessage(int function){
        String[] dialogText = new String[2];
        switch (function){
            case 1:
                dialogText[0] = "Upload status";
                dialogText[1] = "Successfully added entry!";
                break;
            case 2:
                dialogText[0] = "Edit status";
                dialogText[1] = "Successfully edited entry!";
                break;
            case 3:
                dialogText[0] = "Delete status";
                dialogText[1] = "Successfully deleted entry!";
                break;
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(dialogText[0]);
        alert.setContentText(dialogText[1]);

        alert.showAndWait();

    }

    public void fillPage(String rowData) {

        rowData = rowData.substring(1,rowData.length()-1);
        for(String w: rowData.split(", ")) {
            System.out.println(w);
            if(w.equals("-"))
                fieldsData.add("");
            else
                fieldsData.add(w);
        }

        if(field.size() == fieldsData.size()){
            for(int i = 0; i<field.size(); i++){
                if(field.get(i) instanceof TextField){
                    TextField temp = (TextField) field.get(i);
                    temp.setText(fieldsData.get(i));
                } else if(field.get(i) instanceof ComboBox){
                    ComboBox temp = (ComboBox) field.get(i);
                    temp.setValue(fieldsData.get(i));
                } else if(field.get(i) instanceof DatePicker){
                    DatePicker temp = (DatePicker) field.get(i);
                    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    try {
                        LocalDate date = LocalDate.parse(fieldsData.get(i), df);
                        temp.setValue(date);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }

        for (int i = 0; i < fieldHeaders.size(); i++) {
            for (String s : primaryKeys) {
                if (fieldHeaders.get(i).equals(s)) {
                    field.get(i).setDisable(true);
                }
            }
        }
    }

    private String[][] pkValue(){
        String[][] values = new String[2][primaryKeys.size()];
        for (int i = 0; i < fieldHeaders.size(); i++) {
            for (int j = 0; j < primaryKeys.size(); j++) {
                if (fieldHeaders.get(i).equals(primaryKeys.get(j))) {
                    values[0][j] = primaryKeys.get(j);
                    values[1][j] = fieldsData.get(i);
                }
            }
        }
        return values;
    }

    private void saveFunction(){
        String[] data = new String[field.size()];
        for(int i = 0; i<field.size(); i++){
            if(field.get(i) instanceof TextField){
                TextField temp = (TextField) field.get(i);
                if(temp.getText() != null)
                    data[i] = temp.getText();
                else
                    data[i] = "NULL";
            } else if(field.get(i) instanceof ComboBox){
                ComboBox temp = (ComboBox) field.get(i);
                if(temp.getValue() != null)
                    data[i] = temp.getValue().toString();
                else
                    data[i] = "NULL";
            } else if(field.get(i) instanceof DatePicker){
                DatePicker temp = (DatePicker) field.get(i);
                if(temp.getValue() != null)
                    data[i] = temp.getValue().toString();
                else
                    data[i] = "NULL";
            }
            //System.out.println(data[i]);
            if(data[i].contains(": ")) {
                String[] values = data[i].split(": ");
                data[i] = values[0];
            }
        }
        if(function == 1) {
            if (ItemProcesses.pushData(table, data)) {
                DisplayDatabase.buildData(dataTable, table);
                dialogStage.close();
            }
        } else {
            if(modify){
                if (ItemProcesses.updateData(table, data, pkValue())) {
                    DisplayDatabase.buildData(dataTable, table);
                    dialogStage.close();
                }
            } else {
                if (ItemProcesses.deleteData(table, data, pkValue()))
                    DisplayDatabase.buildData(dataTable, table);
                dialogStage.close();
            }
        }
    }
}
