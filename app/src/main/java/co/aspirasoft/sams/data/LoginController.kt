package co.aspirasoft.sams.data

import co.aspirasoft.sams.model.Account

class LoginController(username: String, password: String) {

    private val VIEW_INCOMPLETE = "Some fields are incomplete."
    private val VIEW_MISMATCH = "Invalid username/password combination."
    private val VIEW_CONNECT_ERR = "Check your internet connection."
    private val VIEW_SUCCESS = "Login successful."
    private val account: Account = Account(username, password)

    fun execute(): String {
        val username = account.username
        val password = account.password
        return if (exists(username) && exists(password)) {
            val db = AccountsDatabase
            if (db.contains(account)) {
                VIEW_SUCCESS
            } else {
                VIEW_MISMATCH
            }
        } else {
            VIEW_INCOMPLETE
        }
    }

    /**
     * Checks if the given string exists and is not empty. Whitespaces
     * are treated as empty characters.
     *
     * @param string the string to be checked
     * @return true if string has a non-empty value
     */
    private fun exists(s: String?): Boolean {
        var string: String? = s
        if (string != null) {

            // List of whitespace characters
            val whitespaces = arrayOf(" ", "\n", "\t")

            // Remove white spaces
            for (space in whitespaces) {
                string = string!!.replace(space, "")
            }

            // Return true if string not empty
            return string != ""
        }

        // Return false if string null or empty
        return false
    }

}