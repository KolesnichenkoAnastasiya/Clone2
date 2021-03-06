package com.geekbrains.cloud.client;

import com.geekbrains.cloud.DataBase.DatabaseHandler;
import com.geekbrains.cloud.DataBase.User;
import com.geekbrains.cloud.animations.Shake;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Path;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static javafx.scene.Scene.*;

public class AuthController {
    public static String userLog;
    public File path_dir;
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
            if (!loginText.equals("")&& !passwordText.equals("")) {
                try {
                    loginUser(loginText, passwordText);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            else System.out.println("Login or password is empty");
        });
    }

    private void loginUser(String loginText, String passwordText) throws SQLException {
        DatabaseHandler dbHandler = new DatabaseHandler();
        User user = new User();
        user.setLogin_user(loginText);
        userLog=loginText;
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
            String pathUser = result.getString(5);
            path_dir = new File(pathUser);/*???????? ?? ????????????????????*/
            System.out.println(path_dir);
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