package ci553.happyshop.client.customer;

import ci553.happyshop.base_mvm.AbstractModel;
import ci553.happyshop.catalogue.*;
import ci553.happyshop.catalogue.DTO.ProductWithCategory;
import ci553.happyshop.client.customer.basket.BasketClient;
import ci553.happyshop.data.repository.CategoryRepository;
import ci553.happyshop.data.repository.ProductRepository;
import ci553.happyshop.domain.service.BasketService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;


/**
 * The CustomerModel is responsible for exposing an Observable productList that is bound to the View by the CustomerController.
 * User search is facilitated by a double-filtered list (Search list -> Category list -> underlying product list).
 * Interfaces with DB with repositories.
 */
public class CustomerModel extends AbstractModel
{
    // TODO GET CURRENTLY LOGGED IN USER
    // This is a mockup of a logged in user that will be used to test basket methods
    Customer PLACEHOLDER = new Customer(1, "PLACEHOLDER", "PLACEHOLDER");


    // Get repository instances
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    // Get service instances
    private final BasketService basketService;


    /**
     * Constructs a new CustomerModel instance that handles data from the DB.
     *
     * @param productRepository  for interacting with the <code>Product</code> table
     * @param categoryRepository for interacting with the <code>Category</code> table
     * @param basketService for interacting with the <code>Basket</code> table
     */
    public CustomerModel(ProductRepository productRepository, CategoryRepository categoryRepository, BasketService basketService)
    {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.basketService = basketService;
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

    // Check basket- if not already in the basket, add it
    // If in the basket, increment quantity by one
    public void addToBasket(@NotNull Product product)
    {
        basketService.addOrUpdateItem(PLACEHOLDER.id(), product.getId(), 1);
        loadProducts(); // List has changed, update card data
    }

    public void removeFromBasket(@NotNull Product product)
    {
        basketService.decreaseOrRemoveItem(PLACEHOLDER.id(), product.getId());
        loadProducts();
    }

    public int getBasketQuantity(@NotNull Product product)
    {
        return basketService.getQuantity(PLACEHOLDER.id(), product.getId());
    }

    /**
     * Runs the <code>start</code> method in <code>BasketClient</code>, hides this view
     */
    public void openBasket(Stage stage)
    {
        try {
            BasketClient basketClient = new BasketClient(PLACEHOLDER);
            stage.hide();   // Hide the customer view

            // Create a new stage for the basket client
            Stage basket = new Stage();
            basket.setOnHidden(event ->
            {
                stage.show();
                loadProducts(); // Load the product list if the basket changed anything
            }); // Re-open the customer view when the basket close
            basket.setOnCloseRequest(event ->
            {
                stage.show();
                loadProducts();
            });

            // Start the basket
            basketClient.start(basket);

        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}
