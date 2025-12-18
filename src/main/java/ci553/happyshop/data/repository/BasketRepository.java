package ci553.happyshop.data.repository;

import ci553.happyshop.catalogue.BasketItem;

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
     * @return a list of <code>BasketItem</code> objects
     */
    List<BasketItem> getAllItems(String customerID);

    /**
     * Adds a new item to the <code>BasketTable</code>
     * @param customerID the primary key of a <code>Customer</code> object
     * @param productID the primary key of a <code>Product</code> object
     * @param quantity the amount of items to add
     */
    void addItem(String customerID, String productID, int quantity);

    /**
     * Removes the item matching <code>customerID</code> and <code>productID</code> from the table
     * @param customerID the primary key of a <code>Customer</code> object
     * @param productID the primary key of a <code>Product</code> object
     */
    void removeItem(String customerID, String productID);

    /**
     * Changes the quantity of an item in the <code>BasketTable</code>
     * @param customerID the primary key of a <code>Customer</code> object
     * @param productID the primary key of a <code>Product</code> object
     * @param newQuantity the new quantity for the item
     */
    void updateQuantity(String customerID, String productID, int newQuantity);

    /**
     * Returns the quantity of an item in the <code>BasketTable</code>
     * @param customerID the primary key of a <code>Customer</code> object
     * @param productID the primary key of a <code>Product</code> object
     * @return an <code>int</code> value of the quantity
     */
    int getQuantity(String customerID, String productID);

    /**
     * Removes all items connected to <code>customerID</code>
     * @param customerID the primary key of a <code>Customer</code> object
     */
    void emptyBasket(String customerID);
}
