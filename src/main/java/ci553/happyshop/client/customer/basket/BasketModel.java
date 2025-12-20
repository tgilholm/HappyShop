package ci553.happyshop.client.customer.basket;

import ci553.happyshop.catalogue.Customer;
import ci553.happyshop.catalogue.DTO.BasketItemWithDetails;
import ci553.happyshop.catalogue.Product;
import ci553.happyshop.domain.service.BasketService;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class BasketModel
{
    private final BasketService basketService;
    private static final Logger logger = LogManager.getLogger();
    private final Customer customer;        // The ID of the customer accessing the basket
    private final ObservableList<BasketItemWithDetails> basketItems = FXCollections.observableArrayList();

    public BasketModel(@NotNull BasketService basketService, Customer customer)
    {
        this.basketService = basketService;
        this.customer = customer;

        // Observe the changeCounter in BasketService
        basketService.basketChanged().addListener((observable, oldValue, newValue) -> loadBasketItems());
    }

    public ObservableList<BasketItemWithDetails> getBasketItems()
    {
        return basketItems;
    }

    public void loadBasketItems()
    {
        basketItems.setAll(basketService.getAll(customer.getId()));
        logger.debug("Loaded {} items into basket", basketItems.size());
    }

    public void addToBasket(@NotNull Product product)
    {
        basketService.addOrUpdateItem(customer.getId(), product.getId(), 1);
    }

    public void removeFromBasket(@NotNull Product product)
    {
        basketService.decreaseOrRemoveItem(customer.getId(), product.getId());
    }

    public int getBasketQuantity(@NotNull Product product)
    {
        return basketService.getQuantity(customer.getId(), product.getId());
    }

    // Hides the Basket view
    public void goBack(@NotNull Stage stage)
    {
        stage.close();
    }


    public double getBasketTotal()
    {
        return basketService.getBasketTotalPrice(customer.getId());
    }
}
