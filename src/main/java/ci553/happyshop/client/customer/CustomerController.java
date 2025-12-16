package ci553.happyshop.client.customer;

import ci553.happyshop.catalogue.Product;
import ci553.happyshop.utility.ProductCardPane;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;

import java.net.URL;

/**
 * Controller class for the customer client.
 * Initializes FXML elements and binds the Model to the View
 */

public class CustomerController
{
    private final CustomerModel cusModel;

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
    private TilePane tpProducts;

    public CustomerController(CustomerModel cusModel)
    {
        this.cusModel = cusModel;
    }

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

        // Set up category combobox
        cbCategories.getItems().add("Select Category");
        cbCategories.getSelectionModel().selectFirst();

        // Load products from the database
        cusModel.loadProducts();

        // Bind the product list to the view
        bindProductList();

        // Add a listener to automatically search as users type
        tfSearchBar.textProperty().addListener((observable, oldValue, newValue) ->
        {
            cusModel.setSearchFilter(newValue);
        });

        // Automatically refresh when the filteredList changes
        cusModel.getSearchFilteredList().addListener((ListChangeListener<Product>) change -> bindProductList());
    }

    /**
     * Refreshes the tilePane with the filtered product list. Defines the ButtonActionCallback
     */
    private void bindProductList()
    {
        // Clear the tilePane
        tpProducts.getChildren().clear();

        // Define the callback for the ProductCardPane
        ProductCardPane.ButtonActionCallback callback = new ProductCardPane.ButtonActionCallback()
        {
            @Override
            public void onAddItem(Product product)
            {
                // todo basket
            }

            @Override
            public void onRemoveItem(Product product)
            {

            }

            @Override
            public int getBasketQuantity(Product product)
            {
                return 0;
            }
        };

        // Add each of the products as a card
        for (Product product : cusModel.getSearchFilteredList())
        {
            GridPane productCard = createProductCard(product, callback);

            // Add the click listener to select a product
            productCard.setOnMouseClicked(x ->
            {
                // todo detail pane
            });

            tpProducts.getChildren().add(productCard);
        }
    }

    // Load the layout for each card
    private GridPane createProductCard(Product product, ProductCardPane.ButtonActionCallback callback)
    {
        return new ProductCardPane(product, callback);
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
