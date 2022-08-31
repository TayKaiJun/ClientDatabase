package sample.AccountsPage;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import sample.Main;

import java.awt.*;
import java.io.File;

/**
 * Created by Kai Jun on 13/4/2017.
 */
public class AccountsController {

    private Main main;
    private String[] tooltipmessages = new String[4];
    private Tooltip tooltip = new Tooltip();

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Pane expiringPane;
    @FXML
    private Pane myaccPane;
    @FXML
    private Pane incomestatPane;

    public AccountsController() {
        tooltipmessages[0] = "Shows clients whose policies are expiring in a few weeks' time";
        tooltipmessages[1] = "Shows my accounts and the inflow and outflow of money";
    }

    @FXML
    public void initialize() {

        initializeExpiringPane();
        initializeMyaccPane();

    }

    private void initializeExpiringPane() {
        expiringPane.setOnMouseEntered(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                expiringPane.setStyle("-fx-background-color:#dae7f3;");
            }
        });
        expiringPane.setOnMouseExited(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                expiringPane.setStyle("-fx-background-color:transparent;");
                tooltip.setText("");
                tooltip.hide();
            }
        });
        expiringPane.setOnMouseMoved(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                String msg = "(x: " + event.getX() + ", y: " + event.getY() + ")\n(sceneX: "
                        + event.getSceneX() + ", sceneY: " + event.getSceneY() + ")\n(screenX: "
                        + event.getScreenX() + ", screenY: " + event.getScreenY() + ")";
                tooltip.setText(tooltipmessages[0]);

                Node node = (Node) event.getSource();
                tooltip.show(node, event.getScreenX() + 50, event.getScreenY());
            }

        });
        expiringPane.setOnMouseClicked((MouseEvent mouseEvent) -> {
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                if (mouseEvent.getClickCount() == 1 || mouseEvent.getClickCount() == 2) {
                    System.out.println("ExpiringPane clicked");
                    tooltip.hide();
                    anchorPane.getChildren().clear();
                    try {
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(getClass().getResource("expiring.fxml"));
                        AnchorPane expiring = loader.load();
                        anchorPane.getChildren().add(expiring);
                        ExpiringController expiringController = loader.getController();
                        expiringController.setMain(main);
                        System.out.println("Set main of expiring controller");
                    } catch (Exception e){
                        e.printStackTrace();
                        showErrorMessage(e.getMessage());
                    }
                }
            }
        });

    }

    private void initializeMyaccPane() {
        myaccPane.setOnMouseEntered(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                myaccPane.setStyle("-fx-background-color:#dae7f3;");
            }
        });
        myaccPane.setOnMouseExited(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                myaccPane.setStyle("-fx-background-color:transparent;");
                tooltip.setText("");
                tooltip.hide();
            }
        });
        myaccPane.setOnMouseMoved(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                tooltip.setText(tooltipmessages[1]);

                Node node = (Node) event.getSource();
                tooltip.show(node, event.getScreenX() + 50, event.getScreenY());
            }

        });
        myaccPane.setOnMouseClicked((MouseEvent mouseEvent) -> {
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                if (mouseEvent.getClickCount() == 1 || mouseEvent.getClickCount() == 2) {
                    System.out.println("MyAccPane clicked");
                    tooltip.hide();
                    anchorPane.getChildren().clear();
                    try {
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(getClass().getResource("myacc.fxml"));
                        SplitPane myacc = loader.load();
                        anchorPane.getChildren().add(myacc);
                        MyaccController myaccController = loader.getController();
                        myaccController.setMain(main);
                        System.out.println("Set main of My Acc controller");
                    } catch (Exception e) {
                        e.printStackTrace();
                        showErrorMessage(e.getMessage());
                    }
                }
            }
        });

    }

    private void showErrorMessage(String errorMessage){
        /*
        String errorHeader = "";
        switch (errorType){
            case 0: errorHeader = "Error on Building Data!"; break;
            case 1: errorHeader = "Error in Search Input! (Number format)"; break;
            case 2: errorHeader = "Error in Search Input!(Date format)"; break;
            case 3: errorHeader = "Error in Search Input!"; break;
        }
        */
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error in Loading Panes in Accounts Tab");
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }

    public void resetAccountsPage(){
        anchorPane.getChildren().clear();
        try {
            FXMLLoader loader = main.getAcocountsXML();
            AnchorPane accounts = loader.load();
            anchorPane.getChildren().add(accounts);
            System.out.println("Swapped back to Accounts page");
            AccountsController accountsController = loader.getController();
            accountsController.setMain(main);
        } catch (Exception e) {
            e.printStackTrace();
            showErrorMessage(e.getMessage());
        }
    }

    public void setMain(Main main) {
        this.main = main;
    }
}
