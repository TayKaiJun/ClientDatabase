package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import sample.AccountsPage.AccountsController;
import sample.ContactCardPage.ContactCardController;
import sample.DetailsPage.DetailsController;
import sample.OverviewPage.OverviewController;

/**
 * Created by Kai Jun on 12/4/2017.
 */
public class TabPaneController {

    private Main main;

    @FXML private TabPane tabPane;
    // Inject tab content.
    @FXML
    private Tab overviewtab;
    // Inject controller
    @FXML
    private OverviewController overviewPaneController;
    @FXML
    private Tab detailstab;
    @FXML
    private DetailsController detailsPaneController;
    @FXML
    private Tab accountstab;
    @FXML
    private AccountsController accountsController;
    @FXML
    private Tab contactcardtab;
    @FXML
    private ContactCardController contactCardController;
    @FXML
    private Tab abouttab;

    public TabPaneController() {
    }

    @FXML
    public void initialize() {

    }

    public void setMain(Main main) {
        this.main = main;
        overviewPaneController.setMain(main);
        accountsController.setMain(main);
        detailsPaneController.setMain(main);

        //contactCardController.setMain(main);
    }

    public AccountsController getAccountsController() {
        return accountsController;
    }
}
