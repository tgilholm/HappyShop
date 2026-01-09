package ci553.happyshop.client.customer.basket;

import ci553.happyshop.base_mvm.AbstractModel;
import ci553.happyshop.catalogue.Customer;
import ci553.happyshop.catalogue.DTO.BasketItemWithDetails;
import ci553.happyshop.catalogue.Product;
import ci553.happyshop.domain.service.BasketService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

public class BasketModel extends AbstractModel
{
    private final BasketService basketService;
    private final Customer customer;        // The ID of the customer accessing the basket
    private final ObservableList<BasketItemWithDetails> basketItems = FXCollections.observableArrayList();


    /**
     * Constructs a new BasketModel
     *
     * @param basketService for handling business logic
     * @param customer      the currently logged-in customer
     */
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


    /**
     * Reloads the basket observable with a complete refresh
     */
    public void loadBasketItems()
    {
        basketItems.setAll(basketService.getAll(customer.id()));
        logger.debug("Loaded {} items into basket", basketItems.size());
    }


    public void addToBasket(@NotNull Product product)
    {
        basketService.addOrUpdateItem(customer.id(), product.getId(), 1);
    }


    public void removeFromBasket(@NotNull Product product)
    {
        basketService.decreaseOrRemoveItem(customer.id(), product.getId());
    }


    public int getBasketQuantity(@NotNull Product product)
    {
        return basketService.getQuantity(customer.id(), product.getId());
    }


    // Hides the Basket view
    public void goBack(@NotNull Stage stage)
    {
        stage.close();
    }


    public double getBasketTotal()
    {
        return basketService.getBasketTotalPrice(customer.id());
    }


    public void clearBasket()
    {
    }
}
