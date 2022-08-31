package sample.DetailsPage;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import sample.Database.DBConnection;
import sample.Database.DisplayDatabase;
import sample.Database.GetDatabaseInfo;
import sample.Main;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Created by Kai Jun on 14/4/2017.
 */
public class DetailsController {

    private Main main;
    private ToggleGroup group;
    private Tooltip tooltip = new Tooltip();

    @FXML
    private RadioButton searchall;
    @FXML
    private RadioButton searchspecified;
    @FXML
    private TextField searchbar;
    @FXML
    private ComboBox selectTable;
    @FXML
    private Label help;
    @FXML
    private TableView dataTable;
    @FXML
    private Button logout;

    @FXML
    private Circle addnew;


    public DetailsController(){

    }

    @FXML
    public void initialize(){

        group = new ToggleGroup();
        searchall.setToggleGroup(group);
        searchall.setUserData("All");
        searchall.setSelected(true);
        searchspecified.setToggleGroup(group);
        searchspecified.setUserData("Specified");

        DisplayDatabase.buildData(dataTable, "Client");

        selectTable.getItems().addAll(
                "Client","Vehicle","AccidentRecord","Referrer","ReferredBy","Insurer",
                "Payment","MyPaymentDetail","Individual","Commercial","Relationship", "Driver"
        );
        selectTable.setValue("Client");
        selectTable.getSelectionModel().selectedItemProperty().addListener( new ChangeListener() {
            @Override
            public void changed(ObservableValue ov, Object oldValue, Object newValue) {
                DisplayDatabase.buildData(dataTable,newValue.toString());
                searchbar.setText("");
            }
        });

        /*
        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
            public void changed(ObservableValue<? extends Toggle> ov,
                                Toggle old_toggle, Toggle new_toggle) {
                if (group.getSelectedToggle() != null) {
                    if(group.getSelectedToggle().getUserData().toString().equals("All"))
                        sendQueryforAll();
                    else
                        sendQueryforSpecified();
                }
            }
        });
        */

        searchbar.textProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("textfield changed from " + oldValue + " to " + newValue);
            if(newValue.equals("")){
                DisplayDatabase.buildData(dataTable,selectTable.getValue().toString());
            }
            else if(searchall.isSelected() && !searchspecified.isSelected()){
                sendQueryforAll(selectTable.getValue().toString(),newValue);
            } else {
                ArrayList<String[]> input = new ArrayList<>();
                Scanner scanner = new Scanner(newValue);
                scanner.useDelimiter(" AND ");
                while (scanner.hasNext()){
                    String temp = scanner.next();
                    if(temp == ""){
                        continue;
                    } else {
                        Scanner scanner1 = new Scanner(temp);
                        Pattern p = Pattern.compile("\\s?:\\s?");
                        scanner1.useDelimiter(p);
                        String[] subInput = new String[2];
                        int i = 0;
                        while (scanner1.hasNext()) {
                            //first input should be column name
                            subInput[i] = scanner1.next();
                            i++;
                        }
                        if (i == 1) {
                            subInput[i] = "";
                        }
                        input.add(subInput);
                    }
                }
                sendQueryforSpecified(selectTable.getValue().toString(),input);
            }
        });

        addnew.setOnMouseEntered(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                addnew.setStyle("-fx-background-color:#dae7f3;");
            }
        });
        addnew.setOnMouseExited(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                addnew.setStyle("-fx-background-color:transparent;");
                tooltip.setText("");
                tooltip.hide();
            }
        });
        addnew.setOnMouseMoved(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                tooltip.setText("Add a new row to this table");

                Node node = (Node) event.getSource();
                tooltip.show(node, event.getScreenX() + 50, event.getScreenY());
            }

        });

        addnew.setOnMouseClicked((MouseEvent mouseEvent) -> {
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                if (mouseEvent.getClickCount() == 1 || mouseEvent.getClickCount() == 2) {
                    main.showNewDialog(selectTable.getValue().toString());
                }
            }
        });

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

        dataTable.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                    Node node = ((Node) event.getTarget()).getParent();
                    TableRow row;
                    if (node instanceof TableRow) {
                        row = (TableRow) node;
                    } else {
                        // clicking on text part
                        row = (TableRow) node.getParent();
                    }
                    main.showNewDialog(selectTable.getValue().toString(), row.getItem().toString(), dataTable);
                }
            }
        });

        help.setOnMouseClicked((MouseEvent mouseEvent) -> {
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                if (mouseEvent.getClickCount() == 1 || mouseEvent.getClickCount() == 2) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Dialog");
                    alert.setHeaderText("Help: User Guide");
                    alert.setContentText("To search in specified fields, enter [COLUMN-NAME]:[SEARCH]\n\n" +
                            "Where COLUMN-NAME is the field to search in and SEARCH is the value which you want to find");

                    alert.showAndWait();
                }
            }
        });
    }

    private void sendQueryforAll(String table, String query){
        System.out.println("In all");
        DisplayDatabase.buildDetailsData(dataTable,table,query);
    }

    private void sendQueryforSpecified(String table, ArrayList<String[]> input){
        System.out.println("In specified");
        ArrayList<String> columnHeaders = GetDatabaseInfo.obtainColumnInfo(table).get(0);
        for(int i = 0; i < input.size(); i++) {
            String[] subInput = input.get(i);
            boolean match = false;
            for(int j = 0; j < columnHeaders.size(); j++){
                if(subInput[0].equals(columnHeaders.get(j))){
                    match = true;
                }
            }
            if(!match || subInput[1].equals("")){
                input.remove(i);
                i--;
            }
        }

        //for testing
        for(int i = 0; i < input.size(); i++) {
            String[] subInput = input.get(i);
            System.out.println(subInput[0] + ", " + subInput[1]);
        }

        if(!input.isEmpty())
            DisplayDatabase.buildDetailsData(dataTable, table, input);
    }

    public void setMain(Main main) {
        this.main = main;
    }

}
