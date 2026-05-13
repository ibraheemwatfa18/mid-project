package gui;

import Server.ServerUI;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ServerPortFrameController {

    @FXML private TextField portxt;
    @FXML private TextArea  txtLog;
    @FXML private Label     lblIP;
    @FXML private Label     lblHost;
    @FXML private Label     lblStatus;

    private static ServerPortFrameController instance;

    @FXML
    public void initialize() {
        instance = this;
        portxt.setText("5555");
        lblStatus.setText("Not connected");
    }

    public static void setClientInfo(String ip, String host, String status) {
        if (instance == null) return;
        Platform.runLater(() -> {
            instance.lblIP.setText(ip);
            instance.lblHost.setText(host);
            instance.lblStatus.setText(status);
            instance.txtLog.appendText("Client: " + ip + " (" + host + ") — " + status + "\n");
        });
    }

    public void Done(ActionEvent event) throws Exception {
        String p = portxt.getText().trim();
        if (p.isEmpty()) { System.out.println("Enter a port!"); return; }
        ServerUI.runServer(p);
    }

    public void getExitBtn(ActionEvent event) {
        System.exit(0);
    }

    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/gui/ServerPort.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setTitle("G9 — Server");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}