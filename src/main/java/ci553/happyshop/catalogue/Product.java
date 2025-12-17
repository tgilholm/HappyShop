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

// todo convert primary key to Long
public class Product implements Comparable<Product>
{
	private final long ID;
	private final String name;
	private final String imageName;
	private final double unitPrice;
	private final int stockQuantity;
	private final long categoryID;

	/**
	 * Constructor used by DatabaseRW to make a product by searching ResultSet
	 * @param id: Product ID
	 * @param des: Description of product
	 * @param image: Filename & file-type
	 * @param aPrice: The price of the product
	 * @param stockQuantity: The Quantity of the product in stock
	 */
	public Product(long ID, String name, String imageName, double unitPrice, int stockQuantity, long categoryID)
	{
		this.ID = ID;
		this.name = name;
		this.imageName = imageName;
		this.unitPrice = unitPrice;
		this.stockQuantity = stockQuantity;
		this.categoryID = categoryID;
	}

	// Getter methods
	public long getId()
	{
		return ID;
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

	public long getCategoryId() {return categoryID;}

//	// Sets the quantity ordered
//	public void setOrderedQuantity(int _orderedQuantity)
//	{
//		this.orderedQuantity = _orderedQuantity;
//	}

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


}
