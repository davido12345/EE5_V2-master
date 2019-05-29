package com.example.david.ee5application;

import android.content.Intent;
import android.icu.text.IDNA;
import android.media.MediaCas;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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

import static com.example.david.ee5application.Page_Select_Machine_Admin.machineSelected;

public class Page_Select_Session_Admin extends AppCompatActivity {
    public ArrayList<String> SessionList = new ArrayList<>();
    String TAG = "Loading Page";
    public int machineID = machineSelected;
    public static int session_ID;
    String fetchURL = Links.specificSessions+machineID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading__page_1);
        Log.d(TAG, "Page Created");
        Log.d(TAG, "THE SIZE OF ARRAYLIST CONTAINING PREVIOUS QUERY IS: "+InfoArrays.id_sess.size());
        //Set Textbox
        TextView dataStatus = (TextView)findViewById(R.id.dataStatus);
        dataStatus.setText("Data Retreival Dependant on Network Speed!");
        SessionList.clear();

        //Set Page Title
        TextView pageTitle = (TextView)findViewById(R.id.textViewSessions);
        pageTitle.setText("Machine "+ InfoArrays.id_MowerS.get(machineID)+" Session List");


        ListView listView = findViewById(R.id.listViewSessions);
        listView.setAdapter(null);

        setListView();
        //ListView
    }


    //Fills ListView with information
    public void setListView(){
        SessionList.clear();
        for(int i =0; i<InfoArrays.id_sess.size(); i++) {
            ListView listView = findViewById(R.id.listViewSessions);
            Log.d(TAG, "Adding ArrayList Elements for MACHINE NUMBER: "+machineID);
            /*if(InfoArrays.id_MowerS.get(i) == machineID) {

                SessionList.add("Session ID: " + InfoArrays.id_sess.get(i));
            }
            */
            SessionList.add("Session id: "+InfoArrays.id_sess.get(i));
            Log.d(TAG, "Done Adding ArrayList Elements");


            ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, SessionList);

            listView.setAdapter(arrayAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


                //When an item is clicked we must redirect to a new page for the sessions of this one
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Log.d(TAG, "The machine selected is " + machineSelected);
                    String name = SessionList.get(position); //The name of the machine which is clicked.
                    session_ID = InfoArrays.id_sess.get(position);
                    Log.d(TAG, " How much data received: "+InfoArrays.id_MowerS.size());
                    JSonVolley(Links.allSessionData);

                }
            });

        }
        Log.d(TAG, SessionList.size()+" Is the size of Session List");
        //ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, MachineList);
        //listView.setAdapter(arrayAdapter);
    }

    //Code to send a JSON volley to the DB
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
                    //Fetching all session data then switch.
                    if(url == Links.allSessionData) {

                        Intent intent = new Intent(Page_Select_Session_Admin.this, Page_Arm_State.class);
                        startActivity(intent);

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

    //Code to transfer retrieved JSON data into arraylists in the application.
    public void JSonToArray (JSONObject jsonObject, String url) throws Exception {
        if (url.equals(Links.allMowerData)) {
            InfoArrays.type_Mower.add(jsonObject.getString(Keys.Type));
            InfoArrays.id_Mower.add(jsonObject.getInt(Keys.id_Mower));
            InfoArrays.id_workGroup.add(jsonObject.getInt(Keys.id_workGround));
            InfoArrays.name_Mower.add(jsonObject.getString(Keys.name_Mower));
            Log.d(TAG, "Name INPUT");

        } else if (url.equals(Links.allSessionData)) {

            InfoArrays.sessionData_id.add(jsonObject.getInt(Keys.session_data_id));
            InfoArrays.session_id.add(jsonObject.getInt(Keys.session_id));
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

        } else if (url.equals(Links.allSessions)) {

            InfoArrays.id_sess.add(jsonObject.getInt(Keys.id_sess));
            InfoArrays.id_MowerS.add(jsonObject.getInt(Keys.id_Mower));
            Log.d(TAG, "MOWER DATA RECEIVED");
            InfoArrays.dateS.add(jsonObject.getString(Keys.session_date));
            InfoArrays.startTimeS.add(jsonObject.getString(Keys.session_startTime));
            InfoArrays.Duration.add(jsonObject.getString(Keys.session_Duration));
        } else if (url.equals(Links.specificSessions+machineID)) {

            InfoArrays.id_sess.add(jsonObject.getInt(Keys.id_sess));
            InfoArrays.id_MowerS.add(jsonObject.getInt(Keys.id_Mower));
            Log.d(TAG, "MOWER DATA RECEIVED");
            InfoArrays.dateS.add(jsonObject.getString(Keys.session_date));
            InfoArrays.startTimeS.add(jsonObject.getString(Keys.session_startTime));
            InfoArrays.Duration.add(jsonObject.getString(Keys.session_Duration));
        }
    }




}
