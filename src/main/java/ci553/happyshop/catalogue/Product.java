package ci553.happyshop.catalogue;

/**
 * The Product class holds information about each product:
 *
 * Fields:
 * - productId: Unique identifier for the product (e.g. 0001).
 * - description: Text description of the product.
 * - unitPrice: Price per unit
 * - orderedQuantity: Quantity requested.
 * - stockQuantity: Quantity currently available in stock.
 */

public class Product implements Comparable<Product>
{
	private String proId;
	private String proDescription;
	private String proImageName;
	private double unitPrice;
	private int orderedQuantity = 1; //The quantity of this product in the customer's order.
	private int stockQuantity;

	/**
	 * Constructor used by DatabaseRW to make a product by searching ResultSet
	 * @param id: Product ID
	 * @param des: Description of product
	 * @param image: Filename & file-type
	 * @param aPrice: The price of the product
	 * @param stockQuantity: The Quantity of the product in stock
	 */
	public Product(String id, String des, String image, double aPrice, int stockQuantity)
	{
		proId = id;
		proDescription = des;
		proImageName = image;
		unitPrice = aPrice;
		this.stockQuantity = stockQuantity;
	}

	// Getter methods
	public String getProductId()
	{
		return proId;
	}

	public String getProductDescription()
	{
		return proDescription;
	}

	public String getProductImageName()
	{
		return proImageName;
	}

	public double getUnitPrice()
	{
		return unitPrice;
	}

	public int getOrderedQuantity()
	{
		return orderedQuantity;
	}

	public int getStockQuantity()
	{
		return stockQuantity;
	}

	// Sets the quantity ordered
	public void setOrderedQuantity(int orderedQuantity)
	{
		this.orderedQuantity = orderedQuantity;
	}

	// Compares this product by ID with any other product
	@Override
	public int compareTo(Product otherProduct)
	{
		return this.proId.compareTo(otherProduct.proId); // Sort by proID alphabetically (ascending order);
	}

	@Override
	// Creates a formatted string containing ID, price (2 d.p.), stock amount, and description
	// Used in the Warehouse search page to display searched product information
	public String toString()
	{
		String productInfo = String.format("Id: %s, £%.2f/uint, stock: %d \n%s", proId, unitPrice, stockQuantity,
				proDescription);
		return productInfo;
	}

	/** alternative constructors retained for possible future use.
	 *
	public Product(String id, String des, double aPrice, int orderedQuantity, int stockQuantity) {
	    proId = id;
	    proDescription = des;
	    unitPrice = aPrice;
	    this.orderedQuantity = orderedQuantity;
	    this.stockQuantity = stockQuantity;
	}
	
	public Product(String id, String des, double aPrice, int orderedQuantity) {
	    proId = id;
	    proDescription = des;
	    unitPrice = aPrice;
	    this.orderedQuantity = orderedQuantity;
	}
	 */

}
