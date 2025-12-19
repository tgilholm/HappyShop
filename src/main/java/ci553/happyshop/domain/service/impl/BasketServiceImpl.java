package ci553.happyshop.domain.service.impl;

import ci553.happyshop.catalogue.BasketItem;
import ci553.happyshop.catalogue.BasketItemID;
import ci553.happyshop.catalogue.DTO.BasketItemWithDetails;
import ci553.happyshop.data.repository.BasketRepository;
import ci553.happyshop.data.repository.ProductRepository;
import ci553.happyshop.data.repository.RepositoryFactory;
import ci553.happyshop.domain.service.BasketService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implements methods from <code>BasketService</code>
 */
public class BasketServiceImpl implements BasketService
{
    // Get repository instances
    private final BasketRepository basketRepository = RepositoryFactory.getBasketRepository();
    private final ProductRepository productRepository = RepositoryFactory.getProductRepository();

    // Logger
    private static final Logger logger = LogManager.getLogger();


    /**
     * Decrements the number of items by if the quantity is greater than 1, deletes it completely otherwise.
     * This avoids items with <code>quantity = 0</code> in the basket
     *
     * @param customerID the primary key of a <code>Customer</code> object
     * @param productID  the primary key of a <code>Product</code> object
     */
    @Override
    public void decreaseOrRemoveItem(long customerID, long productID)
    {
        int currentQuantity = getQuantity(customerID, productID);

        if (currentQuantity > 1)
        {   // If there still are items remaining after decrementing, update with quantity -1
            logger.info("Quantity in basket: {}, decrementing quantity by 1. ProductID: {}", currentQuantity, productID);
            updateQuantity(customerID, productID, currentQuantity - 1);
        } else
        {
            // If decrementing would result in quantity = 0, remove the item altogether
            logger.info("Quantity <= 1, deleting item. ProductID: {}", productID);
            basketRepository.delete(new BasketItemID(customerID, productID));
        }
    }


    /**
     * Add a new item to the <code>BasketTable</code> or update its quantity if it already exists
     *
     * @param customerID the primary key of a <code>Customer</code> object
     * @param productID  the primary key of a <code>Product</code> object
     * @param quantity   the number of items to add
     */
    @Override
    public void addOrUpdateItem(long customerID, long productID, int quantity)
    {
        // Get the current quantity of the item (if it doesn't yet exist, getQuantity returns 0)
        int currentQuantity = getQuantity(customerID, productID);

        if (currentQuantity > 0)
        {
            // If it exists, update it
            logger.info("Product with id: {} already exists in basket with quantity {}. Updating quantity.", productID, currentQuantity);
            updateQuantity(customerID, productID, quantity + currentQuantity);
        } else
        {
            // If it doesn't exist, create it
            logger.info("Product with id: {} doesn't exist in basket. Adding new item", productID);
            basketRepository.insert(new BasketItem(customerID, productID, quantity));
        }
    }


    /**
     * Returns the quantity of an item in the <code>BasketTable</code>.
     * Gets the item from the repository and extracts the quantity from it.
     *
     * @param customerID the primary key of a <code>Customer</code> object
     * @param productID  the primary key of a <code>Product</code> object
     * @return an <code>int</code> value of the quantity
     */
    @Override
    public int getQuantity(long customerID, long productID)
    {
        BasketItem basketItem = basketRepository.getById(new BasketItemID(customerID, productID));

        // Null check
        if (basketItem != null)
        {
            return basketItem.getQuantity();
        } else
        {
            logger.error("BasketItem {} {} was null", customerID, productID);
            return 0;
        }

    }

    /**
     * Removes all items connected to <code>customerID</code>
     *
     * @param customerID the primary key of a <code>Customer</code> object
     */
    @Override
    public void emptyBasket(long customerID)
    {
        basketRepository.deleteAllByID(customerID);
    }


    /**
     * Gets a list of <code>BasketItem</code> objects with product and category details attached
     *
     * @param customerID the primary key of a <code>Customer</code> object
     * @return a list of <code>BasketItem</code> objects, or null
     */
    @Override
    public @Nullable List<BasketItemWithDetails> getAll(long customerID)
    {
        List<BasketItem> filteredList = getByCustomerID(basketRepository.getAll(), customerID);

        /*
        Returns a new list of BasketItemWithDetails objects. Equivalent of a for-each that gets the ProductWithCategory from
        productRepository, then extracts the quantity from the BasketItem and creates a BasketItemWithDetails object.
         */
        return filteredList.stream()
                .map(item -> new BasketItemWithDetails(
                        productRepository.getByIdWithCategory(item.getId().productID()),
                        item.getQuantity()))
                .collect(Collectors.toList());
    }

    /**
     * Helper method to update the quantity of a basket item
     *
     * @param customerID  the primary key of a <code>Customer</code> object
     * @param productID   the primary key of a <code>Product</code> object
     * @param newQuantity the new quantity for the item
     */
    private void updateQuantity(long customerID, long productID, int newQuantity)
    {
        basketRepository.update(new BasketItem(customerID, productID, newQuantity));
    }


    /**
     * Helper method to get only the <code>BasketItems</code> from a list where each item's <code>BasketItemID</code>
     * has a <code>customerID</code> matching the parameter
     *
     * @param items      a list of <code>BasketItem</code> objects
     * @param customerID the primary key of a <code>Customer</code> object
     * @return a filtered list of <code>BasketItem</code> objects
     */
    private List<BasketItem> getByCustomerID(@NotNull List<BasketItem> items, long customerID)
    {
        // Returns a new list of elements matching the customer ID
        return items.stream()
                .filter(item -> item.getId().customerID() == customerID)
                .collect(Collectors.toList());
    }


}
