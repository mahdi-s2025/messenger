package controller;

import model.Message;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatRoomController implements Runnable{

    public ChatRoomController() throws IOException {
        run();
    }
    public Socket connectClient() throws IOException {
        try {
            ServerSocket serverSocket = new ServerSocket(1235);
            Socket socket = serverSocket.accept();
            System.out.println("conected");
            return socket;
        }catch (Exception e)
        {
            return null;
        }
    }

    // throws shit
    public Message receiveMessageFromClient() throws Exception {
        if (connectClient() == null)
            throw new Exception("The connection with the client has not been established");
        Socket socket = connectClient();
        ObjectInputStream getter = new ObjectInputStream(socket.getInputStream());
        Message message = (Message) getter.readObject();
        return message;
    }




    @Override
    public void run() {
        try {
             Message message = receiveMessageFromClient();
            DBController.getDbController().addMessage(message);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }





    }
}
//توجه داریم که اسم پکیج هایی که کلاس message در انهاست باید یکسان باشد