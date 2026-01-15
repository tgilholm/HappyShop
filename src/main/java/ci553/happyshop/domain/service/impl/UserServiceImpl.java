package ci553.happyshop.domain.service.impl;

import ci553.happyshop.catalogue.User;
import ci553.happyshop.data.repository.RepositoryFactory;
import ci553.happyshop.data.repository.UserRepository;
import ci553.happyshop.domain.service.UserService;
import ci553.happyshop.utility.EncryptionHandler;
import ci553.happyshop.utility.UserType;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Implements the business logic user methods from the UserService interface
 */
public class UserServiceImpl implements UserService
{
    // Get repository instances
    private final UserRepository userRepository = RepositoryFactory.getCustomerRepository();
    private final StringProperty errorProperty = new SimpleStringProperty("");
    private static final Logger logger = LogManager.getLogger();


    /**
     * Updates the <code>errorProperty</code> to the specified validation error.
     * This is used whenever there is an error in validating a login or account creation.
     * Changes to this property should trigger observers and display alerts.
     *
     * @param error a description of the error
     */
    protected void notifyError(@NotNull String error)
    {
        errorProperty.set(error);
        logger.debug("notifyError() invoked");
    }


    /**
     * Exposes an immutable version of the validation error
     *
     * @return a <code>ReadOnlyStringProperty</code> to be observed by models
     */
    public ReadOnlyStringProperty userError()
    {
        return errorProperty;
    }


    /**
     * Resets the user error field back to the default
     */
    public void resetUserError()
    {
        errorProperty.set("");
    }


    /**
     * Logs in a user with the specified username and password
     *
     * @param username   a <code>String</code> username field
     * @param password   a <code>String</code> password field
     * @param accessType a <code>UserType</code> property of the requested access, backend or frontend
     * @return a <code>User</code> if the login succeeded, else null.
     */
    @Override
    public @Nullable User login(@NotNull String username, @NotNull String password, UserType accessType)
    {
        // Handle empty strings
        if (username.isEmpty() || password.isEmpty())
        {
            notifyError("Failed to login user, empty username or password");
            return null;
        }

        // Encrypt the username and password
        username = EncryptionHandler.encryptString(username);
        password = EncryptionHandler.encryptString(password);

        // Pass to the repository
        User user = userRepository.getUser(username, password);

        // Handle failed logins
        if (user == null)
        {
            notifyError("Failed to login user, incorrect username or password");
            return null;
        }

        UserType userType = user.userType();    // Extract the user's type

        // Handle incorrect user types
        /*
        This condition prevents customers from accessing the staff area, while allowing them
        to reach the customer area and allowing staff to access both.
         */
        if (accessType == UserType.STAFF && userType == UserType.CUSTOMER)
        {
            // Customers cannot access the staff portal
            notifyError("Failed to login user. Customers cannot access the warehouse");
        }

        return user;
    }


    /**
     * Creates a new account with the specified username and password
     *
     * @param username a <code>String</code> username field
     * @param password a <code>String</code> password field
     * @return a <code>User</code> if the creation succeeded, else null.
     */
    @Override
    public @Nullable User createAccount(@NotNull String username, @NotNull String password, UserType userType)
    {
        // Handle empty strings
        if (username.isEmpty() || password.isEmpty())
        {
            logger.info("Failed to create account, empty username or password");
            return null;
        }

        // Encrypt the username and password
        username = EncryptionHandler.encryptString(username);
        password = EncryptionHandler.encryptString(password);

        // Check if the username already exists in the table
        if (userRepository.usernameExists(username))
        {
            logger.info("Cannot create new account, username already exists");
            return null;
        } else
        {
            logger.info("Created new account with username");
            userRepository.insert(new User(0, username, password, userType)); // ID is generated automatically
            User newUser = userRepository.getUser(username, password);

            if (newUser != null)
            {
                return newUser;
            } else
            {
                logger.info("Failed to retrieve new user");
                return null;
            }
        }
    }
}
