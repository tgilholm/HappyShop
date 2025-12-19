package ci553.happyshop.catalogue;


/**
 * Record to provide Composite keys for BasketItem objects
 * Contains <code>long</code> customer and product IDs
 */
public record BasketItemID(long customerID, long productID)
{
}
