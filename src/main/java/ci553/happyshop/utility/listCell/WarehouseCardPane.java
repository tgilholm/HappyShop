package ci553.happyshop.utility.listCell;


import ci553.happyshop.catalogue.Product;
import org.jetbrains.annotations.NotNull;

/**
 * CardPane subclass loading WarehouseTileCell.fxml.
 * Handles "edit" and "delete" button clicks and displays product info
 */
public class WarehouseCardPane extends CardPane
{

    /**
     * Calls the parent constructor to initialize the layout
     */
    public WarehouseCardPane()
    {
        super("/fxml/WarehouseTileCell.fxml");

        // Initialise the product data
        updateProduct(product, callback);
    }

    public void updateProduct(@NotNull Product product, @NotNull WarehouseCardCallback)
}
