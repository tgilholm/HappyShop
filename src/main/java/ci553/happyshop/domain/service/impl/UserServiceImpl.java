package ci553.happyshop.domain.service.impl;

import ci553.happyshop.catalogue.User;
import ci553.happyshop.domain.service.UserService;
import ci553.happyshop.utility.EncryptionHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class UserServiceImpl extends UserService
{

    /**
     * Logs in a user with the specified username and password
     *
     * @param username a <code>String</code> username field
     * @param password a <code>String</code> password field
     * @param accessType a <code>UserType</code> property of the requested access, backend or frontend
     * @return a <code>User</code> if the login succeeded, else null.
     */
    @Override
    public @Nullable User login(@NotNull String username, @NotNull String password, User.UserType accessType)
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

        User.UserType userType = user.userType();    // Extract the user's type

        // Handle incorrect user types
        /*
        This condition prevents customers from accessing the staff area, while allowing them
        to reach the customer area and allowing staff to access both.
         */
        if (accessType == User.UserType.STAFF && userType == User.UserType.CUSTOMER)
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
    public @Nullable User createAccount(@NotNull String username, @NotNull String password, User.UserType userType)
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
