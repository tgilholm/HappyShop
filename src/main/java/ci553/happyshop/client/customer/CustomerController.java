package ci553.happyshop.client.customer;

import ci553.happyshop.catalogue.Product;
import ci553.happyshop.utility.ProductCell;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;

/**
 * Controller class for the customer client.
 * Initializes FXML elements and binds the Model to the View
  */

public class CustomerController
{
    public CustomerModel cusModel;


    @FXML
    public TextField tfSearchBar;

    @FXML
    private ImageView ivSearchIcon;

    @FXML
    private ComboBox<String> cbCategories;

    @FXML
    private Button btnAccount;

    @FXML
    private Button btnCart;

    @FXML
    private ListView<Product> lvProducts;

    /**
     * Initialises the controller after the root element is finished processing.<br>
     * <pre>
     *     - Initialises the View elements
     *     - Sets the ListView cell factory
     *     - Binds the Model productList to the View and loads all products
     * </pre>
     */
    @FXML
    public void initialize()
    {
        // Load the search icon
        URL iconUrl = getClass().getResource("/images/search_icon.png");
        if (iconUrl != null)
        {
            Image image = new Image(iconUrl.toExternalForm());
            ivSearchIcon.setImage(image);
        } else
        {
            System.err.println("Image not found: /images/search_icon.png");
        }

        // Set the ComboBox to display "Select Category" initially
        cbCategories.getItems().add("Select Category");
        cbCategories.getSelectionModel().selectFirst();

        // Set lvProducts cell factory to the custom ProductCell
        lvProducts.setCellFactory(listView -> new ProductCell());

        cusModel.loadProducts();                                    // Load the product list in the Model
        lvProducts.setItems(cusModel.getSearchFilteredList());      // Binds the filteredList to the ListView

        // Add a listener to tfSearch to automatically search as users type
        tfSearchBar.textProperty().addListener((observable, oldValue, newValue) ->
        {
            cusModel.setSearchFilter(newValue);
        });
    }

    // Handle the "account" button input
    public void accountClicked()
    {
        System.out.println("Account button clicked");
    }

    public void cartClicked()
    {
        System.out.println("Cart button clicked");
    }
}
