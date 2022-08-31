package sample.ContactCardPage;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import sample.Main;

/**
 * Created by Kai Jun on 13/4/2017.
 */
public class ContactCardController {

    private Main main;


    public ContactCardController(){

    }

    @FXML
    public void initialize(){

    }

    public void setMain(Main main){
        this.main = main;
    }
}
