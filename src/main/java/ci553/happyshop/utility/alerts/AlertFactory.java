package ci553.happyshop.utility.alerts;

import ci553.happyshop.catalogue.DTO.BasketItemWithDetails;
import ci553.happyshop.catalogue.Product;
import ci553.happyshop.utility.LoginCredentials;
import ci553.happyshop.utility.enums.UserType;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

/**
 * Final helper class. Provides several alert types.
 */
public final class AlertFactory
{
    private AlertFactory()
    {
    }   // Final class, no instantiation


    // Paths to the CSS files for each alert are specified here. Simplifies usages
    // todo write CSS for each alert
    private static final String infoCssPath = null;
    private static final String warningCssPath = null;
    private static final String confirmCssPath = null;
    private static final String loginCssPath = null;
    private static final String receiptCssPath = null;


    /**
     * Returns an INFORMATION type alert with the specified text data, styled with CSS if available
     *
     * @param title   a <code>String</code> title for the alert
     * @param header  a <code>String</code> header line for the alert
     * @param content a <code>String</code> content text for the alert
     * @return a <code>BaseAlert</code> of the specified type.
     */
    @Contract("_, _, _ -> new")
    public static @NotNull BaseAlert info(String title, String header, String content)
    {
        return new BaseAlert(Alert.AlertType.INFORMATION, title, header, content, infoCssPath);
    }


    /**
     * Displays an WARNING type alert with the specified text data, styled with CSS if available
     *
     * @param title   a <code>String</code> title for the alert
     * @param header  a <code>String</code> header line for the alert
     * @param content a <code>String</code> content text for the alert
     */
    public static void warning(String title, String header, String content)
    {
        new BaseAlert(Alert.AlertType.WARNING, title, header, content, warningCssPath).show();
    }


    /**
     * Returns an WARNING type alert with the specified text data, styled with CSS if available.
     * Displays the alert and blocks user input until a value is specified
     *
     * @param title   a <code>String</code> title for the alert
     * @param header  a <code>String</code> header line for the alert
     * @param content a <code>String</code> content text for the alert
     * @return an <code>Optional</code> <code>ButtonType</code>. If the value is present, it can be expected
     * that the user has "confirmed" and the blocking can stop.
     */
    public static Optional<ButtonType> confirmation(String title, String header, String content)
    {
        // Create the alert
        BaseAlert alert = new BaseAlert(Alert.AlertType.CONFIRMATION, title, header, content, confirmCssPath);
        return alert.showAndWait(); // showAndWait displays the alert and waits for user input (blocks input)
    }


    /**
     * Builds a "login popup" alert. This requests a username and password and provides "Login" and "Cancel" buttons.
     * The popup is shown, accepts user data through the login, and parses these into a <code>LoginCredentials</code> object to be
     * passed to a Model.
     *
     * @return an <code>Optional</code> <code>LoginCredentials</code> object. This contains the username and password
     * entered by the user and is only present if the user selects "login". Otherwise, it is null or "not present".
     */
    public static @NotNull Optional<LoginCredentials> login()
    {
        // Create the alert
        BaseAlert alert = new BaseAlert(Alert.AlertType.NONE, "Login", null, null, loginCssPath);

        // Add buttons
        ButtonType login = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);   // Log in
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE); // Exit

        alert.getButtonTypes().setAll(login, cancel);
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        // Username field
        Label lbUsername = new Label("Username");
        TextField tfUsername = new TextField();

        // Password field
        Label lbPassword = new Label("Password");
        TextField tfPassword = new PasswordField();


        // Add these to HBoxes, then add to root
        content.getChildren().add(new HBox(10, lbUsername, tfUsername));     // Username row
        content.getChildren().add(new HBox(10, lbPassword, tfPassword));     // Password row

        alert.setContentNode(content);  // Set the custom layout

        // Display the alert
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == login)        // If the "login" button was pressed
        {
            return Optional.of(new LoginCredentials(tfUsername.getText(), tfPassword.getText(),null));
        } else
        {
            return Optional.empty();        // If the user pressed "cancel"
        }
    }

    public static Optional<LoginCredentials> createAccount() {
        // Create the alert
        BaseAlert alert = new BaseAlert(Alert.AlertType.NONE, "Create Account", null, null, loginCssPath);

        // Add buttons
        ButtonType create = new ButtonType("Create", ButtonBar.ButtonData.OK_DONE);   // Create the account
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE); // Exit

        alert.getButtonTypes().setAll(create, cancel);
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        // Account type radio buttons
        ToggleGroup toggleGroup = new ToggleGroup();
        RadioButton customerType = new RadioButton("Customer Account");
        RadioButton staffType = new RadioButton("Staff Account");

        // Add to toggleGroup
        customerType.setToggleGroup(toggleGroup);
        staffType.setToggleGroup(toggleGroup);

        // Username field
        Label lbUsername = new Label("Username");
        TextField tfUsername = new TextField();

        // Password field
        Label lbPassword = new Label("Password");
        TextField tfPassword = new PasswordField();

        // Add views to HBoxes, then the root
        content.getChildren().add(new HBox(10, customerType, staffType));    // Radio buttons
        content.getChildren().add(new HBox(10, lbUsername, tfUsername));     // Username row
        content.getChildren().add(new HBox(10, lbPassword, tfPassword));     // Password row

        alert.setContentNode(content);  // Set the custom layout

        // Display the alert
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == create)        // If the "create" button was pressed
        {
            return Optional.of(new LoginCredentials(tfUsername.getText(), tfPassword.getText(),
                    toggleGroup.getSelectedToggle() == customerType ? UserType.CUSTOMER : UserType.STAFF));
        } else
        {
            return Optional.empty();        // If the user pressed "cancel"
        }
    }

    /**
     * Builds a receipt alert. Callers should invoke showAndWait and handle the resulting <code>Optional</code> <code>ButtonType</code>.
     *
     * @param items a list of <code>BasketItemWithDetails</code> objects - the items in the basket at time of purchase
     * @param total the sum <code>double</code> total of the outgoing items.
     * @return a custom <code>BaseAlert</code> displaying receipt data.
     */
    public static @NotNull BaseAlert receipt(@NotNull List<BasketItemWithDetails> items, double total)
    {
        // Create the alert - NONE type is used for custom layout
        BaseAlert alert = new BaseAlert(Alert.AlertType.NONE, "Receipt", null, null, receiptCssPath);
        ButtonType save = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE); // For saving to file
        ButtonType close = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE); // To close the alert

        alert.getButtonTypes().setAll(save, close); // Replace any existing buttons with the new ones

        // FXML is not used here
        VBox content = new VBox(15);
        content.setPadding(new Insets(15)); // Spacing & insets

        // Add each "row" of the receipt
        for (BasketItemWithDetails item : items)
        {
            Product product = item.productWithCategory().product();
            String labelLine = String.format("%s x%d - £%.2f",  // Formatted as "Name x123 - £500"
                    product.getName(),                          // The product name
                    item.quantity(),                            // How many were ordered
                    product.getUnitPrice() * item.quantity());  // The total price

            // Add the string to a new label
            content.getChildren().add(new Label(labelLine));
        }

        // Add a space between the rows and the total
        content.getChildren().add(new Separator());
        content.getChildren().add(new Label(String.format("Total Price: £%.2f", total)));   // Grand total

        // Replace the content note in the alert with the custom one
        alert.setContentNode(content);
        return alert;
    }
}
