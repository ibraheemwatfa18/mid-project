package gui;

import client.ChatClient;
import client.ClientUI;
import logic.Message;
import logic.Order;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class AcademicFrameController {

    @FXML private TableView<Order>           tableOrders;
    @FXML private TableColumn<Order,Integer> colNum;
    @FXML private TableColumn<Order,String>  colDate;
    @FXML private TableColumn<Order,Integer> colVisitors;
    @FXML private TableColumn<Order,Integer> colCode;
    @FXML private TableColumn<Order,Integer> colSub;
    @FXML private TableColumn<Order,String>  colPlaced;
    @FXML private TextField txtOrderNum;
    @FXML private TextField txtNewDate;
    @FXML private TextField txtNewVisitors;
    @FXML private Label lblResult;

    @FXML
    public void initialize() {
        colNum.setCellValueFactory(d -> new javafx.beans.property.SimpleIntegerProperty(d.getValue().getOrderNumber()).asObject());
        colDate.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getOrderDate()));
        colVisitors.setCellValueFactory(d -> new javafx.beans.property.SimpleIntegerProperty(d.getValue().getNumberOfVisitors()).asObject());
        colCode.setCellValueFactory(d -> new javafx.beans.property.SimpleIntegerProperty(d.getValue().getConfirmationCode()).asObject());
        colSub.setCellValueFactory(d -> new javafx.beans.property.SimpleIntegerProperty(d.getValue().getSubscriberId()).asObject());
        colPlaced.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getDateOfPlacingOrder()));
    }

    public void loadOrders(ActionEvent event) {
        ClientUI.chat.accept(new Message("GET_ALL_ORDERS", null));
        List<Order> orders = ChatClient.lastOrderList;
        if (orders != null) {
            tableOrders.getItems().setAll(orders);
            lblResult.setText("Loaded " + orders.size() + " orders.");
        }
    }

    public void updateOrder(ActionEvent event) {
        try {
            // Validate order number is not empty
            String orderNumText = txtOrderNum.getText().trim();
            if (orderNumText.isEmpty()) {
                lblResult.setText("Please enter an order number.");
                return;
            }
            int num = Integer.parseInt(orderNumText);

            // NEW: Validate date format AND must be a future date
            String date = txtNewDate.getText().trim();
            if (date.isEmpty()) {
                lblResult.setText("Please enter a date.");
                return;
            }
            try {
                LocalDate newDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                if (!newDate.isAfter(LocalDate.now())) {
                    lblResult.setText("Error: Date must be a future date!");
                    return;
                }
            } catch (DateTimeParseException e) {
                lblResult.setText("Error: Invalid date format! Use YYYY-MM-DD.");
                return;
            }

            // NEW: Validate visitors must be between 1 and 15
            String visText = txtNewVisitors.getText().trim();
            if (visText.isEmpty()) {
                lblResult.setText("Please enter number of visitors.");
                return;
            }
            int vis = Integer.parseInt(visText);
            if (vis < 1 || vis > 15) {
                lblResult.setText("Error: Visitors must be between 1 and 15!");
                return;
            }

            // All validations passed — send update to server
            Order o = new Order(num, date, vis, 0, 0, "");
            ClientUI.chat.accept(new Message("UPDATE_ORDER", o));
            lblResult.setText(ChatClient.lastResult);

        } catch (NumberFormatException e) {
            lblResult.setText("Please enter valid numbers.");
        }
    }

    public void getExitBtn(ActionEvent event) { System.exit(0); }

    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/gui/AcademicFrame.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setTitle("G9 — Client");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
