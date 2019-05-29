package com.example.david.ee5application;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Page_Login extends AppCompatActivity {
        String TAG = "Login Page: ";
        String passCheck;
        String password;
        ArrayList<String> validUsers = new ArrayList<String>();
        ArrayList<String> validPasswords = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        final EditText usernameField = (EditText)findViewById(R.id.loginUser);
        final EditText passwordField = (EditText)findViewById(R.id.loginPass);
        Button Login = (Button)findViewById(R.id.loginButton);

        //Application must contain the valid login as there is no internet connection to confirm driver identities!
        //ENTER ALL VALID USERNAME/PASSWORD COMBINATION BELOW!

        validUsers.add("admin");
        validPasswords.add("admin");

        validUsers.add("driver");
        validPasswords.add("driver");




        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss:SSSS");
        String formattedDate = df.format(c.getTime());
        // formattedDate have current date/time
        //Toast.makeText(this, latitudes, Toast.LENGTH_SHORT).show();




        //Check Login details once the login is verified.
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(usernameField.getText().toString().equals(validUsers.get(0)) && passwordField.getText().toString().equals(validPasswords.get(0)))
                {
                    Intent admin = new Intent(Page_Login.this, Page_Main_Admin.class);
                    startActivity(admin);
                }
                else if(usernameField.getText().toString().equals(validUsers.get(1)) && passwordField.getText().toString().equals(validPasswords.get(1)))
                {
                    Intent driver = new Intent(Page_Login.this, Page_Main_Driver.class);
                    startActivity(driver);
                }
                else
                {
                    Context context = getApplicationContext();
                    Toast toast = Toast.makeText(context, "WRONG PASSWORD OR USERNAME!", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });


    }

    }





