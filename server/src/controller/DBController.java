package controller;

import model.UserAccount;

public class DBController {
    private DBController() {
    }

    private static DBController dbController ;

    public static DBController getDbController(){
        if (dbController == null)
            dbController = new DBController();
        return dbController;
    }

    private UserAccount user ;

    public UserAccount getUser() {
        return user;
    }

    public void setUser(UserAccount user) {
        this.user = user;
    }


}
