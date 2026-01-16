package ci553.happyshop.client.warehouse;

import ci553.happyshop.base_mvm.BaseController;
import ci553.happyshop.catalogue.Category;
import ci553.happyshop.catalogue.DTO.ProductWithCategory;
import ci553.happyshop.catalogue.Product;
import ci553.happyshop.client.login.LoginClient;
import ci553.happyshop.utility.alerts.AlertFactory;
import ci553.happyshop.utility.handlers.ImageHandler;
import ci553.happyshop.utility.listCell.WarehouseCardCallback;
import ci553.happyshop.utility.listCell.WarehouseCardPane;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class WarehouseController extends BaseController<WarehouseModel>
{
    public @FXML ImageView ivSearchIcon;
    public @FXML TextField tfSearchBar, tfName, tfPrice, tfStockQty;
    public @FXML ComboBox<String> cbCategories;
    public @FXML TilePane tpProducts;
    public @FXML ImageView ivDetailImage;
    public @FXML ComboBox<String> cbSelectMode;
    public @FXML Label lbDetailID, lbStockQty;
    public @FXML Button btnBack;
    public @FXML Label lbChangeCategory;

    // Temporary (before saveChanges is invoked) values
    private long modifiedProductID;     // The id of the product to be modified
    private String newImageName;   // The location of the product's image
    private String newStockQuantity;
    private String newPrice;
    private String newName;
    private String newCategory;


    public WarehouseController(WarehouseModel model)
    {
        super(model);
    }


    /**
     * Initializes the controller after the root element is finished processing.
     */
    @Override
    @FXML
    public void initialize()
    {
        ivSearchIcon.setImage(ImageHandler.loadFromString("/images/search_icon.png"));

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

        // Add listeners on the detail pane to automatically update temporary values
        // These are only saved when the saveChanges button is pressed
        tfPrice.textProperty().addListener((observable, oldValue, newValue) ->
                newPrice = newValue
        );

        tfName.textProperty().addListener((observable, oldValue, newValue) ->
                newName = newValue);

        tfStockQty.textProperty().addListener(((observable, oldValue, newValue) ->
                newStockQuantity = newValue));

        // Update the model list when the product service updates
        model.productsChangedProperty().addListener((observable, oldValue, newValue) -> model.loadProducts());

        // Automatically refresh when the filteredList changes
        model.getSearchFilteredList().addListener((ListChangeListener<ProductWithCategory>) change -> bindProductList());


        // Observe the validation error property from the model
        model.validationErrorProperty().addListener(((observable, oldValue, newValue) ->
        {
            if (newValue != null && !newValue.isEmpty())        // Prevent repeated alerts by checking for empty value
            {
                Platform.runLater(() ->
                {
                    AlertFactory.warning("Warehouse", "Update Failed", newValue);    // show on the FX thread
                });

                model.resetUserError(); // Reset the flag
            }
        }));

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
     * Refreshes the tilePane with the filtered product list. Defines the WarehouseCardCallback
     */
    private void bindProductList()
    {
        // Load products and display them in the TilePane
        tpProducts.getChildren().clear();

        // Provide callback behaviour
        WarehouseCardCallback callback = new WarehouseCardCallback(
                this::updateDetailPane,
                this::confirmDelete
        );


        // Add each product as a WarehouseCardPane
        for (ProductWithCategory productWithCategory : model.getSearchFilteredList())
        {
            VBox productCard = createProductCard(productWithCategory, callback);

            // Add to the TilePane
            tpProducts.getChildren().add(productCard);
        }
    }


    /**
     * Creates an Alert to confirm deletion before delegating to the model to carry out deletion
     *
     * @param product the <code>Product</code> to delete
     */
    private void confirmDelete(@NotNull Product product)
    {
        AlertFactory.confirmation("Warehouse", "Confirm Deletion?", "Are you sure you want to delete product: " + product.getName())
                .ifPresent(result ->
                        {
                            if (!result.getButtonData().isCancelButton()) // Cancel delete otherwise
                            {
                                model.deleteItem(product);
                            }
                        }
                );
    }


    /**
     * Updates the detailPane on the View with the details from a <code>ProductWithCategory</code> object
     *
     * @param productWithCategory the <code>ProductWithCategory</code> object to load data from
     */
    private void updateDetailPane(@NotNull ProductWithCategory productWithCategory)
    {
        // Extract product & category
        Product product = productWithCategory.product();
        Category category = productWithCategory.category();

        logger.debug("Product image name: {}", product.getImageName());

        // Set temporary data from product details
        modifiedProductID = product.getId();
        newName = product.getName();
        newImageName = product.getImageName();
        newPrice = String.valueOf(product.getUnitPrice());
        newStockQuantity = String.valueOf(product.getStockQuantity());
        newCategory = category.getName();

        ivDetailImage.setImage(ImageHandler.getImageFromProduct(product));
        tfName.setText(product.getName());
        lbDetailID.setText("ID: " + product.getId());
        tfPrice.setText(String.valueOf(product.getUnitPrice()));
        tfStockQty.setText(String.valueOf(product.getStockQuantity()));
        lbChangeCategory.setText(productWithCategory.category().getName());
    }


    /**
     * Create a new <code>WarehouseCardPane</code> from a <code>Product object</code>
     *
     * @param productWithCategory the <code>productWithCategory</code> object
     * @param callback            a <code>WarehouseCardCallback</code>
     * @return a <code>VBox</code> containing the card layout
     */
    @Contract("_, _ -> new")
    private @NotNull VBox createProductCard(ProductWithCategory productWithCategory, WarehouseCardCallback callback)
    {
        return new WarehouseCardPane(productWithCategory, callback);
    }


    /**
     * Closes the warehouse and re-opens the Login screen
     */
    public void goBack()
    {
        Stage stage = (Stage) btnBack.getScene().getWindow();
        stage.close();

        LoginClient.startLoginClient(new Stage());
    }


    /**
     * Delegates to the model to update product details. Passes the new data with the
     * id of the product to be updated
     */
    public void saveChanges()
    {
        // Display an alert to confirm changes first
        AlertFactory.confirmation("Warehouse", "Confirm changes?", "Do you want to save your changes")
                .ifPresent(result ->
                {
                    if (!result.getButtonData().isCancelButton())
                    {
                        logger.debug("Attempting to save new product data");
                        model.saveChanges(modifiedProductID, newName, newImageName, newPrice, newStockQuantity, newCategory);
                    } else
                    {

                        logger.debug("User cancelled edit");
                    }
                });
    }
}
