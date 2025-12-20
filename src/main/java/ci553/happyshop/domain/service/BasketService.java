package ci553.happyshop.domain.service;

import ci553.happyshop.catalogue.DTO.BasketItemWithDetails;
import ci553.happyshop.data.repository.BasketRepository;
import ci553.happyshop.data.repository.ProductRepository;
import ci553.happyshop.data.repository.RepositoryFactory;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Business logic methods for processing data before accessing the data layer. Defined as an <code>abstract</code>
 * class to define repositories, logging, and data change flags and keep implementations clean
 */
public abstract class BasketService
{
    // Get repository instances
    protected final BasketRepository basketRepository = RepositoryFactory.getBasketRepository();
    protected final ProductRepository productRepository = RepositoryFactory.getProductRepository();
    protected final IntegerProperty changeProperty = new SimpleIntegerProperty(0); // Used for updating lists on changes

    protected static final Logger logger = LogManager.getLogger();


    /**
     * Updates the <code>changeProperty</code>. Use whenever the underlying data has changed to trigger
     * all observers waiting to update internal lists
     */
    protected void notifyChanged()
    {
        changeProperty.set(changeProperty.getValue() + 1);
        logger.debug("notifyChanged() invoked");
    }


    /**
     * Exposes an observable form of the change counter
     * @return an immutable form of the counter
     */
    public ReadOnlyIntegerProperty basketChanged()
    {
        return changeProperty;
    }

    /**
     * Decrements the number of items by if the quantity is greater than 1, deletes it completely otherwise.
     * This avoids items with <code>quantity = 0</code> in the basket
     *
     * @param customerID the primary key of a <code>Customer</code> object
     * @param productID  the primary key of a <code>Product</code> object
     */
    public abstract void decreaseOrRemoveItem(long customerID, long productID);

    /**
     * Add a new item to the <code>BasketTable</code> or update its quantity if it already exists
     *
     * @param customerID the primary key of a <code>Customer</code> object
     * @param productID  the primary key of a <code>Product</code> object
     * @param quantity   the number of items to add
     */
    public abstract void addOrUpdateItem(long customerID, long productID, int quantity);

    /**
     * Returns the quantity of an item in the <code>BasketTable</code>
     *
     * @param customerID the primary key of a <code>Customer</code> object
     * @param productID  the primary key of a <code>Product</code> object
     * @return an <code>int</code> value of the quantity
     */
    public abstract int getQuantity(long customerID, long productID);

    /**
     * Gets the total price of all the BasketItems linked to this customerID
     * @param customerID the primary key of a <code>Customer</code> object
     * @return a <code>double</code> total price
     */
    public abstract double getBasketTotalPrice(long customerID);

    /**
     * Removes all items connected to <code>customerID</code>
     *
     * @param customerID the primary key of a <code>Customer</code> object
     */
    public abstract void emptyBasket(long customerID);

    /**
     * Gets a list of <code>BasketItem</code> objects with product and category details attached
     *
     * @param customerID the primary key of a <code>Customer</code> object
     * @return a list of <code>BasketItem</code> objects, or null
     */
    @Nullable
    public abstract List<BasketItemWithDetails> getAll(long customerID);
}
