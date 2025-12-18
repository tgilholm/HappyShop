package ci553.happyshop.login;

import ci553.happyshop.utility.UIStyle;
import ci553.happyshop.utility.WinPosManager;
import ci553.happyshop.utility.WindowBounds;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * The LoginView class provides the frontend for the user/warehouse staff. It
 * allows them to select which kind of user they are, type in their username and
 * password, and create a new account if they want to. It will also inform the
 * user whether they have an incorrect username or password.
 *
 * @author Thomas Gilholm, University of Brighton
 * @version 1.0
 */

public class LoginView
{
	// LoginView links to the LoginController, which handles user input
	public LoginController lController;

	private final int WIDTH = UIStyle.loginWinWidth;
	private final int HEIGHT = UIStyle.loginWinHeight; // Retrieve parameters for window size from the UIStyle record

	private Stage viewWindow;
	private Scene scene;
	private HBox hbRoot; // Root box, contains all layout elements
	private VBox vbLoginPage;

	private Label lbTitle;
	private Button btnCust;
	private Button btnWarehouse;
	private Stage window;

	// LoginView Constructor, takes the stage as parameter
	// Allows Stage window to be re-used later to hide the view
	public LoginView(Stage s)
	{
		window = s;
		this.start();	// Invokes start upon construction
	}

	// Starts the loginView
	public void start()
	{
		vbLoginPage = createLoginPage();
		hbRoot = new HBox(10, vbLoginPage); // Adds all the layout elements to the root
		hbRoot.setAlignment(Pos.CENTER);

		scene = new Scene(hbRoot, WIDTH, HEIGHT); // Creates a new scene of size WIDTH x HEIGHT
		window.setScene(scene);
		window.setTitle("🛒 HappyShop Login Page");
		WinPosManager.registerWindow(window, WIDTH, HEIGHT);
		window.show();
		viewWindow = window;
	}

	// This method creates the title and login buttons
	private VBox createLoginPage()
	{
		lbTitle = new Label("HappyShop");
		lbTitle.setStyle(UIStyle.loginTitleStyle);

		btnCust = new Button("Customer Login");
		btnCust.setOnAction(this::buttonClicked);
		btnCust.setStyle(UIStyle.loginButtonStyle);

		btnWarehouse = new Button("Warehouse Login");
		btnWarehouse.setOnAction(this::buttonClicked);
		btnWarehouse.setStyle(UIStyle.loginButtonStyle);

		VBox vbLoginPage = new VBox(15, lbTitle, btnCust, btnWarehouse);
		vbLoginPage.setAlignment(Pos.CENTER);
		return vbLoginPage;
	}

	// When either of the buttons are clicked, open a loginPopup
	private void buttonClicked(ActionEvent event)
	{
		Button button = (Button) event.getSource();
		String btnText = button.getText();
		lController.buttonClicked(btnText);
	}

	WindowBounds getWindowBounds()
	{
		return new WindowBounds(viewWindow.getX(), viewWindow.getY(), viewWindow.getWidth(), viewWindow.getHeight());
	}

	public void hideWindow()
	{
		window.hide();
	}
}
