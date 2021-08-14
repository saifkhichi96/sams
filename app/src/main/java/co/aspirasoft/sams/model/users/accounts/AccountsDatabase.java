package co.aspirasoft.sams.model.users.accounts;


public class AccountsDatabase {
    private static final AccountsDatabase ourInstance = new AccountsDatabase();

    private AccountsDatabase() {

    }

    public static AccountsDatabase getInstance() {
        return ourInstance;
    }

    public boolean contains(Account account) {
        return true;
    }
}