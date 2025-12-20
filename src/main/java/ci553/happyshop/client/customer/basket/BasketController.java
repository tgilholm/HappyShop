package ci553.happyshop.client.customer.basket;

import ci553.happyshop.catalogue.DTO.BasketItemWithDetails;
import ci553.happyshop.catalogue.Product;
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
    public Button btnBack, btnCheckout, btnCancel;

    @FXML
    public Label lbCusName, lbBasketTotal;;

    @FXML
    public ListView<BasketItemWithDetails> lvBasketList;


    public BasketController(BasketModel model)
    {
        this.model = model;
    }


    /**
     * Initializes elements after the View has finished loading
     */
    @FXML
    public void initialize()
    {
        model.loadBasketItems();

        // Define callback behaviour
        // todo base model and reusable code
        // todo abstract class that models extend

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

        // Use custom ListCell
        lvBasketList.setCellFactory(param -> new BasketListCell(callback));
        lvBasketList.setItems(model.getBasketItems());        // Clear the ListView and load the list of basket items into it

        // Add a listener to automatically recalculate the "grand total" when the list changes
        model.getBasketItems().addListener((ListChangeListener<BasketItemWithDetails>) change -> updateTotal());

        updateTotal();
    }


    /**
     * Updates the total
     */
    private void updateTotal()
    {
        lbBasketTotal.setText(String.format("Total: £%.00f", model.getBasketTotal()));
    }


    /**
     * Closes this window and returns to the customer view
     */
    public void goBack()
    {
        Stage stage = (Stage) btnBack.getScene().getWindow();
        model.goBack(stage);
    }


    // todo open alert window with brief "receipt" & have option for downloading as file
    // todo decrease stock of all in basket
    // todo low stock handling, if basket exceeds total stock etc, alert window
    // todo create custom AlertWindow class for messages to users
    public void checkout()
    {

    }


    /**
     * Clears the basket
     */
    // todo "are you sure"
    public void cancel()
    {
        model.clearBasket();
    }
}
