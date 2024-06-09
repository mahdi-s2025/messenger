package controller;

import model.ChatPage;
import model.Database;
import model.Message;
import model.UserAccount;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DBController {
    private static DBController dbController ;
    private final Database database;

    private DBController() throws Exception {
        database = Database.getDatabase();
    }


    public static DBController getDbController() throws Exception {
        if (dbController == null)
            dbController = new DBController();
        return dbController;
    }

    public Database getDatabase() {
        return database;
    }

    public UserAccount findUser(String username) throws Exception {
        String cmd = "SELECT * FROM users WHERE username = '" + username + "'";
        return findUserFromDB(cmd);
    }

    public UserAccount findUser(long ID) throws Exception {
        String cmd = "SELECT * FROM users WHERE ID = '" + ID + "'";
        return findUserFromDB(cmd);
    }

    private UserAccount findUserFromDB(String cmd) throws Exception {
        ResultSet result = database.executeQuery(cmd);
        UserAccount targetUser = null;
        if (result.next()) {
            targetUser = new UserAccount(result.getString("name"), result.getString("username"),
                    result.getString("password"), result.getString("phoneNumber"));
            targetUser.setID(result.getLong("ID"));
        }
        result.close();
        return targetUser;
    }

    public UserAccount login(String username, String password ) throws Exception {
        String cmd = "SELECT * FROM users WHERE username = '" + username + "'";
        ResultSet result = database.executeQuery(cmd);
        if (result.next()) {
            if (password.equals(result.getString("password"))) {
                UserAccount account = new UserAccount(result.getString("name"), result.getString("username"),
                        result.getString("password"), result.getString("phoneNumber"));
                account.setID(result.getLong("ID"));
                result.close();
                return account;
            }
            else {
                result.close();
                throw new Exception("The entered password is incorrect!");
            }
        }
        result.close();
        throw new Exception("Username not found!");
    }

    public UserAccount signUp(String name , String username , String password , String phoneNumber ) throws Exception {
        UserAccount newUser = new UserAccount(name, username, password, phoneNumber);
        String cmd = String.format("INSERT INTO users (name, username, password, phoneNumber) VALUES ('%s', '%s', '%s', '%s')",
                name, username, password, phoneNumber);
        PreparedStatement statement = database.getConnection().prepareStatement(cmd, Statement.RETURN_GENERATED_KEYS);
        statement.executeUpdate();
        ResultSet generatedKey = statement.getGeneratedKeys();
        if (generatedKey.next())
            newUser.setID(generatedKey.getLong(1));
        statement.close();
        generatedKey.close();
        return newUser;
    }

    public void addMessage(Message message) throws Exception {
        // changing format for save to database
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = date.format(message.getSentDate());

        String cmd = String.format("INSERT INTO messages VALUES ('%d', '%d', '%s', '%s')",
                message.getSenderUser().getID(), message.getReceiverUser().getID(), message.getText(), formattedDate);

        database.executeSQL(cmd);
    }

    public void addContact(Long userID, Long contactID) throws Exception {
        String cmd = String.format("INSERT INTO contacts VALUES ('%s', '%s')", userID, contactID);
        database.executeSQL(cmd);
    }

    public List<UserAccount> getContacts(long userID) throws Exception {
        List<UserAccount> contacts = new ArrayList<>();
        String cmd = "SELECT ID, name, username, password, phoneNumber FROM users INNER JOIN " +
                "contacts ON users.ID = contacts.contactID WHERE users.ID = '" + userID + "'";
        ResultSet result = database.executeQuery(cmd);
        while (result.next()) {
            UserAccount contact = new UserAccount(result.getString("name"), result.getString("username"),
                    result.getString("password"), result.getString("phoneNumber"));
            contact.setID(result.getLong("ID"));
            contacts.add(contact);
        }
        result.close();
        return contacts;
    }

    public List<Message> getMessages(long user1ID, long user2ID) throws Exception {
        List<Message> messages = new ArrayList<>();
        String cmd = String.format("SELECT * FROM messages WHERE (senderID = '%S' AND " +
                "receiverID = '%S') OR (senderID = '%S' AND receiverID = '%S') ORDER BY sentDate", user1ID, user2ID, user2ID, user1ID);
        ResultSet result = database.executeQuery(cmd);
        while (result.next()) {
            Message message = new Message(result.getString("message"), findUser(result.getLong("senderID")),
                    findUser(result.getLong("receiverID")));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            message.setSentDate(formatter.parse(result.getString("sentDate")));
            messages.add(message);
        }
        result.close();
        return messages;
    }

//    public List<Message> getUserMessagesInChatRoom(UserAccount user) throws Exception {
//        List<Message> messages = new ArrayList<>();
//        String cmd = String.format("SELECT (message, sentDate) FROM messages WHERE (senderID = '%S' AND " +
//                "receiverID = 0) ORDER BY sentDate", user.getID());
//        ResultSet result = database.executeQuery(cmd);
//        UserAccount chatRoom_user = new UserAccount("chatroom", "chatroom", "chatroom", "0");
//        chatRoom_user.setID(0);
//        while (result.next()) {
//            Message message = new Message(result.getString("message"), user, chatRoom_user);
//            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            message.setSentDate(formatter.parse(result.getString("sentDate")));
//            messages.add(message);
//        }
//        result.close();
//        return messages;
//    }
//    public List<ChatPage> getChatPages(UserAccount user) throws Exception {
//        List<ChatPage> chatPages = new ArrayList<>();
//        for (UserAccount contact : user.getContacts()) {
//            ChatPage chatPage = new ChatPage(user, contact, getMessages(user.getID(), contact.getID()));
//            chatPages.add(chatPage);
//        }
//        return chatPages;
//    }

    public List<Message> getAllMessagesInChatRoom() throws Exception {
        List<Message> messages = new ArrayList<>();
        String cmd = "SELECT * FROM messages WHERE receiverID = 0 ORDER BY sentDate";
        ResultSet result = database.executeQuery(cmd);
        UserAccount chatRoom_user = new UserAccount("chatroom", "chatroom", "chatroom", "0");
        chatRoom_user.setID(0);
        while (result.next()) {
            Message message = new Message(result.getString("message"),
                    findUser(result.getLong("senderID")), chatRoom_user);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            message.setSentDate(formatter.parse(result.getString("sentDate")));
            messages.add(message);
        }
        result.close();
        return messages;
    }

}
