package com.example.david.ee5application.Databases;


import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class InfoArrays {

    //DON'T USE...
    //this is only for logging in successfully in the main Activity
    //after that ONLY use UserInfo
    public static ArrayList<String> password = new ArrayList<String>();
    public static ArrayList<String> username = new ArrayList<String>();
    public static ArrayList<Integer> id_Mower = new ArrayList<Integer>();
    public static ArrayList<String> type_Mower = new ArrayList<String>();
    public static ArrayList<String> name_Mower = new ArrayList<String>();
    public static ArrayList<Integer> id_workGroup = new ArrayList<Integer>();
    public static ArrayList<Integer> session_id = new ArrayList<Integer>();
    public static ArrayList<Integer> sessionData_id = new ArrayList<Integer>();
    public static ArrayList<Integer> id_dataSD = new ArrayList<Integer>();
    public static ArrayList<String> id_MowerSD = new ArrayList<String>();
    public static ArrayList<String> id_sessSD = new ArrayList<String>();
    public static ArrayList<String> timeSD = new ArrayList<String>();
    public static ArrayList<Double> GPS_XSD = new ArrayList<Double>();
    public static ArrayList<Double> GPS_YSD = new ArrayList<Double>();
    public static ArrayList<Double> JoystickSD = new ArrayList<Double>();
    public static ArrayList<Double> Oil_TempSD = new ArrayList<Double>();

    public static ArrayList<Double> w_1SD = new ArrayList<Double>();
    public static ArrayList<Double> x_1SD = new ArrayList<Double>();
    public static ArrayList<Double> y_1SD = new ArrayList<Double>();
    public static ArrayList<Double> z_1SD = new ArrayList<Double>();

    public static ArrayList<Double> w_2SD = new ArrayList<Double>();
    public static ArrayList<Double> x_2SD = new ArrayList<Double>();
    public static ArrayList<Double> y_2SD = new ArrayList<Double>();
    public static ArrayList<Double> z_2SD = new ArrayList<Double>();

    public static ArrayList<Double> w_3SD = new ArrayList<Double>();
    public static ArrayList<Double> x_3SD = new ArrayList<Double>();
    public static ArrayList<Double> y_3SD = new ArrayList<Double>();
    public static ArrayList<Double> z_3SD = new ArrayList<Double>();

    public static int maxSession = 0;

    public static ArrayList<Integer> id_sess = new ArrayList<Integer>();
    public static ArrayList<Integer> id_MowerS = new ArrayList<Integer>();
    public static ArrayList<String> dateS = new ArrayList<String>();
    public static ArrayList<String> startTimeS = new ArrayList<String>();
    public static ArrayList<String> Duration = new ArrayList<String>();

    public static ArrayList<LatLng> GpsLocations = new ArrayList<LatLng>();
    public static ArrayList<Double> GpsLocationsX = new ArrayList<Double>();
    public static ArrayList<Double> GpsLocationsY = new ArrayList<Double>();

    public InfoArrays() {

    }
}
