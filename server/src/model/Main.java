package model;


import controller.DBController;
import controller.communicationHandler;
import model.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) throws Exception {

        ServerSocket serverSocket = new ServerSocket(1235);

        while (true){
            Socket clientSocket = serverSocket.accept();
            communicationHandler  handler = new communicationHandler(clientSocket);
            handler.run();
        }
    }
}

