package ci553.happyshop.data.repository;

import ci553.happyshop.catalogue.Category;
import ci553.happyshop.catalogue.Product;
import ci553.happyshop.catalogue.DTO.ProductWithCategory;
import ci553.happyshop.data.database.DatabaseConnection;
import ci553.happyshop.data.database.DatabaseException;
import ci553.happyshop.data.repository.types.CommonRepository;
import ci553.happyshop.data.repository.types.ListableRepository;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * Implements CRUD methods from <code>CommonRepository</code> and <code>getAll</code> methods
 * from <code>ListableRepository</code>
 */
public class ProductRepository implements CommonRepository<Product, Long>, ListableRepository<Product>
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
     * Gets the list of <code>Product</code> entities joined to the linked <code>Category</code>
     * @return a list of <code>ProductWithCategory</code> object
     */
    public List<ProductWithCategory> getAllWithCategories()
    {
        String query = """
                SELECT p.id, p.name, p.imageName, p.unitPrice, p.stockQuantity, p.categoryID, c.id, c.name, c.description
                FROM ProductTable p
                JOIN CategoryTable c ON p.categoryID = c.id
               """;
        List<ProductWithCategory> productsWithCategories = new ArrayList<>();

        try (Connection connection = dbConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet results = statement.executeQuery())
        {
            while(results.next())
            {
                productsWithCategories.add(mapToProductWithCategory(results));
            }

            return productsWithCategories;
        }
        catch (SQLException e)
        {
            throw new DatabaseException("Failed to get list of products with categories", e);
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
     * Gets a specific <code>Product</code> entity joined to the linked <code>Category</code>
     *
     * @param id the <code>Long</code> primary key of the <code>Product</code> entity to get
     * @return a <code>ProductWithCategory</code> object
     */
    public @Nullable ProductWithCategory getByIdWithCategory(@NotNull Long id)
    {
        String query = """
                SELECT p.id, p.name, p.imageName, p.unitPrice, p.stockQuantity, p.categoryID, c.id, c.name, c.description
                FROM ProductTable p
                JOIN CategoryTable c ON p.categoryID = c.id
                WHERE p.id = ?
               """;

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query))
        {
            // Replace "?" with Long id
            statement.setLong(1, id);
            ResultSet results = statement.executeQuery();

            // Return the product or null if not found
            return results.next() ? mapToProductWithCategory(results) : null;

        } catch (SQLException e)
        {
            throw new DatabaseException("Failed to get ProductWithCategory, id: " + id, e);
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
        String query = "INSERT INTO ProductTable(name, imageName, unitPrice, stockQuantity, categoryID) "
                + "VALUES(?, ?, ?, ?, ?)";

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
        statement.setString(1, product.getName());
        statement.setString(2, product.getImageName());
        statement.setDouble(3, product.getUnitPrice());
        statement.setInt(4, product.getStockQuantity());
        statement.setLong(5, product.getCategoryId());
    }


    /**
     * Helper method to convert a <code>ResultSet</code> row to a <code>Product</code> object
     *
     * @param resultSet the result set to parse
     * @return a <code>Product</code> object
     * @throws SQLException if the <code>ResultSet</code> could not be parsed
     */
    @Contract("_ -> new")
    private @NotNull Product mapToProduct(@NotNull ResultSet resultSet) throws SQLException
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

    /**
     * Helper method to convert a <code>ResultSet</code> row to a <code>ProductWithCategory</code> object
     * @param resultSet the result set to parse
     * @return a <code>ProductWithCategory</code> object
     * @throws SQLException if the <code>ResultSet</code> could not be parsed
     */
    @Contract("_ -> new")
    private @NotNull ProductWithCategory mapToProductWithCategory(@NotNull ResultSet resultSet) throws SQLException
    {
        return new ProductWithCategory(
                new Product(    // Get the product object
                        resultSet.getLong(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getDouble(4),
                        resultSet.getInt(5),
                        resultSet.getLong(6)
                ),
                new Category(   // Get the category object
                        resultSet.getLong(7),
                        resultSet.getString(8),
                        resultSet.getString(9)
                )
        );
    }
}
