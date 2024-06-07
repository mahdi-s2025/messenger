package model;

import java.io.IOException;
import java.net.Socket;

public class ClientSocket {
    private static ClientSocket clientSocket;
    private final Socket client;

    private ClientSocket() throws Exception {
        try {
            client = new Socket("localhost", 8085);
        } catch (IOException e) {
            throw new Exception("Server connection failed");
        }
    }
    public static ClientSocket getClientSocket() throws Exception {
        if (clientSocket == null) {
            clientSocket = new ClientSocket();
        }
        return clientSocket;
    }

    public Socket getClient() {
        return client;
    }
}
