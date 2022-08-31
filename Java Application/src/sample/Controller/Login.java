package sample.Controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import sample.Main;

public class Login {

    private Main main;

    @FXML
    private TextField database;
    @FXML
    private TextField user;
    @FXML
    private PasswordField password;
    @FXML
    private Button loginbutton;
    @FXML
    private Button cancelbutton;

    public Login() {
    }

    @FXML
    public void initialize() {

        loginbutton.setDefaultButton(true);
        loginbutton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                String databases = database.getText();
                String users = user.getText();
                String passwords = password.getText();

                try {
                    DBConnection.connect(databases, users, passwords);
                    System.out.println("Connected to database");
                    main.initMainPage();
                } catch (Exception e1){
                    e1.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Error in logging in to database!");
                    alert.setContentText(e1.getMessage());
                    alert.showAndWait();
                }
            }
        });

        cancelbutton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                database.setText("");
                user.setText("");
                password.setText("");
            }
        });
    }

    public void setMain(Main main) {
        this.main = main;
    }

}
