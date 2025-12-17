package ci553.happyshop.data.repository.impl;

import ci553.happyshop.catalogue.Product;
import ci553.happyshop.data.database.DatabaseConnection;
import ci553.happyshop.data.database.DatabaseException;
import ci553.happyshop.data.repository.ProductRepository;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * Inherits from CommonRepository to implement CRUD methods for Product entities
 */
public class ProductRepositoryImpl implements ProductRepository
{
    // dbConnection is used by all CRUD methods to connect to Derby
    private final DatabaseConnection dbConnection;


    /**
     * Constructs a ProductRepositoryImpl with a specified <code>DatabaseConnection</code>
     *
     * @param dbConnection the <code>DatabaseConnection</code> object
     */
    public ProductRepositoryImpl(DatabaseConnection dbConnection)
    {
        this.dbConnection = dbConnection;
    }


    /**
     * Gets the list of all <code>Product</code> entities
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
    }

    /**
     * Gets a specific <code>Product</code> by its ID
     *
     * @param id The primary key of the product
     * @return the product selected, or null
     */
    @Override
    public @Nullable Product getById(@NotNull Long id)
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
    public void insert(@NotNull Product product)
    {
        String query = "INSERT INTO ProductTable(id, name, imageName, unitPrice, stockQuantity, categoryID) "
                + "VALUES(?, ?, ?, ?, ?, ?)";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query))
        {
            // Set PreparedStatement parameters
            setProductParameters(statement, product);
            statement.executeUpdate();
        } catch (SQLException e)
        {
            throw new DatabaseException("Failed to insert product, id" + product.getId(), e);
        }
    }

    /**
     * Updates the details of an existing <code>Product</code> in the table
     *
     * @param product the product to update
     */
    @Override
    public void update(@NotNull Product product)
    {
        String query = "UPDATE ProductTable SET name = ?, imageName = ?, unitPrice = ?, "
                + "stockQuantity = ?, categoryID = ? WHERE id = ?";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query))
        {
            statement.setString(1, product.getName());
            statement.setString(2, product.getImageName());
            statement.setDouble(3, product.getUnitPrice());
            statement.setInt(4, product.getStockQuantity());
            statement.setLong(5, product.getCategoryId());
            statement.setLong(6, product.getId());

            // 0 if product not found, else the number of rows affected
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0)
            {
                throw new DatabaseException("Product not found with id: " + product.getId());
            }
        } catch (SQLException e)
        {
            throw new DatabaseException("Failed to update product with id: " + product.getId(), e);
        }
    }

    /**
     * Removes a <code>Product</code> from the table
     *
     * @param id The primary key of the product
     */
    @Override
    public void delete(@NotNull Long id)
    {
        String query = "DELETE FROM ProductTable WHERE id = ?";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query))
        {
            statement.setLong(1, id);

            // 0 if product not found, else the number of rows affected
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0)
            {
                throw new DatabaseException("Product not found with id: " + id);
            }

        } catch (SQLException e)
        {
            throw new DatabaseException("Failed to delete product with id: " + id);
        }
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
        statement.setString(3, product.getImageName());
        statement.setDouble(4, product.getUnitPrice());
        statement.setInt(5, product.getStockQuantity());
        statement.setLong(6, product.getCategoryId());
    }

    /**
     * Helper method to convert a <code>ResultSet</code> row to a <code>Product</code> object
     *
     * @param resultSet the result set to parse
     * @return a <code>Product</code> object
     * @throws SQLException if the <code>ResultSet</code> could not be parsed
     */
    private Product mapToProduct(@NotNull ResultSet resultSet) throws SQLException
    {
        return new Product(
                resultSet.getLong(1),
                resultSet.getString(2),
                resultSet.getString(3),
                resultSet.getDouble(4),
                resultSet.getInt(5),
                resultSet.getLong(6)
        );
    }
}
