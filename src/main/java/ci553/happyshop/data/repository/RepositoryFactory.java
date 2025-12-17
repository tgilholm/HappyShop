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
}
