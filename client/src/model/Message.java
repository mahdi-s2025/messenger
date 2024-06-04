package model;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {
    private String text;
    private final Date sentDate;
    private final int senderId;  // maybe it doesn't usage
    private final int receiverId; // maybe it doesn't usage

    public Message(String text, int senderId, int receiverId) {
        this.text = text;
        this.sentDate = new Date();
        this.senderId = senderId;
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

    public int getSenderId() {
        return senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }
}
