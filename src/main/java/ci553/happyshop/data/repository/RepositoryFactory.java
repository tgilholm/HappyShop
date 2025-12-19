package ci553.happyshop.data.repository;

import ci553.happyshop.data.database.DatabaseConnection;

/**
 * Factory for creating singleton repository instances.
 * Avoids creating many small factory classes by centralizing repository creation
 */
public class RepositoryFactory
{
    // Re-use the same URL and DatabaseConnection in all factories
    private static final String DB_URL = "jdbc:derby:happyShopDB";
    private static final DatabaseConnection dbConnection = new DatabaseConnection(DB_URL);

    // Singleton instance of each repository
    private static ProductRepository productRepository;
    private static CategoryRepository categoryRepository;
    private static BasketRepository basketRepository;
    private static CustomerRepository customerRepository;


    /**
     * Checks if the <code>ProductRepository</code> already exists and if not, creates a new instance
     * @return the repository instance
     */
    public static ProductRepository getProductRepository() {
        if (productRepository == null)
        {
            productRepository = new ProductRepository(dbConnection);
        }
        return productRepository;
    }

    /**
     * Checks if the <code>CategoryRepository</code> already exists and if not, creates a new instance
     * @return the repository instance
     */
    public static CategoryRepository getCategoryRepository() {
        if (categoryRepository == null)
        {
            categoryRepository = new CategoryRepository(dbConnection);
        }
        return categoryRepository;
    }

    /**
     * Checks if the <code>BasketRepository</code> already exists and if not, creates a new instance
     * @return the repository instance
     */
    public static BasketRepository getBasketRepository() {
        if (basketRepository == null)
        {
            basketRepository = new BasketRepository(dbConnection);
        }
        return basketRepository;
    }

    /**
     * Checks if the <code>CustomerRepository</code> already exists and if not, creates a new instance
     * @return the repository instance
     */
    public static CustomerRepository getCustomerRepository() {
        if (customerRepository == null)
        {
            customerRepository = new CustomerRepository(dbConnection);
        }
        return customerRepository;
    }
}
