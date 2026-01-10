package ci553.happyshop.client.customer.basket;

import ci553.happyshop.base_mvm.BaseModel;
import ci553.happyshop.catalogue.Customer;
import ci553.happyshop.catalogue.DTO.BasketItemWithDetails;
import ci553.happyshop.catalogue.Product;
import ci553.happyshop.domain.service.BasketService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

/**
 * Model for the Basket MVC. Connects to the basketService to get basket details.
 */
public class BasketModel extends BaseModel
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

        // Observe the changeCounter in BasketService and automatically reload the basket
        basketService.basketChanged().addListener((observable, oldValue, newValue) -> loadBasketItems());
    }


    /**
     * Exposes an <code>ObservableList</code> version of the basket items
     *
     * @return a list of <code>BasketItemWithDetails</code> objects
     */
    public ObservableList<BasketItemWithDetails> getBasketItems()
    {
        return basketItems;
    }


    /**
     * Reloads the basket observable list with a complete refresh
     */
    public void loadBasketItems()
    {
        basketItems.setAll(basketService.getAll(customer.id()));
        logger.debug("Loaded {} items into basket", basketItems.size());
    }


    /**
     * Delegates to basketService to add or update the quantity of an item
     *
     * @param product the <code>Product</code> object to update
     */
    public void addToBasket(@NotNull Product product)
    {
        basketService.addOrUpdateItem(customer.id(), product.getId(), 1);
    }


    /**
     * Delegates to basketService to remove or decrease the quantity of an item
     *
     * @param product the <code>Product</code> object to update
     */
    public void removeFromBasket(@NotNull Product product)
    {
        basketService.decreaseOrRemoveItem(customer.id(), product.getId());
    }


    /**
     * Delegates to basketService to get the quantity of a product
     *
     * @param product the <code>Product</code> object from which the quantity is extracted.
     * @return an int value of the quantity
     */
    public int getBasketQuantity(@NotNull Product product)
    {
        return basketService.getQuantity(customer.id(), product.getId());
    }


    /**
     * Hides the view
     *
     * @param stage the <code>Stage</code> to hide
     */
    public void goBack(@NotNull Stage stage)
    {
        stage.close();
    }


    /**
     * Delegates to basketService to get the quantity of the entire basket
     *
     * @return a double of the total price
     */
    public double getBasketTotal()
    {
        return basketService.getBasketTotalPrice(customer.id());
    }


    /**
     * Delegates to basketService to clear the entire basket
     */
    public void clearBasket()
    {
        basketService.clearBasket(customer.id());
    }


    /**
     * Delegates to basketService to reduce stocks of all purchased items and
     */
    public void checkoutBasket()
    {
        basketService.checkoutBasket(customer.id());
    }
}
