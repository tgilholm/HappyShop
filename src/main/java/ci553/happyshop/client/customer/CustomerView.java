package ci553.happyshop.client.customer;

import ci553.happyshop.utility.UIStyle;
import ci553.happyshop.utility.WinPosManager;
import ci553.happyshop.utility.WindowBounds;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

/**
 * The CustomerView is separated into two sections by a line :
 * <p>
 * 1. Search Page – Always visible, allowing customers to browse and search for
 * products. 2. the second page – display either the Trolley Page or the Receipt
 * Page depending on the current context. Only one of these is shown at a time.
 */

/*
 * TODO VIEW REDESIGN - divide the screen up: list of items available on one
 * side, specific item on the other item list takes about 2/3, item view takes
 * about 1/3
 *
 * do the layout first, get the items where they should be add a search bar (add
 * a hint telling users what to do) search bar will automatically search as the
 * user types search results appear in the box below sorted alphabetically
 *
 * add a category selection dropdown (need to edit SQL database to support
 * categories) when a category is selected, only the items that appear in that
 * category will show in the search result search result needs to allow
 * scrolling through options
 *
 * search result may have to be custom implementation new class for the
 * container- use a canvas? box contains "cards" with their own layout icon:
 * name__________no# in cart___ add/remove from cart
 *
 * each card is a set size and needs to fit in two columns in the search result
 * the positions will be recalculated dynamically
 *
 * make sure the code is reusable because it will be used again for the cart
 *
 *
 * out of stock items default to the bottom of the search result- show the in
 * stock items first then a greyed-out out of stock option
 *
 * need to add a LOT of test data to get all of this tested
 *
 *
 * individual item view: box next to the search result larger icon of the item
 * with item info next to it such as the ID, name and amount remaining
 *
 * also any style options this is another box inside the item view (add more sql
 * shit) certain items have styles like clothes will need to sort that out later
 * on***
 *
 *
 * also include a logout, help and cart button logout redirects back to home
 * page cart opens the cart help displays a popup telling users what to do
 *
 *
 * cart view will be another card list total price is calculated dynamically
 * users can add and remove items when an item has none remaining it disappears
 * from the cart
 *
 *
 * customer view must be resizable
 *
 */

public class CustomerView
{
    public CustomerController cusController;

    TextField tfSearchBar; // for user input on the search page. Made accessible so it can be accessed or
    // modified by CustomerModel


    private ImageView ivProduct; // image area in searchPage
    private Label lbProductInfo;// product text info in searchPage
    private TextArea taTrolley; // in trolley Page
    private TextArea taReceipt;// in receipt page

    // Holds a reference to this CustomerView window for future access and
    // management
    // (e.g., positioning the removeProductNotifier when needed).
    private Stage viewWindow;


    /**
     * Loads the FXML and starts a new Stage
     * @param window the Stage to start
     */
    public void start(Stage window)
    {
        GridPane gridPane;                    // Root attribute of CustomerView.fxml

        // Null-check the FXML file
        URL fxmlURL = getClass().getResource("/fxml/CustomerView.fxml");
        if (fxmlURL == null)
        {
            throw new IllegalStateException("CustomerView.fxml not found");
        }
        FXMLLoader loader = getFxmlLoader(fxmlURL);

        try
        {
            gridPane = loader.load();
        } catch (IOException e)
        {
            throw new RuntimeException("Failed to load FXML: " + e.getMessage());
        }

        int WIDTH = UIStyle.customerWinWidth;
        int HEIGHT = UIStyle.customerWinHeight;

        Scene scene = new Scene(gridPane, WIDTH, HEIGHT);

        // Load the CSS
        URL resource = getClass().getResource("/css/styles.css");
        if (resource != null)
        {
            scene.getStylesheets().add(resource.toExternalForm());
        } else
        {
            throw new IllegalStateException("Failed to load CSS");
        }

        window.setScene(scene);
        window.setTitle("🛒 HappyShop Customer Client");
        WinPosManager.registerWindow(window, WIDTH, HEIGHT); // calculate position x and y for this window

        window.show();        // Start the window
        viewWindow = window;// Sets viewWindow to this window for future reference and management.
    }

    /**
     * Creates an FXML loader from a URL and binds <code>CustomerController</code> to it
     * @param fxmlURL the location of the fxml file to load
     * @return an FXML loader set to the location specified by <code>fxmlURL</code>
     */
    private FXMLLoader getFxmlLoader(URL fxmlURL)
    {
        FXMLLoader loader = new FXMLLoader(fxmlURL);        // Loads the FXML

        // Set the ControllerFactory (controller is already defined in FXML)
        loader.setControllerFactory(controllerFactory ->
        {
            if (controllerFactory == CustomerController.class)
            {
                return cusController;
            }

            try
            {
                return controllerFactory.getDeclaredConstructor().newInstance();
            } catch (Exception e)
            {
                throw new RuntimeException("Cannot instantiate Controller");
            }
        });
        return loader;
    }


    // Update the view when new data is received from the model
    public void update(String imageName, String searchResult, String trolley, String receipt)
    {
        ivProduct.setImage(new Image(imageName));
        lbProductInfo.setText(searchResult);
        taTrolley.setText(trolley);
        if (!receipt.isEmpty())
        {
            //showTrolleyOrReceiptPage(vbReceiptPage);
            taReceipt.setText(receipt);
        }
    }


    // Retrieve the X, Y coordinates of the extreme coordinates of this window
    WindowBounds getWindowBounds()
    {
        return new WindowBounds(viewWindow.getX(), viewWindow.getY(), viewWindow.getWidth(), viewWindow.getHeight());
    }
}
