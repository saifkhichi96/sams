package co.aspirasoft.sams.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import co.aspirasoft.sams.R
import co.aspirasoft.sams.data.LoginController
import co.aspirasoft.sams.ui.fragment.FragmentUserDetails

class ActivitySignIn : AppCompatActivity(), View.OnClickListener {

    private lateinit var username: EditText
    private lateinit var password: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        username = findViewById(R.id.login_username)
        password = findViewById(R.id.login_password)
        findViewById<View>(R.id.login).setOnClickListener(this)
        findViewById<View>(R.id.createAccount).setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.createAccount -> startActivity(Intent(applicationContext, ActivitySignUp::class.java))
            R.id.login -> {
                var user = username.text.toString()
                var pwd = password.text.toString()
                val controller = LoginController(user, pwd)
                val response = controller.execute()
                if (username.text.toString() == "" || password.text.toString() == "") Toast.makeText(
                    applicationContext,
                    "Some fields are incomplete.",
                    Toast.LENGTH_SHORT
                ).show() else {
                    val sharedPreferences = getSharedPreferences(ActivitySplash.PREFS_FILE, 0)
                    user = sharedPreferences.getString("Username", null) ?: ""
                    pwd = sharedPreferences.getString("Password", null) ?: ""

                    // Incorrect details.
                    if (username.text.toString() != user || password.text.toString() != pwd) Toast.makeText(
                        applicationContext,
                        "Invalid usernameField/passwordField combination.",
                        Toast.LENGTH_SHORT
                    ).show() else if (!sharedPreferences.getBoolean("userRegistered", false)) {
                        val intent = Intent(applicationContext, FragmentUserDetails::class.java)
                        intent.putExtra("Prompt", "Please complete the registration process!")
                        startActivity(intent)
                    } else {
                        val editor = sharedPreferences.edit()
                        editor.putBoolean("isActiveSession", true)
                        editor.apply()
                        startActivity(Intent(applicationContext, ActivitySchedule::class.java))
                    }
                }
            }
        }
    }
}