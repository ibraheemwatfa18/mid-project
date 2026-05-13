package client;
import common.ChatIF;
import logic.Message;
import java.io.IOException;

public class ClientController implements ChatIF {

    public static int DEFAULT_PORT = 5555;
    ChatClient client;

    public ClientController(String host, int port) {
        try {
            client = new ChatClient(host, port, this);
        } catch (IOException e) {
            System.out.println("Error: Can't setup connection! " + e);
            System.exit(1);
        }
    }

    // called by the GUI with a Message object
    public void accept(Message msg) {
        client.handleMessageFromClientUI(msg);
    }

    // required by ChatIF interface
    @Override
    public void display(String message) {
        System.out.println("> " + message);
    }
}