package ci553.happyshop.client.customer;

import ci553.happyshop.utility.UIStyle;
import ci553.happyshop.utility.WinPosManager;
import ci553.happyshop.utility.WindowBounds;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

/**
 * The CustomerView is separated into two sections by a line :
 *
 * 1. Search Page – Always visible, allowing customers to browse and search for
 * products. 2. the second page – display either the Trolley Page or the Receipt
 * Page depending on the current context. Only one of these is shown at a time.
 */


/* TODO VIEW REDESIGN
 * - divide the screen up: list of items available on one side, specific item on the other
 * item list takes about 2/3, item view takes about 1/3
 * 
 * do the layout first, get the items where they should be
 * add a search bar (add a hint telling users what to do)
 * search bar will automatically search as the user types
 * search results appear in the box below sorted alphabetically
 * 
 * add a category selection dropdown
 * (need to edit SQL database to support categories)
 * when a category is selected, only the items that appear in that
 * category will show in the search result
 * search result needs to allow scrolling through options
 * 
 * search result may have to be custom implementation
 * new class for the container- use a canvas?
 * box contains "cards" with their own layout
 * icon: name__________no# in cart___ add/remove from cart
 * 
 * each card is a set size and needs to fit in two columns in the search result
 * the positions will be recalculated dynamically
 * 
 * make sure the code is reusable because it will be used again for the cart
 * 
 * 
 * out of stock items default to the bottom of the search result- show the in stock items first
 * then a greyed-out out of stock option
 * 
 * need to add a LOT of test data to get all of this tested
 * 
 * 
 * individual item view:
 * box next to the search result
 * larger icon of the item with item info next to it
 * such as the ID, name and amount remaining
 * 
 * also any style options
 * this is another box inside the item view
 * (add more sql shit) certain items have styles like clothes
 * will need to sort that out later on***
 * 
 * 
 * also include a logout, help and cart buttton
 * logout redirects back to home page
 * cart opens the cart
 * help displays a popup telling users what to do
 * 
 * 
 * cart view will be another card list
 * total price is calculated dynamically
 * users can add and remove items
 * when an item has none remaining it disappears from the cart
 *
 * 
 * customer view must be resizable
 * 
 */

public class CustomerView
{
	public CustomerController cusController;

	private final int WIDTH = UIStyle.customerWinWidth;
	private final int HEIGHT = UIStyle.customerWinHeight;
	private final int COLUMN_WIDTH = WIDTH / 2 - 10;

	private Label title;
	
	
	
	private HBox hbRoot; // Top-level layout manager
	private VBox vbTrolleyPage; // vbTrolleyPage and vbReceiptPage will swap with each other when need
	private VBox vbReceiptPage;

	TextField tfId; // for user input on the search page. Made accessible so it can be accessed or
					// modified by CustomerModel
	
					// where your get/setters! WHERE YOUR GET SETTERS SHINE
	
	TextField tfName; // for user input on the search page. Made accessible so it can be accessed by
						// CustomerModel

	// four controllers needs updating when program going on
	private ImageView ivProduct; // image area in searchPage
	private ImageView ivSearchIcon; // Search icon on top bar
	private Label lbProductInfo;// product text info in searchPage
	private TextArea taTrolley; // in trolley Page
	private TextArea taReceipt;// in receipt page
	
	private ComboBox<String> cbCategories; // drop-down box to allow users to select categories

	// Holds a reference to this CustomerView window for future access and
	// management
	// (e.g., positioning the removeProductNotifier when needed).
	private Stage viewWindow;

	
	// Create the VBoxes, draw a divider line and start the view
	public void start(Stage window)
	{
		VBox vbSearchPage = createSearchPage();
		vbTrolleyPage = CreateTrolleyPage();
		vbReceiptPage = createReceiptPage();

		// Create a divider line
		Line line = new Line(0, 0, 0, HEIGHT);
		line.setStrokeWidth(4);
		line.setStroke(Color.PINK);
		VBox lineContainer = new VBox(line);
		lineContainer.setPrefWidth(4); // Give it some space
		lineContainer.setAlignment(Pos.CENTER);

		hbRoot = new HBox(10, vbSearchPage, lineContainer, vbTrolleyPage); // initialize to show trolleyPage
		hbRoot.setAlignment(Pos.CENTER);
		hbRoot.setStyle(UIStyle.rootStyle);

		Scene scene = new Scene(hbRoot, WIDTH, HEIGHT);
		window.setScene(scene);
		window.setTitle("🛒 HappyShop Customer Client");
		WinPosManager.registerWindow(window, WIDTH, HEIGHT); // calculate position x and y for this window
		window.show();
		viewWindow = window;// Sets viewWindow to this window for future reference and management.
	}

	// TODO reformat search page
	// Handles the layout for the search box
	private VBox createSearchPage()
	{
		// Put the title in a separate HBox for alignment
		Label laPageTitle = new Label("HappyShop");
		laPageTitle.setStyle(UIStyle.labelTitleStyle);
		laPageTitle.setAlignment(Pos.TOP_LEFT);
		HBox hbTitle = new HBox(laPageTitle);

		// Search Bar
		
		// Set label properties
		tfId = new TextField();
		tfId.setPromptText("Search for an item by name or ID");
		tfId.setStyle(UIStyle.textFiledStyle);
		tfId.setPrefWidth(COLUMN_WIDTH / 2);

		// Display the search icon
		int icon_xy = 32;
		ivSearchIcon = new ImageView("search_icon.png");
		ivSearchIcon.setFitHeight(icon_xy);
		ivSearchIcon.setFitWidth(icon_xy);
		
		// Categories ComboBox
		cbCategories = new ComboBox<String>();
		cbCategories.getItems().add("Select Category");
		cbCategories.getSelectionModel().selectFirst();
		HBox hbId = new HBox(10, ivSearchIcon, tfId, cbCategories);

		Label laName = new Label("Name:");
		laName.setStyle(UIStyle.labelStyle);
		tfName = new TextField();
		tfName.setPromptText("implement it if you want");
		tfName.setStyle(UIStyle.textFiledStyle);
		HBox hbName = new HBox(10, laName, tfName);	// Attach the Search bar and ComboBox to a HBox
		
		// Search result box

		Label laPlaceHolder = new Label(" ".repeat(15)); // create left-side spacing so that this HBox aligns with
														// others in the layout.
		Button btnSearch = new Button("Search");
		btnSearch.setStyle(UIStyle.buttonStyle);
		btnSearch.setOnAction(this::buttonClicked);
		Button btnAddToTrolley = new Button("Add to Trolley");
		btnAddToTrolley.setStyle(UIStyle.buttonStyle);
		btnAddToTrolley.setOnAction(this::buttonClicked);
		HBox hbBtns = new HBox(10, laPlaceHolder, btnSearch, btnAddToTrolley);

		ivProduct = new ImageView("imageHolder.jpg");
		ivProduct.setFitHeight(60);
		ivProduct.setFitWidth(60);
		ivProduct.setPreserveRatio(true); // Image keeps its original shape and fits inside 60×60
		ivProduct.setSmooth(true); // make it smooth and nice-looking

		lbProductInfo = new Label("Thank you for shopping with us.");
		lbProductInfo.setWrapText(true);
		lbProductInfo.setMinHeight(Label.USE_PREF_SIZE); // Allow auto-resize
		lbProductInfo.setStyle(UIStyle.labelMulLineStyle);
		HBox hbSearchResult = new HBox(5, ivProduct, lbProductInfo);
		hbSearchResult.setAlignment(Pos.CENTER_LEFT);

		VBox vbSearchPage = new VBox(15, hbTitle, hbId, hbName, hbBtns, hbSearchResult);
		vbSearchPage.setPrefWidth(COLUMN_WIDTH);
		vbSearchPage.setAlignment(Pos.TOP_CENTER);
		vbSearchPage.setStyle("-fx-padding: 15px;");

		return vbSearchPage;
	}

	private VBox CreateTrolleyPage()
	{
		Label laPageTitle = new Label("🛒🛒  Trolley 🛒🛒");
		laPageTitle.setStyle(UIStyle.labelTitleStyle);

		taTrolley = new TextArea();
		taTrolley.setEditable(false);
		taTrolley.setPrefSize(WIDTH / 2, HEIGHT - 50);

		Button btnCancel = new Button("Cancel");
		btnCancel.setOnAction(this::buttonClicked);
		btnCancel.setStyle(UIStyle.buttonStyle);

		Button btnCheckout = new Button("Check Out");
		btnCheckout.setOnAction(this::buttonClicked);
		btnCheckout.setStyle(UIStyle.buttonStyle);

		HBox hbBtns = new HBox(10, btnCancel, btnCheckout);
		hbBtns.setStyle("-fx-padding: 15px;");
		hbBtns.setAlignment(Pos.CENTER);

		vbTrolleyPage = new VBox(15, laPageTitle, taTrolley, hbBtns);
		vbTrolleyPage.setPrefWidth(COLUMN_WIDTH);
		vbTrolleyPage.setAlignment(Pos.TOP_CENTER);
		vbTrolleyPage.setStyle("-fx-padding: 15px;");
		return vbTrolleyPage;
	}

	private VBox createReceiptPage()
	{
		Label laPageTitle = new Label("Receipt");
		laPageTitle.setStyle(UIStyle.labelTitleStyle);

		taReceipt = new TextArea();
		taReceipt.setEditable(false);
		taReceipt.setPrefSize(WIDTH / 2, HEIGHT - 50);

		Button btnCloseReceipt = new Button("OK & Close"); // btn for closing receipt and showing trolley page
		btnCloseReceipt.setStyle(UIStyle.buttonStyle);

		btnCloseReceipt.setOnAction(this::buttonClicked);

		vbReceiptPage = new VBox(15, laPageTitle, taReceipt, btnCloseReceipt);
		vbReceiptPage.setPrefWidth(COLUMN_WIDTH);
		vbReceiptPage.setAlignment(Pos.TOP_CENTER);
		vbReceiptPage.setStyle(UIStyle.rootStyleYellow);
		return vbReceiptPage;
	}

	private void buttonClicked(ActionEvent event)
	{
		try
		{
			Button btn = (Button) event.getSource();
			String action = btn.getText();
			if (action.equals("Add to Trolley"))
			{
				showTrolleyOrReceiptPage(vbTrolleyPage); // ensure trolleyPage shows if the last customer did not close
															// their receiptPage
														// their receiptPage
			}
			if (action.equals("OK & Close"))
			{
				showTrolleyOrReceiptPage(vbTrolleyPage);
			}
			cusController.doAction(action);
		} catch (SQLException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	public void update(String imageName, String searchResult, String trolley, String receipt)
	{

		ivProduct.setImage(new Image(imageName));
		lbProductInfo.setText(searchResult);
		taTrolley.setText(trolley);
		if (!receipt.equals(""))
		{
			showTrolleyOrReceiptPage(vbReceiptPage);
			taReceipt.setText(receipt);
		}
	}

	// Replaces the last child of hbRoot with the specified page.
	// the last child is either vbTrolleyPage or vbReceiptPage.
	private void showTrolleyOrReceiptPage(Node pageToShow)
	{
		int lastIndex = hbRoot.getChildren().size() - 1;
		if (lastIndex >= 0)
		{
			hbRoot.getChildren().set(lastIndex, pageToShow);
		}
	}

	WindowBounds getWindowBounds()
	{
		return new WindowBounds(viewWindow.getX(), viewWindow.getY(), viewWindow.getWidth(), viewWindow.getHeight());
	}
}
