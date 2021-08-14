package co.aspirasoft.sams;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.appcompat.app.AppCompatActivity;

public class ActivitySplash extends AppCompatActivity {

    public static final String PREFS_FILE = "PreferencesFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new CountDownTimer(1500, 10) {

            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                SharedPreferences sharedPreferences = getSharedPreferences(PREFS_FILE, 0);
                Boolean userKnown = sharedPreferences.getBoolean("isActiveSession", false);

                if (userKnown)
                    startActivity(new Intent(getApplicationContext(), ActivitySchedule.class));
                else
                    startActivity(new Intent(getApplicationContext(), ActivitySignIn.class));

                finish();
            }
        }.start();
    }
}