package ci553.happyshop.client.login;

import java.sql.SQLException;

import ci553.happyshop.base_mvm.BaseModel;
import ci553.happyshop.catalogue.Customer;
import ci553.happyshop.client.customer.CustomerClient;
import ci553.happyshop.domain.service.LoginService;
import ci553.happyshop.domain.service.ServiceFactory;
import ci553.happyshop.storageAccess.DatabaseRW;
import ci553.happyshop.client.OpenWindows;
import ci553.happyshop.utility.alerts.AlertFactory;
import javafx.stage.Stage;

/**
 * The LoginModel class provides the functionality for users and warehouse staff
 * to log into the application It checks the database to see if the user already
 * has an account, and if not, asks them to create one.
 *
 *
 * TODO add option to go back to login screen
 *
 */
public class LoginModel extends BaseModel
{
	// Connect to the loginService
	private final LoginService loginService = ServiceFactory.getLoginService();


	/**
	 * Attempts to log in a customer. Creates a loginPopup, then validates the result.
	 * If successful, opens the "customer" side of the program
	 */
	public void customerLogin()
	{
		AlertFactory.loginPopup().ifPresent(result ->
        {
            String username = result.username();
            String password = result.password();

            logger.info("Username: {}, Password: {}", username, password);

            // Check for empty username & password
            if (password.isEmpty())
            {
                AlertFactory.warning("Login", "Login Failed", "Please enter a password");
                logger.debug("No password entered");
                return;
            }
            else if (username.isEmpty())
            {
                AlertFactory.warning("Login", "Login Failed", "Please enter a username");
                logger.debug("No username entered");
                return;
            }

            // Pass to the loginService
            Customer customer = loginService.login(username, password);

            if (customer == null)
            {
                AlertFactory.warning("Login", "LoginFailed", "Incorrect username or password");
                return;
            }

            // Pass to the customer window
            CustomerClient.startCustomerClient(new Stage(), customer);

        });
	}


	// TODO refactor to handle both popups with one method each
	// LoginModel connects to the LoginView, which displays GUI items to the user
	//public LoginView lView;
	public DatabaseRW databaseRW;
	private OpenWindows openWindows;
	private LoginPopup cusLoginPopup, warLoginPopup;

	// Default constructor- initialises openWindows
	public LoginModel()
	{
		openWindows = new OpenWindows();
	}

//	public void openCusLoginWindow()
//	{
//		// Opens either a customer or warehouse login window
//		cusLoginPopup = new CustomerLoginPopup(lView, this);
//		cusLoginPopup.showLoginWindow();
//	}
//
//	public void openWarLoginWindow()
//	{
//		// Opens either a customer or warehouse login window
//		warLoginPopup = new WarehouseLoginPopup(lView, this);
//		warLoginPopup.showLoginWindow();
//	}

	// Customer login method. Checks the login table and opens the customer window
	// if correct
	// Contact the database & check login details
//	public void cusLogin(String username, String password) throws SQLException
//	{
//		if (databaseRW.checkLoginDetails(username, password))
//		{
//			// Handle what happens when the user is logged in successfully
//			System.out.printf("Logging in... username: %s, password: %s%n", username, password);
//
//			// Invoke the startCustomerClient() and close the LoginPopup and LoginView
//			//openWindows.startCustomerClient();
//			cusLoginPopup.closePopup();
//			lView.hideWindow(); // Hide is used instead of close so we can return to it later
//		} else
//		{
//			System.out.printf("Login failed... username: %s, password: %s", username, password);
//		}
//	}

//	// Warehouse login method. Checks the login table and opens the customer window
//	// if correct
//	// Contact the database & check login details
//	public void warLogin(String username, String password) throws SQLException
//	{
//		if (databaseRW.checkLoginDetails(username, password))
//		{
//			// Handle what happens when the user is logged in successfully
//			System.out.println(String.format("Logging in... username: %s, password: %s", username, password));
//
//			// Invoke the startCustomerClient() and close the LoginPopup and LoginView
//			openWindows.startWarehouseClient();
//			warLoginPopup.closePopup();
//			lView.hideWindow(); // Hide is used instead of close so we can return to it later
//		} else
//		{
//			System.out.print(String.format("Login failed... username: %s, password: %s", username, password));
//		}
//	}
}
