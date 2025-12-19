package ci553.happyshop.data.repository.impl;

import ci553.happyshop.catalogue.BasketItem;
import ci553.happyshop.catalogue.DTO.BasketItemWithDetails;
import ci553.happyshop.catalogue.DTO.ProductWithCategory;
import ci553.happyshop.data.database.DatabaseConnection;
import ci553.happyshop.data.database.DatabaseException;
import ci553.happyshop.data.repository.BasketRepository;
import ci553.happyshop.data.repository.ProductRepository;
import ci553.happyshop.data.repository.RepositoryFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BasketRepositoryImpl implements BasketRepository
{
    private final DatabaseConnection dbConnection;
    private final ProductRepository productRepository = RepositoryFactory.getProductRepository();
    private final Logger logger = LogManager.getLogger();

    public BasketRepositoryImpl(DatabaseConnection dbConnection)
    {
        this.dbConnection = dbConnection;
    }


    /**
     * Retrieve all the <code>BasketItemWithDetails</code> objects corresponding to <code>customerID</code>
     * Calls the <code>getProductByIdWithCategory</code> method in <code>ProductRepository</code>
     *
     * @param customerID the primary key of a <code>Customer</code> object
     * @return a list of <code>BasketItem</code> objects, or null
     */
    @Override
    public @Nullable List<BasketItemWithDetails> getAllItems(long customerID)
    {
        // Get product and customer details, get quantity, join together tables
        String query = "SELECT productID, quantity FROM BasketTable WHERE customerID = ?";
        List<BasketItemWithDetails> basketItems = new ArrayList<>();

        // Get a connection and execute the query
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query))
        {
            statement.setLong(1, customerID);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next())
            {
                Long productID = resultSet.getLong(1);
                int quantity = resultSet.getInt(2);

                ProductWithCategory productWithCategory = productRepository.getByIdWithCategory(productID);
                if (productWithCategory != null)
                {
                    basketItems.add(new BasketItemWithDetails(productWithCategory, quantity));
                }
            }

            return basketItems;
        } catch (SQLException e)
        {
            throw new DatabaseException("Failed to get all basketItems", e);
        }
    }

    /**
     * Adds a new item to the <code>BasketTable</code>
     *
     * @param customerID the primary key of a <code>Customer</code> object
     * @param productID  the primary key of a <code>Product</code> object
     * @param quantity   the number of items to add
     */
    @Override
    public void addItem(long customerID, long productID, int quantity)
    {
        String query = "INSERT INTO BasketTable(customerID, productID, quantity) VALUES (?, ?, ?)";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query))
        {
            // Set parameters
            statement.setLong(1, customerID);
            statement.setLong(2, productID);
            statement.setInt(3, quantity);
            statement.executeUpdate();

        } catch (SQLException e)
        {
            throw new DatabaseException("Failed to add new item to BasketTable");
        }
    }


    /**
     * Removes the item matching <code>customerID</code> and <code>productID</code> from the table
     *
     * @param customerID the primary key of a <code>Customer</code> object
     * @param productID  the primary key of a <code>Product</code> object
     */
    @Override
    public void removeItem(long customerID, long productID)
    {
        String query = "DELETE FROM BasketTable WHERE customerID = ? AND productID = ?";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query))
        {
            statement.setLong(1, customerID);
            statement.setLong(2, productID);

            // 0 if basket item not found, else the number of rows affected
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0)
            {
                throw new DatabaseException("BasketItem not found with customerID: " + customerID + " and productID: " + productID);
            }

        } catch (SQLException e)
        {
            throw new DatabaseException("Failed to delete basket item with customerID: " + customerID + " and productID: " + productID, e);
        }
    }

    /**
     * Decrements the number of items by if the quantity is greater than 1, deletes it completely otherwise.
     * This avoids items with <code>quantity = 0</code> in the basket
     *
     * @param customerID the primary key of a <code>Customer</code> object
     * @param productID  the primary key of a <code>Product</code> object
     */
    @Override
    public void decreaseOrRemoveItem(long customerID, long productID)
    {
        int currentQuantity = getQuantity(customerID, productID);

        if (currentQuantity > 1)
        {   // If there still are items remaining after decrementing, update with quantity -1
            logger.info("Quantity in basket: {}, decrementing quantity by 1. ProductID: {}", currentQuantity, productID);
            updateQuantity(customerID, productID, currentQuantity - 1);
        } else
        {
            // If decrementing would result in quantity = 0, remove the item altogether
            logger.info("Quantity <= 1, deleting item. ProductID: {}", productID);
            removeItem(customerID, productID);
        }
    }

    /**
     * Add a new item to the <code>BasketTable</code> or update its quantity if it already exists
     *
     * @param customerID the primary key of a <code>Customer</code> object
     * @param productID  the primary key of a <code>Product</code> object
     * @param quantity   the number of items to add
     */
    @Override
    public void addOrUpdateItem(long customerID, long productID, int quantity)
    {
        // Get the current quantity of the item (if it doesn't yet exist, getQuantity returns 0)
        int currentQuantity = getQuantity(customerID, productID);

        if (currentQuantity > 0)
        {
            // If it exists, update it
            logger.info("Product with id: {} already exists in basket with quantity {}. Updating quantity.", productID, currentQuantity);
            updateQuantity(customerID, productID, quantity + currentQuantity);
        } else
        {
            // If it doesn't exist, create it
            logger.info("Product with id: {} doesn't exist in basket. Adding new item", productID);
            addItem(customerID, productID, quantity);
        }
    }

    /**
     * Changes the quantity of an item in the <code>BasketTable</code>
     *
     * @param customerID  the primary key of a <code>Customer</code> object
     * @param productID   the primary key of a <code>Product</code> object
     * @param newQuantity the new quantity for the item
     */
    @Override
    public void updateQuantity(long customerID, long productID, int newQuantity)
    {
        String query = "UPDATE BasketTable SET quantity = ? WHERE customerID = ? AND productID = ?";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query))
        {
            statement.setInt(1, newQuantity);
            statement.setLong(2, customerID);
            statement.setLong(3, productID);

            // 0 if basketItem not found, else the number of rows affected
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0)
            {
                throw new DatabaseException("BasketItem not found with customerID: " + customerID + " and productID: " + productID);
            }
        } catch (SQLException e)
        {
            throw new DatabaseException("Failed to update basketItem with customerID: " + customerID + " and productID: " + productID, e);
        }
    }

    /**
     * Returns the quantity of an item in the <code>BasketTable</code>
     *
     * @param customerID the primary key of a <code>Customer</code> object
     * @param productID  the primary key of a <code>Product</code> object
     * @return an <code>int</code> value of the quantity
     */
    @Override
    public int getQuantity(long customerID, long productID)
    {
        String query = "SELECT quantity FROM BasketTable WHERE customerID = ? AND productID = ?";

        // Get a connection an execute the query
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query))
        {
            statement.setLong(1, customerID);
            statement.setLong(2, productID);

            ResultSet resultSet = statement.executeQuery();

            // Return the quantity if found, 0 otherwise
            return resultSet.next() ? resultSet.getInt(1) : 0;
        } catch (SQLException e)
        {
            throw new DatabaseException("Failed to get quantity", e);
        }
    }

    /**
     * Removes all items connected to <code>customerID</code>
     *
     * @param customerID the primary key of a <code>Customer</code> object
     */
    @Override
    public void emptyBasket(long customerID)
    {
        String query = "DELETE FROM BasketTable WHERE customerID = ?";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query))
        {
            statement.setLong(1, customerID);

            // 0 if product not found, else the number of rows affected
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0)
            {
                throw new DatabaseException("BasketItems not found with customerID: " + customerID);
            }
        } catch (SQLException e)
        {
            throw new DatabaseException("Failed to empty basket", e);
        }
    }


    /**
     * Helper method to convert a <code>ResultSet</code> row to a <code>BasketItem</code> object
     *
     * @param resultSet the result set to parse
     * @return a <code>BasketItem</code> object
     * @throws SQLException if the <code>ResultSet</code> could not be parsed
     */
    public BasketItem mapToBasketItem(@NotNull ResultSet resultSet) throws SQLException
    { // customerID, productID, quantity
        return new BasketItem(
                resultSet.getLong(1),
                resultSet.getLong(2),
                resultSet.getInt(3)
        );
    }
}
