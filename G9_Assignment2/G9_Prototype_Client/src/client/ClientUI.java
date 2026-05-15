package client;

import javafx.application.Application;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import gui.AcademicFrameController;
import java.util.Optional;

public class ClientUI extends Application {
    public static ClientController chat;

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        TextInputDialog dialog = new TextInputDialog("localhost");
        dialog.setTitle("Connect to Server");
        dialog.setHeaderText("Enter Server IP Address");
        dialog.setContentText("IP:");

        Optional<String> result = dialog.showAndWait();
        String host = result.orElse("localhost");

        chat = new ClientController(host, 5555);
        AcademicFrameController frame = new AcademicFrameController();
        frame.start(primaryStage);
    }
}
