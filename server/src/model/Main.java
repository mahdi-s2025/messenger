package model;

import controller.ChatRoomController;
import controller.DBController;
import model.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) throws Exception {
//
//         ChatRoomController a = new ChatRoomController();
//         a.run();

        DBController.getDbController().addMessage(new Message("shit" , 1 , 0  ));
    }
}

