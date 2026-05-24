package Server;

import gui.ServerPortFrameController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.Optional;

public class ServerUI extends Application {

    final public static int DEFAULT_PORT = 5555;

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // --- Password dialog (masked input) ---
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Database Login");
        dialog.setHeaderText("Enter MySQL Password");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        VBox content = new VBox(8, new Label("MySQL Password:"), passwordField);
        content.setPadding(new Insets(10));
        dialog.getDialogPane().setContent(content);

        ButtonType connectBtn = new ButtonType("Connect", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(connectBtn, ButtonType.CANCEL);

        dialog.setResultConverter(btn -> btn == connectBtn ? passwordField.getText() : null);

        Optional<String> result = dialog.showAndWait();
        if (!result.isPresent() || result.get() == null) {
            Platform.exit();
            return;
        }

        DBController.setPassword(result.get());

        // --- Open main server window ---
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