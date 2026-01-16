package ci553.happyshop.service;

import ci553.happyshop.catalogue.DTO.ProductWithCategory;
import ci553.happyshop.catalogue.Product;
import javafx.beans.property.ReadOnlyIntegerProperty;

import java.util.List;

public interface ProductService
{
    /**
     * Exposes an observable form of the change counter
     *
     * @return an immutable form of the counter
     */
    ReadOnlyIntegerProperty productsChanged();


    /**
     * Get the quantity in stock of a specified product
     *
     * @param productID the primary key of a <code>Product</code> object
     * @return an <code>int</code> value of the quantity
     */
    int getStockQuantity(long productID);


    /**
     * Gets all Products with their connected Categories
     * @return a list of <code>ProductWithCategory</code> objects
     */
    List<ProductWithCategory> getAllWithCategories();

    /**
     * Deletes a product via the Repository
     * @param product the <code>Product</code> to delete
     */
    void deleteProduct(Product product);
}
