package ci553.happyshop.client.customer;

import ci553.happyshop.catalogue.Product;
import ci553.happyshop.utility.ProductCell;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;

// Controller class for the customer client
// Initializes FXML elements and handles user input
public class CustomerController {
    public CustomerModel cusModel;

    @FXML
    private ImageView ivSearchIcon;

    @FXML
    private ComboBox<String> cbCategories;

    @FXML
    private Button btnAccount;

    @FXML
    private Button btnCart;

    @FXML
    private ListView<Product> lvProducts;

    // Initializes the controller after the root element is finished processing
    @FXML
    public void initialize() {
        System.out.println("Loading Images");
        URL iconUrl = getClass().getResource("/images/search_icon.png");
        if (iconUrl != null) {
            Image image = new Image(iconUrl.toExternalForm());
            ivSearchIcon.setImage(image);
        } else {
            System.err.println("Image not found: /images/search_icon.png");
        }

        // Set the ComboBox to display "Select Category" initially
        cbCategories.getItems().add("Select Category");
        cbCategories.getSelectionModel().selectFirst();

        // Initializes the ListView with the custom cell factory
        // Uses the ProductCell class to display the product details
        lvProducts.setCellFactory(listView -> new ProductCell());

        // Add a sample product for testing
        lvProducts.getItems().add(new Product("1234", "1234", "0001.jpg", 10, 100));
        lvProducts.refresh();
    }

    // Handle the "account" button input
    public void accountClicked() {
        System.out.println("Account button clicked");
    }

    public void cartClicked() {
        System.out.println("Cart button clicked");
    }
}
