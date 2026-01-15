package ci553.happyshop.domain.service;

import ci553.happyshop.catalogue.User;
import ci553.happyshop.utility.enums.UserType;
import javafx.beans.property.ReadOnlyStringProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Business logic methods for processing data before accessing the data layer
 */
public interface UserService
{
    /**
     * Logs in a user with the specified username and password
     *
     * @param username a <code>String</code> username field
     * @param password a <code>String</code> password field
     * @return a <code>Customer</code> if the login succeeded, else null.
     */
    @Nullable User login(@NotNull String username, @NotNull String password, UserType userType);

    /**
     * Creates a new account with the specified username and password
     *
     * @param username a <code>String</code> username field
     * @param password a <code>String</code> password field
     * @return a <code>Customer</code> if the creation succeeded, else null.
     */
    @Nullable User createAccount(@NotNull String username, @NotNull String password, UserType userType);

    /**
     * Exposes an immutable version of the validation error
     *
     * @return a <code>ReadOnlyStringProperty</code> to be observed by models
     */
    ReadOnlyStringProperty userError();

    /**
     * Resets the user error field back to the default
     */
    void resetUserError();
}
