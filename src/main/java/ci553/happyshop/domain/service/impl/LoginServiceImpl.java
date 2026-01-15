package ci553.happyshop.domain.service.impl;

import ci553.happyshop.catalogue.Customer;
import ci553.happyshop.domain.service.LoginService;
import ci553.happyshop.utility.EncryptionHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LoginServiceImpl extends LoginService
{

    /**
     * Logs in a user with the specified username and password
     *
     * @param username a <code>String</code> username field
     * @param password a <code>String</code> password field
     * @return a <code>Customer</code> if the login succeeded, else null.
     */
    @Override
    public @Nullable Customer login(@NotNull String username, @NotNull String password)
    {
        // Handle empty strings
        if (username.isEmpty() || password.isEmpty())
        {
            logger.info("Failed to login customer, empty username or password");
            return null;
        }

        // Encrypt the username and password
        username = EncryptionHandler.encryptString(username);
        password = EncryptionHandler.encryptString(password);

        // Pass to the repository
        Customer customer = customerRepository.getCustomer(username, password);

        // Handle failed logins
        if (customer == null)
        {
            logger.info("Failed to login customer, incorrect username or password");
            return null;
        } else
        {
            logger.info("Retrieved customer");
            return customer;
        }
    }


    /**
     * Creates a new account with the specified username and password
     *
     * @param username a <code>String</code> username field
     * @param password a <code>String</code> password field
     * @return a <code>Customer</code> if the creation succeeded, else null.
     */
    @Override
    public @Nullable Customer createAccount(@NotNull String username, @NotNull String password)
    {
        // Handle empty strings
        if (username.isEmpty() || password.isEmpty())
        {
            logger.info("Failed to create account, empty username or password");
            return null;
        }

        // Encrypt the username and password
        username = EncryptionHandler.decryptString(username);
        password = EncryptionHandler.decryptString(password);

        // Check if the username already exists in the table
        if (customerRepository.usernameExists(username))
        {
            logger.info("Cannot create new account, username already exists");
            return null;
        } else
        {
            logger.info("Created new account with username");
            customerRepository.insert(new Customer(0, username, password)); // ID is generated automatically
            Customer newCustomer = customerRepository.getCustomer(username, password);

            if (newCustomer != null)
            {
                return newCustomer;
            } else
            {
                logger.info("Failed to retrieve new customer");
                return null;
            }
        }
    }
}
