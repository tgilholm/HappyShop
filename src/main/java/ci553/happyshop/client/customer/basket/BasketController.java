package ci553.happyshop.client.customer.basket;

import ci553.happyshop.catalogue.DTO.BasketItemWithDetails;
import ci553.happyshop.catalogue.Product;
import ci553.happyshop.utility.BasketListCell;
import ci553.happyshop.utility.ButtonActionCallback;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class BasketController
{
    private final Logger logger = LogManager.getLogger();
    private final BasketModel model;

    @FXML
    Button btnBack;

    @FXML
    Label lbCusName;

    @FXML
    ListView<BasketItemWithDetails> lvBasketList;

    // TODO get list of basket items
    // TODO convert basket items to displayable format

    public BasketController(BasketModel model)
    {
        this.model = model;
    }

    @FXML
    public void initialize()
    {
        model.loadBasketItems();

//        // Define callback behaviour
//        ButtonActionCallback callback = new ButtonActionCallback()
//        {
//            @Override
//            public void onAddItem(@NotNull Product product)
//            {
//                logger.info("Adding {} to basket", product.getId());
//                model.addToBasket(product);
//            }
//
//            @Override
//            public void onRemoveItem(@NotNull Product product)
//            {
//                logger.info("Removing {} from basket", product.getId());
//                cusModel.removeFromBasket(product);
//            }
//
//            @Override
//            public int getBasketQuantity(Product product)
//            {
//                return cusModel.getBasketQuantity(product);
//            }
//        };

        // Set the cellFactory of the ListView
        lvBasketList.setCellFactory(param -> new BasketListCell(new ButtonActionCallback()
        {
            @Override
            public void onAddItem(Product product)
            {

            }

            @Override
            public void onRemoveItem(Product product)
            {

            }

            @Override
            public int getBasketQuantity(Product product)
            {
                return 0;
            }
        }));

        // Clear the ListView and load the list of basket items into it
        lvBasketList.setItems(model.getBasketItems());
    }
}
