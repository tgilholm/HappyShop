package ci553.happyshop.client.warehouse;

import ci553.happyshop.base_mvm.BaseController;
import ci553.happyshop.catalogue.Category;
import ci553.happyshop.catalogue.DTO.ProductWithCategory;
import ci553.happyshop.catalogue.Product;
import ci553.happyshop.client.login.LoginClient;
import ci553.happyshop.utility.handlers.ImageHandler;
import ci553.happyshop.utility.handlers.StockDisplayHelper;
import ci553.happyshop.utility.handlers.FileHandler;
import ci553.happyshop.utility.listCell.WarehouseCardCallback;
import ci553.happyshop.utility.listCell.WarehouseCardPane;
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

public class WarehouseController extends BaseController<WarehouseModel>
{
    public @FXML ImageView ivSearchIcon;
    public @FXML TextField tfSearchBar, tfName, tfPrice;
    public @FXML ComboBox<String> cbCategories;
    public @FXML TilePane tpProducts;
    public @FXML ImageView ivDetailImage;
    public @FXML ComboBox<String> cbSelectMode;
    public @FXML Label lbDetailID, lbStockQty;
    public @FXML ComboBox<String> cbChangeCategory;
    public @FXML Button btnBack;


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
     * Refreshes the tilePane with the filtered product list. Defines the WarehouseCardCallback
     */
    private void bindProductList()
    {
        // Load products and display them in the TilePane
        tpProducts.getChildren().clear();

        // Provide callback behaviour
        WarehouseCardCallback callback = new WarehouseCardCallback(
                this::updateDetailPane,
                model::deleteItem
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
     * Updates the detailPane on the View with the details from a <code>ProductWithCategory</code> object
     *
     * @param productWithCategory the <code>ProductWithCategory</code> object to load data from
     */
    private void updateDetailPane(@NotNull ProductWithCategory productWithCategory)
    {
        ivDetailImage.setImage(ImageHandler.getImageFromProduct(productWithCategory.product()));
        tfName.setText(productWithCategory.product().getName());
        lbDetailID.setText("ID: " + productWithCategory.product().getId());
        tfPrice.setText(String.format("£%.2f", productWithCategory.product().getUnitPrice()));

        // Set the category name
        // todo set category in combobox
        //cbChangeCategory.setText(productWithCategory.category().getName());

        // Use the dynamic stock colour method from StockDisplayHelper
        StockDisplayHelper.updateStockLabel(lbStockQty, productWithCategory.product().getStockQuantity());
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


    public void addStock()
    {
    }


    public void removeStock()
    {
    }


    public void saveChanges()
    {

    }


//	void process(String action) throws SQLException, IOException
//	{
//		switch (action)
//		{
//		case "🔍":
//			model.doSearch();
//			break;
//		case "Edit":
//			//model.doEdit();
//			break;
//		case "Delete":
//			//model.doDelete();
//			break;
//		case "➕":
//			model.doChangeStockBy("add");
//			break;
//		case "➖":
//			model.doChangeStockBy("sub");
//			break;
//		case "Submit":
//			//model.doSummit();
//			break;
//		case "Cancel":  // clear the editChild
//			model.doCancel();
//			break;
//		}
//	}


}
