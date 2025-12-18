package ci553.happyshop.data.repository.impl;

import ci553.happyshop.catalogue.BasketItem;
import ci553.happyshop.data.database.DatabaseConnection;
import ci553.happyshop.data.repository.BasketRepository;

import java.util.List;

public class BasketRepositoryImpl implements BasketRepository
{
    private final DatabaseConnection dbConnection;

    public BasketRepositoryImpl(DatabaseConnection dbConnection)
    {
        this.dbConnection = dbConnection;
    }


    /**
     * Retrieve all the <code>BasketItem</code> objects corresponding to <code>customerID</code>
     *
     * @param customerID the primary key of a <code>Customer</code> object
     * @return a list of <code>BasketItem</code> objects
     */
    @Override
    public List<BasketItem> getAllItems(String customerID)
    {
        return List.of();
    }

    /**
     * Adds a new item to the <code>BasketTable</code>
     *
     * @param customerID the primary key of a <code>Customer</code> object
     * @param productID  the primary key of a <code>Product</code> object
     * @param quantity   the amount of items to add
     */
    @Override
    public void addItem(String customerID, String productID, int quantity)
    {

    }

    /**
     * Removes the item matching <code>customerID</code> and <code>productID</code> from the table
     *
     * @param customerID the primary key of a <code>Customer</code> object
     * @param productID  the primary key of a <code>Product</code> object
     */
    @Override
    public void removeItem(String customerID, String productID)
    {

    }

    /**
     * Changes the quantity of an item in the <code>BasketTable</code>
     *
     * @param customerID  the primary key of a <code>Customer</code> object
     * @param productID   the primary key of a <code>Product</code> object
     * @param newQuantity the new quantity for the item
     */
    @Override
    public void updateQuantity(String customerID, String productID, int newQuantity)
    {

    }

    /**
     * Returns the quantity of an item in the <code>BasketTable</code>
     *
     * @param customerID the primary key of a <code>Customer</code> object
     * @param productID  the primary key of a <code>Product</code> object
     * @return an <code>int</code> value of the quantity
     */
    @Override
    public int getQuantity(String customerID, String productID)
    {
        return 0;
    }

    /**
     * Removes all items connected to <code>customerID</code>
     *
     * @param customerID the primary key of a <code>Customer</code> object
     */
    @Override
    public void emptyBasket(String customerID)
    {

    }
}
