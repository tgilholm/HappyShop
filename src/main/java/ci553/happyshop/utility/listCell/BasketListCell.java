package ci553.happyshop.utility.listCell;

import ci553.happyshop.catalogue.Category;
import ci553.happyshop.catalogue.DTO.BasketItemWithDetails;
import ci553.happyshop.catalogue.Product;
import ci553.happyshop.utility.handlers.ImageHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Class extending list cell to provide custom layout for basket items in customers' baskets
 * The basket is a one-column ListView, not an unlimited-column TilePane as in other Views,
 * so this class is free to extend ListCell instead of CardPane.
 */
public class BasketListCell extends ListCell<BasketItemWithDetails>
{
    @FXML
    public Label lbName, lbCategory, lbPrice, lbBasketQty;

    @FXML
    public ImageView ivImage;

    @FXML
    public Button btnAdd, btnRemove;

    private final Node graphic;   // Graphic node for each cell
    private final ProductCardCallback callback;


    /**
     * Constructs a new BasketListCell object with the provided
     * callback behaviour. Loads the custom layout from the FXML file
     *
     * @param callback a <code>ProductCardCallback</code> implementation
     */
    public BasketListCell(@NotNull ProductCardCallback callback)
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


    /**
     * When the underlying product data for this cell updates, this method is
     * invoked and updates the layout of the cell.
     * @param item  The new item for the cell.
     * @param empty whether this cell represents data from the list. If it
     *              is empty, then it does not represent any domain data, but is a cell
     *              being used to render an "empty" row.
     */
    @Override
    protected void updateItem(BasketItemWithDetails item, boolean empty)
    {
        super.updateItem(item, empty);

        if (empty || item == null)
        {
            // clear everything for reused cells
            setGraphic(null);
            setText(null);
            if (lbName != null) lbName.setText("");
            if (lbCategory != null) lbCategory.setText("");
            if (lbPrice != null) lbPrice.setText("");
            if (lbBasketQty != null) lbBasketQty.setText("");
            if (ivImage != null) ivImage.setImage(null);
            if (btnAdd != null) btnAdd.setOnAction(null);
            if (btnRemove != null)
            {
                btnRemove.setOnAction(null);
                btnRemove.setDisable(true);
            }
        } else
        {
            // Extract values from DTO
            Product product = item.productWithCategory().product();
            Category category = item.productWithCategory().category();
            int qty = item.quantity();

            setGraphic(graphic);
            ivImage.setImage(ImageHandler.getImageFromProduct(product));
            lbName.setText(product.getName());
            lbCategory.setText(category.getName());

            setBasketQty(qty);              // Calculate basket quantity
            setTotalPrice(product, qty);   // Calculate total


            // Set button actions
            btnAdd.setOnAction(x -> callback.onAddItem(product));

            // Set button actions
            btnRemove.setOnAction(x -> callback.onRemoveItem(product));

            // Hide the "remove" button if there are none in the basket
            btnRemove.setDisable(callback.getBasketQuantity(product) == 0);

            // Hide the "add" button if the max quantity has been reached
            btnAdd.setDisable(callback.getBasketQuantity(product) >= callback.getStockQuantity(product));
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
