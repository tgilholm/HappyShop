package ci553.happyshop.catalogue;

/**
 * Stores information about an item in a user's basket
 */
public class BasketItem
{
    // Holds a Customer and Product object
    private final Customer customer;
    private final Product product;
    private int quantity;       // Amount of the item in the basket

    public BasketItem(Customer customer, Product product, int quantity)
    {
        this.customer = customer;
        this.product = product;
        this.quantity = quantity;
    }
}
