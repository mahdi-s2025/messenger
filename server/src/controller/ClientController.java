package controller;

import model.Message;
import model.UserAccount;

import java.io.*;
import java.net.Socket;

public class ClientController extends Thread {
    private UserAccount userAccount;
    private final Socket client;
    private final ObjectOutputStream out;
    private final ObjectInputStream in;
    private final UserAccountController userAccountController;
    private final DBController dbController;

    public ClientController(Socket client) throws Exception {
        this.userAccountController = UserAccountController.getUserAccountController();
        this.client = client;
        dbController = DBController.getDbController();
        try {
            out = new ObjectOutputStream(new BufferedOutputStream(client.getOutputStream()));
            out.writeUTF("Server connected");
            out.flush();
            in = new ObjectInputStream(new BufferedInputStream(client.getInputStream()));
            in.readUTF();
        } catch (IOException e) {
            throw new Exception("Client connection failed!");
        }
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

    @Override
    public void run() {
        String[] commands = new String[1];

        while (userAccount == null) {
            try {
                out.writeUTF("Please login or signup!");
                out.flush();
                commands = in.readUTF().split("-");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (commands[0].equals("exit")) break;

            switch (commands[0]) {

                case "Signup" -> {
                    if (commands.length != 5) break;
                    try {
                        String username = userAccountController.checkUsername(commands[2]);
                        String password = userAccountController.checkPassword(commands[3]);
                        String phoneNumber = userAccountController.checkPhoneNumber(commands[4]);
                        userAccount = dbController.signUp(commands[1], username, password, phoneNumber);
                        out.writeUTF("Signup");
                        out.flush();
                        out.writeObject(userAccount);
                        out.flush();
                    } catch (Exception e) {
                        try {
                            //e.printStackTrace(System.err);
                            out.writeUTF(e.getMessage());
                            out.flush();
                        } catch (Exception e1) {
                            e1.printStackTrace(System.err);
                        }
                    }
                }

                case "Login" -> {
                    if (commands.length != 3) break;
                    try {
                        userAccount = dbController.login(commands[1], commands[2]);
                        out.writeUTF("Login");
                        out.flush();
                        out.writeObject(userAccount);
                        out.flush();
                    } catch (Exception e) {
                        try {
                            out.writeUTF(e.getMessage());
                            out.flush();
                        } catch (Exception e1) {
                            e1.printStackTrace(System.err);
                        }
                    }
                }

                default -> {
                    try {
                        out.writeUTF("Invalid command!");
                        out.flush();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        while (!commands[0].equals("exit")) {
            try {
                commands = in.readUTF().split("-");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (commands[0].equals("exit")) break;

            switch (commands[0]) {
                case "SendMessage" -> {
                    if (commands.length != 3) break;
                    try {
                        if (Long.parseLong(commands[1]) == 0) {
                            UserAccount chatRoom = new UserAccount("chatroom", "chatroom", "chatroom", "0");
                            chatRoom.setID(0);
                            Message message = new Message(commands[2], userAccount, chatRoom);
                            DBController.getDbController().addMessage(message);
                            for (ClientController client : CommunicationHandler.getCommunicationHandler().getClientList()) {
                                if (client != this) {
                                    client.out.writeUTF("ReceiveMessage");
                                    client.out.flush();
                                    client.out.writeObject(message);
                                    client.out.flush();
                                }
                            }
                        } else {
                            Message message = new Message(commands[2], userAccount, dbController.findUser(Long.parseLong(commands[1])));
                            DBController.getDbController().addMessage(message);
                            for (ClientController client : CommunicationHandler.getCommunicationHandler().getClientList()) {
                                if (client.userAccount.getID() == message.getReceiverUser().getID()) {
                                    client.out.writeUTF("ReceiveMessage");
                                    client.out.flush();
                                    client.out.writeObject(message);
                                    client.out.flush();
                                }
                            }
                        }
                    } catch (Exception e) {
                        try {
                            out.writeUTF(e.getMessage());
                            out.flush();
                        } catch (Exception e1) {
                            e1.printStackTrace(System.err);
                        }
                    }
                }
                default -> {
                    try {
                        out.writeUTF("Invalid command!");
                        out.flush();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        try {
            out.writeUTF("exit");
            out.flush();
            CommunicationHandler.getCommunicationHandler().getClientList().remove(this);
            out.close();
            in.close();
            if (!client.isClosed()) client.close();
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }
}
