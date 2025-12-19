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

import java.io.IOException;

public class BasketListCell extends ListCell<BasketItemWithDetails>
{
    @FXML
    private Label lbName, lbCategory, lbPrice, lbBasketQty;

    @FXML
    private ImageView ivImage;

    @FXML
    private Button btnAdd, btnRemove;

    private final Node graphic;   // Graphic node for each cell
    private final ButtonActionCallback callback;

    public BasketListCell(ButtonActionCallback callback)
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
            int quantity = item.quantity();

            setGraphic(graphic);

            // Set product image
            ivImage.setImage(ImageHandler.getImageFromProduct(product));

            // Set labels
            lbName.setText(product.getName());
            lbCategory.setText(category.getName());
            lbPrice.setText("Total: £" + product.getUnitPrice() * quantity); // total price
        }
    }
}
