package sample.AccountsPage;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.Main;

import java.util.ArrayList;

/**
 * Created by Kai Jun on 13/4/2017.
 */
public class MyaccController {

    private Main main;
    private ObservableList<Object> row = FXCollections.observableArrayList();
    private ObservableList<ObservableList> data = FXCollections.observableArrayList();;
    private ArrayList<ArrayList> rowString;
    private ArrayList<String> rowValues;

    @FXML
    private Button backButton;
    @FXML
    private CheckBox inputcb;
    @FXML
    private CheckBox outputcb;
    @FXML
    private Label inputTotal;
    @FXML
    private Label outputTotal;
    @FXML
    private TableView transactionTable;
    @FXML
    private TableColumn payer;
    @FXML
    private TableColumn receiver;
    @FXML
    private TableColumn amount;
    @FXML
    private TableColumn date;
    @FXML
    private TableColumn trantype;
    @FXML
    private TableColumn client;
    @FXML
    private TableColumn payer_loan;
    @FXML
    private TableColumn receiver_loan;
    @FXML
    private TableColumn amount_loan;
    @FXML
    private TableColumn date_loan;
    @FXML
    private TableColumn trantype_loan;
    @FXML
    private TableColumn client_loan;

    @FXML
    private TableView loansTable;

    public MyaccController(){

    }

    @FXML
    public void initialize(){

        payer.setCellValueFactory(
                new PropertyValueFactory<Transactions, String>("payer"));
        receiver.setCellValueFactory(
                new PropertyValueFactory<Transactions, String>("receiver"));
        amount.setCellValueFactory(
                new PropertyValueFactory<Transactions, String>("amount"));
        date.setCellValueFactory(
                new PropertyValueFactory<Transactions, String>("date"));
        trantype.setCellValueFactory(
                new PropertyValueFactory<Transactions, String>("trantype"));
        client.setCellValueFactory(
                new PropertyValueFactory<Transactions, String>("client"));
        ProcessQuery.retrievePayments(transactionTable);

        payer_loan.setCellValueFactory(
                new PropertyValueFactory<Transactions, String>("payer"));
        receiver_loan.setCellValueFactory(
                new PropertyValueFactory<Transactions, String>("receiver"));
        amount_loan.setCellValueFactory(
                new PropertyValueFactory<Transactions, String>("amount"));
        date_loan.setCellValueFactory(
                new PropertyValueFactory<Transactions, String>("date"));
        trantype_loan.setCellValueFactory(
                new PropertyValueFactory<Transactions, String>("trantype"));
        client_loan.setCellValueFactory(
                new PropertyValueFactory<Transactions, String>("client"));
        ProcessQuery.retrieveLoans(loansTable);

        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                main.getAccountsCtrl().resetAccountsPage();
            }
        });

        inputcb.setSelected(true);
        outputcb.setSelected(true);

        inputcb.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> ov,
                                Boolean old_val, Boolean new_val) {
                if(inputcb.isSelected()){
                    transactionTable.getItems().clear();
                    if(!outputcb.isSelected()) {
                        System.out.println("In selected & Out deselected");
                        ProcessQuery.retrievePaymentsIn(transactionTable);
                    }
                    else {
                        System.out.println("Both selected");
                        ProcessQuery.retrievePayments(transactionTable);
                    }
                } else {
                    System.out.println("In deselected");
                    transactionTable.getItems().clear();
                    if(outputcb.isSelected()){
                        System.out.println("In deselected & Out selected");
                        ProcessQuery.retrievePaymentsOut(transactionTable);
                    }
                }

            }
        });

        outputcb.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> ov,
                                Boolean old_val, Boolean new_val) {
                if(outputcb.isSelected()){
                    System.out.println("Out selected");
                    transactionTable.getItems().clear();
                    if(!inputcb.isSelected())
                        ProcessQuery.retrievePaymentsOut(transactionTable);
                    else
                        ProcessQuery.retrievePayments(transactionTable);
                } else {
                    System.out.println("out delected");
                    transactionTable.getItems().clear();
                    if(inputcb.isSelected())
                        ProcessQuery.retrievePaymentsIn(transactionTable);
                }
            }
        });

    }

    private void resetColumns(){
        transactionTable.getColumns().clear();

        payer = new TableColumn();
        receiver = new TableColumn();
        amount = new TableColumn();
        date = new TableColumn();
        trantype = new TableColumn();
        client = new TableColumn();


        payer.setCellValueFactory(
                new PropertyValueFactory<Transactions, String>("payer"));
        receiver.setCellValueFactory(
                new PropertyValueFactory<Transactions, String>("receiver"));
        amount.setCellValueFactory(
                new PropertyValueFactory<Transactions, String>("amount"));
        date.setCellValueFactory(
                new PropertyValueFactory<Transactions, String>("date"));
        trantype.setCellValueFactory(
                new PropertyValueFactory<Transactions, String>("trantype"));
        client.setCellValueFactory(
                new PropertyValueFactory<Transactions, String>("client"));

        transactionTable.getColumns().addAll(payer,receiver,amount,date,trantype,client);
    }

    public void setMain(Main main){
        this.main = main;
    }
}
