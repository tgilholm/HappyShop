package ci553.happyshop.utility;

import java.io.IOException;

import ci553.happyshop.catalogue.Product;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;


// Custom ListCell implementation for displaying Product items in a ListView
public class ProductCell extends ListCell<Product> {
    @FXML
    private Label label1;

    @FXML
    private AnchorPane anchorPane;

    private Node graphic;   // The reusable graphic node

    // Load the fxml file in the constructor
    // This prevents the FXML being reloaded every time the listview updates
    public ProductCell() {
        try {
            // Get the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layout/ListCell.fxml"));
            loader.setController(this); // Set this class as the controller

            // Load the FXML into the graphic
            graphic = loader.load();
        } catch (IOException e) {
            System.err.println("Error loading ListCell.fxml: " + e.getMessage());
        }
    }

    // Override the default cell factory to return the reusable graphic node
    @Override
    protected void updateItem(Product product, boolean empty) {
        super.updateItem(product, empty);

        // If the product is null or empty, set the graphic to null
        if (empty || product == null) {
            setGraphic(null);
            System.out.println("setCellFactory - empty item");
        } else {
            // If there is a product, get the layout from the FXML
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/layout/ListCell.fxml"));
            fxmlLoader.setController(this);

            // Set the image in the layout to use the one from the product
            try {
                fxmlLoader.load();
            } catch (Exception e) {
                // Use system.err to throw error messages
                System.err.println("Error loading ListCell.fxml: " + e.getMessage());
            }
            setText(product.toString());
        }
    }
}
