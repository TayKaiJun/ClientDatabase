package sample.Database;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Kai Jun on 12/4/2017.
 */
public class DisplayDatabase {

    private static ObservableList<ObservableList> data;

    public static void buildData(TableView tableview) {
        Connection c;
        data = FXCollections.observableArrayList();
        try{
            c = DBConnection.connect();
            String SQL = "";
            SQL = "SELECT DISTINCT c.clientid as ClientID, vehicleno as \"VehicleNo.\", t.name as Name, CASE WHEN c.referrerid IS NULL THEN NULL ELSE referrername END as Referrer, insurername as Insurer, c.effectivedate as \"Effective Date\", c.expirydate as \"Expiry Date\"\n" +
                        "FROM client c, vehicle, insurer, referrer, (SELECT clientid, name FROM individual UNION SELECT clientid, companyname FROM commercial) t\n" +
                        "WHERE c.clientid = vehicle.clientid AND c.insurerid = insurer.insurerid AND c.clientid = t.clientid AND \n" +
                        "CASE WHEN c.referrerid IS NULL THEN TRUE ELSE c.referrerid = referrer.referrerid END\n" +
                        "ORDER BY c.clientid;";

            //ResultSet
            ResultSet rs = c.createStatement().executeQuery(SQL);

            populateTable(rs,tableview,data);
            System.out.println("Generating all data");


        }catch(Exception e){
            e.printStackTrace();
            showErrorMessage(0,e.getMessage());
        }
    }

    public static void buildData(TableView tableview, String query, int field) {
        String SQL = "SELECT DISTINCT c.clientid as ClientID, vehicleno as \"VehicleNo.\", t.name as Name, CASE WHEN c.referrerid IS NULL THEN NULL ELSE referrername END as Referrer, insurername as Insurer, c.effectivedate as \"Effective Date\", c.expirydate as \"Expiry Date\"\n" +
                "FROM client c, vehicle, insurer, referrer, (SELECT clientid, name FROM individual UNION SELECT clientid, companyname FROM commercial) t\n" +
                "WHERE c.clientid = vehicle.clientid AND c.insurerid = insurer.insurerid AND c.clientid = t.clientid AND \n" +
                "CASE WHEN c.referrerid IS NULL THEN TRUE ELSE c.referrerid = referrer.referrerid END\n" +
                "ORDER BY c.clientid;";
        switch (field) {
            //query is on clientid
            case 0: SQL = "SELECT DISTINCT c.clientid as ClientID, vehicleno as \"VehicleNo.\", t.name as Name, CASE WHEN c.referrerid IS NULL THEN NULL ELSE referrername END as Referrer, insurername as Insurer, c.effectivedate as \"Effective Date\", c.expirydate as \"Expiry Date\"\n" +
                    "FROM client c, vehicle, insurer, referrer, (SELECT clientid, name FROM individual UNION SELECT clientid, companyname FROM commercial) t\n" +
                    "WHERE c.clientid = vehicle.clientid AND c.insurerid = insurer.insurerid AND c.clientid = t.clientid AND \n" +
                    "CASE WHEN c.referrerid IS NULL THEN TRUE ELSE c.referrerid = referrer.referrerid END" +
                    " AND c.clientid = " + query + " \n" +
                    "ORDER BY c.clientid;"; break;
            //query is on vehicleno
            case 1: SQL = "SELECT DISTINCT c.clientid as ClientID, vehicleno as \"VehicleNo.\", t.name as Name, CASE WHEN c.referrerid IS NULL THEN NULL ELSE referrername END as Referrer, insurername as Insurer, c.effectivedate as \"Effective Date\", c.expirydate as \"Expiry Date\"\n" +
                    "FROM client c, vehicle, insurer, referrer, (SELECT clientid, name FROM individual UNION SELECT clientid, companyname FROM commercial) t\n" +
                    "WHERE c.clientid = vehicle.clientid AND c.insurerid = insurer.insurerid AND c.clientid = t.clientid AND \n" +
                    "CASE WHEN c.referrerid IS NULL THEN TRUE ELSE c.referrerid = referrer.referrerid END" +
                    " AND vehicle.vehicleno LIKE '%" + query + "%' \n" +
                    "ORDER BY c.clientid;"; break;
            //query is on name
            case 2: SQL = "SELECT DISTINCT c.clientid as ClientID, vehicleno as \"VehicleNo.\", t.name as Name, CASE WHEN c.referrerid IS NULL THEN NULL ELSE referrername END as Referrer, insurername as Insurer, c.effectivedate as \"Effective Date\", c.expirydate as \"Expiry Date\"\n" +
                    "FROM client c, vehicle, insurer, referrer, (SELECT clientid, name FROM individual UNION SELECT clientid, companyname FROM commercial) t\n" +
                    "WHERE c.clientid = vehicle.clientid AND c.insurerid = insurer.insurerid AND c.clientid = t.clientid AND \n" +
                    "CASE WHEN c.referrerid IS NULL THEN TRUE ELSE c.referrerid = referrer.referrerid END" +
                    " AND t.name LIKE '%" + query + "%' \n" +
                    "ORDER BY c.clientid;"; break;
            //query is on referrer
            case 3: SQL = "SELECT DISTINCT c.clientid as ClientID, vehicleno as \"VehicleNo.\", t.name as Name, CASE WHEN c.referrerid IS NULL THEN NULL ELSE referrername END as Referrer, insurername as Insurer, c.effectivedate as \"Effective Date\", c.expirydate as \"Expiry Date\"\n" +
                    "FROM client c, vehicle, insurer, referrer, (SELECT clientid, name FROM individual UNION SELECT clientid, companyname FROM commercial) t\n" +
                    "WHERE c.clientid = vehicle.clientid AND c.insurerid = insurer.insurerid AND c.clientid = t.clientid AND \n" +
                    "CASE WHEN c.referrerid IS NULL THEN TRUE ELSE c.referrerid = referrer.referrerid END" +
                    " AND referrername LIKE '%" + query + "%' \n" +
                    "ORDER BY c.clientid;"; break;
            //query is on insurer
            case 4: SQL = "SELECT DISTINCT c.clientid as ClientID, vehicleno as \"VehicleNo.\", t.name as Name, CASE WHEN c.referrerid IS NULL THEN NULL ELSE referrername END as Referrer, insurername as Insurer, c.effectivedate as \"Effective Date\", c.expirydate as \"Expiry Date\"\n" +
                    "FROM client c, vehicle, insurer, referrer, (SELECT clientid, name FROM individual UNION SELECT clientid, companyname FROM commercial) t\n" +
                    "WHERE c.clientid = vehicle.clientid AND c.insurerid = insurer.insurerid AND c.clientid = t.clientid AND \n" +
                    "CASE WHEN c.referrerid IS NULL THEN TRUE ELSE c.referrerid = referrer.referrerid END" +
                    " AND insurername LIKE '%" + query + "%' \n" +
                    "ORDER BY c.clientid;"; break;
            //query is on effective date

            case 5: SQL = "SELECT DISTINCT c.clientid as ClientID, vehicleno as \"VehicleNo.\", t.name as Name, CASE WHEN c.referrerid IS NULL THEN NULL ELSE referrername END as Referrer, insurername as Insurer, c.effectivedate as \"Effective Date\", c.expirydate as \"Expiry Date\"\n" +
                    "FROM client c, vehicle, insurer, referrer, (SELECT clientid, name FROM individual UNION SELECT clientid, companyname FROM commercial) t\n" +
                    "WHERE c.clientid = vehicle.clientid AND c.insurerid = insurer.insurerid AND c.clientid = t.clientid AND \n" +
                    "CASE WHEN c.referrerid IS NULL THEN TRUE ELSE c.referrerid = referrer.referrerid END" +
                    " AND c.effectivedate = STR_TO_DATE(\"" + query + "\",'%d/%m/%Y')" + " \n" +
                    "ORDER BY c.clientid;"; break;
            //query is on expiry date
            case 6: SQL = "SELECT DISTINCT c.clientid as ClientID, vehicleno as \"VehicleNo.\", t.name as Name, CASE WHEN c.referrerid IS NULL THEN NULL ELSE referrername END as Referrer, insurername as Insurer, c.effectivedate as \"Effective Date\", c.expirydate as \"Expiry Date\"\n" +
                    "FROM client c, vehicle, insurer, referrer, (SELECT clientid, name FROM individual UNION SELECT clientid, companyname FROM commercial) t\n" +
                    "WHERE c.clientid = vehicle.clientid AND c.insurerid = insurer.insurerid AND c.clientid = t.clientid AND \n" +
                    "CASE WHEN c.referrerid IS NULL THEN TRUE ELSE c.referrerid = referrer.referrerid END" +
                    " AND c.expirydate = STR_TO_DATE(\"" + query + "\",'%d/%m/%Y')" + " \n" +
                    "ORDER BY c.clientid;"; break;
        }
        Connection c;
        data = FXCollections.observableArrayList();
        try{
            c = DBConnection.connect();
            //ResultSet
            ResultSet rs = c.createStatement().executeQuery(SQL);

            System.out.println("Generating Queried Data");

            populateTable(rs,tableview,data);

        }catch(Exception e){
            e.printStackTrace();
            showErrorMessage(0,e.getMessage());
        }
    }

    public static void buildData(TableView tableView, String currentDate, String targetDate){
        Connection c;
        data = FXCollections.observableArrayList();
        try{
            c = DBConnection.connect();
            String SQL = "SELECT DISTINCT c.clientid as ClientID, vehicleno as \"VehicleNo.\", t.name as Name, CASE WHEN c.referrerid IS NULL THEN NULL ELSE referrername END as referrer, insurername, c.effectivedate, c.expirydate, renewalstatus, website \n" +
                    "FROM client c, vehicle, insurer, referrer, (SELECT clientid, name FROM individual UNION SELECT clientid, companyname FROM commercial) t\n" +
                    "WHERE c.clientid = vehicle.clientid AND c.insurerid = insurer.insurerid AND c.clientid = t.clientid AND \n" +
                    "CASE WHEN c.referrerid IS NULL THEN TRUE ELSE c.referrerid = referrer.referrerid END\n" +
                    " AND c.expirydate > '" + currentDate + "' AND c.expirydate < '" + targetDate + "'" +
                    "ORDER BY c.expirydate;";

            //ResultSet
            ResultSet rs = c.createStatement().executeQuery(SQL);

            populateTable(rs,tableView,data);
            System.out.println("Generating data within the expiry date range");


        }catch(Exception e){
            e.printStackTrace();
            showErrorMessage(0,e.getMessage());
        }
    }

    public static void buildData(TableView tableview, String table) {
        Connection c;
        data = FXCollections.observableArrayList();

        try{
            c = DBConnection.connect();
            String SQL = "";
            SQL = "SELECT * FROM " + table + ";";

            //ResultSet
            ResultSet rs = c.createStatement().executeQuery(SQL);

            populateTable(rs,tableview,data);
            System.out.println("Generating all data");


        }catch(Exception e){
            e.printStackTrace();
            showErrorMessage(0,e.getMessage());
        }
    }

    public static void buildDetailsData(TableView tableView, String table, String query){
        Connection c;
        data = FXCollections.observableArrayList();
        try{
            c = DBConnection.connect();
            StringBuilder SQL = new StringBuilder("SELECT * FROM ");
            SQL.append(table);
            SQL.append(" WHERE ");
            ArrayList<String> columnHeader = GetDatabaseInfo.obtainColumnInfo(table).get(0);
            for(int i = 0; i < columnHeader.size(); i++){
                //add the field into the where clause
                SQL.append(columnHeader.get(i));
                SQL.append(" LIKE '%");
                SQL.append(query);
                if(i < columnHeader.size()-1){
                    //add AND if theres more fields to check through
                    SQL.append("%' OR ");
                } else{
                    SQL.append("%';");
                }
            }


            //ResultSet
            ResultSet rs = c.createStatement().executeQuery(SQL.toString());

            populateTable(rs,tableView,data);
            //System.out.println("Generating data within the expiry date range");


        }catch(Exception e){
            e.printStackTrace();
            showErrorMessage(0,e.getMessage());
        }
    }

    public static void buildDetailsData(TableView tableView, String table, ArrayList<String[]> input){
        Connection c;
        data = FXCollections.observableArrayList();
        try{
            c = DBConnection.connect();
            StringBuilder SQL = new StringBuilder("SELECT * FROM ");
            SQL.append(table);
            SQL.append(" WHERE ");
            for(int i = 0; i < input.size(); i++){
                String[] subInput = input.get(i);
                SQL.append(subInput[0]);
                SQL.append(" LIKE '%");
                SQL.append(subInput[1]);
                if(i < input.size() - 1){
                    SQL.append("%' AND ");
                } else{
                    SQL.append("%';");
                }
            }


            //ResultSet
            ResultSet rs = c.createStatement().executeQuery(SQL.toString());

            populateTable(rs,tableView,data);
            //System.out.println("Generating data within the expiry date range");


        }catch(Exception e){
            e.printStackTrace();
            showErrorMessage(0,e.getMessage());
        }
    }

    private static void populateTable(ResultSet rs,TableView tableView, ObservableList<ObservableList> data){
        tableView.getColumns().clear();
        try {
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                //We are using non property style for making dynamic table
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });
                tableView.getColumns().addAll(col);
            }

            /********************************
             * Data added to ObservableList *
             ********************************/

            while (rs.next()) {
                //Iterate Row
                ObservableList<Object> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    //Iterate Column
                    if(rs.getMetaData().getColumnName(i).equals("renewalstatus")){
                        if(rs.getString(i).equals("0")) {
                            row.add("Not renewed (0)");
                            continue;
                        } else{
                            row.add("Renewed (1)");
                            continue;
                        }
                    }

                    if(rs.getMetaData().getColumnName(i).equals("clienttype")){
                        if(rs.getString(i).equals("0")) {
                            row.add("Private (0)");
                            continue;
                        } else{
                            row.add("Commercial (1)");
                            continue;
                        }
                    }

                    if (rs.getString(i) != null) {
                        row.add(rs.getString(i));
                        continue;
                    } else{
                        row.add("-");
                        continue;
                    }
                }
                //System.out.println("Row [1] added "+row );
                data.add(row);

            }
            //FINALLY ADDED TO TableView
            tableView.setItems(data);
        } catch (Exception e1){
            showErrorMessage(1,e1.getMessage());
        }
    }

    private static void showErrorMessage(int errorType, String errorMessage){
        String errorHeader = "";
        switch (errorType){
            case 0: errorHeader = "Error on Building Data!"; break;
            case 1: errorHeader = "Error in Loading Data into Table!"; break;
            //case 2: errorHeader = "Error in closing file writer!"; break;
            //case 3: errorHeader = "Error in opening output file!"; break;
        }
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(errorHeader);
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }
}
