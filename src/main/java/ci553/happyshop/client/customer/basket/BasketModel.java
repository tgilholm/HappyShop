package ci553.happyshop.client.customer.basket;

import ci553.happyshop.catalogue.BasketItem;
import ci553.happyshop.catalogue.Customer;
import ci553.happyshop.data.repository.BasketRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class BasketModel
{
    private final BasketRepository basketRepository;
    private final Customer customer;        // The ID of the customer accessing the basket
    private final ObservableList<BasketItem> basketItems = FXCollections.observableArrayList();

    public BasketModel(BasketRepository basketRepository, Customer customer)
    {
        this.basketRepository = basketRepository;
        this.customer = customer;
    }

    public ObservableList<BasketItem> getBasketItems()
    {
        return basketItems;
    }

    public void loadBasketItems()
    {
        basketItems.setAll(basketRepository.getAllItems(customer.getId()));

    }
}
