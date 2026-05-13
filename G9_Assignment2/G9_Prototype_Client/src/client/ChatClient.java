package client;
import common.ChatIF;
import logic.Message;
import logic.Order;
import ocsf.client.AbstractClient;
import java.io.IOException;
import java.util.List;

public class ChatClient extends AbstractClient {

    ChatIF clientUI;
    public static boolean awaitResponse = false;

    // holds the last order list and last result — GUI reads from here
    public static List<Order> lastOrderList = null;
    public static String lastResult = "";

    public ChatClient(String host, int port, ChatIF clientUI) throws IOException {
        super(host, port);
        this.clientUI = clientUI;
    }

    @Override
    public void handleMessageFromServer(Object msg) {
        awaitResponse = false;
        if (!(msg instanceof Message)) return;
        Message m = (Message) msg;

        if (m.getType().equals("ORDER_LIST")) {
            lastOrderList = (List<Order>) m.getData();
            lastResult = "Loaded " + lastOrderList.size() + " orders.";

        } else if (m.getType().equals("UPDATE_SUCCESS")) {
            lastResult = "Update successful!";

        } else if (m.getType().equals("UPDATE_FAIL")) {
            lastResult = "Update failed.";
        }
    }

    public void handleMessageFromClientUI(Message message) {
        try {
            openConnection();
            awaitResponse = true;
            sendToServer(message);
            while (awaitResponse) {
                try { Thread.sleep(100); } catch (InterruptedException e) { e.printStackTrace(); }
            }
        } catch (IOException e) {
            clientUI.display("Could not send message: " + e);
        }
    }

    public void quit() {
        try { closeConnection(); } catch (IOException e) {}
        System.exit(0);
    }
}