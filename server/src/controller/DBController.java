package controller;

import model.Database;
import model.Message;
import model.UserAccount;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Objects;

public class DBController {
    private DBController() {
    }

    private static DBController dbController ;

    public static DBController getDbController(){
        if (dbController == null)
            dbController = new DBController();
        return dbController;
    }

    // throws shit
    public UserAccount findUsername(String username) throws Exception {
        String cmd = "SELECT * FROM users WHERE username = '" + username + "' ";
        ResultSet result = Database.getDatabase().executeQuery(cmd);
        UserAccount targetUser = null;
        if (result.next()){
            targetUser = new UserAccount( result.getString("name" ) , result.getString("username") ,
                    result.getString("password") , result.getString("phoneNumber"));
            targetUser.setID(result.getLong("ID"));
        }
        return targetUser;


    }

    // throws shit
    public boolean login(String userName  ,  String password ) throws Exception {
        UserAccount account;
        String cmd = "SELECT * FROM users WHERE username = '" + userName + "' ";
        ResultSet result = Database.getDatabase().executeQuery(cmd);
        if (result.next()){
            if (Objects.equals(password, result.getString("password"))) {
                account = new UserAccount(result.getString("name"), result.getString("username"),
                        result.getString("password"), result.getString("phoneNumber"));
                account.setID(result.getLong("ID"));
                UserAccountController.getUserAccountController().setUser(account);
                return true;
            }
            else
                throw new Exception("The password entered is incorrect");
        }
        throw new Exception("The username entered is incorrect");
    }






    public void addUser(UserAccount user) throws Exception {
        String cmd  = "INSERT INTO users(name , username , password , phoneNumber) VALUES ( '%s' , '%s' , '%s' , '%s')".formatted(user.getName(), user.getUsername(), user.getPassword(), user.getPhoneNumber());
        PreparedStatement statement = Database.getDatabase().getConnection().prepareStatement(cmd , Statement.RETURN_GENERATED_KEYS);
        statement.executeUpdate();
        ResultSet generatedKey =  statement.getGeneratedKeys();
        if (generatedKey.next())
            user.setID(generatedKey.getLong(1));
        statement.close();
    }

    // throws shit
    public void addMessage(Message message) throws Exception {
        // changing format for save to database
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = date.format(message.getSentDate());

        String cmd  = "INSERT INTO messages(senderID , receiverID , message , sentDate) VALUES ( '%d' , '%d' , '%s' , '%s')".formatted(message.getSenderId(), message.getReceiverId(), message.getText(), formattedDate);
        Database.getDatabase().executeSQL(cmd);
    }








}



