package controller;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class CommunicationHandler extends Thread {
    private static CommunicationHandler communicationHandler;
    private final ServerSocket server;
    private boolean newConnectionStatus;
    private final List<ClientController> clientList;

    private CommunicationHandler() throws IOException {
        server = new ServerSocket(8085);
        newConnectionStatus = true;
        clientList = new ArrayList<>();
    }

    public static CommunicationHandler getCommunicationHandler() throws IOException {
        if (communicationHandler == null)
            communicationHandler = new CommunicationHandler();
        return communicationHandler;
    }

    public ServerSocket getServer() {
        return server;
    }

    public boolean getNewConnectionStatus() {
        return newConnectionStatus;
    }

    public void setNewConnectionStatus(boolean newConnectionStatus) {
        this.newConnectionStatus = newConnectionStatus;
    }

    public List<ClientController> getClientList() {
        return clientList;
    }

    @Override
    public void run() {
        while (newConnectionStatus) {
            try {
                Socket socket = server.accept();
                ClientController clientController = new ClientController(socket);
                clientList.add(clientController);
                Thread clientThread = new Thread(clientController);
                clientThread.start();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        // shut down
    }



    // implement switches for control server in a method
}
