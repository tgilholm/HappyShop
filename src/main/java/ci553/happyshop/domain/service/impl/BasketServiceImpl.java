package ci553.happyshop.domain.service.impl;

import ci553.happyshop.catalogue.BasketItem;
import ci553.happyshop.catalogue.BasketItemID;
import ci553.happyshop.catalogue.DTO.BasketItemWithDetails;
import ci553.happyshop.catalogue.DTO.ProductWithCategory;
import ci553.happyshop.catalogue.Product;
import ci553.happyshop.data.repository.BasketRepository;
import ci553.happyshop.data.repository.ProductRepository;
import ci553.happyshop.data.repository.RepositoryFactory;
import ci553.happyshop.domain.service.BasketService;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private final IntegerProperty changeProperty = new SimpleIntegerProperty(0); // Used for updating lists on changes

    private static final Logger logger = LogManager.getLogger();

    /**
     * Updates the <code>changeProperty</code>. Use whenever the underlying data has changed to trigger
     * all observers waiting to update internal lists
     */
    private void notifyChanged()
    {
        changeProperty.set(changeProperty.getValue() + 1);
        logger.debug("notifyChanged() invoked");
    }


    /**
     * Exposes an observable form of the change counter
     *
     * @return an immutable form of the counter
     */
    public ReadOnlyIntegerProperty basketChanged()
    {
        return changeProperty;
    }

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

        logger.info("Removing product with id: {} from basket", productID);
        if (currentQuantity > 1)
        {   // If there still are items remaining after decrementing, update with quantity -1
            logger.debug("Quantity in basket: {}, decrementing quantity by 1. ProductID: {}", currentQuantity, productID);
            updateQuantity(customerID, productID, currentQuantity - 1);
        } else
        {
            // If decrementing would result in quantity = 0, remove the item altogether
            logger.debug("Quantity <= 1, deleting item. ProductID: {}", productID);
            basketRepository.delete(new BasketItemID(customerID, productID));
        }

        notifyChanged();
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

        logger.info("Adding product with id: {} to basket", productID);

        if (currentQuantity > 0)
        {
            // If it exists, update it
            logger.debug("Product with id: {} already exists in basket with quantity {}. Updating quantity.", productID, currentQuantity);
            updateQuantity(customerID, productID, quantity + currentQuantity);
        } else
        {
            // If it doesn't exist, create it
            logger.debug("Product with id: {} doesn't exist in basket. Adding new item", productID);
            basketRepository.insert(new BasketItem(customerID, productID, quantity));
        }

        notifyChanged();
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
        // Get the list of basket items and get the quantity from it
        List<BasketItem> items = basketRepository.getAll();

        // Searches the list for the matching item. If found, gets the quantity. Otherwise, returns 0
        return items.stream()
                .filter(item -> item.getId().customerID() == customerID && item.getId().productID() == productID)
                .findFirst()
                .map(BasketItem::getQuantity)
                .orElseGet(() ->
                {
                    return 0;
                });
    }


    /**
     * Gets the total price of all the BasketItems linked to this customerID
     *
     * @param customerID the primary key of a <code>Customer</code> object
     * @return a <code>double</code> total price
     */
    @Override
    public double getBasketTotalPrice(long customerID)
    {
        // Get only the items matching the customerID
        List<BasketItem> items = getAllByCustomerID(customerID);

        /*
        Gets all items matching customer ID, invokes getTotalPriceByID on each BasketItem then uses mapToDouble to
        sum up the total prices into a double
        */

        return items.stream()
                .mapToDouble(item ->
                {
                    // For each basket item retrieve the product's unit price and multiply by quantity
                    return getTotalPriceByID(item.getId().customerID(), item.getId().productID());
                })
                .sum();
    }

    /**
     * Clears all the basket items connected to a specified <code>customerID</code>
     *
     * @param customerID the primary key of a <code>Customer</code> object
     */
    @Override
    public void clearBasket(long customerID)
    {
        basketRepository.deleteAllByID(customerID);
        notifyChanged();
    }

    /**
     * Gets a list of <code>BasketItemWithDetails</code> objects with product and category details attached
     *
     * @param customerID the primary key of a <code>Customer</code> object
     * @return a list of <code>BasketItem</code> objects, or null
     */
    @Override
    public @Nullable List<BasketItemWithDetails> getAll(long customerID)
    {
        // Get only the items matching the customerID
        List<BasketItem> filteredList = getAllByCustomerID(customerID);

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
     * Reduces stock of all items in the basket of a specified <code>customerID</code>,
     * then clears their basket
     *
     * @param customerID the primary key of a <code>Customer</code> object
     */
    @Override
    public void checkoutBasket(long customerID)
    {
        // Get the items matching the customer id
        List<BasketItem> basketItems = getAllByCustomerID(customerID);

        // Reduce the stock of each of the items via the productRepository
        for (BasketItem item : basketItems)
        {
            // Get the product
            Product product = productRepository.getById(item.getId().productID());

            if (product == null)
            {
                logger.warn("Failed to get product with id: {}", item.getId().productID());
            }
            else
            {
                // Get the current quantity
                int quantity = product.getStockQuantity();

                // Reduce the quantity by the specified amount, using math.max to prevent negative values
                product.setStockQuantity(Math.max(0, quantity - item.getQuantity()));   // If qty - ordered qty < 0, then set to 0 instead of a negative

                logger.debug("Purchased {} of {}", item.getQuantity(), product);

                // Dispatch the update
                productRepository.update(product);
            }
        }

        notifyChanged();

        // Clear the basket
        clearBasket(customerID);
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
        notifyChanged();
    }


    /**
     * Helper method to get only the <code>BasketItems</code> from a list where each item's <code>BasketItemID</code>
     * has a <code>customerID</code> matching the parameter
     *
     * @param customerID the primary key of a <code>Customer</code> object
     * @return a filtered list of <code>BasketItem</code> objects
     */
    private List<BasketItem> getAllByCustomerID(long customerID)
    {
        List<BasketItem> items = basketRepository.getAll();
        // Returns a new list of elements matching the customer ID
        return items.stream()
                .filter(item -> item.getId().customerID() == customerID)
                .collect(Collectors.toList());
    }


    /**
     * Helper method to calculate the total price of a BasketItem
     *
     * @param customerID the primary key of a <code>Customer</code> object
     * @param productID  the primary key of a <code>Product</code> object
     * @return an <code>double</code> value of the total price
     */
    private double getTotalPriceByID(long customerID, long productID)
    {
        int quantity = getQuantity(customerID, productID);        // Get the quantity from the basket item

        if (quantity == 0)        // If the item isn't in the basket, return 0
        {
            logger.debug("Price requested for missing BasketItem {} {}", customerID, productID);
            return 0;
        }

        // Extract the unit price & multiply it by quantity
        // Multiply unit price by quantity to get the total price for this BasketItem
        ProductWithCategory productWithCategory = productRepository.getByIdWithCategory(productID);
        if (productWithCategory != null)
        {
            return productWithCategory.product().getUnitPrice() * quantity;
        } else
        {
            logger.debug("Unable to calculate total price");
            return 0;
        }
    }
}
