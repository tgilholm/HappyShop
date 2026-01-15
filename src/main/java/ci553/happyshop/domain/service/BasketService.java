package ci553.happyshop.domain.service;

import ci553.happyshop.catalogue.DTO.BasketItemWithDetails;
import javafx.beans.property.ReadOnlyIntegerProperty;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Business logic methods for processing data before accessing the data layer.
 */
public interface BasketService
{


    /**
     * Decrements the number of items by if the quantity is greater than 1, deletes it completely otherwise.
     * This avoids items with <code>quantity = 0</code> in the basket
     *
     * @param customerID the primary key of a <code>Customer</code> object
     * @param productID  the primary key of a <code>Product</code> object
     */
    void decreaseOrRemoveItem(long customerID, long productID);

    /**
     * Add a new item to the <code>BasketTable</code> or update its quantity if it already exists
     *
     * @param customerID the primary key of a <code>Customer</code> object
     * @param productID  the primary key of a <code>Product</code> object
     * @param quantity   the number of items to add
     */
    void addOrUpdateItem(long customerID, long productID, int quantity);

    /**
     * Returns the quantity of an item in the <code>BasketTable</code>
     *
     * @param customerID the primary key of a <code>Customer</code> object
     * @param productID  the primary key of a <code>Product</code> object
     * @return an <code>int</code> value of the quantity
     */
    int getQuantity(long customerID, long productID);

    /**
     * Gets the total price of all the BasketItems linked to this customerID
     *
     * @param customerID the primary key of a <code>Customer</code> object
     * @return a <code>double</code> total price
     */
    double getBasketTotalPrice(long customerID);


    /**
     * Gets a list of <code>BasketItem</code> objects with product and category details attached
     *
     * @param customerID the primary key of a <code>Customer</code> object
     * @return a list of <code>BasketItem</code> objects, or null
     */
    @Nullable
    List<BasketItemWithDetails> getAll(long customerID);


    /**
     * Clears all the basket items connected to a specified <code>customerID</code>
     *
     * @param customerID the primary key of a <code>Customer</code> object
     */
    void clearBasket(long customerID);


    /**
     * Reduces stock of all items in the basket of a specified <code>customerID</code>,
     * then clears their basket
     *
     * @param customerID the primary key of a <code>Customer</code> object
     */
    void checkoutBasket(long customerID);

    /**
     * Exposes an observable form of the change counter
     *
     * @return an immutable form of the counter
     */
    ReadOnlyIntegerProperty basketChanged();
}
