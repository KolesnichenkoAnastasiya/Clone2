package com.geekbrains.cloud.client;

import com.geekbrains.cloud.DataBase.DatabaseHandler;
import com.geekbrains.cloud.DataBase.User;
import com.geekbrains.cloud.animations.Shake;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthController {

    @FXML
    private Button authSignUpButton;

    @FXML
    private TextField loginField;

    @FXML
    private TextField passwordField;

    @FXML
    private Button regSignUpButton;

    @FXML
    void initialize(){
        regSignUpButton.setOnAction(event -> {
            regSignUpButton.getScene().getWindow().hide();
            newScene("reg.fxml");
        });
        authSignUpButton.setOnAction(event -> {
            String loginText = loginField.getText();
            String passwordText = passwordField.getText();
            if (!loginText.equals("")&& !passwordText.equals(""))
                loginUser(loginText, passwordText);
            else System.out.println("Login or password is empty");
        });
    }

    private void loginUser(String loginText, String passwordText) {
        DatabaseHandler dbHandler = new DatabaseHandler();
        User user = new User();
        user.setLogin_user(loginText);
        user.setPass_user(passwordText);
        ResultSet result = dbHandler.getUser(user);
        int counter = 0;
        while (true){
            try {
                if (!result.next()) break;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            counter++;
        }
        if(counter>=1){
            newScene("client.fxml");
        }
            else {
            Shake userLoginAnimation = new Shake(loginField);
            Shake userPassAnimation = new Shake(passwordField);
            userLoginAnimation.playAnimation();
            userPassAnimation.playAnimation();
        }
    }
    public void newScene(String window){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(window));
        try {
            loader.load();
        }catch (IOException e) {
            e.printStackTrace();
        }
        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }
}