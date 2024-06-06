package controller;

import model.Message;
import model.UserAccount;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ClientController implements Runnable {
    public ClientController(UserAccount loggedInUser, Socket client) {
        this.loggedInUser = loggedInUser;
        this.client = client;
    }

    private UserAccount loggedInUser;

    private Socket client;

    public UserAccount getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(UserAccount loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public Socket getClient() {
        return client;
    }

    public void setClient(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {

        try (ObjectInputStream getter = new ObjectInputStream(client.getInputStream())) {
            Message message;
            while ((message = (Message) getter.readObject()) != null) {
                DBController.getDbController().addMessage(message);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                client.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());;
            }
        }
    }
}
