package ci553.happyshop.client.customer.basket;

import ci553.happyshop.catalogue.DTO.BasketItemWithDetails;
import ci553.happyshop.utility.BasketListCell;
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

        // Set the cellFactory of the ListView
        lvBasketList.setCellFactory(param -> new BasketListCell());

        // Clear the ListView and load the list of basket items into it
        lvBasketList.setItems(model.getBasketItems());
    }
}
