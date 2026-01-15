package ci553.happyshop.domain.service;

import ci553.happyshop.domain.service.impl.BasketServiceImpl;
import ci553.happyshop.domain.service.impl.LoginServiceImpl;
import ci553.happyshop.domain.service.impl.ProductServiceImpl;

/**
 * Factory for creating singleton service instances. Services act as the business layer, mediating
 * between the data and presentation layer. Avoids creating many small factory classes by centralizing service creation.
 * Abstracts business logic from the presentation layer and simplifies the data layer.
 */
public final class ServiceFactory
{
    private ServiceFactory()
    {
    }     // Final class, no instantiation


    // Create singleton service instances
    private static BasketService basketService;
    private static ProductService productService;
    private static LoginService loginService;


    /**
     * Checks if the basket service already exists and if not, creates a new instance
     *
     * @return the service instance
     */
    public static BasketService getBasketService()
    {
        if (basketService == null)
        {
            basketService = new BasketServiceImpl();
        }
        return basketService;
    }


    /**
     * Checks if the product service already exists and if not, creates a new instance
     *
     * @return the service instance
     */
    public static ProductService getProductService()
    {
        if (productService == null)
        {
            productService = new ProductServiceImpl();
        }
        return productService;
    }


    /**
     * Checks if the login service already exists and if not, creates a new instance
     * @return the service instance
     */
    public static LoginService getLoginService()
    {
        if (loginService == null)
        {
            loginService = new LoginServiceImpl();
        }
        return loginService;
    }
}
