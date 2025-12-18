package ci553.happyshop.client.customer;

import ci553.happyshop.catalogue.Order;
import ci553.happyshop.catalogue.Product;
import ci553.happyshop.storageAccess.DatabaseRW;
import ci553.happyshop.orderManagement.OrderHub;
import ci553.happyshop.utility.StorageLocation;
import ci553.happyshop.utility.ProductListFormatter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// TODO update ProductCell layout with add product, num remaining & other details


/**
 * The CustomerModel is responsible for exposing an Observable productList that is bound to the View by the CustomerController.
 * User search is facilitated by a FilteredList
 */
public class CustomerModel
{
    private final DatabaseRW databaseRW;


    /**
     * Constructs a new CustomerModel instance that handles data from the DB.
     *
     * @param databaseRW for interacting with the database
     */
    public CustomerModel(DatabaseRW databaseRW)
    {
        this.databaseRW = databaseRW;
    }


    private final ObservableList<Product> productList = FXCollections.observableArrayList();        // Observable product list
    private FilteredList<Product> searchFilteredList;                                                // Filtered product list
    private FilteredList<Product> categoryFilteredList;                                             // Product list filtered by category


    private Product theProduct = null; // product found from search
    private ArrayList<Product> trolley = new ArrayList<>(); // a list of products in trolley

    // Four UI elements to be passed to CustomerView for display updates.
    private String imageName = "images/imageHolder.jpg";                // Image to show in product preview (Search Page)
    private String displayLaSearchResult = "No Product was searched yet"; // Label showing search result message (Search Page)
    private String displayTaTrolley = "";                                // Text area content showing current trolley items (Trolley Page)
    private String displayTaReceipt = "";                                // Text area content showing receipt after checkout (Receipt Page)


    /**
     * Exposes an ObservableList version of the product list. If the productList changes, observers will be triggered.
     *
     * @return <code>productList</code>
     */
    public ObservableList<Product> getProducts()
    {
        return productList;
    }

    /**
     * Queries the DB to get the current list of products.
     */
    public void loadProducts()
    {
        // Use setAll to update the productList
        productList.setAll(databaseRW.getAll());
    }

    // TODO implement categories

    /**
     * Gets the list of products matching the specified category. Defaults to the base <code>productList</code>.
     * Creates a new <code>filteredList</code> if <code>categoryFilteredList</code> doesn't already exist, returns the existing list
     * otherwise.
     *
     * @return the <code>FilteredList</code> of products matching the category filter
     */
    public FilteredList<Product> getCategoryFilteredList()
    {
        //
        // Defaults to "no category"
        if (categoryFilteredList == null)
        {
            // productList is passed to the filteredList
            categoryFilteredList = new FilteredList<>(productList, p -> true);
        }
        return categoryFilteredList;
    }

    /**
     * Gets the (already filtered by category) list of products matching the search filter.
     * Searches in product description and ID. Creates a new <code>filteredList</code> if
     * <code>searcFilteredList</code> doesn't already exist, returns the existing list otherwise.
     *
     * @return the <code>FilteredList</code> of products matching the filter
     */
    public FilteredList<Product> getSearchFilteredList()
    {
        // Creates filteredProducts if it doesn't exist
        if (searchFilteredList == null)
        {
            // categoryFilteredList is passed
            searchFilteredList = new FilteredList<>(productList, p -> true);
        }
        return searchFilteredList;
    }

    // todo category searching
    //public void setCategoryFilter()

    /**
     * Updates the predicate of the searchFilteredList to search by product ID or description and
     * return the products that match the predicate <code>searchFilter</code>.
     *
     * @param searchFilter a <code>String</code> literal matching either the product ID or description
     */
    public void setSearchFilter(String searchFilter)
    {
        // Update the predicate of filteredList
        searchFilteredList.setPredicate(product ->
        {
            if (searchFilter == null)
            {
                return true;
            } else
            {
                // Return true if the ID or description match the search filter
                String lowercaseSearchFilter = searchFilter.toLowerCase();
                return product.getId().toLowerCase().contains(searchFilter)
                        || product.getProductDescription().toLowerCase().contains(searchFilter);
            }
        });
    }


    // Appends a product to the end of the trolley arrayList
    void addToTrolley()
    {
        if (theProduct != null)
        {
            // When a product is added to the trolley, it is grouped with any other products with the same IDs
            // The trolley is then sorted in-place.
            trolley.add(theProduct);
            trolley = groupProductsById(trolley);
            trolley.sort(null);
            System.out.println(String.format("Trolley contents: %s", getTrolley()));

            displayTaTrolley = ProductListFormatter.buildString(trolley); //build a String for trolley so that we can show it
        } else
        {
            displayLaSearchResult = "Please search for an available product before adding it to the trolley";
            System.out.println("must search and get an available product before add to trolley");
        }
        displayTaReceipt = ""; // Clear receipt to switch back to trolleyPage (receipt shows only when not empty)
    }

    // Called when a user wants to finish adding items and pay
    void checkOut() throws IOException, SQLException
    {
        if (!trolley.isEmpty())
        {
            // Group the products in the trolley by productId to optimise stock checking
            // Check the database for sufficient stock for all products in the trolley.
            // If any products are insufficient, the update will be rolled back.
            // If all products are sufficient, the database will be updated, and insufficientProducts will be empty.
            // Note: If the trolley is already organised (merged and sorted), grouping is unnecessary.
            ArrayList<Product> groupedTrolley = groupProductsById(trolley);
            ArrayList<Product> insufficientProducts = databaseRW.purchaseStocks(groupedTrolley);

            if (insufficientProducts.isEmpty())
            { // If stock is sufficient for all products, tell OrderHub to add a new order
                OrderHub orderHub = OrderHub.getOrderHub();
                Order theOrder = orderHub.newOrder(trolley);
                trolley.clear();
                displayTaTrolley = "";
                // Clear the trolley, and output a receipt of items ordered
                displayTaReceipt = String.format("Order_ID: %s\nOrdered_Date_Time: %s\n%s", theOrder.getOrderId(),
                        theOrder.getOrderedDateTime(), ProductListFormatter.buildString(theOrder.getProductList()));
                System.out.println(displayTaReceipt);
            } else
            { // Some products have insufficient stock — build an error message to inform the customer
                StringBuilder errorMsg = new StringBuilder();
                for (Product p : insufficientProducts)
                {
                    errorMsg.append("\u2022 " + p.getProductId()).append(", ").append(p.getProductDescription())
                            .append(" (Only ").append(p.getStockQuantity()).append(" available, ")
                            .append(p.getOrderedQuantity()).append(" requested)\n");
                }
                theProduct = null;

                //TODO
                // Add the following logic here:
                // 1. Remove products with insufficient stock from the trolley.
                // 2. Trigger a message window to notify the customer about the insufficient stock, rather than directly changing displayLaSearchResult.
                //You can use the provided RemoveProductNotifier class and its showRemovalMsg method for this purpose.
                //remember close the message window where appropriate (using method closeNotifierWindow() of RemoveProductNotifier class)
                displayLaSearchResult = "Checkout failed due to insufficient stock for the following products:\n"
                        + errorMsg.toString();
                System.out.println("stock is not enough");
            }
        } else
        {
            displayTaTrolley = "Your trolley is empty";
            System.out.println("Your trolley is empty");
        }
    }

    /**
     * Groups products by their productId to optimise database queries and updates.
     * By grouping products, we can check the stock for a given `productId` once, rather than repeatedly
     */
    private ArrayList<Product> groupProductsById(ArrayList<Product> proList)
    {
        // HashMaps can only contain one instance of an object
        Map<String, Product> grouped = new HashMap<>();

        // Iterates through the list of products
        for (Product p : proList)
        {
            // For each item in the list, check whether the HashMap already contains it.
            String id = p.getProductId();
            if (grouped.containsKey(id))
            {
                System.out.println(String.format("New product quantity: %s", p.getOrderedQuantity()));
                // If it does, get the product corresponding to the ID and increment its orderedQuantity by the orderedQuantity of the new item.
                Product existing = grouped.get(id);
                System.out.println(String.format("Existing product quantity: %s", existing.getOrderedQuantity()));
                existing.setOrderedQuantity(existing.getOrderedQuantity() + p.getOrderedQuantity());

            } else
            {
                // If the product does not exist in the hash map, add it
                Product newProduct = new Product(p.getProductId(), p.getProductDescription(), p.getProductImageName(),
                        p.getUnitPrice(), p.getStockQuantity());

                // When creating a new instance of the Product object, the OrderedQuantity is not set in the constructor- this has to be done separately
                // This means that when another item is added to the trolley, it will carry over the orderedQuantity.
                newProduct.setOrderedQuantity(p.getOrderedQuantity());
                grouped.put(id, newProduct);
            }
        }
        return new ArrayList<>(grouped.values());
    }

    void cancel()
    {
        trolley.clear();
        displayTaTrolley = "";
    }

    void closeReceipt()
    {

        displayTaReceipt = "";
    }

    // extra notes:
    //Path.toUri(): Converts a Path object (a file or a directory path) to a URI object.
    //File.toURI(): Converts a File object (a file on the filesystem) to a URI object

    //for test only
    public ArrayList<Product> getTrolley()
    {
        return trolley;
    }
}
