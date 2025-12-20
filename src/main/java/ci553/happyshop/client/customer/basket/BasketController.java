package ci553.happyshop.client.customer.basket;

import ci553.happyshop.catalogue.DTO.BasketItemWithDetails;
import ci553.happyshop.catalogue.Product;
import ci553.happyshop.domain.service.BasketService;
import ci553.happyshop.domain.service.ServiceFactory;
import ci553.happyshop.utility.BasketListCell;
import ci553.happyshop.utility.ButtonActionCallback;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.security.Provider;

public class BasketController
{
    private final Logger logger = LogManager.getLogger();
    private final BasketModel model;

    @FXML
    public Button btnBack;

    @FXML
    public Label lbCusName, lbBasketTotal;;

    @FXML
    public ListView<BasketItemWithDetails> lvBasketList;

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

        // Define callback behaviour
        // todo base model and reusable code
        ButtonActionCallback callback = new ButtonActionCallback()
        {
            @Override
            public void onAddItem(@NotNull Product product)
            {
                logger.info("Adding {} to basket", product.getId());
                model.addToBasket(product);
            }

            @Override
            public void onRemoveItem(@NotNull Product product)
            {
                logger.info("Removing {} from basket", product.getId());
                model.removeFromBasket(product);
            }

            @Override
            public int getBasketQuantity(Product product)
            {
                return model.getBasketQuantity(product);
            }
        };

        // Set the cellFactory of the ListView
        lvBasketList.setCellFactory(param -> new BasketListCell(callback));
        // Clear the ListView and load the list of basket items into it
        lvBasketList.setItems(model.getBasketItems());
        logger.info("List view set with {} items ", lvBasketList.getItems().size());

        // Add a listener to automatically recalculate the "grand total" when the list changes
        model.getBasketItems().addListener((ListChangeListener<BasketItemWithDetails>) change -> updateTotal());



        // Hides this window and automatically re-opens the customer window
        btnBack.setOnAction(x ->
        {
            Stage stage = (Stage) btnBack.getScene().getWindow();
            model.goBack(stage);
        });
    }


    /**
     * Updates the total
     */
    private void updateTotal()
    {
        lbBasketTotal.setText(String.format("Total: £%.00f", model.getBasketTotal()));
    }
}
