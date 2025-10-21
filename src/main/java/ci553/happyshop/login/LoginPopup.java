package ci553.happyshop.login;

import ci553.happyshop.utility.UIStyle;
import ci553.happyshop.utility.WindowBounds;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// Class to provide a temporary login window when the user clicks either the
// customer login or warehouse login buttons on the LoginView. Modifies the 
public class LoginPopup
{
	// Connects to the loginView
	public LoginView View;
	private Stage window;
	private Scene scene;

	// The labels, text fields and button
	private Label lbTitle;
	private Label lbUsername;
	private Label lbPassword;
	private Label lbErrorMessage;
	private TextField txtUsername;
	private TextField txtPassword;
	private Button btnLogin;

	private void createScene()
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

		// Create rows of label, textfield
		HBox usernameRow = new HBox(10, lbUsername, txtUsername);
		HBox passwordRow = new HBox(10, lbPassword, txtPassword);

		VBox layoutBox = new VBox(10, lbTitle, usernameRow, passwordRow, btnLogin);
		layoutBox.setAlignment(Pos.CENTER);
		scene = new Scene(layoutBox, UIStyle.loginPopupWidth, UIStyle.loginPopupHeight);

	}

	private void createWindow()
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
		window.show();

	}

	// Opens an instance of the login window
	public void showLoginWindow(String windowTitle)
	{
		if (!(window.isShowing()))
		{
			createWindow();
		}
	}
}


