package com.geekbrains.cloud.client;

import com.geekbrains.cloud.DataBase.DatabaseHandler;
import com.geekbrains.cloud.DataBase.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import java.sql.SQLException;

public class RegController {
    @FXML
    private TextField nameField;

    @FXML
    private TextField loginField;

    @FXML
    private TextField passwordField;

    @FXML
    private Button regSignUpButton;

    @FXML
    void initialize() {
        regSignUpButton.setOnAction(event -> {
            try {
                signUpNewUser();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
}

    private void signUpNewUser() throws SQLException {
        DatabaseHandler dbHandler = new DatabaseHandler();
        String name = nameField.getText();
        String login = loginField.getText();
        String password = passwordField.getText();
        User user = new User(name, login, password);
        dbHandler.signUpUser(user);
    }
}
