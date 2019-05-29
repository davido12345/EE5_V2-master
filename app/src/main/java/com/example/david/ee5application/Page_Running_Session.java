package com.example.david.ee5application;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Page_Running_Session extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Button endActivity = (Button)findViewById(R.id.buttonEndSession);

        //The button that we press once the
        endActivity.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Routine_BT_Data_Receiver.end = true;
                Intent data_check = new Intent(Page_Running_Session.this,  Page_Main_Driver.class);
                startActivity(data_check);
                Context context = getApplicationContext();
                Toast toast = Toast.makeText(context, "Don't Forget to upload the session when you have wifi!!!", Toast.LENGTH_SHORT);
                toast.show();
            }
        });


    }

}
