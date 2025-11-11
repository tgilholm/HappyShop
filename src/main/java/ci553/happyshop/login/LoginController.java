package ci553.happyshop.login;


/**
 * The LoginController model handles the user inputs on the LoginView page
 *
 * @author Thomas Gilholm, University of Brighton
 * @version 1.0
 */

public class LoginController
{
	// LoginController connects to the LoginModel, which executes the backend code
	public LoginModel lModel;

	public void buttonClicked(String btnText)
	{
		switch (btnText)
		{
		case "Customer Login":
			// Open the customer login popup
			lModel.openCusLoginWindow();
			break;
		case "Warehouse Login":
			// Open the warehouse login popup
			lModel.openWarLoginWindow();
		}
	}
	
}
