package controller;

import model.Database;
import model.UserAccount;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserAccountController {

    private UserAccountController() {
    }

    private static UserAccountController userAccount;

    public static UserAccountController getUserAccountController(){
        if (userAccount == null)
            userAccount = new UserAccountController();
        return userAccount;
    }

    private UserAccount user;

    public UserAccount getUser() {
        return user;
    }

    public void setUser(UserAccount user) {
        this.user = user;
    }

    public UserAccount signUp(String name , String userName , String password , String phoneNumber ) throws Exception {
        if (DBController.getDbController().findUsername(userName) != null)
            throw new Exception("duplicate username");
        switch (passwordStrength(password)){
            case 0 -> throw new Exception("password is too short. it has less than 8 characters");
            case 1 -> throw new Exception("password is weak. its strength is 1 of 4, and you can't use it.");
            case 2 -> throw new Exception("password is weak. its strength is 2 of 4, and you can't use it");
            case 3 -> throw new Exception("password is weak. its strength is 3 of 4, and you can't use it");
            case 5 -> throw new Exception("password is too long. it has more than 30 characters");
            case 6 -> throw new Exception("password has invalid character. we can use only 0-9, a-z, A-Z, !@#$%^&*() to make password");
            default -> {
                if (!validPhoneNumber(phoneNumber))
                    throw new Exception("invalid phone number");
                UserAccount account = new UserAccount(name , userName , password  , phoneNumber);
                DBController.getDbController().addUser(account);
                return account;
            }


        }
    }










    /*
    return 0 -> password is too short. it has less than 8 characters
    return 5 -> password is too long. it has more than 30 characters
    return 6 -> password has invalid character. we can use only 0-9, a-z, A-Z, !@#$%^&*() to make password
    return 1 -> password is weak. its strength is 1 of 4, and you can't use it.
    return 2 -> password is weak. its strength is 2 of 4, and you can't use it.
    return 3 -> password is weak. its strength is 3 of 4, and you can't use it.
    return 4 -> password is weak. its strength is 4 of 4, and you can use it. :)
     */

    public static int passwordStrength(String password) {
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


    /*
     if phone number is valid returns true. else returns false.
     */
    public static boolean validPhoneNumber(String phoneNumber) {
        Pattern phoneNumberValidPattern = Pattern.compile("^09[\\d]{9}");
        Matcher phoneNumberMatcher = phoneNumberValidPattern.matcher(phoneNumber);
        return phoneNumberMatcher.matches();
    }


}
