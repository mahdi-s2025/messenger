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

    private UserController() throws Exception {
        try {
            client = new Socket("localhost", 8085);
            out = new PrintWriter(new BufferedOutputStream(client.getOutputStream()), true);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            outObj = new ObjectOutputStream(new BufferedOutputStream(client.getOutputStream()));
            inObj = new ObjectInputStream(new BufferedInputStream(client.getInputStream()));
        } catch (IOException e) {
            throw new Exception("Server connection failed!");
        }
    }

    public static UserController getUserController() throws Exception {
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
        // exception handling

        try {
            userAccount = (UserAccount) inObj.readObject();
            return userAccount;
        } catch (IOException | ClassNotFoundException e) {   // maybe the exceptions should be handled differently
            throw new Exception("Sign up failed!");
        }
    }

    public UserAccount login(String username, String password) throws Exception {
        out.println("login");
        out.println(String.format("%s-%s", username, password));
        // exception handling

        try {
            userAccount = (UserAccount) inObj.readObject();
            return userAccount;
        } catch (IOException | ClassNotFoundException e) {   // maybe the exceptions should be handled differently
            throw new Exception("Login failed!");
        }
    }

    public long ping() throws Exception {
        out.println("ping");
        long startTime = System.currentTimeMillis();
        out.println("Sample text to calculate ping");
        try {
            in.readLine();
            long endTime = System.currentTimeMillis();
            return endTime - startTime;
        } catch (IOException e) {
            throw new Exception("Server doesn't response");
        }
    }

    public void sendToPV(int SenderID, String message) {
        out.println("sendToPV");
        out.println(String.format("%s-%s", SenderID, message));
    }

    public void sendToChatRoom(String message) {
        out.println("sendToChatRoom");
        out.println(String.format("0-%s", message));
    }
}
