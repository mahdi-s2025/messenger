package view;

import model.ClientSocket;
import model.UserAccount;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class SendData extends Thread {
    private static SendData sendData;
    private Socket client;
    private UserAccount userAccount;
    private ObjectOutputStream out;

    private SendData() {
        try {
            client = ClientSocket.getClientSocket().getClient();
            out = new ObjectOutputStream(new BufferedOutputStream(client.getOutputStream()));
        } catch (Exception e) {
            System.out.println("Connection failed!");
        }
    }

    public static SendData getSendData() {
        if (sendData == null) {
            sendData = new SendData();
        }
        return sendData;
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

    public ObjectOutputStream getOut() {
        return out;
    }

    @Override
    public void run() {
        Scanner sc = new Scanner(System.in);
        String outputCommand = "";

        while (!outputCommand.equals("exit")) {
            outputCommand = sc.nextLine();
            try {
                out.writeUTF(outputCommand);
                out.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            out.close();
            if (!client.isClosed()) client.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
