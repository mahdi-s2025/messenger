package controller;


import model.Message;
import model.UserAccount;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class communicationHandler implements Runnable {

    private communicationHandler() throws IOException {
        int port = 8085;
        server = new ServerSocket(port);
        newClientConection = true;
    }

    private static communicationHandler communicationHandler;

    public static communicationHandler getcommunicationHandler() throws IOException {
        if (communicationHandler == null)
            communicationHandler = new communicationHandler();
        return communicationHandler;
    }

    private Socket client;
    private ServerSocket server;
    private boolean newClientConection;
    private UserAccount user;

    public ServerSocket getServer() {
        return server;
    }

    public void setServer(ServerSocket server) {
        this.server = server;
    }

    public Socket getSocket() {
        return client;
    }

    public void setSocket(Socket socket) {
        this.client = socket;
    }

    public boolean isNewClientConection() {
        return newClientConection;
    }

    public void setNewClientConection(boolean newClientConection) {
        this.newClientConection = newClientConection;
    }

    public UserAccount getUser() {
        return user;
    }

    public void setUser(UserAccount user) {
        this.user = user;
    }

    @Override
    public void run() {
        while (newClientConection) {
            try {
                Socket clientSocket = server.accept();
                ObjectInputStream getInfo = new ObjectInputStream(clientSocket.getInputStream());
                String CMD = getInfo.readUTF();
                // CMD = name-username-password-phoneNumber OR CMD = username-password
                String[] words = CMD.split("-");
                if (words.length == 4) {
                    UserAccount user = new UserAccount(words[0], words[1], words[2], words[3]);
                    DBController.getDbController().addUser(user);
                } else if (words.length == 2) {
                   if (DBController.getDbController().login(words[0],words[1])) {
                       ClientController client = new ClientController(UserAccountController.getUserAccountController().getUser(), clientSocket);
                       client.run();
                   }
                }

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }


        }
    }


}
