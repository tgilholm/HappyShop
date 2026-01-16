package ci553.happyshop.client.login;


import ci553.happyshop.base_mvm.BaseController;
import ci553.happyshop.catalogue.User;
import ci553.happyshop.client.customer.CustomerClient;
import ci553.happyshop.client.warehouse.WarehouseClient;
import ci553.happyshop.utility.enums.UserType;
import ci553.happyshop.utility.alerts.AlertFactory;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;


/**
 * LoginController class. Connects to LoginModel to handle customer logins,
 * initializes FXML elements
 */
public class LoginController extends BaseController<LoginModel>
{
    public @FXML Label lbTitle;


    protected LoginController(LoginModel model)
    {
        super(model);
    }


    /**
     * Initializes the controller after the root element is finished processing.
     */
    @Override
    @FXML
    public void initialize()
    {
        /*
        Controllers should not directly access services, they should get them from the model.
        This observable errorProperty is an example
         */


        // Observe the error property from the model & display the error in an Alert if there is a new error

        model.userErrorProperty().addListener(((observable, oldValue, newValue) ->
        {
            if (newValue != null && !newValue.isEmpty())        // Prevent repeated alerts by checking for empty value
            {
                Platform.runLater(() ->
                {
                    AlertFactory.warning("Login", "Login Failed", newValue);    // show the on the FX thread
                });

                model.resetUserError(); // Reset the flag
            }
        }));
    }


    /**
     * Delegates to the model to check if the user exists. If so, launches the customer side of the program
     */
    public void customerLogin()
    {
        AlertFactory.login().ifPresent(loginCredentials ->
        {
            User user = model.customerLogin(loginCredentials);

            if (user != null)
            {
                // If successful, hide the login and launch the new view
                CustomerClient.startCustomerClient(new Stage(), user);
                hideLogin();
            }
        });
    }


    /**
     * Delegates to the model to check if the user exists. If so, launches the staff side of the program
     */
    public void warehouseLogin()
    {
        AlertFactory.login().ifPresent(loginCredentials ->
        {
            User user = model.warehouseLogin(loginCredentials);

            if (user != null)
            {
                // If successful, hide the login and launch the new view
                WarehouseClient.startWarehouseClient(new Stage());
                hideLogin();
            }
        });
    }


    /**
     * Delegates to the model to create a new account. If successful, launches the corresponding
     * area of the program
     */
    public void createAccount()
    {
        AlertFactory.createAccount().ifPresent(loginCredentials ->
        {
            User user = model.createAccount(loginCredentials);

            if (user != null)
            {
                // Launch the view of the corresponding type
                if (user.userType() == UserType.CUSTOMER)
                {
                    CustomerClient.startCustomerClient(new Stage(), user);
                } else
                {
                    WarehouseClient.startWarehouseClient(new Stage());
                }
                hideLogin();
            }
        });
    }


    /**
     * Helper method to hide the login view on a successful account login or creation
     * Gets the current stage from one of the JavaFX elements, then hides the stage
     */
    private void hideLogin()
    {
        if (lbTitle != null && lbTitle.getScene() != null)
        {
            // Get the current stage
            Stage stage = (Stage) lbTitle.getScene().getWindow();

            if (stage != null)
            {
                stage.hide();
            }
        }
    }
}
