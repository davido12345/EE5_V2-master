package com.example.david.ee5application;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;
import java.text.DateFormat;


public class Routine_BT_Data_Receiver {
    public static boolean end = false;

    public static int storedCount = 0;
    public static int lastRecord;

    public static final String TAG2 = "QUAIL: ";
    private static final String TAG = "BluetoothConnectionServ";
    private static final String appName = "MYAPP";
    private static final UUID MY_UUID_INSECURE =
            UUID.fromString("4ca7ec2b-ef6e-405e-b595-87a0b55226e7");

    private final BluetoothAdapter mBluetoothAdapter;
    Context mContext;

    private AcceptThread mInsecureAcceptThread;
    private ConnectThread mConnectThread;
    private BluetoothDevice mmDevice;
    private UUID deviceUUID;
    ProgressDialog mProgressDialog;

    private ConnectedThread mConnectedThread;

    public Routine_BT_Data_Receiver(Context context) {
        mContext = context;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        start();
    }


    /**
     * This thread runs while listening for incoming connections. It behaves
     * like a server-side client. It runs until a connection is accepted
     * (or until cancelled).
     */
    private class AcceptThread extends Thread {

        // The local server socket
        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread(){
            BluetoothServerSocket tmp = null;

            // Create a new listening server socket
            try{
                tmp = mBluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(appName, MY_UUID_INSECURE);

                Log.d(TAG, "AcceptThread: Setting up Server using: " + MY_UUID_INSECURE);
            }catch (IOException e){
                Log.e(TAG, "AcceptThread: IOException: " + e.getMessage() );
            }

            mmServerSocket = tmp;
        }

        public void run(){
            Log.d(TAG, "run: AcceptThread Running.");

            BluetoothSocket socket = null;

            try{
                // This is a blocking call and will only return on a
                // successful connection or an exception
                Log.d(TAG, "run: RFCOM server socket start.....");

                socket = mmServerSocket.accept();

                Log.d(TAG, "run: RFCOM server socket accepted connection.");

            }catch (IOException e){
                Log.e(TAG, "AcceptThread: IOException: " + e.getMessage() );
            }

            //
            if(socket != null){
                connected(socket,mmDevice);
            }

            Log.i(TAG, "END mAcceptThread ");
        }

        public void cancel() {
            Log.d(TAG, "cancel: Canceling AcceptThread.");
            try {
                mmServerSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "cancel: Close of AcceptThread ServerSocket failed. " + e.getMessage() );
            }
        }

    }

    /**
     * This thread runs while attempting to make an outgoing connection
     * with a device. It runs straight through; the connection either
     * succeeds or fails.
     */
    private class ConnectThread extends Thread {
        private BluetoothSocket mmSocket;

        public ConnectThread(BluetoothDevice device, UUID uuid) {
            Log.d(TAG, "ConnectThread: started.");
            mmDevice = device;
            deviceUUID = uuid;
        }



        public void run(){
            BluetoothSocket tmp = null;
            Log.i(TAG, "RUN mConnectThread ");

            // Get a BluetoothSocket for a connection with the
            // given BluetoothDevice
            try {
                Log.d(TAG, "ConnectThread: Trying to create InsecureRfcommSocket using UUID: "
                        +MY_UUID_INSECURE );
                tmp = mmDevice.createRfcommSocketToServiceRecord(deviceUUID);
            } catch (IOException e) {
                Log.e(TAG, "ConnectThread: Could not create InsecureRfcommSocket " + e.getMessage());
            }

            mmSocket = tmp;

            // Always cancel discovery because it will slow down a connection
            mBluetoothAdapter.cancelDiscovery();

            // Make a connection to the BluetoothSocket

            try {
                // This is a blocking call and will only return on a
                // successful connection or an exception
                mmSocket.connect();

                Log.d(TAG, "run: ConnectThread connected.");
            } catch (IOException e) {
                // Close the socket
                try {
                    mmSocket.close();
                    Log.d(TAG, "run: Closed Socket.");
                } catch (IOException e1) {
                    Log.e(TAG, "mConnectThread: run: Unable to close connection in socket " + e1.getMessage());
                }
                Log.d(TAG, "run: ConnectThread: Could not connect to UUID: " + MY_UUID_INSECURE );
            }


            connected(mmSocket,mmDevice);
        }
        public void cancel() {
            try {
                Log.d(TAG, "cancel: Closing Client Socket.");
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "cancel: close() of mmSocket in Connectthread failed. " + e.getMessage());
            }
        }
    }



    /**
     * Start the chat service. Specifically start AcceptThread to begin a
     * session in listening (server) mode. Called by the Activity onResume()
     */
    public synchronized void start() {
        Log.d(TAG, "start");

        // Cancel any thread attempting to make a connection
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }
        if (mInsecureAcceptThread == null) {
            mInsecureAcceptThread = new AcceptThread();
            mInsecureAcceptThread.start();
        }
    }

    /**

     AcceptThread starts and sits waiting for a connection.
     Then ConnectThread starts and attempts to make a connection with the other devices AcceptThread.
     **/

    public void startClient(BluetoothDevice device, UUID uuid){
        Log.d(TAG, "startClient: Started.");

        //initprogress dialog
        mProgressDialog = ProgressDialog.show(mContext,"Connecting Bluetooth"
                ,"Please Wait...",true);

        mConnectThread = new ConnectThread(device, uuid);
        mConnectThread.start();
    }

    /**
     Finally the ConnectedThread which is responsible for maintaining the BTConnection, Sending the data, and
     receiving incoming data through input/output streams respectively.
     **/
    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            Log.d(TAG, "ConnectedThread: Starting.");

            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            //dismiss the progressdialog when connection is established

            try{
                mProgressDialog.dismiss();
            }catch (NullPointerException e){
                e.printStackTrace();
            }

            try {
                tmpIn = mmSocket.getInputStream();
                tmpOut = mmSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run(){
            GPSTracker gps = new GPSTracker(mContext);

            byte[] buffer = new byte[1024];  // buffer store for the stream
            int packet_id = 0;
            int bytes; // bytes returned from read()
            Database_Session_Storage db = new Database_Session_Storage(mContext);
            db.createHistoricLog();
            // Keep listening to the InputStream until an exception occurs

            while (end == false) {
                // Read from the InputStream
                try {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    bytes = mmInStream.read(buffer);
                    //INPUT STREAM TO DATAPACKET
                    String incomingMessage = new String(buffer, 0, bytes);


                    int Mower_id=0;
                    String Date="";
                    String Time="";

                    double GPS_x=gps.getLatitude();
                    double GPS_y=gps.getLongitude();

                    double Joystick_x=0;
                    double Joystick_y=0;
                    double Joystick_z=0;
                    double Joystick_b1=0;
                    double Joystick_b2=0;

                    double Oil_temp=0;
                    double w_1=0;
                    double x_1=0;
                    double y_1=0;
                    double z_1=0;
                    double w_2=0;
                    double x_2=0;
                    double y_2=0;
                    double z_2=0;
                    double w_3=0;
                    double x_3=0;
                    double y_3=0;
                    double z_3=0;

                    Log.d(TAG, "Input Stream: "+incomingMessage);

                    String[] packet = incomingMessage.split("#");
                    String first =  packet[0];
                    Log.d(TAG, "The first packet selected: "+first);
                    String second = packet[1];

                    Log.d(TAG, "The Second packet selected: "+second);
                        if(first.contains("%") && first.contains(">")){
                            // formattedDate have current date/time

                            String[] separated = first.split("\\*");
                            Log.d(TAG, "WE DID THIS 1");

                            Mower_id =  Integer.parseInt(separated[1]);
                            Date = separated[2];

                            try{
                                GPS_x = Double.parseDouble(separated[4]);
                            }  catch (Exception e)   {
                                e.printStackTrace();

                            }

                            try{
                                GPS_y =  Double.parseDouble(separated[5]);
                            }  catch (Exception e)   {
                                e.printStackTrace();

                            }

                            Joystick_x =  (Double.parseDouble(separated[6])/100);
                            Joystick_y =  (Double.parseDouble(separated[7])/100);
                            Joystick_z =  (Double.parseDouble(separated[8])/100);
                            Joystick_b1 = (Double.parseDouble(separated[9])/100);
                            Joystick_b2 = (Double.parseDouble(separated[10])/100);

                            Log.d(TAG, separated[4]);
                            Log.d(TAG, separated[5]);
                            Log.d(TAG, separated[6]);
                            Log.d(TAG, separated[7]);
                            Log.d(TAG, separated[8]);
                            Log.d(TAG, separated[9]);
                            Log.d(TAG, separated[10]);
                            Oil_temp =  Double.parseDouble(separated[11]);

                            w_1 =  ((Double.parseDouble(separated[12])/100)-1);
                            x_1 =  ((Double.parseDouble(separated[13])/100)-1);
                            y_1 =  ((Double.parseDouble(separated[14])/100)-1);
                            z_1 =  ((Double.parseDouble(separated[15])/100)-1);

                            w_2 =  ((Double.parseDouble(separated[16])/100)-1);
                            x_2 =  ((Double.parseDouble(separated[17])/100)-1);
                            y_2 =  ((Double.parseDouble(separated[18])/100)-1);
                            z_2 =  ((Double.parseDouble(separated[19])/100)-1);

                            w_3 =  ((Double.parseDouble(separated[20])/100)-1);
                            x_3 =  ((Double.parseDouble(separated[21])/100)-1);
                            y_3 =  ((Double.parseDouble(separated[22])/100)-1);
                            z_3 =  ((Double.parseDouble(separated[23])/100)-1);
                         } else if (second.contains("%")){

                            // formattedDate have current date/time

                            String[] separated = second.split("\\*");
                            Log.d(TAG, "WE DID THIS 1");

                            Mower_id =  Integer.parseInt(separated[1]);
                            Date = separated[2];

                            try{
                                GPS_x = Double.parseDouble(separated[4]);
                            }  catch (Exception e)   {
                                e.printStackTrace();
                                 GPS_x=gps.getLatitude();

                            }

                            try{
                                GPS_y =  Double.parseDouble(separated[5]);
                            }  catch (Exception e)   {
                                e.printStackTrace();
                                GPS_y=gps.getLongitude();
                            }

                            Joystick_x =  (Double.parseDouble(separated[6])/100);
                            Joystick_y =  (Double.parseDouble(separated[7])/100);
                            Joystick_z =  (Double.parseDouble(separated[8])/100);
                            Joystick_b1 = (Double.parseDouble(separated[9])/100);
                            Joystick_b2 = (Double.parseDouble(separated[10])/100);
                            Log.d(TAG, separated[4]);
                            Log.d(TAG, separated[5]);
                            Log.d(TAG, separated[6]);
                            Log.d(TAG, separated[7]);
                            Log.d(TAG, separated[8]);
                            Log.d(TAG, separated[9]);
                            Log.d(TAG, separated[10]);

                            Oil_temp =  Double.parseDouble(separated[11]);

                            w_1 =  ((Double.parseDouble(separated[12])/100)-1);
                            x_1 =  ((Double.parseDouble(separated[13])/100)-1);
                            y_1 =  ((Double.parseDouble(separated[14])/100)-1);
                            z_1 =  ((Double.parseDouble(separated[15])/100)-1);

                            w_2 =  ((Double.parseDouble(separated[16])/100)-1);
                            x_2 =  ((Double.parseDouble(separated[17])/100)-1);
                            y_2 =  ((Double.parseDouble(separated[18])/100)-1);
                            z_2 =  ((Double.parseDouble(separated[19])/100)-1);

                            w_3 =  ((Double.parseDouble(separated[20])/100)-1);
                            x_3 =  ((Double.parseDouble(separated[21])/100)-1);
                            y_3 =  ((Double.parseDouble(separated[22])/100)-1);
                            z_3 =  ((Double.parseDouble(separated[23])/100)-1);
                        } else {
                            Log.d(TAG, "WE DID THIS NONE!");
                        }

                    Log.d(TAG, "MOWER ID: " + Mower_id);
                    Log.d(TAG, "Time: " + Time);
                    Log.d(TAG, "y_3: " + y_3);
                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss:SSSS");
                    String formattedDate = df.format(c.getTime());
                    db.addNewPacket(packet_id, Page_Main_Driver.machineID, Date, formattedDate, GPS_x, GPS_y, Joystick_x,
                            Joystick_y,Joystick_z,Joystick_b1,Joystick_b2, Oil_temp,
                            w_1, x_1, y_1, z_1, w_2, x_2, y_2, z_2, w_3, x_3, y_3, z_3);

                    Log.d(TAG, "Packet Data of the Time entered as: "+db.getSessionData(packet_id).getKey_Time());
                    Log.d(TAG, "Packet addition was successful with packet ID = "+packet_id);
                    Log.d(TAG, "Session Data received GPS_X = " + db.getSessionData(0).getKey_Time());
                    packet_id++;
                    lastRecord = packet_id;
                    //getPacket(1);


                } catch (IOException e) {
                    Log.e(TAG, "write: Error reading Input Stream. " + e.getMessage() );
                    break;
                }


            }
            ArrayList allDataStored = new ArrayList();
            long sizeStorage = db.getEntriesCount();
            for(int i = 0; i<sizeStorage; i++) {
                Data_Structure_Packet item = (Data_Structure_Packet) db.getAllSessionData().get(i);
                allDataStored.add(item);
                Log.d(TAG, "THE TIME WE HAVE RECORDED IS: "+item.getKey_Time());
            }
            db.close();
        }

        //Call this from the main activity to send data to the remote device
        public void write(byte[] bytes) {
            String text = new String(bytes, Charset.defaultCharset());
            Log.d(TAG, "write: Writing to outputstream: " + text);
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {
                Log.e(TAG, "write: Error writing to output stream. " + e.getMessage() );
            }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }

    private void connected(BluetoothSocket mmSocket, BluetoothDevice mmDevice) {
        Log.d(TAG, "connected: Starting.");

        // Start the thread to manage the connection and perform transmissions
        mConnectedThread = new ConnectedThread(mmSocket);
        mConnectedThread.start();
    }

    /**
     * Write to the ConnectedThread in an unsynchronized manner
     *
     * @param out The bytes to write
     * @see ConnectedThread#write(byte[])
     */
    public void write(byte[] out) {
        // Create temporary object
        ConnectedThread r;

        // Synchronize a copy of the ConnectedThread
        Log.d(TAG, "write: Write Called.");
        //perform the write
        mConnectedThread.write(out);
    }
    //Not used right now


}
