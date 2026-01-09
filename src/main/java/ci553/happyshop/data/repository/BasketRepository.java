package ci553.happyshop.data.repository;

import ci553.happyshop.catalogue.BasketItem;
import ci553.happyshop.catalogue.BasketItemID;
import ci553.happyshop.data.database.DatabaseConnection;
import ci553.happyshop.data.database.DatabaseException;
import ci553.happyshop.data.repository.types.CommonRepository;
import ci553.happyshop.data.repository.types.ListableRepository;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BasketRepository implements CommonRepository<BasketItem, BasketItemID>, ListableRepository<BasketItem>
{
    private final DatabaseConnection dbConnection;
    private final ProductRepository productRepository = RepositoryFactory.getProductRepository();

    public BasketRepository(DatabaseConnection dbConnection)
    {
        this.dbConnection = dbConnection;
    }

    /**
     * Retrieves all <code>BasketItem</code> objects
     *
     * @return List of all basket items
     */
    @Override
    public List<BasketItem> getAll()
    {
        // Get product and customer details, get quantity, join together tables
        String query = "SELECT * FROM BasketTable";
        List<BasketItem> basketItems = new ArrayList<>();

        // Get a connection and execute the query
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query))
        {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next())
            {
                basketItems.add(new BasketItem(
                        resultSet.getLong("customerID"),
                        resultSet.getLong("productID"),
                        resultSet.getInt("quantity")
                ));
            }

            return basketItems;
        } catch (SQLException e)
        {
            throw new DatabaseException("Failed to get all basketItems", e);
        }
    }

    /**
     * Creates a new BasketItem
     *
     * @param item the BasketItem to insert
     */
    @Override
    public void insert(@NotNull BasketItem item)
    {
        String query = "INSERT INTO BasketTable(customerID, productID, quantity) VALUES (?, ?, ?)";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query))
        {
            BasketItemID id = item.getId(); // Extract composite key

            // Set parameters
            statement.setLong(1, id.customerID());
            statement.setLong(2, id.productID());
            statement.setInt(3, item.getQuantity());
            statement.executeUpdate();

        } catch (SQLException e)
        {
            throw new DatabaseException("Failed to add new item to BasketTable");
        }
    }

    /**
     * Retrieves a BasketItem by its ID
     *
     * @param id The composite primary key of the BasketItem
     * @return The BasketItem or null
     */
    @Override
    public @Nullable BasketItem getById(@NotNull BasketItemID id)
    {
        String query = "SELECT * FROM BasketTable WHERE productID = ? AND customerID = ?";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query))
        {
            BasketItem item = null;

            // Extract composite key values
            statement.setLong(1, id.productID());
            statement.setLong(2, id.customerID());
            ResultSet results = statement.executeQuery();

            // Get values from the result set
            if (results.next())
            {
                // product id and customer id are the same
                item = new BasketItem(id.productID(),
                        id.customerID(),
                        results.getInt("quantity"));
            }

            return item;

        } catch (SQLException e)
        {
            throw new DatabaseException("Failed to get BasketItem, id: " + id, e);
        }
    }

    /**
     * Updates an existing BasketItem
     *
     * @param item the new BasketItem
     */
    @Override
    public void update(@NotNull BasketItem item)
    {
        String query = "UPDATE BasketTable SET quantity = ? WHERE customerID = ? AND productID = ?";

        // Extract composite primary key
        BasketItemID id = item.getId();

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query))
        {


            // Set statement values
            statement.setInt(1, item.getQuantity());
            statement.setLong(2, id.customerID());
            statement.setLong(3, id.productID());

            // 0 if basketItem not found, else the number of rows affected
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0)
            {
                throw new DatabaseException("BasketItem not found with id: " + id);
            }
        } catch (SQLException e)
        {
            throw new DatabaseException("Failed to update basketItem with id: " + id, e);
        }
    }

    /**
     * Deletes a BasketItem by its ID
     *
     * @param id The composite primary key of the entity
     */
    @Override
    public void delete(@NotNull BasketItemID id)
    {
        String query = "DELETE FROM BasketTable WHERE customerID = ? AND productID = ?";


        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query))
        {
            statement.setLong(1, id.customerID());
            statement.setLong(2, id.productID());

            // 0 if basket item not found, else the number of rows affected
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0)
            {
                throw new DatabaseException("BasketItem not found with id: " + id);
            }

        } catch (SQLException e)
        {
            throw new DatabaseException("Failed to delete basket item with id: " + id, e);
        }
    }

    /**
     * Deletes all BasketItems connected to a customer
     *
     * @param customerID the primary key of a <code>Customer</code> object
     */
    public void deleteAllByID(long customerID)
    {
        String query = "DELETE FROM BasketTable WHERE customerID = ?";


        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query))
        {
            statement.setLong(1, customerID);

            // 0 if basket item not found, else the number of rows affected
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0)
            {
                throw new DatabaseException("BasketItem not found with customerID: " + customerID);
            }

        } catch (SQLException e)
        {
            throw new DatabaseException("Failed to delete basket item with customerID: " + customerID, e);
        }
    }


}
