package ci553.happyshop.utility;

import java.io.IOException;

import ci553.happyshop.catalogue.Product;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import org.jetbrains.annotations.NotNull;


/**
 * Custom ListCell implementation for displaying Product items in a ListView. Loads the <code>ListCell.fxml</code>
 * to provide a "card" layout for each
 */
public class ProductCell extends ListCell<Product>
{
    private final ButtonActionCallback callback;  // Get an instance of the callback
    private Node graphic;                   // The reusable graphic node

    @FXML
    private Label lbName, lbPrice, lbStockRemaining, lbBasketQuantity;

    @FXML
    private ImageView ivImage;

    @FXML
    private Button btnAdd, btnRemove;

    /**
     * Declares methods called by buttons on each cell.
     * The behaviour for each of these methods must be determined by any class using a ProductCell
     */
    public interface ButtonActionCallback
    {
        void onAddItem(Product product);        // Add an item to the basket

        void onRemoveItem(Product product);     // Remove the item from the basket

        int getBasketQuantity(Product product); // Get the amount of the item in the basket
    }


    // Load the fxml file in the constructor
    // This prevents the FXML being reloaded every time the listview updates
    public ProductCell(ButtonActionCallback callback)
    {
        // Connect the callback from the parameter to this object
        this.callback = callback;


        // Get the FXML file and load the layout into the graphic
        FXMLLoader loader = new FXMLLoader();
        try
        {
            loader.setLocation(getClass().getResource("/fxml/ListCell.fxml"));
            loader.setController(this);
            graphic = loader.load();
        } catch (IOException e)
        {
            System.err.println("Error loading ListCell.fxml: " + e.getMessage());
        }

    }

    // Override the default cell factory to return the reusable graphic node
    @Override
    protected void updateItem(Product product, boolean empty)
    {
        super.updateItem(product, empty);

        // If the product is null or empty, set the graphic to null
        if (empty || product == null)
        {
            setGraphic(null);
            setText(null);
        } else
        {
            setGraphic(graphic);

            // Set ivImage to the product image
            String imageURI = getImageURI(product);
            try
            {
                ivImage.setImage(new Image(imageURI));
            } catch (Exception e)
            {
                // Load a default image if the image file is not found
                ivImage.setImage(new Image("images/imageHolder.jpg"));
            }
            lbName.setText(product.getProductDescription());                    // Product Description
            lbPrice.setText(String.format("$%.2f", product.getUnitPrice()));    // Product Price


            // Set lbStockRemaining to the stock quantity
            // The colour changes depending on the quantity remaining
            int stockRemaining = product.getStockQuantity();

            if (stockRemaining == 0)
            {
                // Red "out of stock" if none left
                lbStockRemaining.setStyle("-fx-text-fill: red;");
                lbStockRemaining.setText("Out of Stock");
            } else if (stockRemaining < 10)
            {
                // Orange "... left" if only a few left
                lbStockRemaining.setStyle("-fx-text-fill: orange;");
                lbStockRemaining.setText(product.getStockQuantity() + " left");
            } else
            {
                // Green "... in stock" if plentiful
                lbStockRemaining.setStyle("-fx-text-fill: green;");
                lbStockRemaining.setText(product.getStockQuantity() + " in stock");
            }

            // Add button action
            btnAdd.setOnAction(x -> {
                // Add the item to the basket & update quantity
                callback.onAddItem(product);
                lbBasketQuantity.setText(String.valueOf(callback.getBasketQuantity(product)));
            });

            // Remove button action
            btnRemove.setOnAction(x -> {
                // Remove the item from the basket & update quantity
                callback.onRemoveItem(product);
                lbBasketQuantity.setText(String.valueOf(callback.getBasketQuantity(product)));
            });

            // Hide the "remove" button if there are none in the basket
            btnRemove.setDisable(callback.getBasketQuantity(product) == 0);

        }
    }

    private static String getImageURI(@NotNull Product product)
    {
        return StorageLocation.imageFolderPath
                .resolve(product.getProductImageName())
                .toAbsolutePath()
                .toUri()
                .toString();
    }
}
