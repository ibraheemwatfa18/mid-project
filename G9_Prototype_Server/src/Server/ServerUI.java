package Server;

import gui.ServerPortFrameController;
import javafx.application.Application;
import javafx.stage.Stage;

public class ServerUI extends Application {

    final public static int DEFAULT_PORT = 5555;

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        ServerPortFrameController frame = new ServerPortFrameController();
        frame.start(primaryStage);
    }

    public static void runServer(String p) {
        int port = 0;
        try {
            port = Integer.parseInt(p);
        } catch (Throwable t) {
            System.out.println("ERROR - Could not parse port!");
            return;
        }
        final int finalPort = port;
        new Thread(() -> {
            EchoServer sv = new EchoServer(finalPort);
            try {
                sv.listen();
            } catch (Exception ex) {
                System.out.println("ERROR - Could not listen for clients! " + ex.getMessage());
            }
        }).start();
    }
}