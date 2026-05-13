package Server;
import logic.Message;
import logic.Order;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;
import java.util.List;
import gui.ServerPortFrameController;

public class EchoServer extends AbstractServer {

    public EchoServer(int port) {
        super(port);
    }

    @Override
    public void handleMessageFromClient(Object msg, ConnectionToClient client) {
        System.out.println("Message received from client: " + client);

        if (!(msg instanceof Message)) return;
        Message incoming = (Message) msg;

        try {
            if (incoming.getType().equals("GET_ALL_ORDERS")) {
                List<Order> orders = DBController.getInstance().getAllOrders();
                client.sendToClient(new Message("ORDER_LIST", orders));

            } else if (incoming.getType().equals("UPDATE_ORDER")) {
                Order o = (Order) incoming.getData();
                boolean ok = DBController.getInstance()
                    .updateOrder(o.getOrderNumber(), o.getOrderDate(), o.getNumberOfVisitors());
                client.sendToClient(new Message(ok ? "UPDATE_SUCCESS" : "UPDATE_FAIL", null));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void serverStarted() {
        System.out.println("Server listening on port " + getPort());
        DBController.getInstance().connect();
    }

    @Override
    protected void serverStopped() {
        System.out.println("Server stopped.");
    }

    @Override
    protected void clientConnected(ConnectionToClient client) {
        String ip   = client.getInetAddress().getHostAddress();
        String host = client.getInetAddress().getHostName();
        System.out.println("Client connected | IP: " + ip + " | Host: " + host);
        // Update the server GUI
        ServerPortFrameController.setClientInfo(ip, host, "Connected");
    }

    @Override
    protected synchronized void clientDisconnected(ConnectionToClient client) {
        System.out.println("Client disconnected.");
        ServerPortFrameController.setClientInfo("-", "-", "Disconnected");
    }
}