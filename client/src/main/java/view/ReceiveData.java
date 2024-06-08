package view;

import model.ClientSocket;
import model.Message;
import model.UserAccount;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
                case "ReceiveMessage" -> {
                    try {
                        Message message = (Message) in.readObject();
                        System.out.println(message.getSenderUser().getName() + "\t " + message.getSenderUser().getUsername() + " :");
                        System.out.println(message.getText());
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
