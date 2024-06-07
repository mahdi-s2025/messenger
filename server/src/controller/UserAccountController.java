package controller;

import model.UserAccount;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserAccountController {

    private UserAccountController() {}

    private static UserAccountController userAccountController;
//    private UserAccount loggedUser;

    public static UserAccountController getUserAccountController() {
        if (userAccountController == null)
            userAccountController = new UserAccountController();
        return userAccountController;
    }


//    public UserAccount getLoggedUser() {
//        return loggedUser;
//    }

//    public void setLoggedUser(UserAccount loggedUser) {
//        this.loggedUser = loggedUser;
//    }

    public String checkPassword(String password) throws Exception {
        switch (passwordStrength(password)) {
            case 0 -> throw new Exception("The password is too short.\nIt must have more than 8 characters");
            case 1 -> throw new Exception("The password is weak. Its strength is 1 out of 4." +
                    "\nYou must use 0-9, a-z, A-Z, and at least one special character: ! @ # % ^ & * ( )");
            case 2 -> throw new Exception("The password is weak. Its strength is 2 out of 4." +
                    "\nYou must use 0-9, a-z, A-Z, and at least one special character: ! @ # % ^ & * ( )");
            case 3 -> throw new Exception("The password is weak. Its strength is 3 out of 4." +
                    "\nYou must use 0-9, a-z, A-Z, and at least one special character: ! @ # % ^ & * ( )");
            case 5 -> throw new Exception("The password is too long.\nIt shouldn't have more than 30 characters");
            case 6 -> throw new Exception("The password has invalid character." +
                    "\nYou must use 0-9, a-z, A-Z, and at least one special character: ! @ # % ^ & * ( )");
            default -> {
                return password;
            }
        }
    }

    public String checkUsername(String username) throws Exception {
        if (DBController.getDbController().findUser(username) != null)
            throw new Exception("Username already exists!");
        Pattern usernamePattern = Pattern.compile("^[A-Za-z1-9]+(_)?[A-Za-z1-9]+$");
        Matcher usernameMatcher = usernamePattern.matcher(username);
        if (!usernameMatcher.matches()) {
            throw new Exception("Username is invalid!");
        }
        return username;
    }

    public String checkPhoneNumber(String phoneNumber) throws Exception {
        if (!validPhoneNumber(phoneNumber))
            throw new Exception("Invalid phone number");
        return phoneNumber;
    }

    public int passwordStrength(String password) {
        int strength = 0;
        if (password.length() < 8)
            return 0;

        if (password.length() > 30)
            return 5;

        Pattern smallLetterPattern = Pattern.compile("^(?=([\\w\\d!@#$%^&*()]*[a-z]))[\\w\\d!@#$%^&*()]*$");
        Matcher smallLetterMatcher = smallLetterPattern.matcher(password);
        if (smallLetterMatcher.matches()) strength++;

        Pattern numberPattern = Pattern.compile("^(?=([\\w\\d!@#$%^&*()]*[0-9]))[\\w\\d!@#$%^&*()]*$");
        Matcher numberMatcher = numberPattern.matcher(password);
        if (numberMatcher.matches()) strength++;

        Pattern capitalLetterPattern = Pattern.compile("^(?=([\\w\\d!@#$%^&*()]*[A-Z]))[\\w\\d!@#$%^&*()]*$");
        Matcher capitalLetterMatcher = capitalLetterPattern.matcher(password);
        if (capitalLetterMatcher.matches()) strength++;

        Pattern characterPattern = Pattern.compile("^(?=([\\w\\d!@#$%^&*()]*[!@#$%^&*()]))[\\w\\d!@#$%^&*()]*$");
        Matcher characterMatcher = characterPattern.matcher(password);
        if (characterMatcher.matches()) strength++;

        if (strength == 0) return 6;
        return strength;
    }

    public boolean validPhoneNumber(String phoneNumber) {
        Pattern phoneNumberValidPattern = Pattern.compile("^09[\\d]{9}");
        Matcher phoneNumberMatcher = phoneNumberValidPattern.matcher(phoneNumber);
        return phoneNumberMatcher.matches();
    }
}
