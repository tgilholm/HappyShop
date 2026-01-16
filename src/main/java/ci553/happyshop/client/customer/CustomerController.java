package ci553.happyshop.client.customer;

import ci553.happyshop.base_mvm.BaseController;
import ci553.happyshop.catalogue.Category;
import ci553.happyshop.catalogue.Product;
import ci553.happyshop.catalogue.DTO.ProductWithCategory;
import ci553.happyshop.client.customer.basket.BasketClient;
import ci553.happyshop.client.login.LoginClient;
import ci553.happyshop.utility.handlers.FileHandler;
import ci553.happyshop.utility.handlers.ImageHandler;
import ci553.happyshop.utility.handlers.StockDisplayHelper;
import ci553.happyshop.utility.listCell.ProductCardCallback;
import ci553.happyshop.utility.listCell.ProductCardPane;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.net.URL;

/**
 * Controller class for the customer client.
 * Initializes FXML elements and binds the Model to the View
 */
public class CustomerController extends BaseController<CustomerModel>
{

    @FXML
    public TextField tfSearchBar;

    @FXML
    private Label lbDetailName, lbDetailPrice, lbDetailBasketQty, lbStockQty, lbDetailID, lbDetailCategoryName;

    @FXML
    private ImageView ivSearchIcon, ivDetailImage;

    @FXML
    private ComboBox<String> cbCategories;

    @FXML
    private Button btnBasket, btnBack;

    @FXML
    private TilePane tpProducts;

    public CustomerController(CustomerModel model)
    {
        super(model);
    }

    /**
     * Initializes the controller after the root element is finished processing.<br>
     * <pre>
     *     - Initializes the View elements
     *     - Sets the ListView cell factory
     *     - Binds Model data to View elements
     * </pre>
     */
    @FXML
    public void initialize()
    {
        // Load the search icon
        URL iconURL = FileHandler.parseURL("/images/search_icon.png");
        if (iconURL != null)
        {
            Image image = new Image(iconURL.toExternalForm());
            ivSearchIcon.setImage(image);
        } else
        {
            logger.warn("Image not found: /images/search_icon.png");
        }

        // Set up category combobox
        refreshComboBox();
        cbCategories.getSelectionModel().selectFirst();

        model.loadProducts();            // Load product list
        model.loadCategories();          // Load category list
        bindProductList();               // Bind the product list to the view

        // Update ComboBox when the categoryList changes
        model.getCategories().addListener((ListChangeListener<Category>) change ->
                refreshComboBox());


        // Add a listener on the comboBox valueProperty, extract the string and set the categoryFilter
        cbCategories.valueProperty().addListener((observable, oldValue, newValue) ->
        {
            model.setCategoryFilter(newValue);
            logger.info("Set category filter to {}", newValue);
        });

        // Add a listener to automatically search as users type
        tfSearchBar.textProperty().addListener((observable, oldValue, newValue) ->
                model.setSearchFilter(newValue));

        // Automatically refresh when the filteredList changes
        model.getSearchFilteredList().addListener((ListChangeListener<ProductWithCategory>) change -> bindProductList());



        logger.info("Finished initializing controller");
    }


    /**
     * Updates the categories comboBox with the list of categories from the model
     */
    private void refreshComboBox()
    {
        // Clear comboBox
        cbCategories.getItems().clear();

        // Add new categories
        for (Category c : model.getCategories())
        {
            cbCategories.getItems().add(c.getName());
        }

        // Add the "select category" placeholder
        cbCategories.getItems().add("Select Category");
    }

    /**
     * Refreshes the tilePane with the filtered product list. Defines the ProductCardCallback
     */
    private void bindProductList()
    {
        tpProducts.getChildren().clear();            // Load products from the database

        // Provide the methods to the callback
        ProductCardCallback callback = new ProductCardCallback(
                model::addToBasket,                     // add product to the basket
                model::removeFromBasket,                // remove product from the basket
                model::getBasketQuantity,               // get the basket quantity
                model::getStockQuantity                 // Get quantity in stock
        );

        // Add each of the products as a card
        for (ProductWithCategory productWithCategory : model.getSearchFilteredList())
        {
            VBox productCard = createProductCard(productWithCategory.product(), callback);

            // Add the click listener to select a product
            productCard.setOnMouseClicked(x ->
            {
                logger.info("Product selected, id: {}", productWithCategory.product().getId());

                // Reset style to remove border on unselected cards
                tpProducts.getChildren().forEach(node ->
                        node.setStyle("-fx-cursor: hand"));

                // Draw a border around the selected item
                productCard.setStyle("-fx-border-color: lightgray; -fx-border-width: 1; -fx-cursor: hand");
                updateDetailPane(productWithCategory);
            });

            tpProducts.getChildren().add(productCard);
        }
    }


    /**
     * Updates the detailPane on the View with the details from a <code>ProductWithCategory</code> object
     * @param productWithCategory the <code>ProductWithCategory</code> object to load data from
     */
    private void updateDetailPane(@NotNull ProductWithCategory productWithCategory)
    {
        ivDetailImage.setImage(ImageHandler.getImageFromProduct(productWithCategory.product()));
        lbDetailName.setText(productWithCategory.product().getName());
        lbDetailID.setText("ID: " + productWithCategory.product().getId());
        lbDetailPrice.setText(String.format("£%.2f", productWithCategory.product().getUnitPrice()));

        // Set the category name
        lbDetailCategoryName.setText(productWithCategory.category().getName());

        // Use the dynamic stock colour method from StockDisplayHelper
        StockDisplayHelper.updateStockLabel(lbStockQty, productWithCategory.product().getStockQuantity());
    }

    /**
     * Create a new <code>ProductCardPane</code> from a <code>Product object</code>
     * @param product the <code>Product</code> object
     * @param callback a <code>ProductCardCallback</code>
     * @return a <code>VBox</code> containing the card layout
     */
    @Contract("_, _ -> new")
    private @NotNull VBox createProductCard(Product product, ProductCardCallback callback)
    {
        return new ProductCardPane(product, callback);
    }

    /**
     * Runs the <code>start</code> method in <code>BasketClient</code>, hides this view
     */
    public void basketClicked()
    {
        // Get the current stage
        Stage stage = (Stage) btnBasket.getScene().getWindow();

        try {
            // Get the current customer from the model
            BasketClient basketClient = new BasketClient(model.getCurrentUser());
            stage.hide();   // Hide the customer view

            // Create a new stage for the basket client
            Stage basket = new Stage();
            basket.setOnHidden(event ->
            {
                stage.show();
                model.loadProducts(); // Load the product list if the basket changed anything
            }); // Re-open the customer view when the basket close
            basket.setOnCloseRequest(event ->
            {
                stage.show();
                model.loadProducts();
            });

            // Start the basket
            basketClient.start(basket);

        } catch (Exception e)
        {
            logger.error("Failed to open basket", e);
        }
    }


    public void goBack()
    {
        Stage stage = (Stage) btnBack.getScene().getWindow();
        stage.close();

        LoginClient.startLoginClient(new Stage());
    }
}
