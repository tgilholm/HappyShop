package ci553.happyshop.data.repository;

import ci553.happyshop.catalogue.Category;
import ci553.happyshop.data.database.DatabaseConnection;
import ci553.happyshop.data.database.DatabaseException;
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
 * Implementation of <code>CategoryRepository</code>. Implements CRUD methods.
 */
public class CategoryRepositoryImpl implements CategoryRepository
{
    private final DatabaseConnection dbConnection;

    public CategoryRepositoryImpl(DatabaseConnection dbConnection)
    {
        this.dbConnection = dbConnection;
    }

    /**
     * Gets the list of all <code>Category</code> entities
     *
     * @return the <code>List</code> of categories
     */
    @Override
    public List<Category> getAll()
    {
        String query = "SELECT * FROM CategoryTable";
        List<Category> categories = new ArrayList<>();

        // Get a connection and execute the query
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet results = statement.executeQuery())
        {
            // Convert ResultSet to a category list
            while (results.next())
            {
                categories.add(mapToCategory(results));
            }

            return categories;
        } catch (SQLException e)
        {
            throw new DatabaseException("Failed to get all categories", e);
        }
    }

    /**
     * Gets a specific <code>Category</code> by its ID
     *
     * @param id The primary key of the category
     * @return the category selected, or null
     */
    @Override
    public @Nullable Category getById(@NotNull Long id)
    {
        String query = "SELECT * FROM CategoryTable WHERE id = ?";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query))
        {
            // Replace "?" with Long id
            statement.setLong(1, id);
            ResultSet results = statement.executeQuery();

            // Return the category or null if not found
            return results.next() ? mapToCategory(results) : null;

        } catch (SQLException e)
        {
            throw new DatabaseException("Failed to get category, id: " + id, e);
        }
    }

    /**
     * Adds a new <code>Category</code> to the table
     *
     * @param category the category to insert
     */
    @Override
    public void insert(@NotNull Category category)
    {
        String query = "INSERT INTO CategoryTable(name, description) "
                + "VALUES(?, ?)";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query))
        {
            // Set PreparedStatement parameters
            setCategoryParameters(statement, category);
            statement.executeUpdate();
        } catch (SQLException e)
        {
            throw new DatabaseException("Failed to insert category, id" + category.getId(), e);
        }
    }

    /**
     * Updates the details of an existing <code>Category</code> in the table
     *
     * @param category the category to update
     */
    @Override
    public void update(@NotNull Category category)
    {
        String query = "UPDATE CategoryTable SET name = ?, description = ? WHERE id = ?";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query))
        {
            statement.setString(1, category.getName());
            statement.setString(2, category.getDescription());
            statement.setLong(3, category.getId());

            // 0 if category not found, else the number of rows affected
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0)
            {
                throw new DatabaseException("Category not found with id: " + category.getId());
            }
        } catch (SQLException e)
        {
            throw new DatabaseException("Failed to update category with id: " + category.getId(), e);
        }
    }

    /**
     * Removes a <code>Category</code> from the table
     *
     * @param id The primary key of the category
     */
    @Override
    public void delete(@NotNull Long id)
    {
        String query = "DELETE FROM CategoryTable WHERE id = ?";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query))
        {
            statement.setLong(1, id);

            // 0 if category not found, else the number of rows affected
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0)
            {
                throw new DatabaseException("Category not found with id: " + id);
            }

        } catch (SQLException e)
        {
            throw new DatabaseException("Failed to delete category with id: " + id);
        }
    }

    /**
     * Helper method to set the category details on an <code>insert()</code> query
     *
     * @param statement The <code>PreparedStatement</code> object to edit
     * @param category  The <code>Category</code> object from which details are extracted
     * @throws SQLException if the statement cannot be edited
     */
    private void setCategoryParameters(@NotNull PreparedStatement statement,
            @NotNull Category category) throws SQLException
    {
        statement.setString(1, category.getName());
        statement.setString(2, category.getDescription());
    }

    /**
     * Helper method to convert a <code>ResultSet</code> row to a <code>Category</code> object
     *
     * @param resultSet the result set to parse
     * @return a <code>Category</code> object
     * @throws SQLException if the <code>ResultSet</code> could not be parsed
     */
    @Contract("_ -> new")
    private @NotNull Category mapToCategory(@NotNull ResultSet resultSet) throws SQLException
    {
        return new Category(
                resultSet.getLong(1),
                resultSet.getString(2),
                resultSet.getString(3)
        );
    }
}
