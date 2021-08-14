package co.aspirasoft.sams;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import co.aspirasoft.sams.controller.LoginController;

public class ActivitySignIn extends AppCompatActivity implements View.OnClickListener {

    EditText username;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.login_username);
        password = (EditText) findViewById(R.id.login_password);

        findViewById(R.id.login).setOnClickListener(this);
        findViewById(R.id.createAccount).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.createAccount:
                startActivity(new Intent(getApplicationContext(), ActivitySignUp.class));
                break;
            case R.id.login:
                String user = username.getText().toString();
                String pwd  = password.getText().toString();

                LoginController controller = new LoginController(user, pwd);
                String response = controller.execute();
                if (username.getText().toString().equals("") || password.getText().toString().equals(""))
                    Toast.makeText(getApplicationContext(),
                            "Some fields are incomplete.",
                            Toast.LENGTH_SHORT).show();

                    // Check login details
                else {
                    SharedPreferences sharedPreferences = getSharedPreferences(ActivitySplash.PREFS_FILE, 0);
                    user = sharedPreferences.getString("Username", null);
                    pwd = sharedPreferences.getString("Password", null);

                    // Incorrect details.
                    if (!username.getText().toString().equals(user) || !password.getText().toString().equals(pwd))
                        Toast.makeText(getApplicationContext(),
                                "Invalid usernameField/passwordField combination.",
                                Toast.LENGTH_SHORT).show();

                        // Registration incomplete
                    else if (!sharedPreferences.getBoolean("userRegistered", false)) {
                        Intent intent = new Intent(getApplicationContext(), FragmentUserDetails.class);
                        intent.putExtra("Prompt", "Please complete the registration process!");
                        startActivity(intent);
                    }

                    // Correct details. Register session and advance to dashboard
                    else {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("isActiveSession", true);
                        editor.apply();

                        startActivity(new Intent(getApplicationContext(), ActivitySchedule.class));
                    }
                }
                break;
        }
    }
}
