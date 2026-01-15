package ci553.happyshop.catalogue;

import ci553.happyshop.utility.enums.OrderState;

import java.util.ArrayList;


/**
 * Each Order has:
 * <pre>
 *     - a unique final long id (automatically generated when adding to the DB)
 *     - a state in which the order is found (Ordered, In Progress, or Collected)
 *     - the date and time it was placed (taken from the system time when ordered)
 *     - a list of OrderItem objects.
 * </pre>
 *
 * This class is mainly used by OrderHub to create and manage order objects during
 * the order life-cycle (ordered → progressing → collected).
 */
public class Order
{
    private final long id;    // order id
    private final OrderState state;    // Ordered, In Progress or Collected
    private final String dateTime;    // The date the order was placed
    private final ArrayList<OrderItem> orderItems = new ArrayList<>();    // Order contents


    public Order(long id, OrderState state, String dateTime)
    {
        this.id = id;
        this.state = state;
        this.dateTime = dateTime;
    }


    // Getter methods
    public long getId()
    {
        return id;
    }


    public OrderState getState()
    {
        return state;
    }


    public String getDateTime()
    {
        return dateTime;
    }


    public ArrayList<OrderItem> getOrderItems()
    {
        return orderItems;
    }


    /**
     * Overrides the base (Object's) toString method to allow
     * easily logging orders and displaying them.
     * @return a <code>String</code> representation of the <code>Order</code>
     */
    @Override
    public String toString()
    {
        return String.format(
                "ID: %s \nState: %s \nDate&Time: %s\nNo# Products: %d",
                id, state, dateTime, orderItems.size()
        );
    }
}
