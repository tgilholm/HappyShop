package ci553.happyshop.data.repository;

import ci553.happyshop.catalogue.Customer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Defines the database operations for AuthRepository implementations.
 * Using an interface means the authentication methods can be modified later on
 * without affecting the contract of this interface.
 */
public interface AuthRepository
{
    /**
     * Retrieve the customer matching the username and password
     * @param username a <code>String</code> object
     * @param password a <code>String</code> object
     * @return a Customer or null
     */
    @Nullable
    public Customer authenticate(@NotNull String username, @NotNull String password);

    /**
     * Checks if a username already exists in the table
     * @param username the <code>String</code> object to check
     * @return true if the username exists, false otherwise
     */
    public boolean usernameExists(@NotNull String username);

    /**
     * Adds a new <code>Customer</code> to the table
     * @param customer the <code>Customer</code> object to add
     */
    public void addCustomer(@NotNull Customer customer);

    /**
     * Gets a specific <code>Customer</code> by id
     * @param id the primary key of the <code>Customer</code> entity
     * @return the <code>Customer</code> object, or null
     */
    @Nullable
    public Customer getById(@NotNull Long id);
}
