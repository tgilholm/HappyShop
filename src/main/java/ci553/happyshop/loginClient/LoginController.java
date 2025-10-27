package ci553.happyshop.loginClient;


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
			lModel.showCusLoginPopup();
			// Open the customer login popup
			break;
			
		case "Warehouse Login":
			lModel.showWarLoginPopup();
			// Open the warehouse login popup
			break;
		}
	}
	
}
