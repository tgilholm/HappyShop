package ci553.happyshop.utility;

import ci553.happyshop.catalogue.Category;
import ci553.happyshop.catalogue.DTO.BasketItemWithDetails;
import ci553.happyshop.catalogue.Product;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class BasketListCell extends ListCell<BasketItemWithDetails>
{
    @FXML
    public Label lbName, lbCategory, lbPrice, lbBasketQty;

    @FXML
    public ImageView ivImage;

    @FXML
    public Button btnAdd, btnRemove;

    private final Node graphic;   // Graphic node for each cell
    private final ButtonActionCallback callback;

    public BasketListCell(@NotNull ButtonActionCallback callback)
    {
        this.callback = callback;

        // Load FXMl
        try
        {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/BasketListCell.fxml"));
            loader.setController(this);
            graphic = loader.load();

        } catch (IOException e)
        {
            throw new RuntimeException("Failed to load BasketListCell.fxml", e);
        }
    }

    @Override
    protected void updateItem(BasketItemWithDetails item, boolean empty)
    {
        super.updateItem(item, empty);

        if (empty || item == null)
        {
            setGraphic(null);
        } else
        {
            // Extract values from DTO
            Product product = item.productWithCategory().product();
            Category category = item.productWithCategory().category();

            setGraphic(graphic);

            // Set product image
            ivImage.setImage(ImageHandler.getImageFromProduct(product));

            // Set labels
            lbName.setText(product.getName());
            lbCategory.setText(category.getName());

            setBasketQty(callback.getBasketQuantity(product));              // Calculate basket quantity

            // Update quantity from callback
            setTotalPrice(product, callback.getBasketQuantity(product));   // Recalculate total

            // Set button actions
            btnAdd.setOnAction(x ->
            {
                callback.onAddItem(product);
                int newQty = callback.getBasketQuantity(product);

                // Update quantity & total price
                setBasketQty(newQty);
                setTotalPrice(product, newQty); // Recalculate total
            });

            // Set button actions
            btnRemove.setOnAction(x ->
            {
                callback.onRemoveItem(product);
                int newQty = callback.getBasketQuantity(product);

                // Check if there are any remaining before setting labels
                if (newQty != 0)
                {
                    setBasketQty(newQty);

                    // Update quantity & total price
                    setTotalPrice(product, newQty); // Recalculate total
                }
                else {
                    setTotalPrice(product, 0);
                    setBasketQty(0);

                }
                btnRemove.setDisable(callback.getBasketQuantity(product) == 0);
            });

            // Hide the "remove" button if there are none in the basket
            btnRemove.setDisable(callback.getBasketQuantity(product) == 0);
        }
    }

    /**
     * Helper method to update the total cost of a basket item
     *
     * @param product the product to update from
     */
    private void setTotalPrice(@NotNull Product product, int quantity)
    {
        lbPrice.setText(String.format("Total: £%.00f", (product.getUnitPrice() * quantity)));
    }

    /**
     * Helper method to update the quantity of a product in the basket
     */
    private void setBasketQty(int quantity)
    {
        lbBasketQty.setText(String.valueOf(quantity));
    }
}
