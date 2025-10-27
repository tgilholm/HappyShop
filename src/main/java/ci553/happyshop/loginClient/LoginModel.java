package ci553.happyshop.loginClient;

/**
 * The LoginModel class provides the functionality for users and warehouse staff
 * to log into the application It checks the database to see if the user already
 * has an account, and if not, asks them to create one.
 *
 * @author Thomas Gilholm, University of Brighton
 * @version 1.0
 */
public class LoginModel
{
	// LoginModel connects to the LoginView, which displays GUI items to the user
	public LoginView lView;
	private CustomerLoginPopup cLoginPopup;
	private WarehouseLoginPopup wLoginPopup;

	public void showCusLoginPopup()
	{
		cLoginPopup = new CustomerLoginPopup(lView, this);
		cLoginPopup.showLoginWindow();
	}

	public void customerLogin(String username, String password)
	{
		System.out.println(username + password);
		// Check the database to see if the username & password provided already exists
	}

	public void showWarLoginPopup()
	{
		wLoginPopup = new WarehouseLoginPopup(lView, this);
		wLoginPopup.showLoginWindow();
	}

}
