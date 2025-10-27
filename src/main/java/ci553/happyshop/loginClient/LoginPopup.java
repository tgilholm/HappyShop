package ci553.happyshop.loginClient;

import java.io.IOException;

import ci553.happyshop.utility.UIStyle;
import ci553.happyshop.utility.WindowBounds;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// Displays a compact login window. Parent class of CustomerLoginPopup and WarehouseLoginPopup classes

public class LoginPopup
{
	// Connects to the loginView
	protected LoginView View;
	protected LoginModel Model;
	protected Stage window;		// Protected keyword is used to allow access by child classes
	protected Scene scene;

	// The labels, text fields and button
	protected Label lbTitle;
	protected Label lbUsername;
	protected Label lbPassword;
	protected Label lbErrorMessage;
	protected TextField txtUsername;
	protected TextField txtPassword;
	protected Button btnLogin;

	// Connects the popup to the view and model to pass information to them
	protected LoginPopup(LoginView v, LoginModel m)
	{
		View = v;
		Model = m;
		window = new Stage();
	}
	
	protected void createScene()
	{
		// Instantiate labels
		lbTitle = new Label("Login");
		lbUsername = new Label("Username: ");
		lbPassword = new Label("Password: ");
		lbErrorMessage = new Label("Error: ");

		// Instantiate text fields
		txtUsername = new TextField();
		txtPassword = new TextField();

		// Instantiate button
		btnLogin = new Button("Login");
		btnLogin.setOnAction(event -> {
			try
			{
				buttonClicked(event);
			} catch (IOException e)
			{
				System.out.println("Error occurred attempting to login: " + e);
			}
		});

		// Create rows of label, textfield and set them into the centre of the popup
		HBox usernameRow = new HBox(10, lbUsername, txtUsername);
		HBox passwordRow = new HBox(10, lbPassword, txtPassword);
		usernameRow.setAlignment(Pos.CENTER);
		passwordRow.setAlignment(Pos.CENTER);

		// Add the layout elements to a VBox and show it on the screen
		VBox layoutBox = new VBox(10, lbTitle, usernameRow, passwordRow, btnLogin);
		layoutBox.setAlignment(Pos.CENTER);
		scene = new Scene(layoutBox, UIStyle.loginPopupWidth, UIStyle.loginPopupHeight);
	}
	
	// Default buttonClicked method, child classes override it
	protected void buttonClicked(ActionEvent event) throws IOException {};

	// Generates the GUI for the popup & adds the layout elements to it
	protected void createWindow()
	{
		if (scene == null)
		{
			createScene();	// Invoke the createScene method to add the layout elements
		}

		window = new Stage();
		window.setScene(scene);

		// Open the loginPopup on top of the Login window
		WindowBounds bounds = View.getWindowBounds();
		window.setX(bounds.x + bounds.width / 2);
		window.setY(bounds.y + bounds.height / 2);
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
	
	public boolean isShowing()
	{
		return window.isShowing();
	}
}
