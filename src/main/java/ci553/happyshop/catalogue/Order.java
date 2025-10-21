package ci553.happyshop.catalogue;

import ci553.happyshop.orderManagement.OrderState;
import ci553.happyshop.utility.ProductListFormatter;

import java.util.ArrayList;

/**
 * The Order class represents a customer order, including metadata and a list of ordered products.
 *
 * Responsibilities:
 * - Stores information about an order, including order ID, current order state, time-stamps, and the list of products.
 * - Provides getter methods for order attributes and allows updating the order state.
 * - Formats the full order details for writing to a file, including time-stamps and item list.
 *
 * An order file example:
 * Order ID: 10
 * State: Ordered
 * OrderedDateTime: 2025-05-03 16:52:24
 * ProgressingDateTime:
 * CollectedDateTime:
 * Items:
 *  0002    DAB Radio          ( 1) £  29.99
 *  0004    Watch              ( 1) £  29.99
 *  0007    USB drive          ( 1) £   6.99
 * --------------------------------------------
 *  Total                               £  66.97
 *
 * This class is mainly used by OrderHub to create and manage order objects during
 * the order life-cycle (ordered → progressing → collected).
 */

public class Order
{
	private int orderId;	// Uniquely identifies each order
	private OrderState state; // Which of the three states (Ordered, Progressing & Collected)
	private String orderedDateTime = "";
	private String progressingDateTime = "";
	private String collectedDateTime = "";
	private ArrayList<Product> productList = new ArrayList<>(); // Contents of the trolley

	// Constructor used by OrderHub to create a new order for a customer.
	// Initialises the order with an ID, state, order date/time, and a list of ordered products.
	public Order(int orderId, OrderState state, String orderedDateTime, ArrayList<Product> productList)
	{
		this.orderId = orderId;
		this.state = state;
		this.orderedDateTime = orderedDateTime;
		this.productList = new ArrayList<>(productList);
	}

	// Getter methods
	public int getOrderId()
	{
		return orderId;
	}

	public OrderState getState()
	{
		return state;
	}

	public String getOrderedDateTime()
	{
		return orderedDateTime;
	}

	public ArrayList<Product> getProductList()
	{
		return productList;
	}

	public void setState(OrderState state)
	{
		this.state = state;
	}

	/**
	 * 	Arranges the order into a readable format containing:
	 *  - Order metadata (ID, state, and time-stamps)
	 *  - Product details included in the order
	 */
	public String orderDetails()
	{
		return String.format(
				"Order ID: %s \n" + "State: %s \n" + "OrderedDateTime: %s \n" + "ProgressingDateTime: %s \n"
						+ "CollectedDateTime: %s\n" + "Items:\n%s",
				orderId, state, orderedDateTime, progressingDateTime, collectedDateTime,
				ProductListFormatter.buildString(productList));
	}
}
