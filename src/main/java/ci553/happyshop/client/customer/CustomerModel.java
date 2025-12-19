package ci553.happyshop.client.customer;

import ci553.happyshop.catalogue.*;
import ci553.happyshop.catalogue.DTO.ProductWithCategory;
import ci553.happyshop.client.customer.basket.BasketClient;
import ci553.happyshop.data.repository.BasketRepository;
import ci553.happyshop.data.repository.CategoryRepository;
import ci553.happyshop.data.repository.ProductRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;


/**
 * The CustomerModel is responsible for exposing an Observable productList that is bound to the View by the CustomerController.
 * User search is facilitated by a double-filtered list (Search list -> Category list -> underlying product list).
 * Interfaces with DB with repositories.
 */
public class CustomerModel
{
    // TODO GET CURRENTLY LOGGED IN USER
    // This is a mockup of a logged in user that will be used to test basket methods
    Customer PLACEHOLDER = new Customer(1, "PLACEHOLDER", "PLACEHOLDER");


    private final Logger logger = LogManager.getLogger();

    // Get repository instances
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BasketRepository basketRepository;


    /**
     * Constructs a new CustomerModel instance that handles data from the DB.
     *
     * @param productRepository  for interacting with the <code>Product</code> table
     * @param categoryRepository for interacting with the <code>Category</code> table
     * @param basketRepository for interacting with the <code>Basket</code> table
     */
    public CustomerModel(ProductRepository productRepository, CategoryRepository categoryRepository, BasketRepository basketRepository)
    {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.basketRepository = basketRepository;
    }


    private final ObservableList<ProductWithCategory> productWithCategoryList = FXCollections.observableArrayList();        // Observable product list
    private final ObservableList<Category> categoryList = FXCollections.observableArrayList();                              // Observable category list
    private FilteredList<ProductWithCategory> searchFilteredList;                                                           // Filtered product list
    private FilteredList<ProductWithCategory> categoryFilteredList;                                                         // Product list filtered by category

    private String imageName = "images/imageHolder.jpg";                // Image to show in product preview (Search Page)

    // todo preview image in detail pane with placeholder image
    // todo placeholder image for image not found cases
    // todo observe quantity remaining in product cards or update manually


    /**
     * Exposes an <code>ObservableList</code> version of the product list.
     *
     * @return a list of <code>ProductWithCategory</code> objects
     */
    public ObservableList<ProductWithCategory> getProducts()
    {
        return productWithCategoryList;
    }

    /**
     * Exposes an ObservableList version of the category list
     *
     * @return the list of <code>Category</code> objects
     */
    public ObservableList<Category> getCategories()
    {
        return categoryList;
    }

    /**
     * Updates the <code>productWithCategoryList</code> from the <code>productRepository</code>
     */
    public void loadProducts()
    {
        // Use setAll to update the productList
        productWithCategoryList.setAll(productRepository.getAllWithCategories());

        logger.info("Retrieved {} products with categories from ProductTable", productWithCategoryList.size());
    }

    /**
     * Updates the <code>categoryList</code> from the <code>productRepository</code>
     */
    public void loadCategories()
    {
        categoryList.setAll(categoryRepository.getAll());

        logger.info("Retrieved {} categories from CategoryTable", categoryList.size());
    }


    /**
     * Gets the list of products matching the specified category. Defaults to the base <code>productList</code>.
     * Wraps around <code>productWithCategoryList</code> if <code>categoryFilteredList</code> doesn't already exist, returns the existing list
     * otherwise.
     *
     * @return the <code>FilteredList</code> of products matching the category filter
     */
    public FilteredList<ProductWithCategory> getCategoryFilteredList()
    {
        // Defaults to "no category"
        if (categoryFilteredList == null)
        {
            // Gets the
            categoryFilteredList = new FilteredList<>(productWithCategoryList, p -> true);
        }
        return categoryFilteredList;
    }

    /**
     * Gets the (already filtered by category) list of products matching the search filter.
     * Searches in product description and ID. Gets the list of products from <code>categoryFilteredList</code>
     * if this list doesn't exist, otherwise returns the existing list.
     *
     * @return the <code>FilteredList</code> of products matching the filter
     */
    public FilteredList<ProductWithCategory> getSearchFilteredList()
    {
        // Creates searchFilteredList if it doesn't exist
        if (searchFilteredList == null)
        {
            // Wrap around the categoryFilteredList if this list doesn't exist yet
            searchFilteredList = new FilteredList<>(getCategoryFilteredList(), p -> true);
        }
        return searchFilteredList;
    }

    /**
     * Updates the predicate of the searchFilteredList to search by product ID or description and
     * return the products that match the predicate <code>searchFilter</code>.
     *
     * @param searchFilter a <code>String</code> literal matching either the product ID or description
     */
    public void setSearchFilter(String searchFilter)
    {
        // Update the predicate of searchFilteredList
        searchFilteredList.setPredicate(productWithCategory ->
        {
            if (searchFilter == null)
            {
                return true;
            } else
            {
                // Return true if the ID or description match the search filter
                return String.valueOf(productWithCategory.product().getId()).contains(searchFilter)
                        || productWithCategory.product().getName().toLowerCase().contains(searchFilter.toLowerCase());
            }
        });
    }

    /**
     * Updates the predicate of the categoryFilteredList to search by categoryName and
     * return the products that match the predicate <code>categoryFilter</code>.
     *
     * @param categoryFilter a <code>String</code> literal matching the category name
     */
    public void setCategoryFilter(String categoryFilter)
    {
        // Update the predicate of categoryFilteredList
        categoryFilteredList.setPredicate(productWithCategory ->
        {
            if (categoryFilter == null)
            {
                return true;
            } else
            {
                // Return true if the category name matches the search filter
                String lowercaseFilter = categoryFilter.toLowerCase().trim();
                return productWithCategory.category().getName().toLowerCase().equals(lowercaseFilter);
            }
        });
    }

    // Check basket- if not already in the basket, add it
    // If in the basket, increment quantity by one
    public void addToBasket(@NotNull Product product)
    {
        basketRepository.addOrUpdateItem(PLACEHOLDER.getId(), product.getId(), 1);
        loadProducts();     // todo currently redraws all cards if product list changes
    }

    public void removeFromBasket(@NotNull Product product)
    {
        basketRepository.decreaseOrRemoveItem(PLACEHOLDER.getId(), product.getId());
        loadProducts();
    }

    public int getBasketQuantity(@NotNull Product product)
    {
        return basketRepository.getQuantity(PLACEHOLDER.getId(), product.getId());
    }

    /**
     * Runs the <code>start</code> method in <code>BasketClient</code>
     */
    public void openBasket()
    {
        try {
            BasketClient basketClient = new BasketClient(PLACEHOLDER);
            basketClient.start(new Stage());
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}
