package co.aspirasoft.sams.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import co.aspirasoft.sams.R
import co.aspirasoft.sams.ui.fragment.FragmentStudentType

class ActivityUserType : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_type)

        findViewById<View>(R.id.user_student).setOnClickListener(this)
    }

    override fun onClick(v: View) {
        val editor = getSharedPreferences(ActivitySplash.PREFS_FILE, 0).edit()
        when (v.id) {
            R.id.user_student -> {
                editor.putString("User Type", "Student")
                startActivity(Intent(applicationContext, FragmentStudentType::class.java))
            }
        }
        editor.apply()
    }

}