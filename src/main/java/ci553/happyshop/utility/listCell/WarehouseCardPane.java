package ci553.happyshop.utility.listCell;


import ci553.happyshop.catalogue.Product;
import ci553.happyshop.utility.handlers.ImageHandler;
import ci553.happyshop.utility.handlers.StockDisplayHelper;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import org.jetbrains.annotations.NotNull;

/**
 * CardPane subclass loading WarehouseTileCell.fxml.
 * Handles "edit" and "delete" button clicks and displays product info
 */
public class WarehouseCardPane extends CardPane
{
    @FXML
    private ImageView ivImage;

    @FXML
    private Label lbName, lbPrice, lbStock;

    @FXML
    private Button btnEdit, btnDelete;

    /**
     * Calls the parent constructor to initialize the layout
     */
    public WarehouseCardPane(Product product, WarehouseCardCallback callback)
    {
        super("/fxml/WarehouseTileCell.fxml");

        // Initialise the product data
        updateProduct(product, callback);
    }

    /**
     * Updates the layout of the WarehouseCardPane when the product data changes
     * @param product the <code>Product</code> from which data is extracted
     * @param callback the callback behaviour for the buttons
     */
    public void updateProduct(@NotNull Product product, @NotNull WarehouseCardCallback callback)
    {
        // Set text fields
        lbName.setText(product.getName());
        lbPrice.setText(String.format("£%.2f", product.getUnitPrice()));

        // Set Product Image
        ivImage.setImage(ImageHandler.getImageFromProduct(product));

        // The colour of lbStock changes depending on the quantity remaining
        int stockRemaining = product.getStockQuantity();
        StockDisplayHelper.updateStockLabel(lbStock, stockRemaining);

        // Add action to buttons
        btnDelete.setOnAction(x ->
        {
            // Delete the item via the callback implementation
            callback.onDeleteItem(product);
        });

        btnEdit.setOnAction(x ->
        {
            // Edit the item via the callback implementation
            callback.onEditItem(product);
        });
    }
}
