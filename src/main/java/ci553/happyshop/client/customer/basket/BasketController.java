package ci553.happyshop.client.customer.basket;

import ci553.happyshop.catalogue.BasketItem;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class BasketController
{
    private final BasketModel model;

    @FXML
    Button btnBack;

    @FXML
    Label lbCusName;

    @FXML
    ListView<BasketItem> lvBasketList;

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

        // Clear the ListView and load the list of basket items into it
        lvBasketList.setItems(model.getBasketItems());
    }
}
