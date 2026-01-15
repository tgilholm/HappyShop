package ci553.happyshop.client.customer;

import ci553.happyshop.base_mvm.BaseModel;
import ci553.happyshop.catalogue.*;
import ci553.happyshop.catalogue.DTO.ProductWithCategory;
import ci553.happyshop.domain.service.BasketService;
import ci553.happyshop.domain.service.CategoryService;
import ci553.happyshop.domain.service.ProductService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * The CustomerModel is responsible for exposing an Observable productList that is bound to the View by the CustomerController.
 * User search is facilitated by a double-filtered list (Search list -> Category list -> underlying product list).
 * Interfaces with DB with repositories.
 */
public class CustomerModel extends BaseModel
{
    private final User currentUser;   // The user logged in to the system
    private final BasketService basketService;
    private final ProductService productService;
    private final CategoryService categoryService;

    private final ObservableList<ProductWithCategory> productWithCategoryList = FXCollections.observableArrayList();
    private final ObservableList<Category> categoryList = FXCollections.observableArrayList();
    private FilteredList<ProductWithCategory> searchFilteredList;
    private FilteredList<ProductWithCategory> categoryFilteredList;

    /*
    The ExecutorService used for background DB queries.
    This allows access to the database without slowing down the system. Note that this means
    Platform.runLater() is used in order to update JavaFX elements on the main thread, as is required.

    singleThreadExecutors run tasks sequentially, parallel to the main thread.
     */
    private final ExecutorService executorService = Executors.newSingleThreadExecutor(runnable ->
    {
        // The executorService will re-use this thread and execute the provided runnable.
        Thread thread = new Thread(runnable, "CustomerModel-DBLoader");
        thread.setDaemon(true); // daemon = true so that the JVM doesn't get stuck on background threads that won't end
        logger.debug("Starting runnable");
        return thread;
    });

    // todo placeholder image
    //private final String imageName = "images/imageHolder.jpg"; // Image to show in product preview (Search Page)


    /**
     * Constructs a new CustomerModel instance that handles data from the DB. Injects services via
     * dependency injection in constructor
     *
     * @param basketService  a <code>BasketService</code> instance
     * @param productService a <code>ProductService</code> instance
     * @param user           the currently logged-in user
     */
    public CustomerModel(User user, @NotNull BasketService basketService, @NotNull ProductService productService,
            CategoryService categoryService)
    {
        this.currentUser = user;
        this.basketService = basketService;
        this.productService = productService;
        this.categoryService = categoryService;

        // Observe the changeCounter in BasketService and automatically reload the product list
        basketService.basketChanged().addListener((observable, oldValue, newValue) -> loadProducts());
    }


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
     * Asynchronously updates the <code>productWithCategoryList</code> from the <code>productRepository</code>
     */
    public void loadProducts()
    {
        // Execute service in a background Thread
        executorService.submit(() ->
        {
            // Retrieve the ProductWithCategory list OFF the main thread
            List<ProductWithCategory> list = productService.getAllWithCategories();

            logger.debug("Retrieved {} products with categories from ProductTable", list.size());

            Platform.runLater(() ->
            {
                // Update the observable list on the JavaFX thread
                productWithCategoryList.setAll(list);
            });
        });
    }


    /**
     * Asynchronously updates the <code>categoryList</code> from the <code>productRepository</code>
     */
    public void loadCategories()
    {
        // Background thread
        executorService.submit(() ->
        {
            List<Category> list = categoryService.getAll();
            logger.debug("Retrieved {} categories from CategoryTable", list.size());

            // Update the observable list on the main thread
            Platform.runLater(() -> categoryList.setAll(list));
        });
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
        // Get the list before filtering-avoids null lists
        getSearchFilteredList().setPredicate(productWithCategory ->
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
        // Get the list before filtering-avoids null lists
        getSearchFilteredList().setPredicate(productWithCategory ->
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


    /**
     * Delegates to basketService to add or update the quantity of an item
     *
     * @param product the <code>Product</code> object to update
     */
    public void addToBasket(@NotNull Product product)
    {
        executorService.submit(() ->
        {
            // Update the basket on the background thread
            basketService.addOrUpdateItem(currentUser.id(), product.getId(), 1);
        });

        loadProducts(); // List has changed, update card data
    }


    /**
     * Delegates to basketService to remove or decrease the quantity of an item
     *
     * @param product the <code>Product</code> object to update
     */
    public void removeFromBasket(@NotNull Product product)
    {
        executorService.submit(() ->
        {
            // Update the basket on the background thread
            basketService.decreaseOrRemoveItem(currentUser.id(), product.getId());
        });

        loadProducts();
    }


    /**
     * Delegates to basketService to get the quantity of a product
     *
     * @param product the <code>Product</code> object from which the quantity is extracted.
     * @return an int value of the quantity
     */
    public int getBasketQuantity(@NotNull Product product)
    {
        return basketService.getQuantity(currentUser.id(), product.getId());
    }


    /**
     * Gets the currently logged-in <code>User</code>
     *
     * @return a <code>User</code> object
     */
    public User getCurrentUser()
    {
        return currentUser;
    }


    public Integer getStockQuantity(@NotNull Product product)
    {
        return productService.getStockQuantity(product.getId());
    }
}
