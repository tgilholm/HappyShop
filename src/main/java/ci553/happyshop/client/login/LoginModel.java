package ci553.happyshop.client.login;

import ci553.happyshop.base_mvm.BaseModel;
import ci553.happyshop.catalogue.User;
import ci553.happyshop.client.customer.CustomerClient;
import ci553.happyshop.domain.service.UserService;
import ci553.happyshop.utility.LoginCredentials;
import ci553.happyshop.utility.UserType;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.StringProperty;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The LoginModel class interfaces with the userService to log in or create accounts
 * for users.
 */
public class LoginModel extends BaseModel
{
    private final UserService userService;

    public LoginModel(UserService userService)
    {
        this.userService = userService; // Connect the service with dependency injection
    }


    /**
     * Delegates to userService to get a customer login
     * @param result the <code>LoginCredentials</code> of the user
     * @return the <code>User</code> object if successful, else null
     */
    public @Nullable User customerLogin(@NotNull LoginCredentials result)
    {
        return userService.login(result.username(), result.password(), UserType.CUSTOMER);
    }

    /**
     * Delegates to userService to get a staff login
     * @param result the <code>LoginCredentials</code> of the user
     * @return the <code>User</code> object if successful, else null
     */
    public @Nullable User warehouseLogin(@NotNull LoginCredentials result)
    {
        return userService.login(result.username(), result.password(), UserType.STAFF);
    }

    /**
     * Delegates to userService to create a new user
     * @param result the <code>LoginCredentials</code> of the user
     * @return the <code>User</code> object if successful, else null
     */
    public @Nullable User createAccount(@NotNull LoginCredentials result)
    {
        return userService.createAccount(result.username(), result.password(), result.userType());
    }



    /**
     * Gets the observable error property from the service
     * @return an immutable <code>StringProperty</code>
     */
    public ReadOnlyStringProperty userErrorProperty()
    {
        return userService.userError();
    }


    /**
     * Resets the <code>StringProperty</code> in the userService
     */
    public void resetUserError()
    {
        userService.resetUserError();
    }
}
