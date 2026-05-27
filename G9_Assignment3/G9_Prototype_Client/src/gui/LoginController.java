package gui;

import client.ChatClient;
import client.ClientUI;
import javafx.application.Platform;
import logic.LoginResult;
import logic.Message;
import logic.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * FXML controller for the login screen ({@code LoginFrame.fxml}).
 *
 * <p>two modes toggled by radio buttons: visitor (ID number) and staff (username + password).
 * on success, populates {@link UserSession} and routes to the role-appropriate home screen.
 */
public class LoginController {

    @FXML private RadioButton rbVisitor;
    @FXML private RadioButton rbStaff;
    @FXML private VBox          visitorBox;
    @FXML private TextField     txtIdNumber;
    @FXML private VBox          staffBox;
    @FXML private TextField     txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private Label  lblError;
    @FXML private Button btnLogin;

    /**
     * shows/hides the visitor or staff input section and clears the error label.
     *
     * @param event the radio-button selection event
     */
    @FXML
    public void onModeChange(ActionEvent event) {
        boolean isVisitor = rbVisitor.isSelected();
        visitorBox.setVisible(isVisitor);
        visitorBox.setManaged(isVisitor);
        staffBox.setVisible(!isVisitor);
        staffBox.setManaged(!isVisitor);
        lblError.setText("");
    }

    /**
     * dispatches to {@link #loginAsVisitor()} or {@link #loginAsStaff()}.
     *
     * @param event the button-click event
     */
    @FXML
    public void handleLogin(ActionEvent event) {
        lblError.setText("");
        if (rbVisitor.isSelected()) loginAsVisitor();
        else                        loginAsStaff();
    }

    /**
     * validates the visitor ID field and sends a {@code LOGIN_VISITOR} message.
     */
    private void loginAsVisitor() {
        String id = txtIdNumber.getText().trim();
        if (id.isEmpty()) {
            setError("Please enter your ID number.");
            return;
        }
        if (!id.matches("\\d{5,15}")) {
            setError("ID number must be 5–15 digits.");
            return;
        }
        ChatClient.lastLoginResult = null;
        ChatClient.lastLoginError  = null;
        ClientUI.chat.accept(new Message("LOGIN_VISITOR", id));
        if (ChatClient.lastLoginResult != null) onLoginSuccess();
        else setError(ChatClient.lastLoginError != null
                ? ChatClient.lastLoginError
                : "ID not found. Please check your ID number.");
    }

    /**
     * validates the username and password fields and sends a {@code LOGIN_USER} message.
     */
    private void loginAsStaff() {
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText();
        if (username.isEmpty()) { setError("Please enter your username."); return; }
        if (password.isEmpty()) { setError("Please enter your password.");  return; }
        ChatClient.lastLoginResult = null;
        ChatClient.lastLoginError  = null;
        ClientUI.chat.accept(new Message("LOGIN_USER", new String[]{username, password}));
        if (ChatClient.lastLoginResult != null) onLoginSuccess();
        else setError(ChatClient.lastLoginError != null
                ? ChatClient.lastLoginError
                : "Invalid username or password.");
    }

    /**
     * stores the login result in {@link UserSession} and routes to the role-appropriate home screen.
     */
    private void onLoginSuccess() {
        LoginResult result = ChatClient.lastLoginResult;
        UserSession.set(result);
        Stage stage = (Stage) btnLogin.getScene().getWindow();
        try {
            switch (result.getRole()) {
                case "VISITOR":
                case "GUIDE":
                    new VisitorController().start(stage);
                    break;
                case "PARK_EMPLOYEE":
                case "PARK_MANAGER":
                    new EmployeeController().start(stage);
                    break;
                case "DEPARTMENT_MANAGER":
                case "SERVICE_REP":
                    new AcademicFrameController().start(stage);
                    break;
                default:
                    setError("Unknown role: " + result.getRole() + ". Contact support.");
            }
        } catch (Exception e) {
            setError("Error loading screen: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * disconnects cleanly and exits. the server keeps the row marked "Disconnected".
     *
     * @param event the button-click event
     */
    @FXML
    public void handleExit(ActionEvent event) {
        if (ClientUI.chat != null) ClientUI.chat.disconnect();
        Platform.exit();
    }

    /**
     * opens the registration screen on the same stage.
     *
     * @param event the button-click event
     */
    @FXML
    public void handleSignUp(ActionEvent event) {
        try {
            new RegisterController().start((Stage) btnLogin.getScene().getWindow());
        } catch (Exception e) {
            setError("Error opening registration form: " + e.getMessage());
        }
    }

    /**
     * shows the login-screen help dialog.
     *
     * @param event the mouse-click event
     */
    @FXML
    public void handleHelp(MouseEvent event) {
        showHelp("GoNature — Help",
            "Welcome to GoNature — National Park Reservation System\n\n" +
            "VISITOR / GUIDE LOGIN\n" +
            "  • Enter your government ID number (5–15 digits).\n" +
            "  • Guides are recognised automatically by their registered ID.\n\n" +
            "STAFF LOGIN\n" +
            "  • Select the 'Staff Member' option.\n" +
            "  • Enter your username and password assigned by the system.\n\n" +
            "NEW USER?\n" +
            "  • Click 'Create an Account' to register as a visitor.\n" +
            "  • You can also join the Family Member Club during registration.");
    }

    /**
     * shows a scrollable help popup — reused for any help content on this screen.
     *
     * @param title   the dialog title
     * @param content the help text to display
     */
    private void showHelp(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Help");
        alert.setHeaderText(title);
        TextArea ta = new TextArea(content);
        ta.setEditable(false);
        ta.setWrapText(true);
        ta.setPrefRowCount(14);
        ta.setPrefWidth(440);
        ta.setStyle("-fx-font-family: 'Segoe UI', Arial, sans-serif; -fx-font-size: 12px;");
        alert.getDialogPane().setContent(ta);
        alert.getDialogPane().setMinWidth(480);
        alert.showAndWait();
    }

    /** @param msg the error text to display */
    private void setError(String msg) { lblError.setText(msg); }

    /**
     * @param primaryStage the stage in which to show the login screen
     * @throws Exception if the FXML file cannot be loaded
     */
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/gui/LoginFrame.fxml"));
        primaryStage.setTitle("GoNature — Login");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
