package ci553.happyshop.client.customer;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;

// Controller class for the customer client
// Initializes FXML elements and handles user input
public class CustomerController
{
	public CustomerModel cusModel;

    @FXML
    private ImageView ivSearchIcon;

    @FXML
    private ComboBox<String> cbCategories;


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

    }


	public void doAction(String action) throws SQLException, IOException
	{
		switch (action)
		{
		case "Search":
			cusModel.search();
			break;
		case "Add to Trolley":
			cusModel.addToTrolley();
			break;
		case "Cancel":
			cusModel.cancel();
			break;
		case "Check Out":
			cusModel.checkOut();
			break;
		case "OK & Close":
			cusModel.closeReceipt();
			break;
		}
	}
}
