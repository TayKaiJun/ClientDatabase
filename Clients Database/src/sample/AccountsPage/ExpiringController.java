package sample.AccountsPage;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TableView;
import sample.Database.DisplayDatabase;
import sample.Main;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Kai Jun on 13/4/2017.
 */
public class ExpiringController {

    private Main main;
    private int days;
    private boolean init = false;
    private Date currentDate;
    private String targetDate;
    private DateFormat df;
    private Calendar calendar;

    @FXML
    private Slider slider;
    @FXML
    private Label datelabel;
    @FXML
    private TableView tableView;
    @FXML
    private Button backButton;

    public ExpiringController(){
        df = new SimpleDateFormat("yyyy-MM-dd");
        currentDate = new Date();
        System.out.println(df.format(currentDate));
        calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
    }

    @FXML
    public void initialize(){
        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                main.getAccountsCtrl().resetAccountsPage();
            }
        });

        datelabel.setText("2 week");
        slider.setMin(14);
        slider.setMax(56);
        slider.setValue(14);
        slider.setBlockIncrement(3);

        Calendar temp = Calendar.getInstance();
        temp.setTime(currentDate);
        temp.add(Calendar.DATE,14);
        targetDate = df.format(temp.getTime());
        String date = df.format(currentDate);
        DisplayDatabase.buildData(tableView,date,targetDate);

        slider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                days = (int) (new_val.doubleValue());
                int oldDays = (int) (old_val.doubleValue()) * -1;
                if(!init){
                    init = true;
                } else {
                    calendar.add(Calendar.DATE, oldDays);
                }
                calendar.add(Calendar.DATE, days);
                targetDate = df.format(calendar.getTime());
                String date = df.format(currentDate);
                //System.out.println(old_val + ", " + new_val + ", " + targetDate);
                updateDateLabel();
                DisplayDatabase.buildData(tableView,date,targetDate);
            }
        });
    }

    private void updateDateLabel(){
        if(days%7 == 0){
            datelabel.setText((int) Math.floor(days/7.0) + " weeks");
        } else {
            datelabel.setText((int) Math.floor(days/7.0) + " weeks " + days%7 + " days");
        }
    }

    public void setMain(Main main){
        this.main = main;
    }
}
