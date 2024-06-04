package controller;


import model.UserAccount;

import java.io.*;
import java.net.Socket;

public class UserController {
    private static UserController userController;
    private final Socket client;
    private final PrintWriter out;
    private final BufferedReader in;

    private UserController() throws IOException {
        client = new Socket("localhost", 8085);
        out = new PrintWriter(new BufferedOutputStream(client.getOutputStream()), true);
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));
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
    }g
}
