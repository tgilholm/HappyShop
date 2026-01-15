package ci553.happyshop.domain.service;

import ci553.happyshop.catalogue.Customer;
import ci553.happyshop.data.repository.BasketRepository;
import ci553.happyshop.data.repository.CustomerRepository;
import ci553.happyshop.data.repository.ProductRepository;
import ci553.happyshop.data.repository.RepositoryFactory;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Business logic methods for processing data before accessing the data layer. Defined as an <code>abstract</code>
 * class to define repositories, logging, and data change flags and keep implementations clean
 */
public abstract class LoginService
{
    // Get repository instances
    protected final CustomerRepository customerRepository = RepositoryFactory.getCustomerRepository();
    //protected final IntegerProperty changeProperty = new SimpleIntegerProperty(0); // Used for updating lists on changes

    protected static final Logger logger = LogManager.getLogger();


    /**
     * Logs in a user with the specified username and password
     * @param username a <code>String</code> username field
     * @param password a <code>String</code> password field
     * @return a <code>Customer</code> if the login succeeded, else null.
     */
    public abstract @Nullable Customer login(@NotNull String username, @NotNull String password);

    /**
     * Creates a new account with the specified username and password
     * @param username a <code>String</code> username field
     * @param password a <code>String</code> password field
     * @return a <code>Customer</code> if the creation succeeded, else null.
     */
    public abstract @Nullable Customer createAccount(@NotNull String username, @NotNull String password);
}
