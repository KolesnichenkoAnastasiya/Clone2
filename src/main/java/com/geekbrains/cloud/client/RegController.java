package com.geekbrains.cloud.client;

import com.geekbrains.cloud.DataBase.DatabaseHandler;
import com.geekbrains.cloud.DataBase.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.File;
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
        String user_directory = "C:\\Users\\Настя\\IdeaProjects\\Clone2_new\\serverDir\\" + login;
        User user = new User(name, login, password, user_directory);
        dbHandler.signUpUser(user);
        File dir_user = new File(user_directory);
        if(!dir_user.exists()) {
            dir_user.mkdir();
        }
    }
}
