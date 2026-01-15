package ci553.happyshop.client.login;


import ci553.happyshop.base_mvm.BaseController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;


/**
 * LoginController class. Connects to LoginModel to handle customer logins,
 * initializes FXML elements
 */
public class LoginController extends BaseController<LoginModel>
{
	protected LoginController(LoginModel model)
	{
		super(model);
	}


//	public void buttonClicked(String btnText)
//	{
//		switch (btnText)
//		{
//		case "Customer Login":
//			// Open the customer login popup
//			lModel.openCusLoginWindow();
//			break;
//		case "Warehouse Login":
//			// Open the warehouse login popup
//			lModel.openWarLoginWindow();
//		}
//	}


	/**
	 * Initializes the controller after the root element is finished processing.
	 */
	@Override
	@FXML
	public void initialize()
	{

	}


	public void customerLogin(ActionEvent actionEvent)
	{
	}


	public void warehouseLogin(ActionEvent actionEvent)
	{
	}
}
