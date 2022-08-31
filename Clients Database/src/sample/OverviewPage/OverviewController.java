package sample.OverviewPage;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import sample.Database.DBConnection;
import sample.Database.DisplayDatabase;
import sample.Main;

import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Kai Jun on 12/4/2017.
 */
public class OverviewController {

    private Main main;

    /*
    @FXML
    private Button search;
    @FXML
    private TextField searchbar;
    @FXML
    private ComboBox selectField;
    @FXML
    private Label help;
    @FXML
    private TableView clientsTable;


    @FXML
    private TableColumn clientid;
    @FXML
    private TableColumn vehicleno;
    @FXML
    private TableColumn name;
    @FXML
    private TableColumn referrer;
    @FXML
    private TableColumn insurer;
    @FXML
    private TableColumn effectivedate;
    @FXML
    private TableColumn expirydate;


    @FXML
    private Button newclient;
    @FXML
    private Button modifyclient;
    @FXML
    private Button deleteclient;
    */
    @FXML
    private TableView clientsTable;
    @FXML
    private Button logout;

    public OverviewController() {
    }

    @FXML
    public void initialize() {
        /*
        TableColumn[] tableColumns = new TableColumn[7];
        tableColumns[0] = clientid;
        tableColumns[1] = vehicleno;
        tableColumns[2] = name;
        tableColumns[3] = referrer;
        tableColumns[4] = insurer;
        tableColumns[5] = effectivedate;
        tableColumns[6] = expirydate;
        */

        DisplayDatabase.buildData(clientsTable);

        /*
        selectField.getItems().addAll(
                "ClientID",
                "Vehicle No.",
                "Name",
                "Referrer",
                "Insurer",
                "Effective Date",
                "Expiry Date"
        );
        search.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                String query = searchbar.getText();
                int field = 0;
                boolean sendquery = true;
                if(selectField.getValue() == null){
                    sendquery = false;
                    showErrorMessage(3,"Field to search in is empty!");
                } else {
                    switch (selectField.getValue().toString()) {
                        case "ClientID":
                            field = 0;
                            try {
                                Integer.parseInt(query);
                            } catch (NumberFormatException e1) {
                                sendquery = false;
                                showErrorMessage(1, "No valid ClientID (must be an integer)!");
                            }
                            break;
                        case "Vehicle No.":
                            field = 1;
                            break;
                        case "Name":
                            field = 2;
                            break;
                        case "Referrer":
                            field = 3;
                            break;
                        case "Insurer":
                            field = 4;
                            break;
                        case "Effective Date":
                            field = 5;
                            Date date = null;
                            try {
                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                date = sdf.parse(query);
                                if (!query.equals(sdf.format(date))) {
                                    sendquery = false;
                                    showErrorMessage(2, "Format of date is wrong! Please input a date of format dd/mm/yyyy");
                                }
                            } catch (ParseException ex) {
                                ex.printStackTrace();
                            }
                            break;
                        case "Expiry Date":
                            field = 6;
                            Date date1 = null;
                            try {
                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                date1 = sdf.parse(query);
                                if (!query.equals(sdf.format(date1))) {
                                    sendquery = false;
                                    showErrorMessage(2, "Format of date is wrong! Please input a date of format dd/mm/yyyy");
                                }
                            } catch (ParseException ex) {
                                ex.printStackTrace();
                            }
                            break;
                    }
                }
                if(query == null || query.equals("")){
                    sendquery = false;
                    DisplayDatabase.buildData(clientsTable);
                    showErrorMessage(3,"Search bar is empty!");
                }
                if(sendquery) {
                    DisplayDatabase.buildData(clientsTable, query, field);
                }
            }
        });


        newclient.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                main.showEditItemDialog(0);
            }
        });

        modifyclient.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                main.showEditItemDialog(1);
            }
        });

        deleteclient.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                main.showEditItemDialog(2);
            }
        });
        */

        logout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                Connection conn;
                try {
                    conn = DBConnection.getConnection();
                    conn.close();
                    System.out.println("Disconnected from database");
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                main.initLoginPage();
            }
        });
    }

    private void showErrorMessage(int errorType, String errorMessage){
        String errorHeader = "";
        switch (errorType){
            case 0: errorHeader = "Error on Building Data!"; break;
            case 1: errorHeader = "Error in Search Input! (Number format)"; break;
            case 2: errorHeader = "Error in Search Input!(Date format)"; break;
            case 3: errorHeader = "Error in Search Input!"; break;
        }
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(errorHeader);
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }

    public void setMain(Main main) {
        this.main = main;
    }
}
