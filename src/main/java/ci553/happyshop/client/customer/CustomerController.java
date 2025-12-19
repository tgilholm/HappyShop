package ci553.happyshop.client.customer;

import ci553.happyshop.catalogue.Category;
import ci553.happyshop.catalogue.Product;
import ci553.happyshop.catalogue.DTO.ProductWithCategory;
import ci553.happyshop.utility.ButtonActionCallback;
import ci553.happyshop.utility.ImageHandler;
import ci553.happyshop.utility.ProductCardPane;
import ci553.happyshop.utility.StockDisplayHelper;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.net.URL;

/**
 * Controller class for the customer client.
 * Initializes FXML elements and binds the Model to the View
 */
public class CustomerController
{
    private final CustomerModel cusModel;
    private final Logger logger = LogManager.getLogger();

    @FXML
    public TextField tfSearchBar;

    @FXML
    private Label lbDetailName, lbDetailPrice, lbDetailBasketQty, lbStockQty, lbDetailID, lbDetailCategoryName;

    @FXML
    private ImageView ivSearchIcon, ivDetailImage;

    @FXML
    private ComboBox<String> cbCategories;

    @FXML
    private Button btnAccount, btnBasket;

    @FXML
    private TilePane tpProducts;

    public CustomerController(CustomerModel cusModel)
    {
        this.cusModel = cusModel;
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
        URL iconUrl = getClass().getResource("/images/search_icon.png");
        if (iconUrl != null)
        {
            Image image = new Image(iconUrl.toExternalForm());
            ivSearchIcon.setImage(image);
        } else
        {
            logger.warn("Image not found: /images/search_icon.png");
        }

        // Set up category combobox
        cbCategories.getItems().add("Select Category");
        cbCategories.getSelectionModel().selectFirst();

        cusModel.loadProducts();            // Load product list
        cusModel.loadCategories();          // Load category list
        bindProductList();                  // Bind the product list to the view
        refreshComboBox();


        // Add a listener on the comboBox valueProperty, extract the string and set the categoryFilter
        cbCategories.valueProperty().addListener((observable, oldValue, newValue) ->
        {
            cusModel.setCategoryFilter(newValue);
            logger.info("Set category filter to {}", newValue);
        });

        // Add a listener to automatically search as users type
        tfSearchBar.textProperty().addListener((observable, oldValue, newValue) ->
                cusModel.setSearchFilter(newValue));

        // Automatically refresh when the filteredList changes
        cusModel.getSearchFilteredList().addListener((ListChangeListener<ProductWithCategory>) change -> bindProductList());

        // Update ComboBox when the categoryList changes
        cusModel.getCategories().addListener((ListChangeListener<Category>) change ->
                refreshComboBox());

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
        for (Category c : cusModel.getCategories())
        {
            cbCategories.getItems().add(c.getName());
        }
    }

    /**
     * Refreshes the tilePane with the filtered product list. Defines the ButtonActionCallback
     */
    private void bindProductList()
    {
        tpProducts.getChildren().clear();            // Load products from the database

        // Define the callback for the ProductCardPane
        ButtonActionCallback callback = new ButtonActionCallback()
        {
            @Override
            public void onAddItem(@NotNull Product product)
            {
                logger.info("Adding {} to basket", product.getId());
                cusModel.addToBasket(product);
            }

            @Override
            public void onRemoveItem(@NotNull Product product)
            {
                logger.info("Removing {} from basket", product.getId());
                cusModel.removeFromBasket(product);
            }

            @Override
            public int getBasketQuantity(Product product)
            {
                return cusModel.getBasketQuantity(product);
            }
        };

        // Add each of the products as a card
        for (ProductWithCategory productWithCategory : cusModel.getSearchFilteredList())
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
     * @param callback a <code>ProductCardPane.ButtonActionCallback</code>
     * @return a <code>VBox</code> containing the card layout
     */
    @Contract("_, _ -> new")
    private @NotNull VBox createProductCard(Product product, ButtonActionCallback callback)
    {
        return new ProductCardPane(product, callback);
    }

    // Handle the "account" button input
    public void accountClicked()
    {
        System.out.println("Account button clicked");
    }

    /**
     * Opens the basket window
     */
    public void basketClicked()
    {
        cusModel.openBasket();
    }
}
