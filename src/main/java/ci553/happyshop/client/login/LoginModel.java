package ci553.happyshop.client.login;

import ci553.happyshop.base_mvm.BaseModel;
import ci553.happyshop.catalogue.User;
import ci553.happyshop.client.customer.CustomerClient;
import ci553.happyshop.domain.service.UserService;
import ci553.happyshop.domain.service.ServiceFactory;
import ci553.happyshop.storageAccess.DatabaseRW;
import ci553.happyshop.client.OpenWindows;
import ci553.happyshop.utility.LoginCredentials;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

/**
 * The LoginModel class provides the functionality for users and warehouse staff
 * to log into the application It checks the database to see if the user already
 * has an account, and if not, asks them to create one.
 * <p>
 * <p>
 * TODO add option to go back to login screen
 *
 */
public class LoginModel extends BaseModel
{
    // Connect to the userService
    private final UserService userService = ServiceFactory.getLoginService();


    /**
     * Attempts to log in a customer. Creates a loginPopup, then validates the result.
     * If successful, opens the "customer" side of the program
     */
    public void customerLogin(@NotNull LoginCredentials result)
    {
        String username = result.username();
        String password = result.password();

        // Pass to the userService
        User user = userService.login(username, password, User.UserType.CUSTOMER);

        if (user != null)
        {
            // Pass to the customer window
            CustomerClient.startCustomerClient(new Stage(), user);
        }
    }


    // TODO refactor to handle both popups with one method each
    // LoginModel connects to the LoginView, which displays GUI items to the user
    //public LoginView lView;
    public DatabaseRW databaseRW;
    private OpenWindows openWindows;
    private LoginPopup cusLoginPopup, warLoginPopup;


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
