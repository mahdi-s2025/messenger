import controller.CommunicationHandler;

public class Main {
    public static void main(String[] args) {
        try {
            CommunicationHandler.getCommunicationHandler().start();
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }
}
