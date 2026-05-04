package client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.util.ArrayList;

public class ClientUI extends Application {
    
    SimpleClient client;
    Stage window;
    String currentStudentId = "";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;
        
        try {
            client = new SimpleClient("localhost", 5555, this);
        } catch(Exception e) {
            System.out.println("Could not connect to server");
        }

        showAcademicFrame(); // Start with the ID screen
    }

    // --- SCREEN 1: Academic Management Tool (Enter ID) ---
    public void showAcademicFrame() {
        window.setTitle("Academic Management Tool");
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(50, 40, 50, 40));
        grid.setVgap(20);
        grid.setHgap(15);
        grid.setStyle("-fx-background-color: #dcb88c;");

        TextField txtId = new TextField();
        Button btnSend = new Button("Send");
        btnSend.setStyle("-fx-background-color: lightblue; -fx-text-fill: red; -fx-font-weight: bold;");
        Button btnExit = new Button("Exit");

        grid.add(new Label("Student ID:"), 0, 0);
        grid.add(txtId, 1, 0);
        grid.add(btnSend, 1, 1);
        grid.add(btnExit, 1, 2);

        btnExit.setOnAction(e -> System.exit(0));
        
        btnSend.setOnAction(e -> {
            if (!txtId.getText().trim().isEmpty()) {
                currentStudentId = txtId.getText();
                showStudentForm(); // Switch to the form screen
            }
        });

        window.setScene(new Scene(grid, 300, 250));
        window.show();
    }

    // --- SCREEN 2: Student Form (Enter Details & Save) ---
    public void showStudentForm() {
        window.setTitle("Student Form");
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(30, 20, 30, 20));
        grid.setVgap(15);
        grid.setHgap(10);
        grid.setStyle("-fx-background-color: #dcb88c;");

        TextField txtIdDisplay = new TextField(currentStudentId);
        txtIdDisplay.setEditable(false); 
        TextField txtName = new TextField();
        TextField txtSurname = new TextField();
        ComboBox<String> cmbFaculty = new ComboBox<>(FXCollections.observableArrayList("SE", "IE", "ME"));
        cmbFaculty.setValue("SE");

        Button btnSave = new Button("Save");
        btnSave.setStyle("-fx-background-color: blue; -fx-text-fill: red; -fx-font-weight: bold;");
        Button btnClose = new Button("Close");
        btnClose.setStyle("-fx-background-color: blue; -fx-text-fill: red; -fx-font-weight: bold;");

        grid.add(new Label("St. ID:"), 0, 0);
        grid.add(txtIdDisplay, 1, 0);
        grid.add(new Label("Name:"), 0, 1);
        grid.add(txtName, 1, 1);
        grid.add(new Label("Surname:"), 0, 2);
        grid.add(txtSurname, 1, 2);
        grid.add(new Label("Faculty:"), 0, 3);
        grid.add(cmbFaculty, 1, 3);
        grid.add(btnSave, 0, 4);
        grid.add(btnClose, 1, 4);

        btnClose.setOnAction(e -> showAcademicFrame()); // Go back to Screen 1

        btnSave.setOnAction(e -> {
            ArrayList<String> data = new ArrayList<>();
            data.add("send"); // Trigger word
            data.add(txtIdDisplay.getText());
            data.add(txtName.getText());
            data.add(txtSurname.getText());
            data.add(cmbFaculty.getValue());
            
            if (client != null) {
                client.handleMessageFromClientUI(data);
            }
        });

        window.setScene(new Scene(grid, 300, 300));
    }

    // --- Pop-up for Server Response ---
    public void displayMessage(String msg) {
        System.out.println(msg);
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Server Response");
            alert.setHeaderText(null);
            alert.setContentText(msg);
            alert.showAndWait();
        });
    }
}