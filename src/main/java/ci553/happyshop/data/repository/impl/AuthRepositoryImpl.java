package ci553.happyshop.data.repository.impl;


import ci553.happyshop.catalogue.Customer;
import ci553.happyshop.data.database.DatabaseConnection;
import ci553.happyshop.data.database.DatabaseException;
import ci553.happyshop.data.repository.AuthRepository;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Implementation of AuthRepository. Defines behaviour for each method
 */
public class AuthRepositoryImpl implements AuthRepository
{
    // dbConnection is used by all methods to connect to Derby
    private final DatabaseConnection dbConnection;

    /**
     * Constructs a AuthRepopsitory_impl with a specified <code>DatabaseConnection</code>
     *
     * @param dbConnection the <code>DatabaseConnection</code> object
     */
    public AuthRepositoryImpl(DatabaseConnection dbConnection)
    {
        this.dbConnection = dbConnection;
    }


    /**
     * Retrieve the customer matching the username and password
     *
     * @param username a <code>String</code> object
     * @param password a <code>String</code> object
     * @return a Customer or null
     */
    @Override
    public @Nullable Customer authenticate(@NotNull String username, @NotNull String password)
    {
        String query = "SELECT * FROM LoginTable WHERE username = ? AND password = ?";

        // Get a connection and execute the query
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query))
        {
            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next())   // If there is a row, there is a customer
            {
                return mapToCustomer(resultSet); // Map the row to a customer and return it
            }

            return null;    // Couldn't find a customer

        } catch (SQLException e)
        {
            throw new DatabaseException("Failed to authenticate with username: " + username);
        }
    }

    /**
     * Checks if a username already exists in the table
     *
     * @param username the <code>String</code> object to check
     * @return true if the username exists, false otherwise
     */
    @Override
    public boolean usernameExists(@NotNull String username)
    {
        String query = "SELECT COUNT(*) FROM LoginTable WHERE username = ?"; // Get the no# of occurrences

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
            throw new DatabaseException("Failed to check if customer exists with username " + username);
        }
    }

    /**
     * Adds a new <code>Customer</code> to the table
     *
     * @param customer the <code>Customer</code> object to add
     */
    @Override
    public void addCustomer(@NotNull Customer customer)
    {
        String query = "INSERT INTO LoginTable (id, username, password) VALUES (?, ?, ?)";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query))
        {
            statement.setLong(1, customer.getId());
            statement.setString(2, customer.getUsername());
            statement.setString(3, customer.getPassword());

            statement.executeUpdate();
        } catch (SQLException e)
        {
            throw new DatabaseException("Failed to add new customer with id: " + customer.getId());
        }
    }

    /**
     * Gets a specific <code>Customer</code> by id
     *
     * @param id the primary key of the <code>Customer</code> entity
     * @return the <code>Customer</code> object, or null
     */
    @Override
    public @Nullable Customer getById(@NotNull Long id)
    {
        String query = "SELECT * FROM LoginTable WHERE id = ?";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query))
        {
            // Replace "?" with Long id
            statement.setLong(1, id);
            ResultSet results = statement.executeQuery();

            // Return the product or null if not found
            return results.next() ? mapToCustomer(results) : null;

        } catch (SQLException e)
        {
            throw new DatabaseException("Failed to get customer with id: " + id, e);
        }
    }

    /**
     * Helper method to convert a <code>ResultSet</code> row to a <code>Customer</code> object
     *
     * @param resultSet the result set to parse
     * @return a <code>Customer</code> object
     * @throws SQLException if the <code>ResultSet</code> could not be parsed
     */
    private Customer mapToCustomer(@NotNull ResultSet resultSet) throws SQLException
    {
        return new Customer(resultSet.getLong(1),
                resultSet.getString(2),
                resultSet.getString(3)
        );
    }
}
