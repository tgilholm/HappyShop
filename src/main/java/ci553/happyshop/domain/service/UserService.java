package ci553.happyshop.domain.service;

import ci553.happyshop.catalogue.User;
import ci553.happyshop.data.repository.UserRepository;
import ci553.happyshop.data.repository.RepositoryFactory;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Business logic methods for processing data before accessing the data layer. Defined as an <code>abstract</code>
 * class to define repositories, logging, and data change flags and keep implementations clean
 */
public abstract class UserService
{
    // Get repository instances
    protected final UserRepository userRepository = RepositoryFactory.getCustomerRepository();
    protected final StringProperty errorProperty = new SimpleStringProperty("");
    //protected final IntegerProperty changeProperty = new SimpleIntegerProperty(0); // Used for updating lists on changes

    protected static final Logger logger = LogManager.getLogger();


    /**
     * Updates the <code>errorProperty</code> to the specified validation error.
     * This is used whenever there is an error in validating a login or account creation.
     * Changes to this property should trigger observers and display alerts.
     * @param error a description of the error
     */
    protected void notifyError(@NotNull String error)
    {
        errorProperty.set(error);
        logger.debug("notifyError() invoked");
    }


    /**
     * Exposes an immutable version of the validation error
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
     * @param username a <code>String</code> username field
     * @param password a <code>String</code> password field
     * @return a <code>Customer</code> if the login succeeded, else null.
     */
    public abstract @Nullable User login(@NotNull String username, @NotNull String password, User.UserType userType);

    /**
     * Creates a new account with the specified username and password
     * @param username a <code>String</code> username field
     * @param password a <code>String</code> password field
     * @return a <code>Customer</code> if the creation succeeded, else null.
     */
    public abstract @Nullable User createAccount(@NotNull String username, @NotNull String password, User.UserType userType);
}
