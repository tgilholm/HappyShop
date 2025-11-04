package ci553.happyshop.login;

import java.sql.SQLException;

import ci553.happyshop.storageAccess.DatabaseRW;

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
	public DatabaseRW databaseRW;
	private CustomerLoginPopup cusLoginPopup;
	

	public void openCusLoginWindow()
	{
		// Opens either a customer or warehouse login window
		cusLoginPopup = new CustomerLoginPopup(lView, this);
		cusLoginPopup.showLoginWindow(); 
	}
	
	// Contact the database & check login details
	public void cusLogin(String username, String password) throws SQLException
	{
		if (databaseRW.checkLoginDetails(username, password))
		{
			System.out.println(String.format("Logging in... username: %s, password: %s", 
					username, password));
		}
		else {
			System.out.print(String.format("Login failed... username: %s, password: %s", 
					username, password));
		}
	}
	
//	System.out.println(String.format("Logging in to customer %s, username: %s, password: %s", 
//			i.getID(), i.getUsername(), i.getPassword()));
	
}
