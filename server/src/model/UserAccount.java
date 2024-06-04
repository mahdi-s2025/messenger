package model;

import java.io.Serializable;

public class UserAccount implements Serializable {
    private final long ID;
    private String name;
    private String username;
    private String password;
    private String phoneNumber;
    // maybe need a list of contacts
    // also maybe need a new table in database to storage them.

    public UserAccount(long ID, String name, String username, String password, String phoneNumber) {
        this.ID = ID;
        this.name = name;
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
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
}
