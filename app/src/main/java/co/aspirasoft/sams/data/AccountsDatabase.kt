package co.aspirasoft.sams.data

import co.aspirasoft.sams.model.Account

object AccountsDatabase {

    operator fun contains(account: Account?): Boolean {
        return true
    }

}