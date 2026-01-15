package ci553.happyshop.data.repository;


import ci553.happyshop.catalogue.User;
import ci553.happyshop.data.database.DatabaseConnection;
import ci553.happyshop.data.repository.types.CommonRepository;
import ci553.happyshop.utility.UserType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

/**
 * Implements CommonRepository (but not ListableRepository as getAll methods are unneeded)
 * Interacts with the UserTable to perform CRUD operations
 */
public class UserRepository implements CommonRepository<User, Long>
{
    // dbConnection is used by all methods to connect to Derby
    private final DatabaseConnection dbConnection;
    private static final Logger logger = LogManager.getLogger();

    /**
     * Constructs a UserRepository with a specified <code>DatabaseConnection</code>
     *
     * @param dbConnection the <code>DatabaseConnection</code> object
     */
    public UserRepository(DatabaseConnection dbConnection)
    {
        this.dbConnection = dbConnection;
    }


    /**
     * Retrieve the user matching the username and password
     *
     * @param username a <code>String</code> object
     * @param password a <code>String</code> object
     * @return a <code>User</code> if found, null otherwise
     */
    public @Nullable User getUser(@NotNull String username, @NotNull String password)
    {
        String query = "SELECT * FROM UserTable WHERE username = ? AND password = ?";

        // Get a connection and execute the query
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query))
        {
            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next())   // If there is a row, there is a user
            {
                return mapToUser(resultSet); // Map the row to a user and return it
            }

            return null;    // Couldn't find a user

        } catch (SQLException e)
        {
            logger.error("Failed to get user", e);
            return null;
        }
    }

    /**
     * Checks if a username already exists in the table
     *
     * @param username the <code>String</code> object to check
     * @return true if the username exists, false otherwise
     */
    public boolean usernameExists(@NotNull String username)
    {
        String query = "SELECT COUNT(*) FROM UserTable WHERE username = ?"; // Get the no# of occurrences

        // Get a connection and execute the query
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query))
        {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            // Returns true if there is at least 1 occurrence of the username, false otherwise
            return (resultSet.next()) && (resultSet.getInt(1) > 0);
        } catch (SQLException e)
        {
            logger.error("Failed to check if username exists", e);
            return false;
        }
    }

    /**
     * Gets a specific <code>User</code> by id
     *
     * @param id the primary key of the <code>User</code> entity
     * @return the <code>User</code> object, or null
     */
    @Override
    public @Nullable User getById(@NotNull Long id)
    {
        String query = "SELECT * FROM UserTable WHERE id = ?";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query))
        {
            // Replace "?" with Long id
            statement.setLong(1, id);
            ResultSet results = statement.executeQuery();

            // Return the user or null if not found
            return results.next() ? mapToUser(results) : null;

        } catch (SQLException e)
        {
            logger.error("Failed to get user by id", e);
            return null;
        }
    }

    /**
     * Helper method to convert a <code>ResultSet</code> row to a <code>User</code> object
     *
     * @param resultSet the result set to parse
     * @return a <code>User</code> object
     * @throws SQLException if the <code>ResultSet</code> could not be parsed
     */
    @Contract("_ -> new")
    private @NotNull User mapToUser(@NotNull ResultSet resultSet) throws SQLException
    {
        return new User(resultSet.getLong(1),
                resultSet.getString(2),
                resultSet.getString(3),
                Objects.equals(resultSet.getString(4).toLowerCase(), "staff") ? UserType.STAFF : UserType.CUSTOMER
        );  // User type is set to STAFF or CUSTOMER
    }

    /**
     * Adds a new <code>User</code> to the table
     *
     * @param user the <code>User</code> object to add
     */
    @Override
    public void insert(@NotNull User user)
    {
        String query = "INSERT INTO UserTable (username, password, type) VALUES (?, ?, ?)";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query))
        {
            statement.setString(1, user.username());
            statement.setString(2, user.password());
            statement.setString(3, user.userType().toString().toLowerCase());    // STAFF to staff

            statement.executeUpdate();
        } catch (SQLException e)
        {
            logger.error("Failed to add new user", e);
        }
    }

    /**
     * Updates an existing user
     *
     * @param user the user to update
     */
    @Override
    public void update(@NotNull User user)
    {
        // todo implement
    }

    /**
     * Delete a user by its ID
     *
     * @param id The primary key of the user
     */
    @Override
    public void delete(@NotNull Long id)
    {
        // todo implement
    }
}
