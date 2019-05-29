package com.example.david.ee5application;

import android.content.Context;
import android.content.Intent;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Page_Main_Admin extends AppCompatActivity {
    String TAG = "JSON REQUEST:";

    //Buttons onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.administrator_page);
        clearOldMachineData();


        Button Data_checking = (Button)findViewById(R.id.Data_check);
        Button Map_checking = (Button)findViewById(R.id.Map_check);

        Data_checking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();
                Toast toast = Toast.makeText(context, "Retrieving Machine Data From Database", Toast.LENGTH_SHORT);
                toast.show();
                //JSonVolley(Links.allMowerData);

                JSonVolley(Links.allMowerDataFromSD);

            }
        });

        Map_checking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent map_check = new Intent(Page_Main_Admin.this, Page_Map.class);
                startActivity(map_check);
                finish();
            }
        });


    }

    //URL request to webpage over the internet
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
                    Intent data_check = new Intent(Page_Main_Admin.this, Page_Select_Machine_Admin.class);
                    startActivity(data_check);
                    finish();

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
        if(url.equals(Links.allMowerData)) {
            InfoArrays.type_Mower.add(jsonObject.getString(Keys.Type));
            InfoArrays.id_Mower.add(jsonObject.getInt(Keys.id_Mower));
            InfoArrays.id_workGroup.add(jsonObject.getInt(Keys.id_workGround));
            InfoArrays.name_Mower.add(jsonObject.getString(Keys.name_Mower));

        }else if(url.equals(Links.allSessionData)) {

            InfoArrays.id_dataSD.add(jsonObject.getInt(Keys.session_data_id));
            InfoArrays.id_MowerSD.add(jsonObject.getString(Keys.session_mower_id));
            InfoArrays.timeSD.add(jsonObject.getString(Keys.session_data_time));
            InfoArrays.GPS_XSD.add(jsonObject.getDouble(Keys.session_data_Gps_x));
            InfoArrays.GPS_YSD.add(jsonObject.getDouble(Keys.session_data_Gps_y));
            InfoArrays.JoystickSD.add(jsonObject.getDouble(Keys.session_data_Joystic));
            InfoArrays.Oil_TempSD.add(jsonObject.getDouble(Keys.session_data_Oil_temp));

            InfoArrays.w_1SD.add(jsonObject.getDouble(Keys.session_data_w_1));
            InfoArrays.x_1SD.add(jsonObject.getDouble(Keys.session_data_x_1));
            InfoArrays.y_1SD.add(jsonObject.getDouble(Keys.session_data_y_1));
            InfoArrays.z_1SD.add(jsonObject.getDouble(Keys.session_data_z_1));

            InfoArrays.w_2SD.add(jsonObject.getDouble(Keys.session_data_w_2));
            InfoArrays.x_2SD.add(jsonObject.getDouble(Keys.session_data_x_2));
            InfoArrays.y_2SD.add(jsonObject.getDouble(Keys.session_data_y_2));
            InfoArrays.z_2SD.add(jsonObject.getDouble(Keys.session_data_z_2));

            InfoArrays.w_3SD.add(jsonObject.getDouble(Keys.session_data_w_3));
            InfoArrays.x_3SD.add(jsonObject.getDouble(Keys.session_data_x_3));
            InfoArrays.y_3SD.add(jsonObject.getDouble(Keys.session_data_y_3));
            InfoArrays.z_3SD.add(jsonObject.getDouble(Keys.session_data_z_3));

        }else if(url.equals(Links.allSessions)) {

            InfoArrays.id_sess.add(jsonObject.getInt(Keys.id_sess));
            InfoArrays.id_MowerS.add(jsonObject.getInt(Keys.id_Mower));
            InfoArrays.dateS.add(jsonObject.getString(Keys.session_date));
            InfoArrays.startTimeS.add(jsonObject.getString(Keys.session_startTime));
            InfoArrays.Duration.add(jsonObject.getString(Keys.session_Duration));

        }else if(url.equals((Links.allMowerDataFromSD))){

            InfoArrays.id_MowerS.add(jsonObject.getInt(Keys.id_Mower));
        }
        //Log.d(TAG, "getting size :" + InfoArrays.firstNames.size());

    }

    //Empties existing arrayLists when we reopen this page to prevent duplicate data.
    public void clearOldMachineData(){
        InfoArrays.type_Mower.clear();
        InfoArrays.id_Mower.clear();
        InfoArrays.id_workGroup.clear();
        InfoArrays.name_Mower.clear();
        InfoArrays.id_MowerS.clear();

    }
}
