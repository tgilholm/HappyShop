package ci553.happyshop.data.repository;

import ci553.happyshop.catalogue.Product;
import ci553.happyshop.data.database.DatabaseConnection;
import ci553.happyshop.data.database.DatabaseException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// TODO Implement Product Repo methods
// TODO change Product to Long ID

/**
 * Inherits from DataRepository to implement CRUD methods for Product entities
 */
public class ProductRepository implements DataRepository<Product, Long>
{
    // dbConnection is used by all CRUD methods to connect to Derby
    private final DatabaseConnection dbConnection;


    /**
     * Constructs a ProductRepository with a specified <code>DatabaseConnection</code>
     *
     * @param dbConnection the <code>DatabaseConnection</code> object
     */
    public ProductRepository(DatabaseConnection dbConnection)
    {
        this.dbConnection = dbConnection;
    }


    /**
     * Gets the list of all <code>Product</code> entities in ProductTable
     *
     * @return the <code>List</code> of products
     */
    @Override
    public List<Product> getAll()
    {
        String query = "SELECT * FROM ProductTable";
        List<Product> products = new ArrayList<>();

        // Get a connection and execute the query
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet results = statement.executeQuery())
        {
            // Convert ResultSet to product list
            while (results.next())
            {
                products.add(mapToProduct(results));
            }

            return products;
        } catch (SQLException e)
        {
            throw new DatabaseException("Failed to get all products", e);
        }

        return List.of();
    }

    /**
     * Gets a specific <code>Product</code> by its ID
     *
     * @param id The primary key of the product
     * @return the product selected, or null
     */
    @Override
    public @Nullable Product getById(Long id)
    {
        String query = "SELECT * FROM ProductTable WHERE id = ?";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query))
        {
            // Replace "?" with Long id
            statement.setLong(1, id);
            ResultSet results = statement.executeQuery();

            // Return the product or null if not found
            return results.next() ? mapToProduct(results) : null;

        } catch (SQLException e)
        {
            throw new DatabaseException("Failed to get product, id: " + id, e);
        }
    }

    /**
     * Adds a new <code>Product</code> to the table
     *
     * @param product the product to insert
     */
    @Override
    public void insert(Product product)
    {
        String query = "INSERT INTO ProductTable(productID, name, unitPrice, imageName, stockQuantity, categoryID) "
                + "VALUES(?, ?, ?, ?, ?, ?)";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query))
        {
            // Set PreparedStatement parameters
            setProductParameters(statement, product);
            statement.executeUpdate();
        } catch (SQLException e)
        {
            throw new DatabaseException("Failed to insert product, id" + product.getProductId(), e);
        }
    }

    /**
     * Updates the details of an existing <code>Product</code> in the table
     *
     * @param product the product to update
     */
    @Override
    public void update(Product product)
    {
        String query = "UPDATE ProductTable SET name = ?, unitPrice = ?, imageName = ?, "
                + "stockQuantity = ?, categoryID = ? WHERE productID = ?";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query))
        {
            statement.setString(1, product.getName());
            statement.setDouble(2, product.getUnitPrice());
            statement.setString(3, product.getImageName());
            statement.setInt(4, product.getStockQuantity());
            statement.setLong(5, product.getCategoryId());
            statement.setLong(6, product.getId());

        } catch (SQLException e)
        {

        }
    }

    @Override
    public void delete(Long id)
    {

    }

    @Override
    public boolean exists(Long id)
    {
        return false;
    }

    /**
     * Helper method to set the product details on an <code>insert()</code> query
     *
     * @param statement The <code>PreparedStatement</code> object to edit
     * @param product   The <code>Product</code> object from which details are extracted
     * @throws SQLException if the statement cannot be edited
     */
    private void setProductParameters(@NotNull PreparedStatement statement,
            @NotNull Product product) throws SQLException
    {
        statement.setLong(1, product.getId());
        statement.setString(2, product.getName());
        statement.setDouble(3, product.getUnitPrice());
        statement.setString(4, product.getImageName());
        statement.setInt(5, product.getStockQuantity());
        statement.setLong(6, product.getCategoryId());
    }
}
