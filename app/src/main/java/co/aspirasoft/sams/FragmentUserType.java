package co.aspirasoft.sams;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class FragmentUserType extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_type);

        findViewById(R.id.user_student).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        SharedPreferences.Editor editor = getSharedPreferences(ActivitySplash.PREFS_FILE, 0).edit();
        switch (v.getId()) {
            case R.id.user_student:
                editor.putString("User Type", "Student");
                startActivity(new Intent(getApplicationContext(), FragmentStudentType.class));
                break;
        }
        editor.apply();
    }
}