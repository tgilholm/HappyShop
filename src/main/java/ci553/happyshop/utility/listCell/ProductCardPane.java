package ci553.happyshop.utility.listCell;

import ci553.happyshop.catalogue.Product;
import ci553.happyshop.utility.handlers.ImageHandler;
import ci553.happyshop.utility.handlers.StockDisplayHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import org.jetbrains.annotations.NotNull;


/**
 * A CardPane subclass that loads ProductTileCell.fxml.
 * Handles button clicks and updates product details.
 */
public class ProductCardPane extends CardPane
{
    @FXML
    private ImageView ivImage;

    @FXML
    private Label lbName, lbPrice, lbStock, lbBasketQty;

    @FXML
    private Button btnAdd, btnRemove;


    /**
     * Constructs a new <code>ProductCardPane</code> from a specified <code>Product</code> and a <code>callback</code>
     * Loads FXML in the parent class and initializes with product data.
     *
     * @param product  the specific product to initialize the layout with
     * @param callback defined actions for the buttons on the layout
     */
    public ProductCardPane(Product product, ProductCardCallback callback)
    {
        super("/fxml/ProductTileCell.fxml");    // Invoke super constructor to load the FXML

        // Initialise the product data
        updateProduct(product, callback);
    }


    /**
     * Updates the layout of the ProductCardPane when the product data changes
     *
     * @param product  the <code>Product</code> from which data is extracted
     * @param callback the callback behaviour for the buttons
     */
    public void updateProduct(@NotNull Product product, @NotNull ProductCardCallback callback)
    {
        // Set text fields
        lbName.setText(product.getName());
        lbPrice.setText(String.format("£%.2f", product.getUnitPrice()));

        // Set Product Image
        ivImage.setImage(ImageHandler.getImageFromProduct(product));

        // The colour of lbStock changes depending on the quantity remaining
        int stockRemaining = product.getStockQuantity();
        StockDisplayHandler.updateStockLabel(lbStock, stockRemaining);


        lbBasketQty.setText(String.valueOf(callback.getBasketQuantity(product)));

        // Add button action
        btnAdd.setOnAction(x ->
        {
            // Add the item to the basket & update quantity
            callback.onAddItem(product);
            lbBasketQty.setText(String.valueOf(callback.getBasketQuantity(product)));
        });

        // Remove button action
        btnRemove.setOnAction(x ->
        {
            // Remove the item from the basket & update quantity
            callback.onRemoveItem(product);
            lbBasketQty.setText(String.valueOf(callback.getBasketQuantity(product)));
        });

        // Hide the "remove" button if there are none in the basket
        btnRemove.setDisable(callback.getBasketQuantity(product) == 0);

        // Hide the "add" button if the max quantity has been reached
        btnAdd.setDisable(callback.getBasketQuantity(product) >= callback.getStockQuantity(product));
    }
}
