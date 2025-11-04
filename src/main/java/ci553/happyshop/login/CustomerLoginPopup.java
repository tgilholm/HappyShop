package ci553.happyshop.login;

import java.io.IOException;
import java.sql.SQLException;

import ci553.happyshop.utility.WindowBounds;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

// A child class of LoginPopup that supports customer login only
public class CustomerLoginPopup extends LoginPopup
{
	public CustomerLoginPopup(LoginView v, LoginModel m)
	{
		super(v, m);
		// TODO Auto-generated constructor stub
	}

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
	// Tells the LoginModel to contact the database & check the login
	protected void buttonClicked(ActionEvent event) throws IOException
	{
		try
		{
			Model.cusLogin(txtUsername.getText(), txtPassword.getText());
		} catch (SQLException e)
		{
			System.out.println("Login failed " + e);
		}
	}
}
