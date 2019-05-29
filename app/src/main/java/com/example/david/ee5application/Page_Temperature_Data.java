package com.example.david.ee5application;

import android.content.Context;
import android.icu.text.IDNA;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.david.ee5application.Databases.InfoArrays;
import com.example.david.ee5application.Databases.Links;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

public class Page_Temperature_Data extends AppCompatActivity {
    ArrayList<Double> temperatures = new ArrayList<>();
    ArrayList<String> timestamps = new ArrayList<>();
    //ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temperature_sensor_data_page);

        final GraphView graph = (GraphView) findViewById(R.id.graph);
        for(int i = 0; i<InfoArrays.Oil_TempSD.size(); i++)
        {
            temperatures.add(InfoArrays.Oil_TempSD.get(i));
            timestamps.add(InfoArrays.timeSD.get(i));
        }

        try {
            ;
            int size = InfoArrays.timeSD.size();
            DataPoint[] values = new DataPoint[size];
            for(int i = 0; i<temperatures.size(); i++){

                values[i] = new DataPoint(i, temperatures.get(i));
                Log.d("DATAREVIEW", "The number of datas is: "+i);
            }
            LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(values);
            graph.addSeries(series);
            //listView = findViewById(R.id.tempData);
        } catch (IllegalArgumentException e) {

        }

        //Pitches = InfoArrays.w_1SD;


    }


}
