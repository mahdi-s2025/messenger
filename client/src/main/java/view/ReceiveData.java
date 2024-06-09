package view;

import model.ChatPage;
import model.ClientSocket;
import model.Message;
import model.UserAccount;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class ReceiveData extends Thread {
    private static ReceiveData receiveData;
    private Socket client;
    private UserAccount userAccount;
    private ObjectInputStream in;

    private ReceiveData() {
        try {
            client = ClientSocket.getClientSocket().getClient();
            in = new ObjectInputStream(new BufferedInputStream(client.getInputStream()));
            System.out.println(in.readUTF());
            SendData.getSendData().getOut().writeUTF("Client connected");
            SendData.getSendData().getOut().flush();
        } catch (Exception e) {
            System.out.println("Connection failed!");
        }
    }

    public static ReceiveData getReceiveData() {
        if (receiveData == null) {
            receiveData = new ReceiveData();
        }
        return receiveData;
    }

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    public Socket getClient() {
        return client;
    }

    public void setClient(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        String resultCommand = "";
        while (!resultCommand.equals("exit")) {
            switch (resultCommand) {
                case "Signup", "Login" -> {
                    try {
                        userAccount = (UserAccount) in.readObject();
                        SendData.getSendData().setUserAccount(userAccount);
                        for (Message message : userAccount.getCurrentChatPage().getMessages()) {
                            System.out.println(message.getSenderUser().getName() + "\t" + message.getSenderUser().getUsername() + "\t" + message.getSentDate() + ":");
                            System.out.println(message.getText());
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
                case "ReceiveMessage" -> {
                    try {
                        Message message = (Message) in.readObject();
                        if (userAccount.getCurrentChatPage().getUser1().getID() == message.getReceiverUser().getID()
                                && userAccount.getCurrentChatPage().getUser2().getID() == message.getSenderUser().getID()) {
                            userAccount.getCurrentChatPage().getMessages().add(message);
                            System.out.println(message.getSenderUser().getName() + "\t" + message.getSenderUser().getUsername() + "\t" + message.getSentDate() + ":");
                            System.out.println(message.getText());
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }

                case "OnlineUsers" -> {
                    try {
                        // List<UserAccount> onlineUsers = new ArrayList<>();
                        List objects = (List) in.readObject();
                        for (Object object : objects) {
                            if (object instanceof UserAccount onlineUser) {
                                System.out.println(onlineUser);
                            }
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }

                case "SelectUser" -> {
                    try {
                        userAccount.setCurrentChatPage((ChatPage) in.readObject());
                        for (Message message : userAccount.getCurrentChatPage().getMessages()) {
                            System.out.println(message.getSenderUser().getName() + "\t" + message.getSenderUser().getUsername() + "\t" + message.getSentDate() + ":");
                            System.out.println(message.getText());
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }

                case "NewContact" -> {
                    try {
                        userAccount.getContacts().add((UserAccount) in.readObject());
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }

                default -> System.out.println(resultCommand);
            }
            try {
                resultCommand = in.readUTF();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        try {
            in.close();
            if (!client.isClosed()) client.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
