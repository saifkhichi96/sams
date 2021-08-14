package co.aspirasoft.sams.controller;

import co.aspirasoft.sams.model.users.accounts.Account;
import co.aspirasoft.sams.model.users.accounts.AccountsDatabase;

public class LoginController {

    private final String VIEW_INCOMPLETE = "Some fields are incomplete.";
    private final String VIEW_MISMATCH   = "Invalid username/password combination.";
    private final String VIEW_CONNECT_ERR= "Check your internet connection.";
    private final String VIEW_SUCCESS    = "Login successful.";

    private Account account;

    public LoginController(String username, String password){
        account = new Account(username, password);
    }

    public String execute() {
        String username = account.getUsername();
        String password = account.getPassword();

        if (exists(username) && exists(password)) {

            AccountsDatabase db = AccountsDatabase.getInstance();

            if (db.contains(account)) {
                return VIEW_SUCCESS;
            } else {
                return VIEW_MISMATCH;
            }

        } else {
            return VIEW_INCOMPLETE;
        }

    }

    /**
     * Checks if the given string exists and is not empty. Whitespaces
     * are treated as empty characters.
     *
     * @param string the string to be checked
     * @return true if string has a non-empty value
     */
    private boolean exists(String string) {
        if (string != null) {

            // List of whitespace characters
            String[] whitespaces = new String[]{" ", "\n", "\t"};

            // Remove white spaces
            for (String space : whitespaces) {
                string = string.replace(space, "");
            }

            // Return true if string not empty
            if (!string.equals("")) {
                return true;
            }
        }

        // Return false if string null or empty
        return false;
    }
}
