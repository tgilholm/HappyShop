package ci553.happyshop.catalogue;

/**
 * Stores information about an item in a user's basket
 */
public class BasketItem
{
    // Composite primary key of userID and product ID
    private final long customerID;
    private final long productID;

    private int quantity;       // Amount of the item in the basket

    public BasketItem(long customerID, long productID, int quantity)
    {
        this.customerID = customerID;
        this.productID = productID;
        this.quantity = quantity;
    }
}
