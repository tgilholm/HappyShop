package ci553.happyshop.client.customer.basket;

import ci553.happyshop.base_mvm.BaseController;
import ci553.happyshop.catalogue.DTO.BasketItemWithDetails;
import ci553.happyshop.utility.BasketListCell;
import ci553.happyshop.utility.ButtonActionCallback;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class BasketController extends BaseController<BasketModel>
{

    @FXML
    public Button btnBack, btnCheckout, btnCancel;

    @FXML
    public Label lbCusName, lbBasketTotal;;

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
    public void initialize()
    {
        model.loadBasketItems();

        // Define callback behaviour
        ButtonActionCallback callback = new ButtonActionCallback(
                model::addToBasket,             // add product to the basket
                model::removeFromBasket,        // remove product from the basket
                model::getBasketQuantity        // get the basket quantity
        );

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
