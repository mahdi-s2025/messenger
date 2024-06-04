package controller;

import model.Database;
import model.UserAccount;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

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



    public void addUser(UserAccount user) throws Exception {
        String cmd  = "INSERT INTO users VALUES ( '" + user.getName() + "' , '" + user.getUsername() + "' , " +
                "'" + user.getPassword() + "' , '" + user.getPhoneNumber() + "')";
        PreparedStatement statement = Database.getDatabase().getConnection().prepareStatement(cmd , Statement.RETURN_GENERATED_KEYS);
        statement.executeUpdate();
        user.setID(statement.getGeneratedKeys().getLong(1));
    }







}



