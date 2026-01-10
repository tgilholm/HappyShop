package ci553.happyshop.catalogue;

import org.jetbrains.annotations.NotNull;

/**
 * Holds information about a product. Implements comparable to define methods for
 * comparing products to one another.
 */
public class Product implements Comparable<Product>
{
    private final long id;      // Only ID is final (unchangeable)
    private String name;        // Name, price, qty, etc. can be changed
    private String imageName;
    private double unitPrice;
    private int stockQuantity;
    private long categoryID;


    /**
     * Constructor to create a new <code>Product</code> object
     *
     * @param id            <code>long</code> unique product id
     * @param name          <code>String</code> product name
     * @param imageName     <code>String</code> image filename
     * @param unitPrice     <code>double</code> price per product
     * @param stockQuantity <code>int</code> number of products in stock
     * @param categoryID    <code>long</code> category foreign key
     */
    public Product(long id, String name, String imageName, double unitPrice, int stockQuantity, long categoryID)
    {
        this.id = id;
        this.name = name;
        this.imageName = imageName;
        this.unitPrice = unitPrice;
        this.stockQuantity = stockQuantity;
        this.categoryID = categoryID;
    }


    // Getter methods
    public long getId()
    {
        return id;
    }


    public String getName()
    {
        return name;
    }


    public String getImageName()
    {
        return imageName;
    }


    public double getUnitPrice()
    {
        return unitPrice;
    }


    public int getStockQuantity()
    {
        return stockQuantity;
    }


    public long getCategoryId()
    {
        return categoryID;
    }


    // Setter methods
    public void setStockQuantity(int stockQuantity)
    {
        this.stockQuantity = stockQuantity;
    }

    public void setName(String name)
    {
        this.name = name;
    }


    public void setImageName(String imageName)
    {
        this.imageName = imageName;
    }


    public void setCategoryID(long categoryID)
    {
        this.categoryID = categoryID;
    }


    public void setUnitPrice(double unitPrice)
    {
        this.unitPrice = unitPrice;
    }


    /**
     * Compares this <code>Product</code> object with another
     *
     * @param otherProduct the object to be compared.
     * @return -1 if less than the other product, 1 if greater than it, 0 if equal.
     */
    @Override
    public int compareTo(@NotNull Product otherProduct)
    {
        if (this.getId() > otherProduct.getId())
        {
            return 1;       // Return 1 if greater than otherProduct
        } else if (this.getId() < otherProduct.getId())
        {
            return -1;      // Return -1 if less than otherProduct
        }
        return 0; // Return 0 if equal
    }


    @Override

    public String toString()
    {
        return String.format("Id: %s, £%.2f/uint, stock: %d \n%s", id, unitPrice, stockQuantity,
                name);
    }


}
