package ci553.happyshop.service;

import ci553.happyshop.catalogue.Category;
import ci553.happyshop.catalogue.DTO.ProductWithCategory;
import ci553.happyshop.catalogue.Product;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyStringProperty;

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
     * Exposes an immutable version of the validation error
     *
     * @return a <code>ReadOnlyStringProperty</code> to be observed by models
     */
    ReadOnlyStringProperty userError();

    /**
     * Resets the user error field back to the default
     */
    void resetUserError();


    /**
     * Get the quantity in stock of a specified product
     *
     * @param productID the primary key of a <code>Product</code> object
     * @return an <code>int</code> value of the quantity
     */
    int getStockQuantity(long productID);


    /**
     * Gets all Products with their connected Categories
     *
     * @return a list of <code>ProductWithCategory</code> objects
     */
    List<ProductWithCategory> getAllWithCategories();

    /**
     * Deletes a product via the Repository
     *
     * @param product the <code>Product</code> to delete
     */
    void deleteProduct(Product product);

    /**
     * Validates the new product details. If they are accepted, pass to the ProductRepository
     * to update the product details
     *
     * @param id               the id of the product being updated, as a <code>long</code>
     * @param newName          the new name for the product, as a <code>String</code>
     * @param newImageName     the new name of the product's image, as a <code>String</code>
     * @param newPrice         the new price for the product, as a <code>String</code>
     * @param newStockQuantity the new stock quantity, as an <code>int</code>
     * @param newCategory      the name of a category, as a <code>String</code>
     */
    void updateProduct(long id, String newName, String newImageName, String newPrice, int newStockQuantity,
            String newCategory);
}
