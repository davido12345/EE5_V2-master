package com.example.david.ee5application;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.david.ee5application.Databases.InfoArrays;
import com.example.david.ee5application.Databases.Links;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;


public class Page_Map extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String TAG = "Google Map";
    ArrayList<Marker> MarkerList = new ArrayList<Marker>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Log.d(TAG,"The arraylist contains: "+ InfoArrays.GpsLocations.size());
    }

    //Comment from google API:
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Adds all the marker coordinates from the arraylist onto the map
        for (int i=0; i<InfoArrays.GpsLocations.size(); i++)
        {
            LatLng temp = InfoArrays.GpsLocations.get(i);
            Log.d(TAG, "The value of the coordinate added: "+temp);
            Marker newMarker = mMap.addMarker(new MarkerOptions().position(temp));
            MarkerList.add(newMarker);
        }

        //Draws a polyline between all the coordinates in order to draw the route of the mower.
        PolylineOptions rectOptions = new PolylineOptions().color(Color.RED).width(2);
        for(int i = 0; i<MarkerList.size(); i++) {
            LatLng temp = InfoArrays.GpsLocations.get(i);
            rectOptions.add(new LatLng(InfoArrays.GpsLocationsX.get(i), InfoArrays.GpsLocationsY.get(i)));
        }
        Polyline polyline = mMap.addPolyline(rectOptions);

        //Zooms camera to relevant location so user is not looking at the whole earth.
        if(InfoArrays.GpsLocations.size()>0) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(InfoArrays.GpsLocationsX.get(0), InfoArrays.GpsLocationsY.get(0)), 12.0f));
        }
    }
}
