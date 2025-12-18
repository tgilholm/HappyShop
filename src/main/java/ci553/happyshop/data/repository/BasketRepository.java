package ci553.happyshop.data.repository;

import ci553.happyshop.catalogue.BasketItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Defines operations for interfacing with <code>BasketTable</code>. This does not implement <code>CommonRepository</code>
 * because the <code>BasketItem</code> object has a composite primary key.
 */
public interface BasketRepository
{
    /**
     * Retrieve all the <code>BasketItem</code> objects corresponding to <code>customerID</code>
     * @param customerID the primary key of a <code>Customer</code> object
     * @return a list of <code>BasketItem</code> objects, or null
     */
    @Nullable
    List<BasketItem> getAllItems(long customerID);

    /**
     * Adds a new item to the <code>BasketTable</code>
     * @param customerID the primary key of a <code>Customer</code> object
     * @param productID the primary key of a <code>Product</code> object
     * @param quantity the number of items to add
     */
    void addItem(long customerID, long productID, int quantity);


    /**
     * Add a new item to the <code>BasketTable</code> or update its quantity if it already exists
     * @param customerID the primary key of a <code>Customer</code> object
     * @param productID the primary key of a <code>Product</code> object
     * @param quantity the number of items to add
     */
    void addOrUpdateItem(long customerID, long productID, int quantity);

    /**
     * Removes the item matching <code>customerID</code> and <code>productID</code> from the table
     * @param customerID the primary key of a <code>Customer</code> object
     * @param productID the primary key of a <code>Product</code> object
     */
    void removeItem(long customerID, long productID);

    /**
     * Decrements the number of items by if the quantity is greater than 1, deletes it completely otherwise
     * @param customerID the primary key of a <code>Customer</code> object
     * @param productID the primary key of a <code>Product</code> object
     */
    void decreaseOrRemoveItem(long customerID, long productID);

    /**
     * Changes the quantity of an item in the <code>BasketTable</code>
     * @param customerID the primary key of a <code>Customer</code> object
     * @param productID the primary key of a <code>Product</code> object
     * @param newQuantity the new quantity for the item
     */
    void updateQuantity(long customerID, long productID, int newQuantity);

    /**
     * Returns the quantity of an item in the <code>BasketTable</code>
     * @param customerID the primary key of a <code>Customer</code> object
     * @param productID the primary key of a <code>Product</code> object
     * @return an <code>int</code> value of the quantity
     */
    int getQuantity(long customerID, long productID);

    /**
     * Removes all items connected to <code>customerID</code>
     * @param customerID the primary key of a <code>Customer</code> object
     */
    void emptyBasket(long customerID);
}
