package server;

import java.util.ArrayList;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;

public class EchoServer extends AbstractServer {

    public EchoServer(int port) {
        super(port);
    }

    @Override
    protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
        System.out.println("Message received from client: " + msg);

        if (msg instanceof ArrayList) {
            @SuppressWarnings("unchecked")
            ArrayList<String> data = (ArrayList<String>) msg;

            if (!data.isEmpty() && data.get(0).equals("send")) {
                String response = DBController.parsingTheData(data);
                
                try {
                    client.sendToClient(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void serverStarted() {
        System.out.println("Server listening for connections on port " + getPort());
        DBController.connectToDB();
    }

    @Override
    protected void serverStopped() {
        System.out.println("Server has stopped listening for connections.");
    }
}