package ci553.happyshop.domain.service;

import ci553.happyshop.domain.service.impl.BasketServiceImpl;

/**
 * Factory for creating singleton service instances. Services act as the business layer, mediating
 * between the data and presentation layer. Avoids creating many small factory classes by centralizing service creation.
 * Abstracts business logic from the presentation layer and simplifies the data layer.
 */
public class ServiceFactory
{
    // Create singleton service instances
    private static BasketService basketService;


    /**
     * Checks if the basket service already exists and if not, creates a new instance
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
}
