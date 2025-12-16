package ci553.happyshop.utility;

import ci553.happyshop.catalogue.Product;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;


/**
 * A custom GridPane subclass that loads ListCell.fxml. Handles button clicks and updates product details.
 */
public class ProductCardPane extends GridPane
{
    @FXML
    private ImageView ivImage;

    @FXML
    private Label lbName, lbPrice, lbStock, lbBasketQty;

    @FXML
    private Button btnAdd, btnRemove;

    /**
     * Declares methods called by buttons on each card.
     * The behaviour for each of these methods must be determined by any class using a ProductCardPane
     */
    public interface ButtonActionCallback
    {
        void onAddItem(Product product);        // Add an item to the basket

        void onRemoveItem(Product product);     // Remove the item from the basket

        int getBasketQuantity(Product product); // Get the amount of the item in the basket
    }

    /**
     *
     * @param product
     * @param callback
     */
    public ProductCardPane(Product product, ButtonActionCallback callback)
    {
        //FXML is loaded here to prevent it being reloaded every time a card is updated
        FXMLLoader loader = new FXMLLoader();

        try
        {
            loader.setLocation(getClass().getResource("/fxml/ListCell.fxml"));
            loader.setController(this);

            loader.load();
        } catch (IOException e)
        {
            System.err.println("Error loading ListCell.fxml " + e.getMessage());
        }

        // Initialise the product data
        updateProduct(product, callback);
    }

    public void updateProduct(@NotNull Product product, ButtonActionCallback callback)
    {
        // Set text fields
        lbName.setText(product.getProductDescription());
        lbPrice.setText(String.format("$%.2f", product.getUnitPrice()));

        // Set product image
        String imageURI = getImageURI(product);
        ivImage.setImage(new Image(imageURI));

        // The colour of lbStock changes depending on the quantity remaining
        int stockRemaining = product.getStockQuantity();

        if (stockRemaining == 0)
        {
            // Red "out of stock" if none left
            lbStock.setStyle("-fx-text-fill: red;");
            lbStock.setText("Out of Stock");
        } else if (stockRemaining < 10)
        {
            // Orange "... left" if only a few left
            lbStock.setStyle("-fx-text-fill: orange;");
            lbStock.setText(product.getStockQuantity() + " left");
        } else
        {
            // Green "... in stock" if plentiful
            lbStock.setStyle("-fx-text-fill: green;");
            lbStock.setText(product.getStockQuantity() + " in stock");
        }

        lbBasketQty.setText(String.valueOf(callback.getBasketQuantity(product)));

        // Add button action
        btnAdd.setOnAction(x -> {
            // Add the item to the basket & update quantity
            callback.onAddItem(product);
            lbBasketQty.setText(String.valueOf(callback.getBasketQuantity(product)));
        });

        // Remove button action
        btnRemove.setOnAction(x -> {
            // Remove the item from the basket & update quantity
            callback.onRemoveItem(product);
            lbBasketQty.setText(String.valueOf(callback.getBasketQuantity(product)));
        });

        // Hide the "remove" button if there are none in the basket
        btnRemove.setDisable(callback.getBasketQuantity(product) == 0);

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
