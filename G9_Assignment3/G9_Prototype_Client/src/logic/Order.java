package logic;

import java.io.Serializable;

/**
 * Represents a park visit order.
 * Implements Serializable so it can be sent over the network via OCSF.
 */
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    private int orderNumber;
    private String orderDate;           // stored as String "YYYY-MM-DD"
    private int numberOfVisitors;
    private int confirmationCode;
    private int subscriberId;
    private String dateOfPlacingOrder;  // stored as String "YYYY-MM-DD"

    public Order(int orderNumber, String orderDate, int numberOfVisitors,
                 int confirmationCode, int subscriberId, String dateOfPlacingOrder) {
        this.orderNumber = orderNumber;
        this.orderDate = orderDate;
        this.numberOfVisitors = numberOfVisitors;
        this.confirmationCode = confirmationCode;
        this.subscriberId = subscriberId;
        this.dateOfPlacingOrder = dateOfPlacingOrder;
    }

    // Getters
    public int getOrderNumber()         { return orderNumber; }
    public String getOrderDate()        { return orderDate; }
    public int getNumberOfVisitors()    { return numberOfVisitors; }
    public int getConfirmationCode()    { return confirmationCode; }
    public int getSubscriberId()        { return subscriberId; }
    public String getDateOfPlacingOrder() { return dateOfPlacingOrder; }

    // Setters (only the two fields we allow updating)
    public void setOrderDate(String orderDate)               { this.orderDate = orderDate; }
    public void setNumberOfVisitors(int numberOfVisitors)    { this.numberOfVisitors = numberOfVisitors; }

    @Override
    public String toString() {
        return "Order #" + orderNumber +
               " | Date: " + orderDate +
               " | Visitors: " + numberOfVisitors +
               " | ConfCode: " + confirmationCode +
               " | SubID: " + subscriberId +
               " | Placed: " + dateOfPlacingOrder;
    }
}
