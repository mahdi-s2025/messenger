package model;

import java.io.IOException;
import java.net.ServerSocket;

public class Socket {

    private Socket() throws Exception {
        serverSocket = new ServerSocket(8085);
    }

    private static Socket socket ;

    public static Socket getSocket() throws Exception {
        if (socket == null)
            socket = new Socket();
        return socket;
    }

    ServerSocket serverSocket ;



}
