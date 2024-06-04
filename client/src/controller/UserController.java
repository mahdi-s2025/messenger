package controller;


import model.UserAccount;

import java.io.*;
import java.net.Socket;

public class UserController {
    private static UserController userController;
    private final Socket client;
    private final PrintWriter out;
    private final BufferedReader in;
    private final ObjectOutputStream outObj;
    private final ObjectInputStream inObj;

    private UserController() throws IOException {
        client = new Socket("localhost", 8085);
        out = new PrintWriter(new BufferedOutputStream(client.getOutputStream()), true);
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        outObj = new ObjectOutputStream(new BufferedOutputStream(client.getOutputStream()));
        inObj = new ObjectInputStream(new BufferedInputStream(client.getInputStream()));
    }

    public static UserController getUserController() throws IOException {
        if (userController == null) {
            userController = new UserController();
        }
        return userController;
    }

    private UserAccount userAccount;

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public Socket getClient() {
        return client;
    }

    public PrintWriter getOut() {
        return out;
    }

    public BufferedReader getIn() {
        return in;
    }

    public ObjectOutputStream getOutObj() {
        return outObj;
    }

    public ObjectInputStream getInObj() {
        return inObj;
    }

    public UserAccount signup(String name, String username, String password, String phoneNumber) throws Exception {
        out.println("signup");
        out.println(String.format("%s-%s-%s-%s", name, username, password, phoneNumber));
        UserAccount newUserAccount;  // maybe should initialize with null
        // exception handling

        try {
            newUserAccount = (UserAccount) inObj.readObject();
            userAccount = newUserAccount;
        } catch (IOException | ClassNotFoundException e) {   // maybe the exceptions should be handled differently
            throw new Exception("Sign up failed!");
        }
        return newUserAccount;
    }

    public UserAccount login(String username, String password) throws Exception {
        out.println("login");
        out.println(String.format("%s-%s", username, password));
        UserAccount loggedInUser;  // maybe should initialize with null
        // exception handling

        try {
            loggedInUser = (UserAccount) inObj.readObject();
            userAccount = loggedInUser;
        } catch (IOException | ClassNotFoundException e) {   // maybe the exceptions should be handled differently
            throw new Exception("Login failed!");
        }
        return loggedInUser;
    }

}
}
