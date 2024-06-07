package model;

import java.sql.*;

public class Database {

    private Database() throws Exception {
        URL = "jdbc:mysql://localhost/messenger";
        userName = "root";
        password = "root";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, userName, password);
        } catch (Exception e) {
            throw new Exception("Database connection failed!");
            //throw new Exception(e);
        }
    }

    private static Database database;

    public static Database getDatabase() throws Exception {
        if (database == null)
            database = new Database();
        return database;
    }

    final private String URL;
    final private String userName;
    final private String password;
    final private Connection connection;

    public String getURL() {
        return URL;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public Connection getConnection() {
        return connection;
    }

    public void executeSQL(String cmd) throws Exception {
        try {
            Statement statement = connection.prepareStatement(cmd);
            statement.execute(cmd);
            statement.close();
        } catch (Exception e) {
            throw new Exception("Error executing SQL: " + cmd);
        }
    }

    public ResultSet executeQuery(String cmd) throws Exception {
        try {
            Statement statement = connection.prepareStatement(cmd);
            return statement.executeQuery(cmd);
        } catch (Exception e) {
            throw new Exception("Error executing Query: " + cmd);
        }
    }
}
