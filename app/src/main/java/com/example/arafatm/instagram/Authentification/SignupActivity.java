package com.example.arafatm.instagram.Authentification;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.arafatm.instagram.Home.TimelineActivity;
import com.example.arafatm.instagram.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignupActivity extends AppCompatActivity {

    private EditText Name;
    private EditText userName;
    private EditText passWord;
    private EditText repPassWord;
    private EditText email;
    private Button signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Name = (EditText) findViewById(R.id.pt_name);
        userName = (EditText) findViewById(R.id.pt_userName);
        passWord = (EditText) findViewById(R.id.pt_password);
        repPassWord = (EditText) findViewById(R.id.pt_password_rep);
        email = (EditText) findViewById(R.id.pt_email);
        signupButton = (Button) findViewById(R.id.bt_signup1);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View view) {
                //get all inputs
                final String name = Name.getText().toString();
                final String username = userName.getText().toString();
                final String password = passWord.getText().toString();
                final String password_rep = repPassWord.getText().toString();
                final String Email = email.getText().toString();

                if (name.isEmpty() || username.isEmpty() || password.isEmpty() || Email.isEmpty()) {
                    Toast.makeText(SignupActivity.this, R.string.all_fields_required, Toast.LENGTH_SHORT).show();
                } else {
                    //check for inout validity
                    if (!(password != password_rep)) {
                        Toast.makeText(SignupActivity.this, R.string.error_mismatch_password, Toast.LENGTH_SHORT).show();
                    } else {
                        //registers the user
                        register(name, username, password, Email);
                    }
                }
            }
        });
    }

    private void register(String name, String username, String password, String email) {
        // Create the ParseUser
        ParseUser user = new ParseUser();
        // Set core properties
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);

        // Set custom properties
        user.put("name", name);
        // Invoke signUpInBackground
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    Log.e("SignupActivity", "Register succcessful");
                    final Intent intent = new Intent(SignupActivity.this, TimelineActivity.class);
                    startActivity(intent);
                } else {
                    Log.e("SignupActivity", "Register failed");
                    e.printStackTrace();
                }
            }
        });
    }

}
