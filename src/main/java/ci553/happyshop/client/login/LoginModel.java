package ci553.happyshop.client.login;

import ci553.happyshop.base_mvm.BaseModel;
import ci553.happyshop.catalogue.User;
import ci553.happyshop.client.customer.CustomerClient;
import ci553.happyshop.domain.service.UserService;
import ci553.happyshop.domain.service.ServiceFactory;
import ci553.happyshop.storageAccess.DatabaseRW;
import ci553.happyshop.client.OpenWindows;
import ci553.happyshop.utility.LoginCredentials;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

/**
 * The LoginModel class provides the functionality for users and warehouse staff
 * to log into the application It checks the database to see if the user already
 * has an account, and if not, asks them to create one.
 * <p>
 * <p>
 * TODO add option to go back to login screen
 *
 */
public class LoginModel extends BaseModel
{
    // Connect to the userService
    private final UserService userService = ServiceFactory.getLoginService();


    /**
     * Attempts to log in a customer. Creates a loginPopup, then validates the result.
     * If successful, opens the "customer" side of the program
     */
    public void customerLogin(@NotNull LoginCredentials result)
    {
        String username = result.username();
        String password = result.password();

        // Pass to the userService
        User user = userService.login(username, password, User.UserType.CUSTOMER);

        if (user != null)
        {
            // Pass to the customer window
            CustomerClient.startCustomerClient(new Stage(), user);
        }
    }
}
