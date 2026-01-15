package ci553.happyshop.utility.listCell;

import ci553.happyshop.catalogue.Product;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * Declares the methods for the interaction with Warehouse cards.
 * Functionality is delegated to implementations for the "edit"
 * and "delete" buttons
 */
public class WarehouseCardCallback
{
    private final Logger logger = LogManager.getLogger();

    private final Consumer<Product> onEdit;     // Edits a specific Product
    private final Consumer<Product> onDelete;   // Deletes a specific Product


    /**
     * Constructs a WarehouseCardCallback with the provided functions determining what
     * happens with editing and deleting
     * @param onEdit    a <code>Consumer</code> method determining "edit item" behaviour for a specified <code>Product</code>
     * @param onDelete a <code>Consumer</code> method determining "delete item" behaviour for a specified <code>Product</code>
     */
    public WarehouseCardCallback(Consumer<Product> onEdit, Consumer<Product> onDelete)
    {
        this.onEdit = onEdit;
        this.onDelete = onDelete;
    }

    /**
     * Executes the <code>onEdit</code> <code>Consumer</code> on a specified <code>Product</code>
     *
     * @param product the <code>Product</code> object to add
     */
    public void onEditItem(@NotNull Product product)
    {
        logger.debug("Adding {} to basket", product);
        onEdit.accept(product);  // "accepts" or carries out the method
    }


    /**
     * Executes the <code>onDelete</code> <code>Consumer</code> on a specified <code>Product</code>
     *
     * @param product the <code>Product</code> object to remove
     */
    void onDeleteItem(@NotNull Product product)
    {
        logger.debug("Removing {} from basket", product);
        onDelete.accept(product);
    }
}
