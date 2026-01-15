package ci553.happyshop.client.login;

import java.io.IOException;

import ci553.happyshop.utility.UIStyle;
import ci553.happyshop.utility.WindowBounds;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


// TODO fix formatting
// TODO add incorrect password feature
// TODO add create account feature
// Displays a compact login window. Parent class of CustomerLoginPopup and WarehouseLoginPopup classes

public class LoginPopup
{
	// Connects to the loginView
	//public LoginView View;

	// Connects to the loginModel
	public LoginModel Model;
	protected Stage window; // Protected keyword is used to allow access by child classes
	protected Scene scene;

	// The labels, text fields and button
	protected Label lbTitle;
	protected Label lbUsername;
	protected Label lbPassword;
	protected Label lbErrorMessage;
	protected TextField txtUsername;
	protected PasswordField txtPassword; // Use a PasswordField to protect user privacy
	protected Button btnLogin;

//	public LoginPopup(LoginView v, LoginModel m)
//	{
//		View = v;
//		Model = m;
//		window = new Stage();
//	}

	protected void createScene()
	{
		// Instantiate labels
		lbTitle = new Label("Login");
		lbUsername = new Label("Username: ");
		lbPassword = new Label("Password: ");
		lbErrorMessage = new Label("Error: ");

		// Instantiate text fields
		txtUsername = new TextField();
		txtPassword = new PasswordField();

		// Instantiate button
		btnLogin = new Button("Login");
		btnLogin.setOnAction(event -> {
			try
			{
				buttonClicked(event);
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

		// Create rows of label, textfield
		HBox usernameRow = new HBox(10, lbUsername, txtUsername);
		HBox passwordRow = new HBox(10, lbPassword, txtPassword);

		VBox layoutBox = new VBox(10, lbTitle, usernameRow, passwordRow, btnLogin);
		layoutBox.setAlignment(Pos.CENTER);
		scene = new Scene(layoutBox, UIStyle.loginPopupWidth, UIStyle.loginPopupHeight);
	}

	// Default buttonClicked method, child classes override it
	protected void buttonClicked(ActionEvent event) throws IOException
	{
	};

	protected void createWindow()
	{
		if (scene == null)
		{
			createScene();
		}

		window = new Stage();
		window.setScene(scene);

		// Open the loginPopup on top of the Login window
		//WindowBounds bounds = View.getWindowBounds();
//		window.setX(bounds.x + bounds.width / 2);
//		window.setY(bounds.y + bounds.height / 2);
		window.show();

	}

	// Opens an instance of the login window
	public void showLoginWindow()
	{
		if (!window.isShowing())
		{
			createWindow();
		}
	}

	// Shut down the popup on a successful login
	public void closePopup()
	{
		window.close();
	}
}
