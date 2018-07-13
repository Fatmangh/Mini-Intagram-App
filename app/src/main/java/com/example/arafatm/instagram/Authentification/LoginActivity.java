package com.example.arafatm.instagram.Authentification;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.arafatm.instagram.Home.TimelineActivity;
import com.example.arafatm.instagram.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText userName;
    private EditText passWord;
    private Button loginButton;
    private TextView signupText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userName = (EditText) findViewById(R.id.pt_userName);
        passWord = (EditText) findViewById(R.id.pt_password);
        loginButton = (Button) findViewById(R.id.bt_login);
        signupText = (TextView) findViewById(R.id.bt_signup);

        /*calls login when the user press the login button*/
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = userName.getText().toString();
                final String password = passWord.getText().toString();
                login(username, password);
            }
        });

        /*go to regsiter page when the user presses the login button*/
        signupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    /*Logs the user into parse*/
    private void login(String username, String password) {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e == null) {
                    Log.e("LoginActivity", "Login succcessful");
                    final Intent intent = new Intent(LoginActivity.this, TimelineActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.e("LoginActivity", "Login Failed");
                    e.printStackTrace();
                }
            }
        });
    }
}





