package ci553.happyshop.data.repository;

import ci553.happyshop.data.database.DatabaseConnection;
import ci553.happyshop.data.repository.impl.AuthRepositoryImpl;
import ci553.happyshop.data.repository.impl.BasketRepositoryImpl;
import ci553.happyshop.data.repository.impl.CategoryRepositoryImpl;
import ci553.happyshop.data.repository.impl.ProductRepositoryImpl;

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
    private static AuthRepository authRepository;


    /**
     * Checks if the <code>ProductRepository</code> already exists and if not, creates a new instance
     * @return the repository instance
     */
    public static ProductRepository getProductRepository() {
        if (productRepository == null)
        {
            productRepository = new ProductRepositoryImpl(dbConnection);
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
            categoryRepository = new CategoryRepositoryImpl(dbConnection);
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
            basketRepository = new BasketRepositoryImpl(dbConnection);
        }
        return basketRepository;
    }

    /**
     * Checks if the <code>AuthRepository</code> already exists and if not, creates a new instance
     * @return the repository instance
     */
    public static AuthRepository getAuthRepository() {
        if (authRepository == null)
        {
            authRepository = new AuthRepositoryImpl(dbConnection);
        }
        return authRepository;
    }
}
