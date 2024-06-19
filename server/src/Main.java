import controller.CommunicationHandler;
import model.Database;

import java.sql.Statement;

public class Main {
    public static void main(String[] args) {
//        String command = "DELETE FROM users WHERE ID > 5 ";
//        try {
//            Statement statement = Database.getDatabase().getConnection().prepareStatement(command);
//            statement.execute(command);
//        } catch (Exception e) {
//            e.printStackTrace(System.out);
//        }
        try {
            CommunicationHandler.getCommunicationHandler().start();
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }
}
