package com.example.david.ee5application;

import android.content.Context;
import android.content.Intent;
import android.icu.text.IDNA;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.google.android.gms.maps.model.LatLng;
import com.EE5.arm.UnityPlayerActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.david.ee5application.Page_Select_Machine_Admin.machineSelected;

public class Page_Arm_State extends AppCompatActivity {
    int machineID = machineSelected;

    String TAG = "ArmStatePage: ";
    public static int m_ID = 0;
    public static int s_ID = 0;
    public static String mapsURL = "";
    public static String temperatureURL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.arm_state_page);
        m_ID = Page_Select_Machine_Admin.machine_ID;
        s_ID = Page_Select_Session_Admin.session_ID;

        Button mapCheck = (Button)findViewById(R.id.mapButtonArm);

        mapCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String queryURL = Links.specificSessionsGPS+m_ID+"&id_Session="+s_ID;
                Context context = getApplicationContext();
                Toast toast = Toast.makeText(context, "Preparing Map", Toast.LENGTH_SHORT);
                toast.show();
                mapsURL = queryURL;
                clearOldSessData();
                JSonVolley(queryURL);


            }
        });

        Button Login = (Button)findViewById(R.id.button4);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent unity = new Intent(Page_Arm_State.this, UnityPlayerActivity.class);
                unity.putExtra("arguments", String.format ("%d %d", m_ID, s_ID));
                startActivity(unity);

            }
        });

        Button sensorCheck = (Button)findViewById(R.id.button5);

        sensorCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String queryURL = Links.temperatureData+m_ID+"&id_Session="+s_ID;
                Context context = getApplicationContext();
                Toast toast = Toast.makeText(context, "Fetching temperature data", Toast.LENGTH_SHORT);
                toast.show();
                temperatureURL = queryURL;
                clearOldSessData();
                JSonVolley(queryURL);



            }
        });
    }
    //Sends URL queries over the internet
    public void JSonVolley(final String url) {
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, "got a response");
                //manipulate response
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);

                        try {
                            JSonToArray(jsonObject, url);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    //TRY NEW ACTIVITY LAUNCH HERE
                    if (url.equals(mapsURL)) {
                    Intent Map = new Intent(Page_Arm_State.this, Page_Map.class);
                    startActivity(Map);
                    finish();
                    } else if (url.equals(temperatureURL)) {
                        Intent data1 = new Intent(Page_Arm_State.this, Page_Temperature_Data.class);
                        startActivity(data1);
                        finish();
                    }

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

    //Decodes received GPS information
    public void JSonToArray (JSONObject jsonObject, String url) throws Exception {

        if (url.equals(mapsURL)) {
            LatLng temp = new LatLng(jsonObject.getDouble(Keys.session_data_Gps_x), jsonObject.getDouble(Keys.session_data_Gps_y));
            InfoArrays.GpsLocations.add(temp);
            InfoArrays.GpsLocationsX.add(jsonObject.getDouble(Keys.session_data_Gps_x));
            InfoArrays.GpsLocationsY.add(jsonObject.getDouble(Keys.session_data_Gps_y));
        } else if (url.equals(temperatureURL)) {
            InfoArrays.Oil_TempSD.add(jsonObject.getDouble(Keys.session_data_Oil_temp));
            InfoArrays.timeSD.add(jsonObject.getString(Keys.session_data_time));
            //Log.d(TAG, "getting size :" + InfoArrays.firstNames.size());
        }
    }

    //Empties existing data in the arraylists to prevent duplicates.
    public void clearOldSessData(){
        InfoArrays.GpsLocationsX.clear();
        InfoArrays.GpsLocationsY.clear();
        InfoArrays.GpsLocations.clear();
        InfoArrays.Oil_TempSD.clear();
        InfoArrays.timeSD.clear();

    }
}
