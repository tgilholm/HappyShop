package ci553.happyshop.catalogue;

/**
 * A Data Transfer Object that contains a <code>Product</code> and <code>Category</code> object.
 * This allows accessing <code>Category</code> names from a <code>Product</code> context
 */
public record ProductWithCategory(Product product, Category category) {}
