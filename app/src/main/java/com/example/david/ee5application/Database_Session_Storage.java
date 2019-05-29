package com.example.david.ee5application;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.Calendar;

import java.util.ArrayList;
import java.util.List;

import static com.example.david.ee5application.Routine_BT_Data_Receiver.TAG2;

public class Database_Session_Storage extends SQLiteOpenHelper {
    //TO DO: PACKET IDS ARE NOT ENOUGH TO SELECT APPROPRIATELY, NEED PACKET ID AND SESSION ID.
    public static int storedCount;
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Session_Datas";
    public static final String TABLE_NAME = "Session_Data";
    public static final String HISTORY_TABLE = "Data_History";

    public static final String Key_Packet_id = "Packet_id";
    public static final String Key_Session_id = "Session_id";
    public static final String Key_Mower_id = "Mower_id";
    public static final String Key_Date = "Date";
    public static final String Key_Time = "Time";
    public static final String Key_Gps_x = "Gps_x";
    public static final String Key_Gps_y = "Gps_y";

    public static final String Key_Joystick_x = "Joystick_x";
    public static final String Key_Joystick_y = "Joystick_y";
    public static final String Key_Joystick_z = "Joystick_z";
    public static final String Key_Joystick_b1 = "Joystick_b1";
    public static final String Key_Joystick_b2 = "Joystick_b2";

    public static final String Key_Oil_Temp = "Oil_temp";

    public static final String Key_w_1 = "w_1";
    public static final String Key_x_1 = "x_1";
    public static final String Key_y_1 = "y_1";

    public static final String Key_w_2 = "w_2";
    public static final String Key_x_2 = "x_2";
    public static final String Key_y_2 = "y_2";

    public static final String Key_w_3 = "w_3";
    public static final String Key_x_3 = "x_3";
    public static final String Key_y_3 = "y_3";

    public static final String Key_z_1 = "z_1";
    public static final String Key_z_2 = "z_2";
    public static final String Key_z_3 = "z_3";
    public static final String STRING_EMPTY ="";

    //Table to store all historic sessions Recorded.
    public static final String Key_Table_id = "Table_id";
    private static final String SQL_CREATE_DATA_HISTORY =
            "CREATE TABLE Data_History("+Key_Table_id+" INTEGER PRIMARY KEY);";

    private static final String SQL_CREATE_SESSION_DATA =
            "CREATE TABLE "+TABLE_NAME+"(" +Key_Packet_id + " INTEGER," +
                    Key_Session_id + " INTEGER, " + Key_Mower_id + " INTEGER, " +
                    Key_Date + " TEXT, "+Key_Time+" TEXT, "+Key_Gps_x+" REAL, "+Key_Gps_y+" REAL, "+
                    Key_Joystick_x+" REAL, "+Key_Joystick_y+" REAL, "+Key_Joystick_z+" REAL, "+Key_Joystick_b1+" REAL, "
                    +Key_Joystick_b2+" REAL, "+Key_Oil_Temp+" REAL, "+
                    Key_w_1+" REAL, "+Key_x_1+" REAL, "+ Key_y_1+" REAL, "+ Key_z_1+" REAL, "+
                    Key_w_2+" REAL, "+Key_x_2+" REAL, "+ Key_y_2+" REAL, "+ Key_z_2+" REAL, "+
                    Key_w_3+" REAL, "+Key_x_3+" REAL, "+ Key_y_3+" REAL, "+Key_z_3+" REAL);";
    private static final String SQL_DELETE_SESSION_DATA = "DROP TABLE IF EXISTS "+TABLE_NAME;

    private static final String TAG = "DATABASE: ";

    public Database_Session_Storage(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //When new Object Database_Session_Storage Created
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_DATA_HISTORY);
        db.execSQL(SQL_CREATE_SESSION_DATA);
    }

    //Drop the table while upgrading the database
    // such as adding new column or changing existing constraint
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1){

        db.execSQL(SQL_DELETE_SESSION_DATA);
        this.onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        this.onUpgrade(db, oldVersion, newVersion);
    }

        //Create a new Row entry in the datastructure that may be input into the database.
    public void addNewPacket(
            int packet_id, int Mower_id, String Date, String Time, double GPS_x, double GPS_y, double Joystick_x,
            double Joystick_y, double Joystick_z,  double Joystick_b1,  double Joystick_b2,
            double Oil_temp, double w_1, double x_1, double y_1, double z_1, double w_2,
            double x_2, double y_2, double z_2, double w_3, double x_3, double y_3, double z_3
    )
    {
        Data_Structure_Packet sessionDetails = new Data_Structure_Packet();
            Log.d(TAG, "addPacket PacketID: "+packet_id);
            sessionDetails.setPacket_id(packet_id);
            sessionDetails.setKey_Mower_id((Mower_id));
            sessionDetails.setSession_id(storedCount);
            sessionDetails.setKey_Date(Date);
            sessionDetails.setKey_Time(Time);
            sessionDetails.setKey_Gps_x((GPS_x));
            sessionDetails.setKey_Gps_y((GPS_y));

            sessionDetails.setKey_Joystick_x((Joystick_x));
            sessionDetails.setKey_Joystick_y((Joystick_y));
            sessionDetails.setKey_Joystick_z((Joystick_z));
            sessionDetails.setKey_Joystick_b1((Joystick_b1));
            sessionDetails.setKey_Joystick_b2((Joystick_b2));

            sessionDetails.setKey_Oil_Temp((Oil_temp));

            sessionDetails.setKey_w_1((w_1));
            sessionDetails.setKey_w_2((w_2));
            sessionDetails.setKey_w_3((w_3));

            sessionDetails.setKey_x_1((x_1));
            sessionDetails.setKey_x_2((x_2));
            sessionDetails.setKey_x_3((x_3));

            sessionDetails.setKey_y_1((y_1));
            sessionDetails.setKey_y_2((y_2));
            sessionDetails.setKey_y_3((y_3));

            sessionDetails.setKey_z_1((z_1));
            sessionDetails.setKey_z_2((z_2));
            sessionDetails.setKey_z_3((z_3));

            addSessionData(sessionDetails);

    }

    //Turn a formatted datastructure into a physical entry in the database
    public long addSessionData(Data_Structure_Packet data){

        //Gets the data repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();

        //Create map with movie details to be inserted
        ContentValues session_details = new ContentValues();
        session_details.put(Key_Packet_id, data.getPacket_id());
        session_details.put(Key_Mower_id, data.getKey_Mower_id());
        session_details.put(Key_Session_id, data.getSession_id());
        session_details.put(Key_Gps_x, data.getKey_Gps_x());
        session_details.put(Key_Gps_y, data.getKey_Gps_y());
        session_details.put(Key_Date, data.getKey_Date());
        session_details.put(Key_Time, data.getKey_Time());
        session_details.put(Key_Oil_Temp, data.getKey_Oil_Temp());


        session_details.put(Key_Joystick_x, data.getKey_Joystick_x());
        session_details.put(Key_Joystick_y, data.getKey_Joystick_y());
        session_details.put(Key_Joystick_z, data.getKey_Joystick_z());
        session_details.put(Key_Joystick_b1, data.getKey_Joystick_b1());
        session_details.put(Key_Joystick_b2, data.getKey_Joystick_b2());


        session_details.put(Key_w_1, data.getKey_w_1());
        session_details.put(Key_w_2, data.getKey_w_2());
        session_details.put(Key_w_3, data.getKey_w_3());

        session_details.put(Key_x_1, data.getKey_x_1());
        session_details.put(Key_x_2, data.getKey_x_2());
        session_details.put(Key_x_3, data.getKey_x_3());

        session_details.put(Key_y_1, data.getKey_y_1());
        session_details.put(Key_y_2, data.getKey_y_2());
        session_details.put(Key_y_3, data.getKey_y_3());

        session_details.put(Key_z_1, data.getKey_z_1());
        session_details.put(Key_z_2, data.getKey_z_2());
        session_details.put(Key_z_3, data.getKey_z_3());
        long newRowId = db.insert(TABLE_NAME, null, session_details);
        Log.d(TAG, "Inserting packet into database with rowID: "+newRowId);
        db.close();
        return newRowId;
    }

    //Retreives all Rows from the database.
    public List getAllSessionData() {
        List sessionDetailsList = new ArrayList();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        //If necessary, order can be added by:
        //+ " ORDER BY " + KEY_PACKET_ID + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //if TABLE has rows
        if (cursor.moveToFirst()) {
            //Loop through the table rows
            do {
                Data_Structure_Packet sessionDetails = new Data_Structure_Packet();
                sessionDetails.setPacket_id(cursor.getInt(0));
                sessionDetails.setKey_Mower_id(cursor.getInt(2));
                sessionDetails.setSession_id(cursor.getInt(1));
                sessionDetails.setKey_Date(cursor.getString(3));
                sessionDetails.setKey_Time(cursor.getString(4));
                sessionDetails.setKey_Gps_x(cursor.getDouble(5));
                sessionDetails.setKey_Gps_y(cursor.getDouble(6));

                sessionDetails.setKey_Joystick_x(cursor.getDouble(7));
                sessionDetails.setKey_Joystick_y(cursor.getDouble(8));
                sessionDetails.setKey_Joystick_z(cursor.getDouble(9));
                sessionDetails.setKey_Joystick_b1(cursor.getDouble(10));
                sessionDetails.setKey_Joystick_b2(cursor.getDouble(11));

                sessionDetails.setKey_Oil_Temp(cursor.getDouble(12));

                sessionDetails.setKey_w_1(cursor.getDouble(13));
                sessionDetails.setKey_x_1(cursor.getDouble(14));
                sessionDetails.setKey_y_1(cursor.getDouble(15));
                sessionDetails.setKey_z_1(cursor.getDouble(16));

                sessionDetails.setKey_w_2(cursor.getDouble(17));
                sessionDetails.setKey_x_2(cursor.getDouble(18));
                sessionDetails.setKey_y_2(cursor.getDouble(19));
                sessionDetails.setKey_z_2(cursor.getDouble(20));

                sessionDetails.setKey_w_3(cursor.getDouble(21));
                sessionDetails.setKey_x_3(cursor.getDouble(22));
                sessionDetails.setKey_y_3(cursor.getDouble(23));
                sessionDetails.setKey_z_3(cursor.getDouble(24));

                //Add movie details to list
                sessionDetailsList.add(sessionDetails);
            } while (cursor.moveToNext());
        }
        db.close();
        return sessionDetailsList;
    }

    //Returns packet by ID
    public Data_Structure_Packet getSessionData(int PacketId) {

        Data_Structure_Packet sessionDetails = new Data_Structure_Packet();
        SQLiteDatabase db = this.getReadableDatabase();
        //specify the columns to be fetched
        String[] columns = {Key_Packet_id, Key_Mower_id, Key_Session_id, Key_Date, Key_Time, Key_Gps_x, Key_Gps_y,
                Key_Joystick_x,Key_Joystick_y,Key_Joystick_z,Key_Joystick_b1,Key_Joystick_b2, Key_Oil_Temp, Key_w_1, Key_x_1, Key_y_1, Key_z_1,
                Key_w_2, Key_x_2, Key_y_2, Key_z_2, Key_w_3, Key_x_3, Key_y_3,  Key_z_3};
        //Select condition
        String selection = PacketId + " = ?";
        //Arguments for selection
        String[] selectionArgs = {String.valueOf(PacketId)};


        Cursor cursor = db.query(TABLE_NAME, columns, selection,
                selectionArgs, null, null, null);
        if (null != cursor && cursor.moveToFirst()) {
            cursor.moveToFirst();
            sessionDetails.setPacket_id(cursor.getInt(0));
            sessionDetails.setKey_Mower_id(cursor.getInt(1));
            sessionDetails.setSession_id(cursor.getInt(2));
            sessionDetails.setKey_Date(cursor.getString(3));
            sessionDetails.setKey_Time(cursor.getString(4));
            sessionDetails.setKey_Gps_x(cursor.getDouble(5));
            sessionDetails.setKey_Gps_y(cursor.getDouble(6));
            sessionDetails.setKey_Joystick(cursor.getDouble(7));
            sessionDetails.setKey_Oil_Temp(cursor.getDouble(8));

            sessionDetails.setKey_w_1(cursor.getDouble(9));
            sessionDetails.setKey_x_1(cursor.getDouble(10));
            sessionDetails.setKey_y_1(cursor.getDouble(11));
            sessionDetails.setKey_z_1(cursor.getDouble(12));

            sessionDetails.setKey_w_2(cursor.getDouble(13));
            sessionDetails.setKey_x_2(cursor.getDouble(14));
            sessionDetails.setKey_y_2(cursor.getDouble(15));
            sessionDetails.setKey_z_2(cursor.getDouble(16));

            sessionDetails.setKey_w_3(cursor.getDouble(17));
            sessionDetails.setKey_x_3(cursor.getDouble(18));
            sessionDetails.setKey_y_3(cursor.getDouble(19));
            sessionDetails.setKey_z_3(cursor.getDouble(20));

        }
        db.close();
        return sessionDetails;

    }

    //Allows us to update an existing packet
    public void updatePacket(Data_Structure_Packet data) {
        SQLiteDatabase db = this.getWritableDatabase();
        String packetIds[] = {String.valueOf(data.getPacket_id())};

        ContentValues session_details = new ContentValues();
        session_details.put(Key_Packet_id, data.getPacket_id());
        session_details.put(Key_Mower_id, data.getKey_Mower_id());
        session_details.put(Key_Session_id, data.getSession_id());
        session_details.put(Key_Gps_x, data.getKey_Gps_x());
        session_details.put(Key_Gps_y, data.getKey_Gps_y());
        session_details.put(Key_Date, data.getKey_Date());
        session_details.put(Key_Time, data.getKey_Time());

        session_details.put(Key_Oil_Temp, data.getKey_Oil_Temp());
        session_details.put(Key_Joystick_x, data.getKey_Joystick_x());
        session_details.put(Key_Joystick_y, data.getKey_Joystick_y());
        session_details.put(Key_Joystick_z, data.getKey_Joystick_z());
        session_details.put(Key_Joystick_b1, data.getKey_Joystick_b1());
        session_details.put(Key_Joystick_b2, data.getKey_Joystick_b2());

        session_details.put(Key_w_1, data.getKey_w_1());
        session_details.put(Key_w_2, data.getKey_w_2());
        session_details.put(Key_w_3, data.getKey_w_3());
        session_details.put(Key_x_1, data.getKey_x_1());
        session_details.put(Key_x_2, data.getKey_x_2());
        session_details.put(Key_x_3, data.getKey_x_3());
        session_details.put(Key_y_1, data.getKey_y_1());
        session_details.put(Key_y_2, data.getKey_y_2());
        session_details.put(Key_y_3, data.getKey_y_3());
        session_details.put(Key_z_1, data.getKey_z_1());
        session_details.put(Key_z_2, data.getKey_z_2());
        session_details.put(Key_z_3, data.getKey_z_3());
        db.update(TABLE_NAME, session_details, Key_Packet_id + " = ?", packetIds);
        db.close();
    }

    //Deletes an existing row entry based on two constraints
    public void deletePacket(int packetId, int sessionId) {
        String packetIds[] = {String.valueOf(packetId), String.valueOf(sessionId)};
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, Key_Packet_id + " = ? AND " + Key_Session_id + " = ?", packetIds);
        db.close();
    }

    //Creates a second table that monitors the number of different session entries.
    public void createHistoricLog()
    {
        //This query checks the existing table to see how many tables already exist in history.
        int NthEntry = 0;
        //FOR THE ASCENDING/DESCENDING ORDER HERE IS IMPORTANT!!!
        String selectQuery = "SELECT MAX("+Key_Table_id+") FROM Data_History";
        SQLiteDatabase db_read = this.getReadableDatabase();
        Cursor cursor = db_read.rawQuery(selectQuery, null);
        //if table has rows
        if(cursor.moveToFirst()){
            do{
                NthEntry = cursor.getInt(0);
                Log.d(TAG2, "The Nth entry has located the highest value entry of: "+NthEntry);
                storedCount = NthEntry;
            } while(cursor.moveToNext());
        }
        db_read.close();

        //This part then creates an entry in the database for the current session.
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues session_history = new ContentValues();
        if(NthEntry >= 0)
        {
            session_history.put(Key_Table_id,  NthEntry+1);
        } else {
            session_history.put(Key_Table_id,  0);
        }
        long newRowId = db.insert(HISTORY_TABLE, null, session_history);
        db.close();

    }

    //This method is redundant, however can be used to find the highest rowID entry in the database
    public int checkMax()
    {
        //This query checks the existing table to see how many tables already exist in history.
        int NthEntry = 0;
        //FOR THE ASCENDING/DESCENDING ORDER HERE IS IMPORTANT!!!
        String selectQuery = "SELECT MAX("+Key_Table_id+") FROM Data_History";
        SQLiteDatabase db_read = this.getReadableDatabase();
        Cursor cursor = db_read.rawQuery(selectQuery, null);
        //if table has rows
        if(cursor.moveToFirst()){
            do{
                NthEntry = cursor.getInt(0);
                Log.d(TAG2, "The Nth entry has located the highest value entry of: "+NthEntry);

            } while(cursor.moveToNext());
        }
        db_read.close();

        //This part then creates an entry in the database for the current session.
        return NthEntry;
    }
    //Returns the total number of entries in the database.
    public long getEntriesCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        db.close();
        return count;
    }

}
