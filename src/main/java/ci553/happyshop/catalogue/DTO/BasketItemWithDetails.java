package ci553.happyshop.catalogue.DTO;

/**
 * Data Transfer object containing a <code>ProductWithCategory</code> object and the <code>quantity</code> of it in the basket
 * @param productWithCategory a <code>ProductWithCategory</code> object
 * @param quantity an integer value for the number of products
 */
public record BasketItemWithDetails(ProductWithCategory productWithCategory, int quantity) {}
