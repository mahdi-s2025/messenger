package model;


import java.util.List;

public class ChatPage {
    private UserAccount user1;
    private UserAccount user2;
    private List<Message> messages;

    public ChatPage(UserAccount user1, UserAccount user2, List<Message> messages) {
        this.user1 = user1;
        this.user2 = user2;
        this.messages = messages;
    }

    public UserAccount getUser1() {
        return user1;
    }

    public void setUser1(UserAccount user1) {
        this.user1 = user1;
    }

    public UserAccount getUser2() {
        return user2;
    }

    public void setUser2(UserAccount user2) {
        this.user2 = user2;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
