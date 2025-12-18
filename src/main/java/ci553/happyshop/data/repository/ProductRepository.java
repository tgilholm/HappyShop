package ci553.happyshop.data.repository;

import ci553.happyshop.catalogue.Product;
import ci553.happyshop.catalogue.ProductWithCategory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Defines CRUD operations for interfacing with ProductTable
 */
public interface ProductRepository extends CommonRepository<Product, Long>
{

    /**
     * Gets the list of all <code>Product</code> entities
     *
     * @return the <code>List</code> of products
     */
    List<Product> getAll();

    /**
     * Gets the list of <code>Product</code> entities joined to the linked <code>Category</code>
     * @return a list of <code>ProductWithCategory</code> object
     */
    List<ProductWithCategory> getAllWithCategories();

    /**
     * Gets a specific <code>Product</code> by its ID
     *
     * @param id The primary key of the product
     * @return the product selected, or null
     */
    @Nullable
    Product getById(@NotNull Long id);


    /**
     * Adds a new <code>Product</code> to the table
     *
     * @param product the product to insert
     */
    void insert(@NotNull Product product);


    /**
     * Updates the details of an existing <code>Product</code> in the table
     *
     * @param product the product to update
     */
    void update(@NotNull Product product);


    /**
     * Removes a <code>Product</code> from the table
     *
     * @param id The primary key of the product
     */
    void delete(@NotNull Long id);
}
