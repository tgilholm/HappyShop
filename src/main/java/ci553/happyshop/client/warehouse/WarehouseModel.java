package ci553.happyshop.client.warehouse;

import ci553.happyshop.base_mvm.BaseModel;
import ci553.happyshop.catalogue.Category;
import ci553.happyshop.catalogue.DTO.ProductWithCategory;
import ci553.happyshop.catalogue.Product;
import ci553.happyshop.service.CategoryService;
import ci553.happyshop.service.ProductService;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import org.jetbrains.annotations.NotNull;

import java.util.List;


/**
 * The Warehouse model interfaces with the Services to get and set product information
 * It features the same double-filtered list present in the Customer model, albeit
 * with a different card displayed in the TilePane
 */
public class WarehouseModel extends BaseModel
{
    private final ProductService productService;
    private final CategoryService categoryService;

    private final ObservableList<ProductWithCategory> productWithCategoryList = FXCollections.observableArrayList();
    private final ObservableList<Category> categoryList = FXCollections.observableArrayList();
    private FilteredList<ProductWithCategory> searchFilteredList;
    private FilteredList<ProductWithCategory> categoryFilteredList;


    /**
     * Constructs a new WarehouseModel with dependency injection
     *
     * @param productService  a <code>ProductService</code> instance
     * @param categoryService a <code>CategoryService</code> instance
     */
    public WarehouseModel(@NotNull ProductService productService, @NotNull CategoryService categoryService)
    {
        this.productService = productService;
        this.categoryService = categoryService;
    }


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
     * Get the observable error property from the service
     * @return an immutable <code>StringProperty</code>
     */
    public ReadOnlyStringProperty validationErrorProperty()
    {
        return productService.userError();
    }

    public ReadOnlyIntegerProperty productsChangedProperty()
    {
        return productService.productsChanged();
    }

    /**
     * Resets the <code>StringProperty</code> in the productService
     */
    public void resetUserError()
    {
        productService.resetUserError();
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
        getCategoryFilteredList().setPredicate(productWithCategory ->
        {
            if (categoryFilter == null
                    || categoryFilter.trim().isEmpty()
                    || categoryFilter.trim().equalsIgnoreCase("Select Category")) // ignore the "default category"
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
     * Delegates to the productService to delete a product from the database
     *
     * @param product the <code>Product</code> to delete
     */
    public void deleteItem(Product product)
    {
        productService.deleteProduct(product);
    }


    /**
     * Passes the temporary values to the ProductService
     *
     * @param id               the id of the product being updated, as a <code>long</code>
     * @param newName          the new name for the product, as a <code>String</code>
     * @param newImageName     the new name of the product's image, as a <code>String</code>
     * @param newPrice         the new price for the product, as a <code>String</code>
     * @param newStockQuantity the new stock quantity, as a <code>String</code>
     * @param newCategory      the name of a category, as a <code>String</code>
     */
    public void saveChanges(long id, String newName, String newImageName, String newPrice, String newStockQuantity,
            String newCategory)
    {
        // Pass to the productService to be validated
        productService.updateProduct(id, newName, newImageName, newPrice, newStockQuantity, newCategory);
    }
}
