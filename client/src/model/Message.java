package model;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {
    private String text;
    private final Date sentDate;
    private final UserAccount senderUser;
    private final long receiverId;  // maybe need receiverUser;

    public Message(String text, UserAccount senderUser, long receiverId) {
        this.text = text;
        this.sentDate = new Date();
        this.senderUser = senderUser;
        this.receiverId = receiverId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public UserAccount getSenderUser() {
        return senderUser;
    }

    public long getReceiverId() {
        return receiverId;
    }
}
