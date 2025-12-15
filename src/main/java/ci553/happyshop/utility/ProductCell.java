package ci553.happyshop.utility;

import java.io.IOException;

import ci553.happyshop.catalogue.Product;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;


/**
 * Custom ListCell implementation for displaying Product items in a ListView. Loads the <code>ListCell.fxml</code>
 * to provide a "card" layout for each
 */
public class ProductCell extends ListCell<Product> {
    @FXML
    private Label lbName, lbPrice, lbStockRemaining;

    @FXML
    private ImageView ivImage;

    @FXML
    private GridPane gridPane;

    private Node graphic;   // The reusable graphic node

    // Load the fxml file in the constructor
    // This prevents the FXML being reloaded every time the listview updates
    public ProductCell() {
        FXMLLoader loader = new FXMLLoader();
        try {
            // Get the FXML file
            loader.setLocation(getClass().getResource("/fxml/ListCell.fxml"));
            loader.setController(this);
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
            setText(null);
        } else {
            setGraphic(graphic);

            // Set the layout fields to the product details
            // Set ivImage to the product image
            if (ivImage != null) {
                String imageURI = getImageURI(product);
                try {
                    ivImage.setImage(new Image(imageURI));
                } catch (Exception e) {
                    // Load a default image if the image file is not found
                    ivImage.setImage(new Image("images/imageHolder.jpg"));
                }
            }

            // Set lbName to the product description
            if (lbName != null) {
                lbName.setText(product.getProductDescription());
            }

            // Set lbPrice to the product price
            if (lbPrice != null) {
                lbPrice.setText(String.format("$%.2f", product.getUnitPrice()));
            }

            // Set lbStockRemaining to the stock quantity
            if (lbStockRemaining != null)
            {
                lbStockRemaining.setText(product.getStockQuantity() + " in stock");
            }


        }
    }

    private static String getImageURI(Product product) {
        return StorageLocation.imageFolderPath
                .resolve(product.getProductImageName())
                .toAbsolutePath()
                .toUri()
                .toString();
    }
}
