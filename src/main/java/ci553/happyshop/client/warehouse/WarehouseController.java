package ci553.happyshop.client.warehouse;

import ci553.happyshop.base_mvm.BaseController;
import ci553.happyshop.catalogue.Category;
import ci553.happyshop.catalogue.DTO.ProductWithCategory;
import ci553.happyshop.catalogue.Product;
import ci553.happyshop.utility.ButtonActionCallback;
import ci553.happyshop.utility.FileHandler;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;

public class WarehouseController extends BaseController<WarehouseModel>
{
    public WarehouseModel model;
    public @FXML ImageView ivSearchIcon;
    public @FXML TextField tfSearchBar, tfName, tfPrice;
    public @FXML ComboBox<String> cbCategories;
    public @FXML TilePane tpProducts;
    public @FXML ImageView ivDetailImage;
    public @FXML ComboBox<String> cbSelectMode;
    public @FXML Label lbDetailID;
    public @FXML ComboBox<String> cbChangeCategory;


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


    public void goBack(ActionEvent actionEvent)
    {
    }


    public void addStock(ActionEvent actionEvent)
    {
    }


    public void removeStock(ActionEvent actionEvent)
    {
    }


    public void saveChanges(ActionEvent actionEvent)
    {

    }

    /**
     * Refreshes the tilePane with the filtered product list. Defines the ButtonActionCallback
     */
    private void bindProductList()
    {
        tpProducts.getChildren().clear();            // Load products from the database

        // Provide the methods to the callback
        ButtonActionCallback callback = new ButtonActionCallback(
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
