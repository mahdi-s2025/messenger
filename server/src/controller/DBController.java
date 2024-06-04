package controller;

import model.Database;
import model.UserAccount;

import java.sql.ResultSet;

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
    private UserAccount findUsername(String username) throws Exception {
        String cmd = "SELECT * FROM users WHERE username = '" + username + "' ";
        ResultSet result = Database.getDatabase().executeQuery(cmd);
        UserAccount targetUser = null;
        if (result.next()){
            targetUser = new UserAccount(result.getLong("ID" ), result.getString("name" ) , result.getString("username") ,
                    result.getString("password") , result.getString("phoneNumber"));
        }
        return targetUser;


    }






}
