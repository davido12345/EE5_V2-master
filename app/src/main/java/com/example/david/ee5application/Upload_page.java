package com.example.david.ee5application;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.david.ee5application.Databases.InfoArrays;
import com.example.david.ee5application.Databases.Keys;
import com.example.david.ee5application.Databases.Links;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.david.ee5application.Page_Main_Driver.machineID;
import static com.example.david.ee5application.Page_Main_Driver.maxSession;

public class Upload_page extends AppCompatActivity {
    public String TAG = "UPLOAD PAGE:";
    private ProgressBar progressBar;
    public static int maxSessionInDatabase;
    int i = 0;
    long globalSizeStorage = 10;
    ArrayList allDataStored = new ArrayList();
    Database_Session_Storage db2 = null;
    Context mContext;

    //Location

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_page);

        progressBar = findViewById(R.id.Progress);
        progressBar.setMax(100);
        progressBar.setProgress(0);



        Intent start = getIntent();
        Context context = getApplicationContext();
        mContext = context;
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        allDataStored.clear();

        //Location


        if (mWifi.isConnected()) {
            Toast toast = Toast.makeText(context, "WIFI CONNECTED, UPLOAD STARTING", Toast.LENGTH_SHORT);
            toast.show();

            //PART ONE, WITH THE RELEVANT MACHINE ID CHECK THE SESSIONS IN THE DATABASE TO SEE IF THE SESSION IDs NEED TO BE UPDATED IN APP.

            Database_Session_Storage db = new Database_Session_Storage(context);
            int maximumTableStored = db.checkMax();
            long sizeStorage = db.getEntriesCount();
            globalSizeStorage = sizeStorage;

            Log.d(TAG, "THE max table value is: "+i);
                for(int i =0; i<sizeStorage ; i++) {
                    Data_Structure_Packet item = (Data_Structure_Packet) db.getAllSessionData().get(i);
                    allDataStored.add(item);
                    Log.d(TAG, "There are exactly: " + allDataStored.size() + "Stored in the Database");
                    Log.d(TAG, "The ITEM ID: " + item.getPacket_id());
                    Log.d(TAG, "The TIMESTAMP: " + item.getKey_Time());
                    Log.d(TAG, "The SESSION_ID: " + item.getSession_id());
                    Log.d(TAG, "NEW SESS_ID: "+(Page_Main_Driver.maxSession+1));

                    String insertPacketToDataBase = "https://a18ee5mow2.studev.groept.be/InsertSessionData.php?id_Session=" + (item.getSession_id()+Page_Main_Driver.maxSession+2) + "&id_Mower=" + Page_Main_Driver.machineID +
                            "&time_SessionData=" + item.getKey_Time() + "&Gps_x=" + item.getKey_Gps_x() + "&Gps_y=" + item.getKey_Gps_y() + "&Joystick_x=" + item.getKey_Joystick_x() +
                            "&Joystick_y=" + item.getKey_Joystick_y() + "&Joystick_z=" + item.getKey_Joystick_z() + "&Joystick_b1=" + item.getKey_Joystick_b1() +
                            "&Joystick_b2=" + item.getKey_Joystick_b2() + "&Oil_temp=" + item.getKey_Oil_Temp() + "&w_1=" + item.getKey_w_1() + "&x_1=" + item.getKey_x_1() +
                            "&y_1=" + item.getKey_y_1() + "&z_1=" + item.getKey_z_1() + "&w_2=" + item.getKey_w_2() + "&x_2=" + item.getKey_x_2() + "&y_2=" + item.getKey_y_2() + "&z_2=" + item.getKey_z_2() +
                            "&w_3=" + item.getKey_w_3() + "&x_3=" + item.getKey_x_3() + "&y_3=" + item.getKey_y_3() + "&z_3=" + item.getKey_z_3() + "";

                    JSonVolley(insertPacketToDataBase);
                    db.deletePacket(item.getPacket_id(), item.getSession_id());
                }

            //JSonVolley(Links.specificMowerMax+machineID);
            //SOME KIND OF LOOP:



        } else {
            Toast toast = Toast.makeText(context, "WIFI NOT CONNECTED, CONNECT AND TRY AGAIN.", Toast.LENGTH_SHORT);
            toast.show();
        }

        if(start != null && start.getIntExtra("Start",0) == 1)
        {
            for (int i = 0; i < 100; i++) {
                new Thread() {
                    @Override()
                    public void run() {
                        for (int i = 0; i < 100; i++) {
                            try {
                                Thread.sleep(1500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            progressBar.incrementProgressBy(1);
                        }
                    }
                }.start();
            }
        }
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

    //Code to transfer retrieved JSON data into arraylists in the application.
    public void JSonToArray (JSONObject jsonObject, String url) throws Exception {
        Log.d(TAG, "The TRIAL ID RIGHT BEFORE RUNNING LOOP: "+i);
        Log.d(TAG, "Global Size Storage: "+globalSizeStorage);


        }
       /* if (url.equals(Links.specificMowerMax+machineID)) {

            //InfoArrays.maxSession = 5;//
            //Log.d(TAG, "Received from DB MaxSession = "+jsonObject.getInt(Keys.maximumSessionValue));
            //maxSessionInDatabase = jsonObject.getInt(Keys.maximumSessionValue);
            Database_Session_Storage db = new Database_Session_Storage(mContext);
            long dbEntries = db.getEntriesCount();
            Log.d(TAG,"dbEntries are equal to: "+dbEntries);
            int startpoint = db.getAllSessionData().size();

            for(int i = 0;  i<startpoint; i++) {
                //WORKS TO FETCH RECORDS!!!
                Data_Structure_Packet item = (Data_Structure_Packet) db.getAllSessionData().get(i);
                Log.d("SIZE","dbEntries are equal to: "+db.getAllSessionData().size());
                int sessionId = item.getSession_id();

                //item.setSession_id(sessionId + maxSessionInDatabase);
               /* db.deletePacket(packet_Id, sessionId);
                db.addNewPacket(packet_Id, Page_Main_Driver.machineID, item.getKey_Date(), item.getKey_Time(), item.getKey_Gps_x(), item.getKey_Gps_y(), item.getKey_Joystick(),
                        item.getKey_Oil_Temp(), item.getKey_w_1(), item.getKey_y_1(), item.getKey_x_1(), item.getKey_w_2(),
                        item.getKey_y_2(), item.getKey_x_2(), item.getKey_w_3(), item.getKey_y_3(), item.getKey_x_3()
               );*/

/*
                Log.d(TAG, "USING LISTS COMPONENT OF GPS_X: "+item.getKey_Gps_x());
                Log.d(TAG, "SESSION ID!: "+item.getSession_id());
                int sessionIDOfficial = sessionId+maxSessionInDatabase;
                if(sessionIDOfficial == maxSessionInDatabase){
                    sessionIDOfficial++;
                    Log.d(TAG, "EXTRA INDEX");
                }
                Log.d(TAG, "UPLOADING A DATA!");

                String insertPacketToDataBase = "https://a18ee5mow2.studev.groept.be/InsertSessionData.php?id_Session="+(5)+"&id_Mower="+ Page_Main_Driver.machineID+
                        "&time_SessionData="+item.getKey_Time()+"&Gps_x="+item.getKey_Gps_x()+"&Gps_y="+item.getKey_Gps_x()+"&Joystick_x="+item.getKey_Joystick_x()+
                        "&Joystick_y="+item.getKey_Joystick_y()+"&Joystick_z="+item.getKey_Joystick_z()+"&Joystick_b1="+item.getKey_Joystick_b1()+
                        "&Joystick_b2="+item.getKey_Joystick_b2()+"&Oil_temp="+item.getKey_Oil_Temp()+"&w_1="+item.getKey_w_1()+"&x_1="+item.getKey_x_1()+
                        "&y_1="+item.getKey_y_1()+"&z_1="+item.getKey_z_1()+"&w_2="+item.getKey_w_2()+"&x_2="+item.getKey_x_2()+"&y_2="+item.getKey_y_2()+"&z_2="+item.getKey_z_2()+
                        "&w_3="+item.getKey_w_3()+"&x_3="+item.getKey_x_3()+"&y_3="+item.getKey_y_3()+"&z_3="+item.getKey_z_3()+"";

                JSonVolley(insertPacketToDataBase);
            }
            getApplicationContext().deleteDatabase("Session_Data");

        }*/




    public void runVolley(int i){
        //Database_Session_Storage db = new Database_Session_Storage(mContext);


    }

        //Log.d(TAG, "getting size :" + InfoArrays.firstNames.size());

    public void clearOldSessData(){
        InfoArrays.id_sess.clear();
        InfoArrays.id_MowerS.clear();
        InfoArrays.dateS.clear();
        InfoArrays.startTimeS.clear();
        InfoArrays.Duration.clear();
    }
    public void accessDatabase(){
        //db = new CurrencyDBHelper(this);

    }

}
