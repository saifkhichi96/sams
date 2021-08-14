package co.aspirasoft.sams.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import co.aspirasoft.sams.R

class ActivitySplash : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        object : CountDownTimer(1500, 10) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                val sharedPreferences = getSharedPreferences(PREFS_FILE, 0)
                val userKnown = sharedPreferences.getBoolean("isActiveSession", false)
                if (userKnown) startActivity(
                    Intent(
                        applicationContext,
                        ActivitySchedule::class.java
                    )
                ) else startActivity(
                    Intent(
                        applicationContext, ActivitySignIn::class.java
                    )
                )
                finish()
            }
        }.start()
    }

    companion object {
        const val PREFS_FILE = "PreferencesFile"
    }

}