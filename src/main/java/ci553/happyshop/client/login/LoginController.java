package ci553.happyshop.client.login;


import ci553.happyshop.base_mvm.BaseController;
import ci553.happyshop.domain.service.ServiceFactory;
import ci553.happyshop.domain.service.UserService;
import ci553.happyshop.utility.alerts.AlertFactory;
import javafx.fxml.FXML;


/**
 * LoginController class. Connects to LoginModel to handle customer logins,
 * initializes FXML elements
 */
public class LoginController extends BaseController<LoginModel>
{
    private final UserService userService = ServiceFactory.getLoginService();


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
        // Observe the login service's error property & display the error in an Alert
        userService.userError().addListener(((observable, oldValue, newValue) ->
        {
            if (newValue != null && !newValue.isEmpty())        // Prevent repeated alerts by checking for empty value
            {
                AlertFactory.warning("Login", "Login Failed", newValue);    // show the alert
                userService.resetUserError();
            }
        }));
    }


    /**
     * Delegates to the LoginModel to attempt to log in to the customer system
     */
    public void customerLogin()
    {
        AlertFactory.loginPopup().ifPresent(model::customerLogin);
    }


    /**
     * Delegates to the LoginModel to attempt to log in to the customer system
     */
    public void warehouseLogin()
    {
    }


}
