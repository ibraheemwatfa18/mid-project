package client;

import ocsf.client.AbstractClient;

public class SimpleClient extends AbstractClient {

    ClientUI clientUI;

    public SimpleClient(String host, int port, ClientUI ui) throws java.io.IOException {
        super(host, port);
        this.clientUI = ui;
        openConnection();
    }

    @Override
    protected void handleMessageFromServer(Object msg) {
        clientUI.displayMessage(msg.toString());
    }

    public void handleMessageFromClientUI(Object message) {
        try {
            sendToServer(message);
        } catch (Exception e) {
            System.out.println("Could not send message to server.");
        }
    }
}