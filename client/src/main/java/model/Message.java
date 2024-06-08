package model;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {
    private String text;
    private final Date sentDate;
    private final UserAccount senderUser;
    private final UserAccount receiverUser;

    public Message(String text, UserAccount senderUser, UserAccount receiverUser) {
        this.text = text;
        this.sentDate = new Date();
        this.senderUser = senderUser;
        this.receiverUser = receiverUser;
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

    public UserAccount getReceiverUser() {
        return receiverUser;
    }
}
