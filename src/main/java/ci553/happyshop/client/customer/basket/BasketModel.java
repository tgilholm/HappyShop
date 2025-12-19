package ci553.happyshop.client.customer.basket;

import ci553.happyshop.catalogue.Customer;
import ci553.happyshop.catalogue.DTO.BasketItemWithDetails;
import ci553.happyshop.client.customer.CustomerModel;
import ci553.happyshop.data.repository.BasketRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BasketModel
{
    private final BasketRepository basketRepository;
    private final CustomerModel customerModel;
    private static final Logger logger = LogManager.getLogger();
    private final Customer customer;        // The ID of the customer accessing the basket
    private final ObservableList<BasketItemWithDetails> basketItems = FXCollections.observableArrayList();

    public BasketModel(BasketRepository basketRepository, Customer customer, CustomerModel customerModel)
    {
        this.basketRepository = basketRepository;
        this.customer = customer;
        this.customerModel = customerModel; // Reference of CustomerModel for editing basket
    }

    public ObservableList<BasketItemWithDetails> getBasketItems()
    {
        return basketItems;
    }

    public void loadBasketItems()
    {
        basketItems.setAll(basketRepository.getAllItems(customer.getId()));
        logger.info("Loaded {} items into basket", basketItems.size());
    }
}
