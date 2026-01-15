package ci553.happyshop.domain.service;

import ci553.happyshop.data.repository.ProductRepository;
import ci553.happyshop.data.repository.RepositoryFactory;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class ProductService
{
    // Get repository instances
    protected final ProductRepository productRepository = RepositoryFactory.getProductRepository();
    protected final IntegerProperty changeProperty = new SimpleIntegerProperty(0); // Used for updating lists on changes

    protected static final Logger logger = LogManager.getLogger();


    /**
     * Updates the <code>changeProperty</code>. Use whenever the underlying data has changed to trigger
     * all observers waiting to update internal lists
     */
    protected void notifyChanged()
    {
        changeProperty.set(changeProperty.getValue() + 1);
        logger.debug("notifyChanged() invoked");
    }


    /**
     * Exposes an observable form of the change counter
     *
     * @return an immutable form of the counter
     */
    public ReadOnlyIntegerProperty productsChanged()
    {
        return changeProperty;
    }


    /**
     * Get the quantity in stock of a specified product
     * @param productID  the primary key of a <code>Product</code> object
     * @return an <code>int</code> value of the quantity
     */
    public abstract int getStockQuantity(long productID);
}
