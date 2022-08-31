package sample.Controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import sample.Main;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;

/**
 * Created by Kai Jun on 11/4/2017.
 */
public class MainpageController {

    private Main main;
    private String filepathstring;
    private String[] insertcommands = new String[12];
    private String[] tables = new String[12];
    private String sql;

    @FXML
    private Label filepath;
    @FXML
    private Label outputfilepath;
    @FXML
    private Label uploadstatus;
    @FXML
    private Button file;
    @FXML
    private Button upload;
    @FXML
    private Button export;
    @FXML
    private Button logout;

    public MainpageController() {
        tables[0] = "client";
        tables[1] = "vehicle";
        tables[2] = "insurer";
        tables[3] = "mypaymentdetail";
        tables[4] = "referrer";
        tables[5] = "referredby";
        tables[6] = "accidentrecord";
        tables[7] = "payment";
        tables[8] = "individual";
        tables[9] = "commercial";
        tables[10] = "driver";
        tables[11] = "relationship";
    }

    @FXML
    public void initialize() {

        file.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                DirectoryChooser directoryChooser = new DirectoryChooser();
                File selectedDirectory =
                        directoryChooser.showDialog(main.getPrimaryStage());

                if(selectedDirectory == null){
                    filepath.setText("No Directory selected");
                }else{
                    filepathstring = selectedDirectory.getAbsolutePath();
                    filepath.setText(filepathstring);
                    filepath.setOnMouseClicked((MouseEvent mouseEvent) -> {
                        if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                            if (mouseEvent.getClickCount() == 1 || mouseEvent.getClickCount() == 2) {
                                Desktop desktop = Desktop.getDesktop();
                                File file = new File(filepathstring);
                                try{
                                    desktop.open(file);
                                } catch (Exception e1){
                                    e1.printStackTrace();
                                }
                            }
                        }
                    });
                    createCommands();
                    upload.setDisable(false);
                }
            }
        });

        upload.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                Connection conn = null;
                try {
                    conn = DBConnection.getConnection();
                    Statement stmt = conn.createStatement();
                    defineSQL();

                    for(int i = 0; i < tables.length; i++){
                        stmt.execute("DELETE FROM " + tables[i]);
                    }
                    stmt.execute(sql);


                    for(int i = 0; i < insertcommands.length; i++){
                        stmt.execute(insertcommands[i]);
                    }

                    uploadstatus.setText("Bulk load successful!");
                    uploadstatus.setOnMouseClicked((MouseEvent mouseEvent) -> {
                        if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                            if (mouseEvent.getClickCount() == 1 || mouseEvent.getClickCount() == 2) {
                                //do nothing
                            }
                        }
                    });



                } catch (Exception e1){
                    e1.printStackTrace();
                    showErrorMessage(0,e1.getMessage());
                    uploadstatus.setText("Failed to load data");
                    uploadstatus.setOnMouseClicked((MouseEvent mouseEvent) -> {
                        if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                            if (mouseEvent.getClickCount() == 1 || mouseEvent.getClickCount() == 2) {
                                showErrorMessage(0,e1.getMessage());
                            }
                        }
                    });
                }
            }
        });

        export.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                BufferedWriter bw = null;
                FileWriter fw = null;
                String outputPath;

                try {

                    DirectoryChooser directoryChooser = new DirectoryChooser();
                    File selectedDirectory =
                            directoryChooser.showDialog(main.getPrimaryStage());

                    if(selectedDirectory == null){
                        return;
                    }else{
                        outputPath = selectedDirectory.getAbsolutePath();
                    }

                    File file = new File(outputPath + "\\exportedData.txt");

                    // if file exists, then delete it to remake a file
                    if (file.exists()) {
                        file.delete();
                        file.createNewFile();
                    }

                    fw = new FileWriter(file.getAbsoluteFile(), true);
                    bw = new BufferedWriter(fw);

                    for(int j = 0; j < tables.length; j++) {
                        bw.write("TABLE: " + tables[j] + "\r\n");
                        try {
                            Connection conn = DBConnection.getConnection();
                            Statement stmt = conn.createStatement();
                            ResultSet rs = stmt.executeQuery("SELECT * FROM " + tables[j]);

                            ResultSetMetaData rsmd = rs.getMetaData();
                            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                                if (i < rsmd.getColumnCount()) {
                                    bw.write(rsmd.getColumnName(i) + ":" + rsmd.getColumnTypeName(i) + "\t");
                                } else {
                                    bw.write(rsmd.getColumnName(i) + ":" + rsmd.getColumnTypeName(i) + "\r\n");
                                }
                            }

                            while (rs.next()) {
                                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                                    if (i < rsmd.getColumnCount()) {
                                        bw.write(rs.getString(rsmd.getColumnName(i)) + "\t");
                                    } else {
                                        bw.write(rs.getString(rsmd.getColumnName(i)) + "\r\n");
                                    }
                                }
                            }


                        } catch (Exception e1) {
                            e1.printStackTrace();
                            showErrorMessage(1, e1.getMessage());
                        }

                        bw.write("\r\n");
                    }

                    outputfilepath.setText(outputPath + "\\exportedData.txt");
                    outputfilepath.setOnMouseClicked((MouseEvent mouseEvent) -> {
                        if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                            if (mouseEvent.getClickCount() == 1 || mouseEvent.getClickCount() == 2) {
                                Desktop desktop = Desktop.getDesktop();
                                File outputFile = new File(outputPath + "\\exportedData.txt");
                                try {
                                    desktop.open(outputFile);
                                } catch (Exception e1){
                                    e1.printStackTrace();
                                    showErrorMessage(3,e1.getMessage());
                                }
                            }
                        }
                    });
                    System.out.println("Done");

                } catch (IOException e1) {
                    e1.printStackTrace();
                    showErrorMessage(1,e1.getMessage());
                } finally {
                    try {
                        if (bw != null) bw.close();
                        if (fw != null) fw.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        showErrorMessage(2,ex.getMessage());
                    }
                }
            }
        });

        logout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                Connection conn = null;
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

    private void defineSQL(){
        sql = "--OK\n" +
                "LOAD DATA INFILE 'C:/Users/Kai Jun/Documents/School Work/CS/Project/Data/CSV tables/referrer.csv' \n" +
                "INTO TABLE referrer \n" +
                "FIELDS TERMINATED BY ',' \n" +
                "ENCLOSED BY '\"'\n" +
                "LINES TERMINATED BY '\\n'\n" +
                "IGNORE 1 ROWS;\n" +
                "\n" +
                "--OK\n" +
                "LOAD DATA INFILE 'C:/Users/Kai Jun/Documents/School Work/CS/Project/Data/CSV tables/insurer.csv' \n" +
                "INTO TABLE insurer \n" +
                "FIELDS TERMINATED BY ',' \n" +
                "ENCLOSED BY '\"'\n" +
                "LINES TERMINATED BY '\\n'\n" +
                "IGNORE 1 ROWS;\n" +
                "\n" +
                "--OK\n" +
                "LOAD DATA INFILE 'C:/Users/Kai Jun/Documents/School Work/CS/Project/Data/CSV tables/mypaymentdetail.csv' \n" +
                "INTO TABLE mypaymentdetail \n" +
                "FIELDS TERMINATED BY ',' \n" +
                "ENCLOSED BY '\"'\n" +
                "LINES TERMINATED BY '\\n'\n" +
                "IGNORE 1 ROWS;\n" +
                "\n" +
                "--OK\n" +
                "LOAD DATA INFILE 'C:/Users/Kai Jun/Documents/School Work/CS/Project/Data/CSV tables/client.csv' \n" +
                "INTO TABLE client \n" +
                "FIELDS TERMINATED BY ',' \n" +
                "ENCLOSED BY '\"'\n" +
                "LINES TERMINATED BY '\\n'\n" +
                "IGNORE 1 ROWS\n" +
                "(clientid,referrerid,@effectivedate,@expirydate,premium,grosspremium,clienttype,insurerid,renewalstatus)\n" +
                "SET effectivedate = STR_TO_DATE(@effectivedate, '%d/%m/%Y'), expirydate = STR_TO_DATE(@expirydate, '%d/%m/%Y');\n" +
                "\n" +
                "--OK\n" +
                "LOAD DATA INFILE 'C:/Users/Kai Jun/Documents/School Work/CS/Project/Data/CSV tables/vehicle.csv' \n" +
                "INTO TABLE vehicle \n" +
                "FIELDS TERMINATED BY ',' \n" +
                "ENCLOSED BY '\"'\n" +
                "LINES TERMINATED BY '\\n'\n" +
                "IGNORE 1 ROWS\n" +
                "(vehicleno,clientid,vmake,vmodel,@rdtaxexpiry)\n" +
                "SET rdtaxexpiry = STR_TO_DATE(@rdtaxexpiry,'%d/%m/%Y');\n" +
                "--SET rdtaxexpiry = STR_TO_DATE(case when rdtaxexpiry = null then null else rdtaxexpiry end, '%c/%e/%y' );\n" +
                "\n" +
                "--OK\n" +
                "LOAD DATA INFILE 'C:/Users/Kai Jun/Documents/School Work/CS/Project/Data/CSV tables/accidentrecord.csv' \n" +
                "INTO TABLE accidentrecord \n" +
                "FIELDS TERMINATED BY ',' \n" +
                "ENCLOSED BY '\"'\n" +
                "LINES TERMINATED BY '\\n'\n" +
                "IGNORE 1 ROWS\n" +
                "(clientid,@doa,claimamount,remarks)\n" +
                "SET doa = STR_TO_DATE(@doa, '%d/%m/%Y');\n" +
                "\n" +
                "--OK\n" +
                "LOAD DATA INFILE 'C:/Users/Kai Jun/Documents/School Work/CS/Project/Data/CSV tables/referredby.csv' \n" +
                "INTO TABLE referredby \n" +
                "FIELDS TERMINATED BY ',' \n" +
                "ENCLOSED BY '\"'\n" +
                "LINES TERMINATED BY '\\n'\n" +
                "IGNORE 1 ROWS\n" +
                "(clientid,referrerid,ramount,rpaymenttype,@rpaymentdate)\n" +
                "SET rpaymentdate = STR_TO_DATE(@rpaymentdate,'%d/%m/%Y');\n" +
                "--SET rpaymentdate = (CASE WHEN @rpaymentdate = NULL THEN NULL ELSE STR_TO_DATE(@rpaymentdate,'%c/%e/%Y'));\n" +
                "\n" +
                "--OK\n" +
                "LOAD DATA INFILE 'C:/Users/Kai Jun/Documents/School Work/CS/Project/Data/CSV tables/payment.csv' \n" +
                "INTO TABLE payment \n" +
                "FIELDS TERMINATED BY ',' \n" +
                "ENCLOSED BY '\"'\n" +
                "LINES TERMINATED BY '\\n'\n" +
                "IGNORE 1 ROWS\n" +
                "(clientid,paymentid,@mypdate,myptype,@clientpdate,clientptype,amount)\n" +
                "SET mypdate = STR_TO_DATE(@mypdate, '%d/%m/%Y'),clientpdate = STR_TO_DATE(@clientpdate, '%d/%m/%Y');\n" +
                "\n" +
                "--OK\n" +
                "LOAD DATA INFILE 'C:/Users/Kai Jun/Documents/School Work/CS/Project/Data/CSV tables/individual.csv' \n" +
                "INTO TABLE individual \n" +
                "FIELDS TERMINATED BY ',' \n" +
                "ENCLOSED BY '\"'\n" +
                "LINES TERMINATED BY '\\n'\n" +
                "IGNORE 1 ROWS\n" +
                "(clientid,ic,name,@dob,maritalstatus,occupation,nationality,drivingexp,address,homeno,phoneno,email)\n" +
                "SET dob = STR_TO_DATE(@dob, '%d/%m/%Y');\n" +
                "\n" +
                "--OK\n" +
                "LOAD DATA INFILE 'C:/Users/Kai Jun/Documents/School Work/CS/Project/Data/CSV tables/commercial.csv' \n" +
                "INTO TABLE commercial \n" +
                "FIELDS TERMINATED BY ',' \n" +
                "ENCLOSED BY '\"'\n" +
                "LINES TERMINATED BY '\\n'\n" +
                "IGNORE 1 ROWS;\n" +
                "\n" +
                "--OK\n" +
                "LOAD DATA INFILE 'C:/Users/Kai Jun/Documents/School Work/CS/Project/Data/CSV tables/driver.csv' \n" +
                "INTO TABLE driver \n" +
                "FIELDS TERMINATED BY ',' \n" +
                "ENCLOSED BY '\"'\n" +
                "LINES TERMINATED BY '\\n'\n" +
                "IGNORE 1 ROWS\n" +
                "(clientid,ic,name,@dob,maritalstatus,occupation,nationality,drivingexp)\n" +
                "SET dob = STR_TO_DATE(@dob, '%d/%m/%Y');\n" +
                "\n" +
                "--OK\n" +
                "LOAD DATA INFILE 'C:/Users/Kai Jun/Documents/School Work/CS/Project/Data/CSV tables/relationship.csv' \n" +
                "INTO TABLE relationship \n" +
                "FIELDS TERMINATED BY ',' \n" +
                "ENCLOSED BY '\"'\n" +
                "LINES TERMINATED BY '\\n'\n" +
                "IGNORE 1 ROWS;";
    }

    private void createCommands(){
        insertcommands[0] = "LOAD DATA INFILE '" + filepathstring.replace('\\','/') + "/" + tables[4] +".csv' \n" +
                "INTO TABLE referrer \n" +
                "FIELDS TERMINATED BY ',' \n" +
                "ENCLOSED BY '\"'\n" +
                "LINES TERMINATED BY '\\n'\n" +
                "IGNORE 1 ROWS;";
        insertcommands[1] = "LOAD DATA INFILE '"+ filepathstring.replace('\\','/') + "/" + tables[2] +".csv' \n" +
                "INTO TABLE insurer \n" +
                "FIELDS TERMINATED BY ',' \n" +
                "ENCLOSED BY '\"'\n" +
                "LINES TERMINATED BY '\\n'\n" +
                "IGNORE 1 ROWS;";
        insertcommands[2] = "LOAD DATA INFILE '"+ filepathstring.replace('\\','/') + "/" + tables[3] +".csv' \n" +
                "INTO TABLE mypaymentdetail \n" +
                "FIELDS TERMINATED BY ',' \n" +
                "ENCLOSED BY '\"'\n" +
                "LINES TERMINATED BY '\\n'\n" +
                "IGNORE 1 ROWS;";
        insertcommands[3] = "LOAD DATA INFILE '"+ filepathstring.replace('\\','/') + "/" + tables[0] +".csv' \n" +
                "INTO TABLE client \n" +
                "FIELDS TERMINATED BY ',' \n" +
                "ENCLOSED BY '\"'\n" +
                "LINES TERMINATED BY '\\n'\n" +
                "IGNORE 1 ROWS\n" +
                "(clientid,referrerid,@effectivedate,@expirydate,premium,grosspremium,clienttype,insurerid,renewalstatus)\n" +
                "SET effectivedate = STR_TO_DATE(@effectivedate, '%d/%m/%Y'), expirydate = STR_TO_DATE(@expirydate, '%d/%m/%Y');";
        insertcommands[4] = "LOAD DATA INFILE '"+ filepathstring.replace('\\','/') + "/" + tables[1] +".csv' \n" +
                "INTO TABLE vehicle \n" +
                "FIELDS TERMINATED BY ',' \n" +
                "ENCLOSED BY '\"'\n" +
                "LINES TERMINATED BY '\\n'\n" +
                "IGNORE 1 ROWS\n" +
                "(vehicleno,clientid,vmake,vmodel,@rdtaxexpiry)\n"+
                "SET rdtaxexpiry = STR_TO_DATE(@rdtaxexpiry,'%d/%m/%Y')";
          //      "SET rdtaxexpiry = STR_TO_DATE(case when rdtaxexpiry = '' then null else rdtaxexpiry end, '%c/%e/%y' );";
        insertcommands[5] = "LOAD DATA INFILE '"+ filepathstring.replace('\\','/') + "/" + tables[6] +".csv' \n" +
                "INTO TABLE accidentrecord \n" +
                "FIELDS TERMINATED BY ',' \n" +
                "ENCLOSED BY '\"'\n" +
                "LINES TERMINATED BY '\\n'\n" +
                "IGNORE 1 ROWS\n" +
                "(clientid,@doa,claimamount,remarks)\n" +
                "SET doa = STR_TO_DATE(@doa, '%d/%m/%Y');";
        insertcommands[6] = "LOAD DATA INFILE '"+ filepathstring.replace('\\','/') + "/" + tables[5] +".csv' \n" +
                "INTO TABLE referredby \n" +
                "FIELDS TERMINATED BY ',' \n" +
                "ENCLOSED BY '\"'\n" +
                "LINES TERMINATED BY '\\n'\n" +
                "IGNORE 1 ROWS\n" +
                "(clientid,referrerid,ramount,rpaymenttype,@rpaymentdate)\n" +
                "SET rpaymentdate = STR_TO_DATE(@rpaymentdate,'%d/%m/%Y');";
                //"SET rpaymentdate = CASE WHEN @rpaymentdate = NULL THEN NULL ELSE STR_TO_DATE(@rpaymentdate,'%d/%m/%Y');";
                //"SET (case when @rpaymentdate = \"NULL\" then rpaymentdate = NULL else rpaymentdate = STR_TO_DATE(@rpaymentdate,'%d,'%m,'Y'));";
        insertcommands[7] = "LOAD DATA INFILE '"+ filepathstring.replace('\\','/') + "/" + tables[7] +".csv' \n" +
                "INTO TABLE payment \n" +
                "FIELDS TERMINATED BY ',' \n" +
                "ENCLOSED BY '\"'\n" +
                "LINES TERMINATED BY '\\n'\n" +
                "IGNORE 1 ROWS\n" +
                "(clientid,paymentid,@mypdate,myptype,@clientpdate,clientptype,amount)\n" +
                "SET mypdate = STR_TO_DATE(@mypdate, '%d/%m/%Y'),clientpdate = STR_TO_DATE(@clientpdate, '%d/%m/%Y');";
                //"SET CASE WHEN @rpaymentdate = NULL THEN rpaymentdate = NULL ELSE rpaymentdate = STR_TO_DATE(@rpaymentdate, '%d/%m/%Y') END;";
        insertcommands[8] = "LOAD DATA INFILE '"+ filepathstring.replace('\\','/') + "/" + tables[8] +".csv' \n" +
                "INTO TABLE individual \n" +
                "FIELDS TERMINATED BY ',' \n" +
                "ENCLOSED BY '\"'\n" +
                "LINES TERMINATED BY '\\n'\n" +
                "IGNORE 1 ROWS\n" +
                "(clientid,ic,address,@dob,maritalstatus,occupation,nationality,drivingexp,address,homeno,phoneno,email)\n" +
                "SET dob = STR_TO_DATE(@dob, '%d/%m/%Y');";
        insertcommands[9] = "LOAD DATA INFILE '"+ filepathstring.replace('\\','/') + "/" + tables[9] +".csv' \n" +
                "INTO TABLE commercial \n" +
                "FIELDS TERMINATED BY ',' \n" +
                "ENCLOSED BY '\"'\n" +
                "LINES TERMINATED BY '\\n'\n" +
                "IGNORE 1 ROWS;";
        insertcommands[10] = "LOAD DATA INFILE '"+ filepathstring.replace('\\','/') + "/" + tables[10] +".csv' \n" +
                "INTO TABLE driver \n" +
                "FIELDS TERMINATED BY ',' \n" +
                "ENCLOSED BY '\"'\n" +
                "LINES TERMINATED BY '\\n'\n" +
                "IGNORE 1 ROWS\n" +
                "(clientid,ic,name,@dob,maritalstatus,occupation,nationality,drivingexp)\n" +
                "SET dob = STR_TO_DATE(@dob, '%d/%m/%Y');";
        insertcommands[11] = "LOAD DATA INFILE '"+ filepathstring.replace('\\','/') + "/" + tables[11] +".csv' \n" +
                "INTO TABLE relationship \n" +
                "FIELDS TERMINATED BY ',' \n" +
                "ENCLOSED BY '\"'\n" +
                "LINES TERMINATED BY '\\n'\n" +
                "IGNORE 1 ROWS;";
    }

    private void showErrorMessage(Integer errorType, String errorMessage){
        String errorHeader = "";
        switch (errorType){
            case 0: errorHeader = "Error in bulk loading!"; break;
            case 1: errorHeader = "Error in exporting!"; break;
            case 2: errorHeader = "Error in closing file writer!"; break;
            case 3: errorHeader = "Error in opening output file!"; break;
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
