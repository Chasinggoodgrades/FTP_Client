package fxmlControllers;

import Utility.FTPServerFunctions;
import Exceptions.UserAlreadyExists;
import Utility.FXMLSceneController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.application.Platform;

import java.io.IOException;
import java.sql.SQLException;

public class LoginPage {

    @FXML
    public TextField usernameInput;

    @FXML
    private PasswordField passwordInput;

    @FXML
    private Label errorLabel; // The Label for displaying the error message

    @FXML
    private void handleLogin() throws Exception {
        FTPServerFunctions.ftpClient = null;
        // Get the values from the username and password fields
        String username = usernameInput.getText();
        String password = passwordInput.getText();


        // try logging in and check if it failed
        if (!FTPServerFunctions.setupConnection(username, password))
            showError("Invalid username or password. Please try again");
        else FXMLSceneController.swapScene("FTPMain");
    }

    @FXML
    private void handleAccountCreate() throws IOException {
        FTPServerFunctions.ftpClient = null;

        // Get the values from the username and password fields
        String username = usernameInput.getText();
        String password = passwordInput.getText();

        try{

            FTPServerFunctions.addUser(username, password, false);

            // try logging in and check if it failed
            if (!FTPServerFunctions.setupConnection(username, password))
                showError("Contact Admin; Account creation error!");
            else FXMLSceneController.swapScene("FTPMain");

        } catch (UserAlreadyExists e){
            showError("Account Already Exists!");
        } catch (SQLException e){
            showError("Username or password contains invalid characters!");
        }
    }

    /**
     * Helper method to show errors on the error label
     * @param message the message to display in the error label
     */
    private void showError(String message){
        //clear field
        usernameInput.setText("");
        passwordInput.setText("");

        //show error label
        errorLabel.setVisible(true);
        errorLabel.setText(message);
    }

    @FXML
    private void handleKeyPress(KeyEvent event) throws Exception {
        if (event.getCode().equals(KeyCode.TAB)) {
            if (event.getSource() == usernameInput) {
                Platform.runLater(() -> passwordInput.requestFocus());
            } else if (event.getSource() == passwordInput) {
                Platform.runLater(() -> usernameInput.requestFocus());
            }
            event.consume(); // Consume the event to prevent it from triggering other actions
        }
        else if(event.getCode().equals(KeyCode.ENTER))
        {
            handleLogin();
        }
    }
}
