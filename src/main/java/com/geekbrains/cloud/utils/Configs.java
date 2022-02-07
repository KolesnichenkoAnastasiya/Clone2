package com.geekbrains.cloud.utils;
import java.sql.Connection;
import java.sql.Statement;

public class Configs {
    public static final String dbHost = "localhost";
    public static final String dbPort = "3306";
    public static final String dbUserName = "root";
    public static final String dbName = "new_schema";
    protected static String dbTable ="new_table";
    public static final String dbPassword ="";
    public static final String URL="jdbc:mysql://"+ dbHost + ":" + dbPort + "/" +dbName;
    public static Statement statement;
    public static Connection dbConnection;
}

