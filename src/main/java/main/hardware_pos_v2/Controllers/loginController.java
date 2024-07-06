package main.hardware_pos_v2.Controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import main.hardware_pos_v2.Database.DAO.AccountDAO;
import main.hardware_pos_v2.Database.Entity.Account;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public class loginController {

    @FXML
    private Button exitButton;

    @FXML
    private Label feedBackLabel;

    @FXML
    private HBox header;

    @FXML
    private Label heading;

    @FXML
    private Button loginButton;

    @FXML
    private TextField passwordInput;

    @FXML
    private Label passwordLabel;

    @FXML
    private TextField usernameInput;

    @FXML
    private Label usernameLabel;

    @FXML
    public void authenticateUser() {
        String name = usernameInput.getText();
        String password = passwordInput.getText();

        // Check if there's an input
        if (name.trim().isEmpty()) {
            feedBackLabel.setVisible(false);
            feedBackLabel.setText("Please provide a username");
            feedBackLabel.setVisible(true);
            return;
        }
        if (password.trim().isEmpty()) {
            feedBackLabel.setVisible(false);
            feedBackLabel.setText("Please provide a password");
            feedBackLabel.setVisible(true);
            return;
        }

        // fetch the account from the database
        AccountDAO accountDAO = new AccountDAO();
        Map<Boolean, String> result = accountDAO.readAccount(name.trim().toLowerCase());

        // Check if account was found
        if (!result.containsKey(true)) {
            feedBackLabel.setText(result.get(false));
            feedBackLabel.setVisible(true);
            return;
        }

        // Fetch account details
        Account account = accountDAO.getAccount(name.trim().toLowerCase());

        // Check if password matches the stored password
        if (!BCrypt.checkpw(password, account.getPassword())) {
            feedBackLabel.setText("The password is incorrect");
            feedBackLabel.setStyle("-fx-text-fill: red");
            feedBackLabel.setVisible(true);
            return;
        }

        // If authentication is successful
        feedBackLabel.setText("Login successful");
        feedBackLabel.setStyle("-fx-text-fill: green");
        feedBackLabel.setVisible(true);
        changeScene();
    }

    public void changeScene() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main-menu.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setFullScreen(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void exitTheApp() {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
        Platform.exit();
    }
}
