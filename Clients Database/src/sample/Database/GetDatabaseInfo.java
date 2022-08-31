package sample.Database;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import sample.AccountsPage.ExpiringController;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Kai Jun on 13/4/2017.
 */
public class GetDatabaseInfo {

    //private static ObservableList<ObservableList> data;

    public static ArrayList<ArrayList> obtainColumnInfo(String table) {
        Connection c;
        ArrayList<String> columnHeader = new ArrayList<>(); //first set of array is name
        ArrayList<String> columnType = new ArrayList<>(); //second is data type
        try{
            c = DBConnection.connect();
            String SQL = "SELECT * FROM " + table + ";";
            //ResultSet
            ResultSet rs = c.createStatement().executeQuery(SQL);

            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                final int j = i;
                String columnData = rs.getMetaData().getColumnName(i + 1);
                Scanner scn = new Scanner(columnData);
                scn.useDelimiter(":");
                int n = 0;
                if(scn.hasNext()) {
                    switch (n){
                        case 0: columnHeader.add(scn.next()); n++; break;
                        case 1: columnType.add(scn.next()); n++; break;
                    }
                }
            }

            System.out.println("Generating all data");

            ArrayList<ArrayList> columnData = new ArrayList<ArrayList>(2)  ;
            columnData.add(columnHeader);
            columnData.add(columnType);

            return columnData;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<String> obtainPrimaryKeys(String table){
        Connection conn;
        ArrayList<String> primaryKeys = new ArrayList<>();
        try {
            conn = DBConnection.connect();
            ResultSet rs = null;
            DatabaseMetaData meta = conn.getMetaData();
            // The Oracle database stores its table names as Upper-Case,
            // if you pass a table name in lowercase characters, it will not work.
            // MySQL database does not care if table name is uppercase/lowercase.
            //
            rs = meta.getPrimaryKeys(null, null, table);


            while (rs.next()) {
                String columnName = rs.getString("COLUMN_NAME");
                //System.out.println("getPrimaryKeys(): columnName=" + columnName);
                primaryKeys.add(columnName);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return primaryKeys;
    }

    public static ArrayList<String> obtainForeignKeys(String table){
        Connection conn;
        ArrayList<String> foreignKeys = new ArrayList<>();
        try {
            conn = DBConnection.connect();
            ResultSet rs = null;
            DatabaseMetaData meta = conn.getMetaData();
            // The Oracle database stores its table names as Upper-Case,
            // if you pass a table name in lowercase characters, it will not work.
            // MySQL database does not care if table name is uppercase/lowercase.
            //

            rs = meta.getImportedKeys(conn.getCatalog(), null, table);
            while (rs.next()) {
                String fkTableName = rs.getString("FKTABLE_NAME");
                String fkColumnName = rs.getString("FKCOLUMN_NAME");
                int fkSequence = rs.getInt("KEY_SEQ");
                //System.out.println("getExportedKeys(): fkTableName="+fkTableName);
                //System.out.println("getExportedKeys(): fkColumnName="+fkColumnName);
                foreignKeys.add(fkColumnName);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return foreignKeys;
    }

}
