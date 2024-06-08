package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserAccount implements Serializable {
    private long ID;
    private String name;
    private String username;
    private String password;
    private String phoneNumber;

    private List<UserAccount> contacts;
//    private List<ChatPage> chatPages;
    private ChatPage currentChatPage;

    public UserAccount(String name, String username, String password, String phoneNumber) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.contacts = new ArrayList<>();
//        this.chatPages = new ArrayList<>();
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public long getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<UserAccount> getContacts() {
        return contacts;
    }

    public void setContacts(List<UserAccount> contacts) {
        this.contacts = contacts;
    }

//    public List<ChatPage> getChatPages() {
//        return chatPages;
//    }
//
//    public void setChatPages(List<ChatPage> chatPages) {
//        this.chatPages = chatPages;
//    }

    public ChatPage getCurrentChatPage() {
        return currentChatPage;
    }

    public void setCurrentChatPage(ChatPage currentChatPage) {
        this.currentChatPage = currentChatPage;
    }
}
