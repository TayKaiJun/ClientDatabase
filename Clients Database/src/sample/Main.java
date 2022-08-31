package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.AccountsPage.AccountsController;
import sample.DetailsPage.ItemController;
import sample.LoginPage.LoginController;

import java.io.IOException;

public class Main extends Application {

    private Stage primaryStage;
    private TabPaneController tabPaneController;
    private String[] tables = new String[12];

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Insurance Accounts Data Loader");

        populateTableArray();
        initLoginPage();
    }

    private void populateTableArray(){
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

    public String[] getTables(){
        return tables;
    }

    public void initLoginPage() {
        try {
            GridPane rootLayout;
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("login.fxml"));
            rootLayout = (GridPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();

            LoginController controller = loader.getController();
            controller.setMain(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showNewDialog(String table) {

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("DetailsPage/newItem.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add new");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            ItemController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setMain(this);
            controller.setTable(table);
            controller.setFunction(1);
            controller.retreiveData();
            controller.populatePage();
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void showNewDialog(String table, String rowData, TableView dataTable) {

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("DetailsPage/newItem.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add new");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            ItemController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setMain(this);
            controller.setDataTable(dataTable);
            controller.setTable(table);
            controller.retreiveData();
            controller.setFunction(2);
            controller.populatePage();
            controller.fillPage(rowData);
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static void main(String[] args) {
        launch(args);
    }

    public void initTabPane() {
        try {
            TabPane rootLayout;
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("LayoutsInTab/rootPane.fxml"));
            rootLayout = (TabPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();

            tabPaneController = loader.getController();
            System.out.println(this);
            tabPaneController.setMain(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FXMLLoader getAcocountsXML(){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("LayoutsInTab/accounts.fxml"));
        return loader;
    }

    public AccountsController getAccountsCtrl(){
        return tabPaneController.getAccountsController();
    }

}
