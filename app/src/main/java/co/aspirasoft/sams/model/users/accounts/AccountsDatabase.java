package co.aspirasoft.sams.model.users.accounts;


public class AccountsDatabase {
    private static AccountsDatabase ourInstance = new AccountsDatabase();

    public static AccountsDatabase getInstance() {
        return ourInstance;
    }

    private AccountsDatabase() {

    }

    public boolean contains(Account account) {
        return true;
    }
}