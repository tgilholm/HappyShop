package ci553.happyshop.utility;

import ci553.happyshop.catalogue.Product;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Declares methods called by buttons on cards (ListCell, TilePane etc)
 */
public class ButtonActionCallback
{
    private final Logger logger = LogManager.getLogger();

    // Define functions provided by classes using this callback
    // This allows interchangeability between models and code reuse.
    // Consumers take a parameter and return nothing.
    // Functions take a parameter and return a result
    private final Consumer<Product> onAdd;  // Represents a "void" function
    private final Consumer<Product> onRemove;
    private final Function<Product, Integer> getQty;    // Represents a function with an Integer return type


    /**
     * Constructs a ButtonActionCallback with the provided functions determining
     * @param onAdd a <code>Consumer</code> method determining "add item" behaviour for a specified <code>Product</code>
     * @param onRemove a <code>Consumer</code> method determining "remove item" behaviour for a specified <code>Product</code>
     * @param getQty a <code>Function</code> that gets the quantity of a specified <code>Product</code>
     */
    public ButtonActionCallback(Consumer<Product> onAdd, Consumer<Product> onRemove, Function<Product, Integer> getQty)
    {
        this.onAdd = onAdd;
        this.onRemove = onRemove;
        this.getQty = getQty;
    }


    /**
     * Executes the <code>onAdd</code> <code>Consumer</code> on a specified <code>Product</code>
     * @param product the <code>Product</code> object to add
     */
    public void onAddItem(@NotNull Product product)
    {
        logger.debug("Adding {} to basket", product);
        onAdd.accept(product);  // "accepts" or carries out the method
    }

    /**
     * Executes the <code>onRemove</code> <code>Consumer</code> on a specified <code>Product</code>
     * @param product the <code>Product</code> object to remove
     */
    void onRemoveItem(@NotNull Product product)
    {
        logger.debug("Removing {} from basket", product);
        onRemove.accept(product);
    }


    /**
     * Executes the <code>getQty</code> <code>Function</code> on a specified <code>Product</code>
     * @param product the <code>Product</code> object from which the quantity is extracted.
     * @return an int value of the quantity
     */
    int getBasketQuantity(@NotNull Product product)
    {
        logger.debug("Getting qty for {}", product);
        return getQty.apply(product); // "applies" or gets the result from the method
    }
}