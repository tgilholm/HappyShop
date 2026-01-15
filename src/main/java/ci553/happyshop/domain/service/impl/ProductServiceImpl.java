package ci553.happyshop.domain.service.impl;

import ci553.happyshop.catalogue.Product;
import ci553.happyshop.domain.service.ProductService;

public class ProductServiceImpl extends ProductService
{
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
}
