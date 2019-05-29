package com.example.david.ee5application;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.david.ee5application.Databases.Keys;
import com.example.david.ee5application.Databases.Links;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Page_Main_Driver extends AppCompatActivity {

    //IMPORTANT TO SETUP THIS VARIABLE.
    //*************************************************************************************************************
    public static final int machineID = 2; // SET IN THE CONFIG
    //*************************************************************************************************************
    String TAG = "DRIVERPAGE: ";
    public static int maxSession;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_mainpage);

        Button Upload = (Button)findViewById(R.id.UploadButton);
        Button ConnectToBluetooth = (Button)findViewById(R.id.buttonBluetooth);

        Context context = getApplicationContext();
        Database_Session_Storage db = new Database_Session_Storage(context);
        if(db.getEntriesCount() == 24)
        {
            getApplicationContext().deleteDatabase("Session_Data");
        }

        //Button to open the upload class
        Upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            JSonVolley(Links.specificMowerMax);

            }
        });

        //Button to open Bluetooth pairing
        ConnectToBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent blueToothPairing = new Intent(Page_Main_Driver.this, Page_Finding_Bluetooth_Device.class);
                startActivity(blueToothPairing);

            }
        });


    }
    public void JSonVolley(final String url) {
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, "got a response");
                //manipulate response
                try {
                    Log.d(TAG, "REQUEST SENDING");
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        Log.d(TAG, "DATA RECEIVED");
                        try {
                            JSonToArray(jsonObject, url);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                    Log.d(TAG, "TRY HERE SPACE REACHED");
                    //TRY HERE
                    Intent data_upload = new Intent(Page_Main_Driver.this,Upload_page.class);
                    data_upload.putExtra("Start",1);
                    startActivity(data_upload);
                    //finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(jsonArrayRequest);
    }

    public void JSonToArray (JSONObject jsonObject, String url) throws Exception {
        jsonObject.getInt(Keys.maximumSessionValue);
        Log.d(TAG, "Received from DB MaxSession = "+jsonObject.getInt(Keys.maximumSessionValue));
        maxSession = jsonObject.getInt(Keys.maximumSessionValue);



    }

}
