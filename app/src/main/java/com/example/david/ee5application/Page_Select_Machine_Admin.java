package com.example.david.ee5application;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

public class Page_Select_Machine_Admin extends AppCompatActivity {
    public static int machineSelected;
    ListView listView;
    String TAG = "David: ";
    public ArrayList<String> MachineList = new ArrayList<>();
    public static int machine_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_checking);
        clearOldSessData();
        listView = findViewById(R.id.listView);
        MachineList.clear();
        Context context = getApplicationContext();

        //ListView
        setListView();
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, MachineList);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


        //When an item is clicked we must redirect to a new page for the sessions of this one
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                machineSelected = position;
                Log.d(TAG, "The machine selected is "+machineSelected);
                String name = MachineList.get(position); //The name of the machine which is clicked.
                machine_ID = InfoArrays.id_MowerS.get(position);
                Log.d(TAG, " How much data received: "+InfoArrays.id_MowerS.size());
                String fetchURL = Links.distinctSessionsFromSD+InfoArrays.id_MowerS.get(position);
                Log.d(TAG, "THE URL ACCESSED IS: "+fetchURL);
                clearOldSessData();
                Context context = getApplicationContext();
                Toast toast = Toast.makeText(context, "Retrieving Session Data From Database", Toast.LENGTH_SHORT);
                toast.show();
                JSonVolley(fetchURL);


           }
        });
    }

    //Inits List View
    public void setListView(){
        for(int i =0; i<InfoArrays.id_MowerS.size(); i++) {
            Log.d(TAG, "Adding ArrayList Elements");
            MachineList.add("Machine "+InfoArrays.id_MowerS.get(i));
            Log.d(TAG, "Done Adding ArrayList Elements");
        }
        Log.d(TAG, MachineList.size()+" Is the size of machinelist");
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

                    //TRY HERE
                    Intent intent = new Intent(Page_Select_Machine_Admin.this, Page_Select_Session_Admin.class);
                    startActivity(intent);
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
        if (url.equals(Links.allMowerData)) {
            InfoArrays.type_Mower.add(jsonObject.getString(Keys.Type));
            InfoArrays.id_Mower.add(jsonObject.getInt(Keys.id_Mower));
            InfoArrays.id_workGroup.add(jsonObject.getInt(Keys.id_workGround));
            InfoArrays.name_Mower.add(jsonObject.getString(Keys.name_Mower));

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

        } else {

            InfoArrays.id_sess.add(jsonObject.getInt(Keys.id_sess));

        }
        //Log.d(TAG, "getting size :" + InfoArrays.firstNames.size());
    }

    //Deletes existing data in arraylist to prevent duplicates.
    public void clearOldSessData(){
        InfoArrays.id_sess.clear();
    }


}
