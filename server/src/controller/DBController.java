package controller;

import model.Database;
import model.Message;
import model.UserAccount;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;

public class DBController {
    private static DBController dbController ;
    private final Database database;

    private DBController() throws Exception {
        database = Database.getDatabase();
    }


    public static DBController getDbController() throws Exception {
        if (dbController == null)
            dbController = new DBController();
        return dbController;
    }

    public Database getDatabase() {
        return database;
    }

    public UserAccount findUser(String username) throws Exception {
        String cmd = "SELECT * FROM users WHERE username = '" + username + "'";
        ResultSet result = database.executeQuery(cmd);
        UserAccount targetUser = null;
        if (result.next()) {
            targetUser = new UserAccount(result.getString("name"), result.getString("username"),
                    result.getString("password"), result.getString("phoneNumber"));
            targetUser.setID(result.getLong("ID"));
        }
        result.close();
        return targetUser;
    }

    public UserAccount login(String userName, String password ) throws Exception {
        String cmd = "SELECT * FROM users WHERE username = '" + userName + "'";
        ResultSet result = database.executeQuery(cmd);
        if (result.next()) {
            if (password.equals(result.getString("password"))) {
                UserAccount account = new UserAccount(result.getString("name"), result.getString("username"),
                        result.getString("password"), result.getString("phoneNumber"));
                account.setID(result.getLong("ID"));
                result.close();
                return account;
            }
            else {
                result.close();
                throw new Exception("The entered password is incorrect!");
            }
        }
        result.close();
        throw new Exception("Username not found!");
    }

    public UserAccount signUp(String name , String userName , String password , String phoneNumber ) throws Exception {
        UserAccount newUser = new UserAccount(name, userName, password, phoneNumber);
        String cmd = String.format("INSERT INTO users (name, username, password, phoneNumber) VALUES ('%s', '%s', '%s', '%s')",
                name, userName, password, phoneNumber);
        PreparedStatement statement = database.getConnection().prepareStatement(cmd, Statement.RETURN_GENERATED_KEYS);
        statement.executeUpdate();
        ResultSet generatedKey = statement.getGeneratedKeys();
        if (generatedKey.next())
            newUser.setID(generatedKey.getLong(1));
        statement.close();
        generatedKey.close();
        return newUser;
    }

    public void addMessage(Message message) throws Exception {
        // changing format for save to database
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = date.format(message.getSentDate());

        String cmd = String.format("INSERT INTO messages VALUES ('%d', '%d', '%s', '%s')",
                message.getSenderUser().getID(), message.getReceiverId(), message.getText(), formattedDate);

        database.executeSQL(cmd);
    }
}



