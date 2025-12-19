package ci553.happyshop.client.customer.basket;

import ci553.happyshop.catalogue.Customer;
import ci553.happyshop.catalogue.DTO.BasketItemWithDetails;
import ci553.happyshop.domain.service.BasketService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BasketModel
{
    private final BasketService basketService;
    private static final Logger logger = LogManager.getLogger();
    private final Customer customer;        // The ID of the customer accessing the basket
    private final ObservableList<BasketItemWithDetails> basketItems = FXCollections.observableArrayList();

    public BasketModel(BasketService basketService, Customer customer)
    {
        this.basketService = basketService;
        this.customer = customer;
    }

    public ObservableList<BasketItemWithDetails> getBasketItems()
    {
        return basketItems;
    }

    public void loadBasketItems()
    {
        basketItems.setAll(basketService.getAll(customer.getId()));
        logger.info("Loaded {} items into basket", basketItems.size());
    }
}
