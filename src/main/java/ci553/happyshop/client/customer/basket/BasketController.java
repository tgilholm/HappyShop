package ci553.happyshop.client.customer.basket;

import ci553.happyshop.base_mvm.BaseController;
import ci553.happyshop.catalogue.DTO.BasketItemWithDetails;
import ci553.happyshop.utility.BasketListCell;
import ci553.happyshop.utility.ButtonActionCallback;
import ci553.happyshop.utility.alerts.AlertFactory;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.util.Optional;

/**
 * Controller for the basket MVC. Connects to the model & binds data to FXML view elements
 */
public class BasketController extends BaseController<BasketModel>
{

    @FXML
    public Button btnBack, btnCheckout, btnCancel;

    @FXML
    public Label lbCusName, lbBasketTotal;

    @FXML
    public ListView<BasketItemWithDetails> lvBasketList;


    public BasketController(BasketModel model)
    {
        super(model);
    }


    /**
     * Initializes elements after the View has finished loading
     */
    @FXML
    @Override
    public void initialize()
    {
        model.loadBasketItems();

        // Define callback behaviour
        ButtonActionCallback callback = new ButtonActionCallback(
                model::addToBasket,             // add product to the basket
                model::removeFromBasket,        // remove product from the basket
                model::getBasketQuantity,       // get the basket quantity
                model::getStockQuantity         // get the product's stock quantity
        );

        // Use custom ListCell
        lvBasketList.setCellFactory(param -> new BasketListCell(callback));
        lvBasketList.setItems(model.getBasketItems());        // Clear the ListView and load the list of basket items into it

        // Add a listener to automatically recalculate the "grand total" when the list changes
        model.getBasketItems().addListener((ListChangeListener<BasketItemWithDetails>) change -> updateTotal());

        updateTotal();
    }


    /**
     * Updates the total displayed in the basket total label
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


    /**
     * Requests user confirmation. Once received, either does nothing or delegates to the Model
     * to decrease stock of all purchased items and displays a receipt.
     */
    public void checkout()
    {
        // Update the list
        model.loadBasketItems();

        // Confirm the checkout
        Optional<ButtonType> result = AlertFactory.confirmation(
                "Checkout",
                "Checkout",
                "Are you sure you want to checkout your basket");

        result.ifPresent(buttonPressed ->
        {
            if (buttonPressed.getButtonData().isCancelButton())
            {
                // Cancel checkout
                logger.info("User cancelled checkout");
            } else
            {
                logger.info("Checking out items");

                // Display the receipt
                AlertFactory.receipt(model.getBasketItems(), model.getBasketTotal()).showAndWait();

                // Tell the model to purchase all basket items and clear the basket
                model.checkoutBasket();
            }
        });

    }


    /**
     * Requests user confirmation. Once received, either does nothing or delegates to the Model
     * to remove all items from a user's basket
     */
    public void cancel()
    {
        // Display a confirmation alert
        Optional<ButtonType> result = AlertFactory.confirmation(
                "Reset",
                "Reset Basket?",
                "Are you sure you want to reset your entire basket? This will remove all items");

        result.ifPresent(buttonType ->
        {
            if (buttonType.getButtonData().isCancelButton())
            {
                logger.info("User cancelled reset basket");
            } else
            {
                logger.info("Resetting basket");
                model.clearBasket();
            }
        });
    }
}
