package sample.AccountsPage;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import sample.Database.DBConnection;
import sample.TabPaneController;

import java.sql.Array;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by Kai Jun on 15/4/2017.
 */
public class ProcessQuery {

    //private static ObservableList<ObservableList> data;
    private static String SQL  = "SELECT DISTINCT t.name as Name, grosspremium, insurername, \n" +
            "CASE WHEN c.referrerid IS NULL THEN NULL ELSE referrername END as Referrer, \n" +
            "CASE WHEN rpaymentdate IS NULL THEN NULL ELSE rpaymentdate END as ReferrerPDate, \n" +
            "CASE WHEN rpercent IS NULL THEN NULL ELSE rpercent END as Rcomm,\n" +
            "CASE WHEN mypdate IS NULL THEN NULL ELSE mypdate END as MyPaymentDate, \n" +
            "CASE WHEN clientpdate IS NULL THEN NULL ELSE clientpdate END as ClientPaymentDate\n" +
            "\n" +
            "FROM client c, insurer, referredby, payment, referrer, \n" +
            "(SELECT clientid, name FROM individual UNION SELECT clientid, companyname FROM commercial) t\n" +
            "\n" +
            "WHERE c.clientid = t.clientid AND c.insurerid = insurer.insurerid AND\n" +
            "CASE WHEN c.referrerid IS NULL THEN TRUE ELSE c.referrerid = referrer.referrerid END AND\n" +
            "c.clientid = referredby.clientid AND c.clientid = payment.clientid AND grosspremium IS NOT NULL\n" +
            "\n" +
            "UNION\n" +
            "\n" +
            "SELECT DISTINCT t.name as Name, grosspremium, insurername, \n" +
            "CASE WHEN c.referrerid IS NULL THEN NULL ELSE referrername END as Referrer, \n" +
            "CASE WHEN c.referrerid IS NULL THEN NULL ELSE rpaymentdate END as ReferrerPaymentDate, \n" +
            "CASE WHEN c.referrerid IS NULL THEN NULL ELSE rpercent END as Rcomm, \n" +
            "CASE WHEN mypdate IS NULL THEN NULL ELSE mypdate END as MyPaymentDate, \n" +
            "CASE WHEN clientpdate IS NULL THEN NULL ELSE clientpdate END as ClientPaymentDate\n" +
            "\n" +
            "FROM client c, insurer, referredby, payment, referrer, \n" +
            "(SELECT clientid, name FROM individual UNION SELECT clientid, companyname FROM commercial) t\n" +
            "\n" +
            "WHERE c.clientid = t.clientid AND c.insurerid = insurer.insurerid AND c.referrerid IS NULL AND c.clientid = payment.clientid AND grosspremium IS NOT NULL;";

    public static void retrievePayments(TableView tableView) {
        Connection c;
        ArrayList<ArrayList> row = new ArrayList<>();
        try{
            c = DBConnection.connect();

            //ResultSet
            ResultSet rs = c.createStatement().executeQuery(SQL);
            //Organise data first
            while (rs.next()) {
                //Iterate Row
                ArrayList<String> values = new ArrayList<>();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    if (rs.getString(i) != null) {
                        values.add(rs.getString(i));
                    } else {
                        values.add("-");
                    }
                }
                row.add(values);
            }

        }catch(Exception e){
            e.printStackTrace();
            showErrorMessage(0,e.getMessage());
        }

        ObservableList<Transactions> tableRow = FXCollections.observableArrayList();

        for(int i = 0; i < row.size(); i++){
            ArrayList<String> data = row.get(i);
            if(!data.get(3).equals("-") && !data.get(4).equals("-")){
                Transactions transactions = new Transactions();
                transactions.setPayer("TKS");
                transactions.setReceiver(data.get(3));
                try {
                    double amt = Double.parseDouble(data.get(1))*(Double.parseDouble(data.get(5)))/100.0;
                    amt = Math.round(amt * 100.0) / 100.0;
                    transactions.setAmount(amt);
                } catch (Exception e){
                    e.printStackTrace();
                    showErrorMessage(2,e.getMessage());
                }
                transactions.setDate(data.get(4));
                transactions.setTrantype("OUT");
                transactions.setClient(data.get(0));
                tableRow.add(transactions);
            }
            if(data.get(6).equals("-") && !data.get(7).equals("-")){
                Transactions transactions = new Transactions();
                transactions.setPayer(data.get(0));
                transactions.setReceiver(data.get(2));
                try {
                    transactions.setAmount(Double.parseDouble(data.get(1)));
                } catch (Exception e){
                    e.printStackTrace();
                    showErrorMessage(2,e.getMessage());
                }
                transactions.setDate(data.get(7));
                transactions.setTrantype("NIL");
                transactions.setClient(data.get(0));
                tableRow.add(transactions);
            }
            if(!data.get(6).equals("-") && !data.get(7).equals("-")){
                Transactions transactions = new Transactions();
                transactions.setPayer("TKS");
                transactions.setReceiver(data.get(2));
                try {
                    transactions.setAmount(Double.parseDouble(data.get(1)));
                } catch (Exception e){
                    e.printStackTrace();
                    showErrorMessage(2,e.getMessage());
                }
                transactions.setDate(data.get(6));
                transactions.setTrantype("OUT");
                transactions.setClient(data.get(0));
                tableRow.add(transactions);

                Transactions transactions1 = new Transactions();
                transactions1.setPayer(data.get(0));
                transactions1.setReceiver("TKS");
                try {
                    transactions1.setAmount(Double.parseDouble(data.get(1)));
                } catch (Exception e){
                    e.printStackTrace();
                    showErrorMessage(2,e.getMessage());
                }
                transactions1.setDate(data.get(7));
                transactions1.setTrantype("IN");
                transactions1.setClient(data.get(0));
                tableRow.add(transactions1);

            }

            Transactions transactions = new Transactions();
            transactions.setPayer(data.get(2));
            transactions.setReceiver("TKS");
            try {
                double amt = Double.parseDouble(data.get(1))*0.15;
                amt = Math.round(amt * 100.0) / 100.0;
                transactions.setAmount(amt);
            } catch (Exception e){
                e.printStackTrace();
                showErrorMessage(2,e.getMessage());
            }
            transactions.setDate("NIL");
            transactions.setTrantype("IN");
            transactions.setClient(data.get(0));
            tableRow.add(transactions);
        }


        tableView.setItems(tableRow);

    }

    public static void retrieveLoans(TableView tableView) {
        Connection c;
        ArrayList<ArrayList> row = new ArrayList<>();
        try{
            c = DBConnection.connect();

            //ResultSet
            ResultSet rs = c.createStatement().executeQuery(SQL);
            //Organise data first
            while (rs.next()) {
                //Iterate Row
                ArrayList<String> values = new ArrayList<>();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    if (rs.getString(i) != null) {
                        values.add(rs.getString(i));
                    } else {
                        values.add("-");
                    }
                }
                row.add(values);
            }

        }catch(Exception e){
            e.printStackTrace();
            showErrorMessage(0,e.getMessage());
        }

        ObservableList<Transactions> tableRow = FXCollections.observableArrayList();

        for(int i = 0; i < row.size(); i++){
            ArrayList<String> data = row.get(i);
            if(!data.get(3).equals("-") && data.get(4).equals("-")){
                Transactions transactions = new Transactions();
                transactions.setPayer("TKS");
                transactions.setReceiver(data.get(3));
                try {
                    double amt = Double.parseDouble(data.get(1))*(Double.parseDouble(data.get(5)))/100.0;
                    amt = Math.round(amt * 100.0) / 100.0;
                    transactions.setAmount(amt);
                } catch (Exception e){
                    e.printStackTrace();
                    showErrorMessage(2,e.getMessage());
                }
                transactions.setDate("UNPAID");
                transactions.setTrantype("IOU");
                transactions.setClient(data.get(0));
                tableRow.add(transactions);
            }
            if(!data.get(6).equals("-") && data.get(7).equals("-")){
                Transactions transactions = new Transactions();
                transactions.setPayer(data.get(0));
                transactions.setReceiver("TKS");
                try {
                    transactions.setAmount(Double.parseDouble(data.get(1)));
                } catch (Exception e){
                    e.printStackTrace();
                    showErrorMessage(2,e.getMessage());
                }
                transactions.setDate("UNPAID");
                transactions.setTrantype("UOI");
                transactions.setClient(data.get(0));
                tableRow.add(transactions);
            }
        }


        tableView.setItems(tableRow);

    }

    public static void retrievePaymentsIn(TableView tableView) {
        Connection c;
        ArrayList<ArrayList> row = new ArrayList<>();
        try{
            c = DBConnection.connect();

            //ResultSet
            ResultSet rs = c.createStatement().executeQuery(SQL);
            //Organise data first
            while (rs.next()) {
                //Iterate Row
                ArrayList<String> values = new ArrayList<>();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    if (rs.getString(i) != null) {
                        values.add(rs.getString(i));
                    } else {
                        values.add("-");
                    }
                }
                row.add(values);
            }

        }catch(Exception e){
            e.printStackTrace();
            showErrorMessage(0,e.getMessage());
        }

        ObservableList<Transactions> tableRow = FXCollections.observableArrayList();

        for(int i = 0; i < row.size(); i++){
            ArrayList<String> data = row.get(i);
            if(!data.get(3).equals("-") && !data.get(4).equals("-")){
                Transactions transactions = new Transactions();
                transactions.setPayer("TKS");
                transactions.setReceiver(data.get(3));
                try {
                    double amt = Double.parseDouble(data.get(1))*(Double.parseDouble(data.get(5)))/100.0;
                    amt = Math.round(amt * 100.0) / 100.0;
                    transactions.setAmount(amt);
                } catch (Exception e){
                    e.printStackTrace();
                    showErrorMessage(2,e.getMessage());
                }
                transactions.setDate(data.get(4));
                transactions.setTrantype("OUT");
                transactions.setClient(data.get(0));
                tableRow.add(transactions);
            }
            if(data.get(6).equals("-") && !data.get(7).equals("-")){
                Transactions transactions = new Transactions();
                transactions.setPayer(data.get(0));
                transactions.setReceiver(data.get(2));
                try {
                    transactions.setAmount(Double.parseDouble(data.get(1)));
                } catch (Exception e){
                    e.printStackTrace();
                    showErrorMessage(2,e.getMessage());
                }
                transactions.setDate(data.get(7));
                transactions.setTrantype("NIL");
                transactions.setClient(data.get(0));
                tableRow.add(transactions);
            }
            if(!data.get(6).equals("-") && !data.get(7).equals("-")){
                Transactions transactions = new Transactions();
                transactions.setPayer("TKS");
                transactions.setReceiver(data.get(2));
                try {
                    transactions.setAmount(Double.parseDouble(data.get(1)));
                } catch (Exception e){
                    e.printStackTrace();
                    showErrorMessage(2,e.getMessage());
                }
                transactions.setDate(data.get(6));
                transactions.setTrantype("OUT");
                transactions.setClient(data.get(0));
                tableRow.add(transactions);

                Transactions transactions1 = new Transactions();
                transactions1.setPayer(data.get(0));
                transactions1.setReceiver("TKS");
                try {
                    transactions1.setAmount(Double.parseDouble(data.get(1)));
                } catch (Exception e){
                    e.printStackTrace();
                    showErrorMessage(2,e.getMessage());
                }
                transactions1.setDate(data.get(7));
                transactions1.setTrantype("IN");
                transactions1.setClient(data.get(0));
                tableRow.add(transactions1);

            }

            Transactions transactions = new Transactions();
            transactions.setPayer(data.get(2));
            transactions.setReceiver("TKS");
            try {
                double amt = Double.parseDouble(data.get(1))*0.15;
                amt = Math.round(amt * 100.0) / 100.0;
                transactions.setAmount(amt);
            } catch (Exception e){
                e.printStackTrace();
                showErrorMessage(2,e.getMessage());
            }
            transactions.setDate("NIL");
            transactions.setTrantype("IN");
            transactions.setClient(data.get(0));
            tableRow.add(transactions);
        }

        ObservableList<Transactions> filtered = FXCollections.observableArrayList();
        for(int i = 0; i < tableRow.size(); i++){
            if(tableRow.get(i).getTrantype().equals("IN")) {
                filtered.add(tableRow.get(i));
            }
        }
        tableView.setItems(filtered);

    }

    public static void retrievePaymentsOut(TableView tableView) {
        Connection c;
        ArrayList<ArrayList> row = new ArrayList<>();
        try{
            c = DBConnection.connect();

            //ResultSet
            ResultSet rs = c.createStatement().executeQuery(SQL);
            //Organise data first
            while (rs.next()) {
                //Iterate Row
                ArrayList<String> values = new ArrayList<>();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    if (rs.getString(i) != null) {
                        values.add(rs.getString(i));
                    } else {
                        values.add("-");
                    }
                }
                row.add(values);
            }

        }catch(Exception e){
            e.printStackTrace();
            showErrorMessage(0,e.getMessage());
        }

        ObservableList<Transactions> tableRow = FXCollections.observableArrayList();

        for(int i = 0; i < row.size(); i++){
            ArrayList<String> data = row.get(i);
            if(!data.get(3).equals("-") && !data.get(4).equals("-")){
                Transactions transactions = new Transactions();
                transactions.setPayer("TKS");
                transactions.setReceiver(data.get(3));
                try {
                    double amt = Double.parseDouble(data.get(1))*(Double.parseDouble(data.get(5)))/100.0;
                    amt = Math.round(amt * 100.0) / 100.0;
                    transactions.setAmount(amt);
                } catch (Exception e){
                    e.printStackTrace();
                    showErrorMessage(2,e.getMessage());
                }
                transactions.setDate(data.get(4));
                transactions.setTrantype("OUT");
                transactions.setClient(data.get(0));
                tableRow.add(transactions);
            }
            if(data.get(6).equals("-") && !data.get(7).equals("-")){
                Transactions transactions = new Transactions();
                transactions.setPayer(data.get(0));
                transactions.setReceiver(data.get(2));
                try {
                    transactions.setAmount(Double.parseDouble(data.get(1)));
                } catch (Exception e){
                    e.printStackTrace();
                    showErrorMessage(2,e.getMessage());
                }
                transactions.setDate(data.get(7));
                transactions.setTrantype("NIL");
                transactions.setClient(data.get(0));
                tableRow.add(transactions);
            }
            if(!data.get(6).equals("-") && !data.get(7).equals("-")){
                Transactions transactions = new Transactions();
                transactions.setPayer("TKS");
                transactions.setReceiver(data.get(2));
                try {
                    transactions.setAmount(Double.parseDouble(data.get(1)));
                } catch (Exception e){
                    e.printStackTrace();
                    showErrorMessage(2,e.getMessage());
                }
                transactions.setDate(data.get(6));
                transactions.setTrantype("OUT");
                transactions.setClient(data.get(0));
                tableRow.add(transactions);

                Transactions transactions1 = new Transactions();
                transactions1.setPayer(data.get(0));
                transactions1.setReceiver("TKS");
                try {
                    transactions1.setAmount(Double.parseDouble(data.get(1)));
                } catch (Exception e){
                    e.printStackTrace();
                    showErrorMessage(2,e.getMessage());
                }
                transactions1.setDate(data.get(7));
                transactions1.setTrantype("IN");
                transactions1.setClient(data.get(0));
                tableRow.add(transactions1);

            }

            Transactions transactions = new Transactions();
            transactions.setPayer(data.get(2));
            transactions.setReceiver("TKS");
            try {
                double amt = Double.parseDouble(data.get(1))*0.15;
                amt = Math.round(amt * 100.0) / 100.0;
                transactions.setAmount(amt);
            } catch (Exception e){
                e.printStackTrace();
                showErrorMessage(2,e.getMessage());
            }
            transactions.setDate("NIL");
            transactions.setTrantype("IN");
            transactions.setClient(data.get(0));
            tableRow.add(transactions);
        }

        ObservableList<Transactions> filtered = FXCollections.observableArrayList();
        for(int i = 0; i < tableRow.size(); i++){
            if(tableRow.get(i).getTrantype().equals("OUT")) {
                filtered.add(tableRow.get(i));
            }
        }
        tableView.setItems(filtered);

    }

    private static void showErrorMessage(int errorType, String errorMessage){
        String errorHeader = "";
        switch (errorType){
            case 0: errorHeader = "Error on Building Data!"; break;
            case 1: errorHeader = "Error in Loading Data into Table!"; break;
            case 2: errorHeader = "Error in Parsing double!"; break;
            //case 3: errorHeader = "Error in opening output file!"; break;
        }
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(errorHeader);
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }
}
