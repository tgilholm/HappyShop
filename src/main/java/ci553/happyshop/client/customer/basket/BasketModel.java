package ci553.happyshop.client.customer.basket;

import ci553.happyshop.base_mvm.BaseModel;
import ci553.happyshop.catalogue.DTO.BasketItemWithDetails;
import ci553.happyshop.catalogue.Product;
import ci553.happyshop.catalogue.User;
import ci553.happyshop.domain.service.BasketService;
import ci553.happyshop.domain.service.ProductService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Model for the Basket MVC. Connects to the basketService to get basket details.
 */
public class BasketModel extends BaseModel
{
    private final BasketService basketService;
    private final ProductService productService;
    private final User user;        // The ID of the user accessing their basket
    private final ObservableList<BasketItemWithDetails> basketItems = FXCollections.observableArrayList();


    /**
     * Constructs a new BasketModel
     *
     * @param user the currently logged-in user
     */
    public BasketModel(User user, @NotNull BasketService basketService, @NotNull ProductService productService)
    {
        this.basketService = basketService;
        this.productService = productService;
        this.user = user;

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
        // Async refresh
        executorService.submit(() ->
        {
            List<BasketItemWithDetails> list = basketService.getAll(user.id());
            if (list != null)
            {
                // Execute the change to the observable list on the JavaFX thread
                Platform.runLater(() ->
                {
                    basketItems.setAll(list);
                });
                logger.debug("Loaded {} items into basket", list.size());
            }
            {
                logger.debug("Basket is empty");
            }

        });
    }


    /**
     * Delegates to basketService to add or update the quantity of an item
     *
     * @param product the <code>Product</code> object to update
     */
    public void addToBasket(@NotNull Product product)
    {
        executorService.submit(() ->
        {
            // Delegate to service
            basketService.addOrUpdateItem(user.id(), product.getId(), 1);
        });
    }


    /**
     * Delegates to basketService to remove or decrease the quantity of an item
     *
     * @param product the <code>Product</code> object to update
     */
    public void removeFromBasket(@NotNull Product product)
    {
        executorService.submit(() ->
        {
            // Delegate to service
            basketService.decreaseOrRemoveItem(user.id(), product.getId());
        });
    }


    /**
     * Delegates to basketService to get the quantity of a product
     *
     * @param product the <code>Product</code> object from which the quantity is extracted.
     * @return an int value of the quantity
     */
    public int getBasketQuantity(@NotNull Product product)
    {
        return basketService.getQuantity(user.id(), product.getId());
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
        return basketService.getBasketTotalPrice(user.id());
    }


    /**
     * Delegates to basketService to clear the entire basket
     */
    public void clearBasket()
    {
        executorService.submit(() ->
        {
            // Execute on a background thread
            basketService.clearBasket(user.id());
        });
    }


    /**
     * Delegates to basketService to reduce stocks of all purchased items and
     */
    public void checkoutBasket()
    {
        executorService.submit(() ->
        {
            // Delegate to a background thread
            basketService.checkoutBasket(user.id());
        });
    }


    /**
     * Gets the stock quantity of the specified product
     *
     * @param product a <code>Product</code> object
     * @return the quantity in stock, as an int.
     */
    public int getStockQuantity(@NotNull Product product)
    {
        return productService.getStockQuantity(product.getId());
    }
}
