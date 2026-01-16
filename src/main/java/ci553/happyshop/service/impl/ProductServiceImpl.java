package ci553.happyshop.service.impl;

import ci553.happyshop.catalogue.DTO.ProductWithCategory;
import ci553.happyshop.catalogue.Product;
import ci553.happyshop.data.repository.ProductRepository;
import ci553.happyshop.data.repository.RepositoryFactory;
import ci553.happyshop.service.ProductService;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ProductServiceImpl implements ProductService
{
    // Get repository instances
    ProductRepository productRepository = RepositoryFactory.getProductRepository();
    IntegerProperty changeProperty = new SimpleIntegerProperty(0); // Used for updating lists on changes

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
        }
        else {
            logger.debug("Unable to find product with id: {}", productID);
            return 0;
        }
    }

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
        productRepository.delete(product.getId());
    }
}
