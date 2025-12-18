package ci553.happyshop.catalogue;

/**
 * A Data Transfer Object that contains a <code>Product</code> and <code>Category</code> object.
 * This allows accessing <code>Category</code> names from a <code>Product</code> context
 */
public class ProductWithCategory
{
    private final Product product;

    private final Category category;

    /**
     * Constructs a new <code>ProductWithCategory</code> object from the provided <code>Product</code>
     * and <code>Category</code> objects
     * @param product the <code>Product</code> object
     * @param category the <code>Category</code> object
     */
    public ProductWithCategory(Product product, Category category)
    {
        this.product = product;
        this.category = category;
    }
}
