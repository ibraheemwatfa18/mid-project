package logic;

import java.io.Serializable;

/**
 * Wraps every message sent between client and server.
 * The 'type' field tells the receiver what to do.
 * The 'data' field carries the payload (an Order, a String, etc.)
 *
 * Message types used in this system:
 *   "GET_ALL_ORDERS"   - client asks server to fetch all orders from DB
 *   "ORDER_LIST"       - server sends back a list of orders
 *   "UPDATE_ORDER"     - client asks server to update order_date & num_visitors
 *   "UPDATE_SUCCESS"   - server confirms update succeeded
 *   "UPDATE_FAIL"      - server reports update failed
 *   "ERROR"            - generic error message (data = error string)
 */
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    private String type;
    private Object data;

    public Message(String type, Object data) {
        this.type = type;
        this.data = data;
    }

    public String getType() { return type; }
    public Object getData() { return data; }

    @Override
    public String toString() {
        return "Message[type=" + type + ", data=" + data + "]";
    }
}
