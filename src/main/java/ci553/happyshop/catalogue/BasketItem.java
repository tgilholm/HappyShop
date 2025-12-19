package ci553.happyshop.catalogue;

/**
 * Stores information about an item in a user's basket
 */
public class BasketItem
{
    // Holds a Customer and Product object
    private final BasketItemID id;      // Composite primary key
    private final int quantity;         // Amount of the item in the basket

    public BasketItem(Customer customer, Product product, BasketItemID id, int quantity)
    {
        this.id = id;
        this.quantity = quantity;
    }

    public BasketItemID getId()
    {
        return id;
    }

    public int getQuantity()
    {
        return quantity;
    }
}
