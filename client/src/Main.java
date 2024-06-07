import view.ReceiveData;
import view.SendData;

public class Main {
    public static void main(String[] args) {
        ReceiveData.getReceiveData().start();
        SendData.getSendData().start();
    }
}
