package ci553.happyshop.data.repository.impl;

import ci553.happyshop.catalogue.BasketItem;
import ci553.happyshop.data.database.DatabaseConnection;
import ci553.happyshop.data.database.DatabaseException;
import ci553.happyshop.data.repository.BasketRepository;
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

    public BasketRepositoryImpl(DatabaseConnection dbConnection)
    {
        this.dbConnection = dbConnection;
    }


    /**
     * Retrieve all the <code>BasketItem</code> objects corresponding to <code>customerID</code>
     *
     * @param customerID the primary key of a <code>Customer</code> object
     * @return a list of <code>BasketItem</code> objects, or null
     */
    @Override
    public @Nullable List<BasketItem> getAllItems(long customerID)
    {
        String query = "SELECT customerID, productID, quantity FROM BasketTable WHERE customerID = ?";
        List<BasketItem> basketItems = new ArrayList<>();

        // Get a connection an execute the query
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery())
        {
            while (resultSet.next())
            {
                basketItems.add(mapToBasketItem(resultSet));
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
     * Changes the quantity of an item in the <code>BasketTable</code>
     *
     * @param customerID  the primary key of a <code>Customer</code> object
     * @param productID   the primary key of a <code>Product</code> object
     * @param newQuantity the new quantity for the item
     */
    @Override
    public void updateQuantity(long customerID, long productID, int newQuantity)
    {
        String query = "UPDATE BasketTable SET quantity = ?, "
                + "WHERE customerID = ? AND productID = ?";

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
