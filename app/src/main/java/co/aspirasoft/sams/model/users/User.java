package co.aspirasoft.sams.model.users;

import co.aspirasoft.sams.model.users.accounts.Account;

public abstract class User {

    private int id;
    private String name;
    private Account account;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
