package ci553.happyshop.login;

/**
 * The LoginModel class provides the functionality for users and warehouse staff to log into the application
 * It checks the database to see if the user already has an account, and if not, asks them to create one.
 *
 * @author Thomas Gilholm, University of Brighton
 * @version 1.0
 */
public class LoginModel
{
	// LoginModel connects to the LoginView, which displays GUI items to the user
	public LoginView lView;
	private LoginPopup loginPopup;
	
	
	
	s
	public void openLoginWindow(String windowTitle)
	{
		// Opens either a customer or warehouse login window
		loginPopup.showLoginWindow(windowTitle); 
	}
	
}
