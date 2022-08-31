package sample.DetailsPage;

import javafx.scene.control.Alert;
import sample.Database.DBConnection;
import sample.Database.GetDatabaseInfo;

import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Kai Jun on 14/4/2017.
 */
public class ItemProcesses {

    public static ArrayList<String> getFKvalues(String table, String fk){

        Connection conn;
        ArrayList<String> foreignKeys = new ArrayList<>();
        try {
            conn = DBConnection.connect();
            String tupleName = "";
            switch (fk){
                case "clientid": tupleName = "clientid"; break;
                case "referrerid": tupleName = "referrername"; break;
                case "insurerid": tupleName = "insurername"; break;
                case "rpaymenttype":
                case "myptype": fk = "typeid"; tupleName = "cardno"; break;
                case "ic": tupleName = "name"; break;
            }

            String SQL;
            if(!fk.equals(tupleName)) {
                SQL = "SELECT " + fk + " , " + tupleName + " FROM " + table + ";";
            } else{
                SQL = "SELECT " + fk + " FROM " + table + ";";
            }

            System.out.println(SQL);

            ResultSet rs = conn.createStatement().executeQuery(SQL);

            while (rs.next()) {
                StringBuilder rowText = new StringBuilder("");
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    if(i == 1) {
                        rowText.append(rs.getString(i));
                    }
                    else{
                        rowText.append(": ");
                        rowText.append(rs.getString(i));
                    }
                }
                foreignKeys.add(rowText.toString());
            }

            return foreignKeys;

        } catch (SQLException e){
            e.printStackTrace();
            showErrorMessage(0,e.getMessage());
        }
        return foreignKeys;
    }

    public static boolean pushData(String table, String[] data){

        Connection conn;
        try {
            conn = DBConnection.connect();
            Statement stmt = conn.createStatement();

            StringBuilder SQL = new StringBuilder("INSERT INTO ");
            SQL.append(table);
            SQL.append(" VALUES (\"");

            for(int i = 0; i < data.length; i++){
                if(i < data.length-1) {
                    SQL.append(data[i]);
                    SQL.append("\", \"");
                } else{
                    SQL.append(data[i]);
                    SQL.append("\")");
                }
            }
            System.out.println(SQL.toString());
            stmt.execute(SQL.toString());
            conn.close();
            return true;
        } catch (SQLException e){
            e.printStackTrace();
            showErrorMessage(1,e.getMessage());
            return false;
        }
    }

    public static boolean updateData(String table, String[] data, String[][] pkValues){
        ArrayList<String> fieldName = GetDatabaseInfo.obtainColumnInfo(table).get(0);
        Connection conn;
        try {
            conn = DBConnection.connect();
            Statement stmt = conn.createStatement();

            if(data.length == fieldName.size()) {
                StringBuilder SQL = new StringBuilder("UPDATE ");
                SQL.append(table);
                SQL.append(" SET ");

                for (int i = 0; i < data.length; i++) {
                    SQL.append(fieldName.get(i));
                    if(!data[i].equals("NULL")) {
                        SQL.append(" = \"");
                        SQL.append(data[i]);
                        SQL.append("\"");
                    } else {
                        SQL.append(" = ");
                        SQL.append(data[i]);
                    }
                    if (i < data.length - 1) {
                        SQL.append(" , ");
                    }
                }

                SQL.append(" WHERE ");
                for(int i = 0; i < pkValues[0].length; i++){
                    SQL.append(pkValues[0][i]);
                    SQL.append(" = ");
                    SQL.append(pkValues[1][i]);
                    if (i < pkValues[0].length - 1) {
                        SQL.append(" , ");
                    }
                }
                SQL.append(";");

                System.out.println(SQL.toString());
                stmt.execute(SQL.toString());
            }
            conn.close();
            return true;
        } catch (SQLException e){
            e.printStackTrace();
            showErrorMessage(1,e.getMessage());
            return false;
        }
    }

    public static boolean deleteData(String table, String[] data, String[][] pkValues){

        Connection conn;
        try {
            conn = DBConnection.connect();
            Statement stmt = conn.createStatement();

            StringBuilder SQL = new StringBuilder("DELETE FROM ");
            SQL.append(table);
            SQL.append(" WHERE ");

            for(int i = 0; i < pkValues[0].length; i++){
                SQL.append(pkValues[0][i]);
                SQL.append(" = ");
                SQL.append(pkValues[1][i]);
                if (i < pkValues[0].length - 1) {
                    SQL.append(" , ");
                }
            }
            SQL.append(";");

            System.out.println(SQL.toString());
            stmt.execute(SQL.toString());
            conn.close();
            return true;
        } catch (SQLException e){
            e.printStackTrace();
            showErrorMessage(1,e.getMessage());
            return false;
        }
    }

    private static void showErrorMessage(int errorType, String errorMessage){
        String errorHeader = "";
        switch (errorType){
            case 0: errorHeader = "Error on getting foreign key values!"; break;
            case 1: errorHeader = "Error in saving data to database!"; break;
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
