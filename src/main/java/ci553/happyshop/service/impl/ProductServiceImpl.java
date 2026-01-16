package ci553.happyshop.service.impl;

import ci553.happyshop.catalogue.Category;
import ci553.happyshop.catalogue.DTO.ProductWithCategory;
import ci553.happyshop.catalogue.Product;
import ci553.happyshop.data.repository.CategoryRepository;
import ci553.happyshop.data.repository.ProductRepository;
import ci553.happyshop.data.repository.RepositoryFactory;
import ci553.happyshop.service.ProductService;
import javafx.beans.property.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ProductServiceImpl implements ProductService
{
    // Get repository instances
    ProductRepository productRepository = RepositoryFactory.getProductRepository();
    CategoryRepository categoryRepository = RepositoryFactory.getCategoryRepository();
    IntegerProperty changeProperty = new SimpleIntegerProperty(0); // Used for updating lists on changes
    private final StringProperty errorProperty = new SimpleStringProperty("");  // Used for returning input validation conditions

    Logger logger = LogManager.getLogger();


    /**
     * Updates the <code>changeProperty</code>. Use whenever the underlying data has changed to trigger
     * all observers waiting to update internal lists
     */
    private void notifyChanged()
    {
        changeProperty.set(changeProperty.getValue() + 1);
        logger.debug("notifyChanged() invoked");
    }


    /**
     * Updates the <code>errorProperty</code> to the specified validation error.
     * This is used whenever there is an error in updating product data entered by users.
     * Changes to this property should trigger observers and display alerts.
     *
     * @param error a description of the error
     */
    private void notifyError(@NotNull String error)
    {
        errorProperty.set(error);
        logger.debug("notifyError() invoked");
    }


    /**
     * Exposes an observable form of the change counter
     *
     * @return an immutable form of the counter
     */
    @Override
    public ReadOnlyIntegerProperty productsChanged()
    {
        return changeProperty;
    }


    /**
     * Exposes an immutable version of the validation error
     *
     * @return a <code>ReadOnlyStringProperty</code> to be observed by models
     */
    @Override
    public ReadOnlyStringProperty userError()
    {
        return errorProperty;
    }


    /**
     * Resets the user error field back to the default
     */
    @Override
    public void resetUserError()
    {
        errorProperty.set("");
    }


    /**
     * Get the quantity in stock of a specified product
     *
     * @param productID the primary key of a <code>Product</code> object
     * @return an <code>int</code> value of the quantity
     */
    @Override
    public int getStockQuantity(long productID)
    {
        Product product = productRepository.getById(productID);

        if (product != null)
        {
            return product.getStockQuantity();
        } else
        {
            logger.debug("Unable to find product with id: {}", productID);
            return 0;
        }
    }


    /**
     * Gets all Products with their connected Categories
     *
     * @return a list of <code>ProductWithCategory</code> objects
     */
    @Override
    public List<ProductWithCategory> getAllWithCategories()
    {
        return productRepository.getAllWithCategories();
    }


    /**
     * Deletes a product via the Repository
     *
     * @param product the <code>Product</code> to delete
     */
    @Override
    public void deleteProduct(@NotNull Product product)
    {
        logger.info("Deleting product {}", product.getId());

        productRepository.delete(product.getId());

        // Trigger observers
        notifyChanged();
    }


    /**
     * Validates the new product details. If they are accepted, pass to the ProductRepository
     * to update the product details
     *
     * @param id               the id of the product being updated, as a <code>long</code>
     * @param newName          the new name for the product, as a <code>String</code>
     * @param newImageName     the new name of the product's image, as a <code>String</code>
     * @param newPrice         the new price for the product, as a <code>String</code>
     * @param newStockQuantity the new stock quantity, as a <code>String</code>
     * @param newCategory      the name of a category, as a <code>String</code>
     *
     */
    @Override
    public void updateProduct(long id, String newName, String newImageName, String newPrice, String newStockQuantity,
            String newCategory)
    {
        // Check that the product exists
        if (productRepository.getById(id) == null)
        {
            notifyError("Cannot update product- no product exists with id: " + id);
            return;
        }

        // Check for empty strings
        if (newName == null || newName.isEmpty())
        {
            notifyError("Cannot update product- product cannot have empty name");
            return;
        }
        if (newPrice == null || newPrice.isEmpty())
        {
            notifyError("Cannot update product- product cannot have empty price");
            return;
        }
        if (newStockQuantity == null || newStockQuantity.isEmpty())
        {
            notifyError("Cannot update product- product cannot have empty quantity");
            return;
        }
        if (newImageName == null || newImageName.isEmpty())
        {
            //todo pass placeholder image location
        }

        // Parse stock quantity to int
        int intStockQuantity = 0;
        try
        {
            intStockQuantity = Integer.parseInt(newStockQuantity);
        } catch (NumberFormatException e)
        {
            notifyError("Cannot update product- cannot parse new stock quantity to a number");
            return;
        }

        // Check for negative stock quantity
        if (intStockQuantity < 0)
        {
            notifyError("Cannot update product- product cannot have negative quantity");
            return;
        }

        // Parse the new price to a double
        double doubleNewPrice = 0.0;
        try
        {
            doubleNewPrice = Double.parseDouble(newPrice);
        } catch (NumberFormatException e)
        {
            notifyError("Cannot update product- cannot parse new price to a number");
            return;
        }

        // Check that the requested category exists
        Category category = categoryRepository.getByName(newCategory);
        if (category == null)
        {
            notifyError("Cannot update product- cannot find category: " + newCategory);
            return;
        }

        logger.info("Updating product: {}", id);

        // Pass new data to the repository
        Product newProduct = new Product(id, newName, newImageName, doubleNewPrice, intStockQuantity, category.getId());
        productRepository.update(newProduct);

        notifyChanged();    // Indicate to observers that the product list has updated
    }


}
