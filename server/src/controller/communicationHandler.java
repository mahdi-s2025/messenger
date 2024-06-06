package controller;



import model.Message;

import java.io.ObjectInputStream;
import java.net.Socket;

public class communicationHandler implements Runnable {
    private Socket socket;

    public communicationHandler(Socket clientSocket) {
        socket = clientSocket;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }


    public void run() {
        try (ObjectInputStream getter = new ObjectInputStream(socket.getInputStream())) {
            Message message;
            while ((message = (Message) getter.readObject()) != null) {
                DBController.getDbController().addMessage(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
