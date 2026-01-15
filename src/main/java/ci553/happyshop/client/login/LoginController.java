package ci553.happyshop.client.login;


import ci553.happyshop.base_mvm.BaseController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;


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


    /**
     * Delegates to the LoginModel to attempt to log in to the customer system
     */
    public void customerLogin()
    {
        model.customerLogin();
    }


    /**
     * Delegates to the LoginModel to attempt to log in to the customer system
     */
    public void warehouseLogin()
    {
    }


}
