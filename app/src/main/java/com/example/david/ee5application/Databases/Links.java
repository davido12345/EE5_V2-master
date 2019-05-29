package com.example.david.ee5application.Databases;

public class Links {
    public static final String getUsername = "https://a18ee5mow2.studev.groept.be/GetUserName.php?Usernames=";  //%27admin%27"
    public static final String getPassword = "https://a18ee5mow2.studev.groept.be/GetPassword.php?Password="; //%27admin%27"
    public static final String allMowerData = "https://a18ee5mow2.studev.groept.be/GetAllMowerData.php";
    public static final String allSessionData = "https://a18ee5mow2.studev.groept.be/GetAllSessionData.php";
    public static final String allSessions = "https://a18ee5mow2.studev.groept.be/GetAllSessions.php";
    public static final String specificSessions = "https://a18ee5mow2.studev.groept.be/GetSpecificSessions.php?mow_id=";
    public static final String specificSessionsGPS = "https://a18ee5mow2.studev.groept.be/GetGPSData.php?id_Mower=";
    public static final String specificMowerMax = "https://a18ee5mow2.studev.groept.be/GetSpecificMowerMaxSession.php";
    public static final String insertPacket = "https://a18ee5mow2.studev.groept.be/InsertSessionData.php?id_SessionData=1&id_Session=2&id_Mower=3&time_SessionData=4&Gps_x=5&Gps_y=6&Joystick=7&Oil_temp=8&w_1=9&x_1=10&y_1=11&w_2=12&x_2=13&y_2=14&w_3=15&x_3=16&y_3=17";
    public static final String allMowerDataFromSD = "https://a18ee5mow2.studev.groept.be/GetAllMowerDataFromSD.php";
    public static final String distinctSessionsFromSD = "https://a18ee5mow2.studev.groept.be/GetSessionsFromSD.php?id_Mower=";
    public static final String temperatureData = "https://a18ee5mow2.studev.groept.be/GetTemperatureData.php?id_Mower=";
}
