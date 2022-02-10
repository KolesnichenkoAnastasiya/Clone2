package com.geekbrains.cloud.DataBase;
import com.geekbrains.cloud.utils.Configs;
import java.sql.*;

public class DatabaseHandler extends Configs {

    static {
        try {
            dbConnection = DriverManager.getConnection(URL, dbUserName, dbPassword);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
    static {
        try {
            statement = dbConnection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static Connection getDbConnection() throws SQLException {
        return dbConnection;}

    public static void main(String[] args) throws ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
    }

    public static void signUpUser(User user){
        try{
            String insert = "INSERT INTO " + Configs.dbTable + "(name_user, login_user, pass_user, user_directory) VALUES (?, ?, ?, ?);";
            PreparedStatement prSt = getDbConnection().prepareStatement(insert);
            prSt.setString(1, user.getName_user());
            prSt.setString(2, user.getLogin_user());
            prSt.setString(3, user.getPass_user());
            String path = "C:\\Users\\Настя\\IdeaProjects\\Clone2_new\\serverDir\\" + user.getLogin_user();
            prSt.setString(4, path);
            prSt.executeUpdate();

        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public ResultSet getUser (User user) {
        ResultSet resSet = null;
        try{
        String select = "SELECT * FROM " + dbTable + " WHERE login_user =? AND pass_user =?";
        PreparedStatement prSt = getDbConnection().prepareStatement(select);
        prSt.setString(1, user.getLogin_user());
        prSt.setString(2, user.getPass_user());
        resSet = prSt.executeQuery();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return resSet;
    }
}
