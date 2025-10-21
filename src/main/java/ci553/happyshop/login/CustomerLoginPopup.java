package ci553.happyshop.login;

import java.io.IOException;

import ci553.happyshop.utility.WindowBounds;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

// A child class of LoginPopup that supports customer login only
public class CustomerLoginPopup extends LoginPopup
{
	// Overrides the createWindow method to set the title to Customer Login
	protected void createWindow()
	{
		if (scene == null)
		{
			createScene();
		}

		window = new Stage();
		window.setScene(scene);

		// Open the loginPopup on top of the Login window
		WindowBounds bounds = View.getWindowBounds();
		window.setX(bounds.x + bounds.width / 2);
		window.setY(bounds.y + bounds.height / 2);
		window.setTitle("Customer Login");
		window.show();
	}

	// Overrides the buttonClicked method to handle customer logins
	protected void buttonClicked(ActionEvent event) throws IOException
	{

	}
}
