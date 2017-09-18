package com.maldo.instacloning;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity {

    EditText etUsername;
    EditText etPassword;
    Button btnMode;
    TextView tvChangeMode;

    boolean loginMode = true;
    private boolean doubleBackToExitPressedOnce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnMode = (Button) findViewById(R.id.btnMode);
        tvChangeMode = (TextView) findViewById(R.id.tvChangeMode);

        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");

        ParseInit();

        if (ParseUser.getCurrentUser() != null){
            GoToHome();
        }
        onBackPressed();

    }
    private void ParseInit () {
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("9c344d161a974bb20f0cbb5e869a455e2bacb9ef")
                .server("http://ec2-52-14-11-143.us-east-2.compute.amazonaws.com/parse")
                .build()
        );
    }

    public void BtnClicked (View view){

        if(loginMode) {
            ParseUser.logInInBackground(etUsername.getText().toString(), etPassword.getText().toString(), new LogInCallback(){
                @Override
                public void done(ParseUser user, ParseException e) {
                    if(e==null){
                        Toast.makeText(MainActivity.this, "Login Sebagai - " + user.getUsername(), Toast.LENGTH_SHORT).show();
                        GoToHome();
                    } else {
                        Toast.makeText(MainActivity.this, "Login Gagal - " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
            );
        } else {
            ParseUser user = new ParseUser();
            user.setUsername(etUsername.getText().toString());
            user.setPassword(etPassword.getText().toString());

            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Toast.makeText(MainActivity.this, "Berhasil !", Toast.LENGTH_SHORT).show();
                        GoToHome();
                    } else {
                        Toast.makeText(MainActivity.this, "Gagal - " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    public void ChangeMode(View view) {
        if(loginMode) {
            loginMode = false;
            btnMode.setText("Sign UP");
            tvChangeMode.setText("Or, Log In");
        }else {
            loginMode = true;
            btnMode.setText("Log in");
            tvChangeMode.setText("Or, Sign In");
        }
    }

    public void GoToHome(){
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

}
