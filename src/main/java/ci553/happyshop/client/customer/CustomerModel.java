package ci553.happyshop.client.customer;

import ci553.happyshop.base_mvm.BaseModel;
import ci553.happyshop.catalogue.*;
import ci553.happyshop.catalogue.DTO.ProductWithCategory;
import ci553.happyshop.data.repository.CategoryRepository;
import ci553.happyshop.data.repository.ProductRepository;
import ci553.happyshop.data.repository.RepositoryFactory;
import ci553.happyshop.domain.service.BasketService;
import ci553.happyshop.domain.service.ProductService;
import ci553.happyshop.domain.service.ServiceFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import org.jetbrains.annotations.NotNull;


/**
 * The CustomerModel is responsible for exposing an Observable productList that is bound to the View by the CustomerController.
 * User search is facilitated by a double-filtered list (Search list -> Category list -> underlying product list).
 * Interfaces with DB with repositories.
 */
public class CustomerModel extends BaseModel
{
    // TODO GET CURRENTLY LOGGED IN USER
    // This is a mockup of a logged in user that will be used to test basket methods
    private final Customer currentCustomer = new Customer(1, "PLACEHOLDER", "PLACEHOLDER");

    // Get repository instances
    private final ProductRepository productRepository = RepositoryFactory.getProductRepository();
    private final CategoryRepository categoryRepository = RepositoryFactory.getCategoryRepository();

    // Get service instances
    private final BasketService basketService = ServiceFactory.getBasketService();
    private final ProductService productService = ServiceFactory.getProductService();


    /**
     * Constructs a new CustomerModel instance that handles data from the DB.
     */
    public CustomerModel()
    {
        // Observe the changeCounter in BasketService and automatically reload the product list
        basketService.basketChanged().addListener((observable, oldValue, newValue) -> loadProducts());
    }


    private final ObservableList<ProductWithCategory> productWithCategoryList = FXCollections.observableArrayList();        // Observable product list
    private final ObservableList<Category> categoryList = FXCollections.observableArrayList();                              // Observable category list
    private FilteredList<ProductWithCategory> searchFilteredList;                                                           // Filtered product list
    private FilteredList<ProductWithCategory> categoryFilteredList;                                                         // Product list filtered by category

    private final String imageName = "images/imageHolder.jpg";                // Image to show in product preview (Search Page)


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

        logger.debug("Retrieved {} products with categories from ProductTable", productWithCategoryList.size());
    }


    /**
     * Updates the <code>categoryList</code> from the <code>productRepository</code>
     */
    public void loadCategories()
    {
        categoryList.setAll(categoryRepository.getAll());

        logger.debug("Retrieved {} categories from CategoryTable", categoryList.size());
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


    /**
     * Delegates to basketService to add or update the quantity of an item
     *
     * @param product the <code>Product</code> object to update
     */
    public void addToBasket(@NotNull Product product)
    {
        basketService.addOrUpdateItem(currentCustomer.id(), product.getId(), 1);
        loadProducts(); // List has changed, update card data
    }


    /**
     * Delegates to basketService to remove or decrease the quantity of an item
     *
     * @param product the <code>Product</code> object to update
     */
    public void removeFromBasket(@NotNull Product product)
    {
        basketService.decreaseOrRemoveItem(currentCustomer.id(), product.getId());
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
        return basketService.getQuantity(currentCustomer.id(), product.getId());
    }


    /**
     * Gets the currently logged-in <code>Customer</code>
     * @return a <code>Customer</code> object
     */
    public Customer getCurrentCustomer()
    {
        return currentCustomer;
    }


    public Integer getStockQuantity(@NotNull Product product)
    {
        return productService.getStockQuantity(product.getId());
    }
}
