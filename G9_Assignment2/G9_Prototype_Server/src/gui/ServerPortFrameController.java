package gui;

import Server.ServerUI;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ServerPortFrameController {

    @FXML private TextField portxt;
    @FXML private TextArea  txtLog;
    // CHANGED: TableView instead of 3 Labels
    @FXML private TableView<String[]> clientTable;
    @FXML private TableColumn<String[], String> colIP;
    @FXML private TableColumn<String[], String> colHost;
    @FXML private TableColumn<String[], String> colStatus;

    private static ServerPortFrameController instance;
    private static ObservableList<String[]> clientList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        instance = this;
        portxt.setText("5555");

        // Set up table columns to read from String array
        colIP.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[0]));
        colHost.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[1]));
        colStatus.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[2]));

        clientTable.setItems(clientList);
    }

    // NEW: adds a row to the table for each connected client
    public static void addClient(String ip, String host) {
        if (instance == null) return;
        Platform.runLater(() -> {
            clientList.add(new String[]{ip, host, "Connected"});
            instance.txtLog.appendText("Client connected: " + ip + " (" + host + ")\n");
        });
    }

    // NEW: finds client by IP and changes status to "Disconnected"
    public static void removeClient(String ip) {
        if (instance == null) return;
        Platform.runLater(() -> {
            for (int i = 0; i < clientList.size(); i++) {
                if (clientList.get(i)[0].equals(ip)) {
                    clientList.get(i)[2] = "Disconnected";
                    instance.clientTable.refresh();
                    instance.txtLog.appendText("Client disconnected: " + ip + "\n");
                    break;
                }
            }
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
