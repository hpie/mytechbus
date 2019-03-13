package mytechbus.hpie.com.mytechbus;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.andprn.port.android.USBPort;
import com.andprn.port.android.USBPortConnection;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class DashboardActivity extends AppCompatActivity {
    private SessionHandler session;
    String booking_message = "";
    Button btn_connect;
    private ProgressDialog pDialog;

    //-------------------------------------------------------------

    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;

    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;
    BluetoothAdapter badapter;
    String btname = "";
    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;

    Button btn_normal, btn_bold, btn_large, btn_underline, btn_image, btn_barcode, btn_bill, btn_usb, btn_serial, btn_pdf;
    ImageView imageView;
    Spinner sp_pname;
    EditText edtext;

    FetchLocation fetchLocation;

    Bitmap mBitmap;
    int Ptype = 0;

    private static final String TAG = "MTB PRINT";

    // USB
    UsbManager mUsbManager;
    private USBPort port;
    private UsbDevice usbDevice;
    USBPortConnection connection;
    int prn, type = 0;
    private PendingIntent mPermissionIntent;

    Context context;
    private FIleOperations fIleOperations;

    //--------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        session = new SessionHandler(getApplicationContext());
        User user = session.getUserDetails();
        TextView welcomeText = findViewById(R.id.welcomeText);

        fIleOperations = new FIleOperations();

        fetchLocation = new FetchLocation(DashboardActivity.this,DashboardActivity.this);

        welcomeText.setText("Welcome "+user.getFullName()+", your session will expire on "+user.getSessionExpiryDate());

        Button bookingBtn = findViewById(R.id.btnBooking);
        Button uploadLogBtn = findViewById(R.id.btnLogs);

        btn_connect = (Button) findViewById(R.id.btn_connect);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        //--------------------------------------------------------------------

        context = this;
        btn_connect = (Button) findViewById(R.id.btn_connect);
        //btn_normal = (Button) findViewById(R.id.btn_normal);

        sp_pname = (Spinner) findViewById(R.id.sp_pname);
        //edtext = (EditText) findViewById(R.id.edtext);

        mUsbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        port = new USBPort(mUsbManager, context);
        this.mPermissionIntent = PendingIntent.getBroadcast(context, 0, new Intent("com.android.example.USB_PERMISSION"), Intent.FILL_IN_ACTION);

        if (!isbluetoothconnection()) {
            Toast.makeText(getApplicationContext(), "Bluetooth Not Connect", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Bluetooth Connected", Toast.LENGTH_SHORT).show();
        }

        List<String> list = new ArrayList<String>(loadbtpname());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, list);
        sp_pname.setAdapter(adapter);

        //---------------------------------------------------------------

        if (bundle != null) {
            booking_message = bundle.getString(Constants.KEY_MESSAGE);

            if(!booking_message.equals(Constants.KEY_EMPTY)) {
                //Toast.makeText(getApplicationContext(), booking_message,Toast.LENGTH_LONG).show();
                showDialog(booking_message);
            }
        }

        bookingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Log.d("myLogs"," availibility : " +session.getRouteAvailibilty() + " |||| conition " +session.getRouteAvailibilty().equals("1"));
                if(session.getRouteAvailibilty().equals("1")) {
                    Intent i = new Intent(getApplicationContext(), BookingActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    showDialog("No route assigned. Please contact operator");
                }
            }
        });

        uploadLogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload_logs();
            }
        });

        Button logoutBtn = findViewById(R.id.btnLogout);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.logoutUser();
                Intent i = new Intent(DashboardActivity.this, LoginActivity.class);
                startActivity(i);
                finish();

            }
        });

        btn_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn_connect.getTag().toString().trim().equals("0")) {
                    if (sp_pname.getCount() > 0) {
                        btname = sp_pname.getSelectedItem().toString();
                        new ConnectBT().execute();
                    }
                } else {
                    try {
                        closeBT();
                        btn_connect.setTag("0");
                        btn_connect.setText("connect");
                        toast("Printer DisConnected");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        GetDeviceList();

    }

    private void showDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(message)
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    /**
     *  Blutooth device connection functionality
     */

    public class ConnectBT extends AsyncTask<Void, Void, String> {

        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DashboardActivity.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Connecting...");
            pDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                findBT();
                openBT();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (pDialog.isShowing())
                pDialog.dismiss();
            if (Constants.mmSocket != null) {
                if (Constants.mmSocket.isConnected()) {
                    btn_connect.setTag("1");
                    btn_connect.setText("disconnect");
                } else {
                    toast("Printer Not Connect");
                }
            } else {
                toast("Printer Not Connect");
            }
        }
    }


    public void findBT() {

        try {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            if (mBluetoothAdapter == null) {
                toast("No Bluetooth Adapter Available");
            }
            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {

                    // RPP300 is the name of the bluetooth printer device
                    // we got this name from the list of paired devices
                    if (device.getName().equals(btname)) {
                        mmDevice = device;
                        break;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openBT() throws IOException {
        try {
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            Constants.mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            Constants.mmSocket.connect();
            Constants.mmOutputStream = Constants.mmSocket.getOutputStream();
            Constants.mmInputStream = Constants.mmSocket.getInputStream();

            beginListenForData();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void closeBT() throws IOException {
        try {
            stopWorker = true;
            if (Constants.mmOutputStream != null) {
                Constants.mmOutputStream.close();
                Constants.mmInputStream.close();
                Constants.mmSocket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void toast(String msg) {
        Toast toast2 = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
        toast2.setGravity(Gravity.CENTER, 0, 0);
        toast2.show();
    }

    public void beginListenForData() {
        try {
            final Handler handler = new Handler();

            // This is the ASCII code for a newline character
            final byte delimiter = 10;

            stopWorker = false;
            readBufferPosition = 0;
            readBuffer = new byte[1024];

            workerThread = new Thread(new Runnable() {
                public void run() {
                    while (!Thread.currentThread().isInterrupted()
                            && !stopWorker) {

                        try {

                            int bytesAvailable = Constants.mmInputStream.available();
                            if (bytesAvailable > 0) {
                                byte[] packetBytes = new byte[bytesAvailable];
                                Constants.mmInputStream.read(packetBytes);
                                for (int i = 0; i < bytesAvailable; i++) {
                                    byte b = packetBytes[i];
                                    if (b == delimiter) {
                                        byte[] encodedBytes = new byte[readBufferPosition];
                                        System.arraycopy(readBuffer, 0,
                                                encodedBytes, 0,
                                                encodedBytes.length);
                                        final String data = new String(
                                                encodedBytes, "US-ASCII");
                                        readBufferPosition = 0;

                                        handler.post(new Runnable() {
                                            public void run() {
                                                //       myLabel.setText(data);
                                            }
                                        });
                                    } else {
                                        readBuffer[readBufferPosition++] = b;
                                    }
                                }
                            }

                        } catch (IOException ex) {
                            stopWorker = true;
                        }

                    }
                }
            });

            workerThread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void GetDeviceList() {
        UsbManager usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        HashMap<String, UsbDevice> devicelist = usbManager.getDeviceList();
        Iterator<UsbDevice> deviceIterator = devicelist.values().iterator();
        if (deviceIterator.hasNext()) {
            Toast.makeText(getApplicationContext(), "Device Found", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "No Device Found", Toast.LENGTH_SHORT).show();
        }
        String i = "";
        while (deviceIterator.hasNext()) {
            UsbDevice device = deviceIterator.next();
            i += "\n" +
                    "DeviceID: " + device.getDeviceId() + "\n" +
                    "DeviceName: " + device.getDeviceName() + "\n" +
                    "DeviceClass: " + device.getDeviceClass() + " - "
                    + translateDeviceClass(device.getDeviceClass()) + "\n" +
                    "DeviceSubClass: " + device.getDeviceSubclass() + "\n" +
                    "VendorID: " + device.getVendorId() + "\n" +
                    "ProductID: " + device.getProductId() + "\n";
        }
        Log.e("Device List : ", i);
    }

    private String translateDeviceClass(int deviceClass) {
        switch (deviceClass) {
            case UsbConstants.USB_CLASS_APP_SPEC:
                return "Application specific USB class";
            case UsbConstants.USB_CLASS_AUDIO:
                return "USB class for audio devices";
            case UsbConstants.USB_CLASS_CDC_DATA:
                return "USB class for CDC devices (communications device class)";
            case UsbConstants.USB_CLASS_COMM:
                return "USB class for communication devices";
            case UsbConstants.USB_CLASS_CONTENT_SEC:
                return "USB class for content security devices";
            case UsbConstants.USB_CLASS_CSCID:
                return "USB class for content smart card devices";
            case UsbConstants.USB_CLASS_HID:
                return "USB class for human interface devices (for example, mice and keyboards)";
            case UsbConstants.USB_CLASS_HUB:
                return "USB class for USB hubs";
            case UsbConstants.USB_CLASS_MASS_STORAGE:
                return "USB class for mass storage devices";
            case UsbConstants.USB_CLASS_MISC:
                return "USB class for wireless miscellaneous devices";
            case UsbConstants.USB_CLASS_PER_INTERFACE:
                return "USB class indicating that the class is determined on a per-interface basis";
            case UsbConstants.USB_CLASS_PHYSICA:
                return "USB class for physical devices";
            case UsbConstants.USB_CLASS_PRINTER:
                return "USB class for printers";
            case UsbConstants.USB_CLASS_STILL_IMAGE:
                return "USB class for still image devices (digital cameras)";
            case UsbConstants.USB_CLASS_VENDOR_SPEC:
                return "Vendor specific USB class";
            case UsbConstants.USB_CLASS_VIDEO:
                return "USB class for video devices";
            case UsbConstants.USB_CLASS_WIRELESS_CONTROLLER:
                return "USB class for wireless controller devices";
            default:
                return "Unknown USB class!";
        }
    }

    public List<String> loadbtpname() {
        List<String> list = new ArrayList<>();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                list.add(device.getName());
            }
        }

        return list;
    }

    public boolean isbluetoothconnection() {
        try {
            badapter = BluetoothAdapter.getDefaultAdapter();
            if (badapter != null) {
                badapter.enable();
                if (!badapter.isEnabled()) {
                    Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBluetooth, 0);

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private void upload_logs() {
        displayLoader();
        //------------------------------------------------------------------------------------------------------------
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.upload_logs_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.dismiss();
                        JSONObject login_response = null;
                        try {
                            JSONObject upload_response = new JSONObject(response);

                            if (upload_response.getInt(Constants.KEY_STATUS) == 1) {

                                DateFormat file_dt = new SimpleDateFormat("yyyy_MM_dd");
                                String file_date = file_dt.format(Calendar.getInstance().getTime());
                                String fileName = session.getIMEI() + "_" + file_date.toString() + ".txt";


                                fIleOperations.deleteLogFiles(fileName, DashboardActivity.this, "daily_log");

                                fIleOperations.deleteLogFiles("", DashboardActivity.this, "crashReports");

                                Toast.makeText(getApplicationContext(), "Log files uploaded successfully.",Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Something went wrong. Please try again later!",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        Log.d("myLogs", "Log sent response : " + response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();
                        Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                File fileDirectory = context.getDir("daily_log", context.MODE_PRIVATE); //Creating an internal dir;
                File[] dirFiles = fileDirectory.listFiles();

                Map<String,String> params = new HashMap<String, String>();

                if (dirFiles.length != 0) {
                    // loops through the array of files, outputing the name to console
                    for (int j = 0; j < dirFiles.length; j++) {
                        // String fileOutput = dirFiles[j].toString();

                        //Log.d("myLogs", "Log FileName:" + dirFiles[j].getName());

                        String file_upload_contents = fIleOperations.readLogFile(dirFiles[j].getName(), DashboardActivity.this, "daily_log");

                        params.put(dirFiles[j].getName(), ""+ file_upload_contents);

                        //Log.d("myLogs", "Files : " + fileOutput);
                    }
                }

                File crashDirectory = context.getDir("crashReports", context.MODE_PRIVATE); //Creating an internal dir;
                File[] dirCrash = crashDirectory.listFiles();

                if (dirCrash.length != 0) {
                    // loops through the array of files, outputing the name to console
                    for (int j = 0; j < dirCrash.length; j++) {
                        // String fileOutput = dirFiles[j].toString();

                        //Log.d("myLogs", "crash FileName:" + dirCrash[j].getName());

                        String file_upload_contents = fIleOperations.readLogFile(dirCrash[j].getName(), DashboardActivity.this, "crashReports");

                        params.put(dirCrash[j].getName(), ""+ file_upload_contents);

                        //Log.d("myLogs", "Files : " + fileOutput);
                    }
                }
                params.put("user_id", session.getUserId());

                params.put(Constants.KEY_LATITUDE, ""+ fetchLocation.latitude);
                params.put(Constants.KEY_LONGITUDE,""+ fetchLocation.longitude);

                return params;
            }
        };

        // Access the RequestQueue through your singleton class.
        mytechbus.hpie.com.mytechbus.MySingleton.getInstance(DashboardActivity.this).addToRequestQueue(stringRequest);
        //------------------------------------------------------------------------------------------------------------
        //-----------------------------------------------------------------------------------------
    }

    private void upload_log() {
        displayLoader();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.login_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.dismiss();

                        try {
                            Log.d("myLogs : Login : ", response);
                            //Check if user got logged in successfully
                            JSONObject login_response = new JSONObject(response);

                            if (login_response.getInt(Constants.KEY_STATUS) == 1) {

                            }else{
                                Toast.makeText(getApplicationContext(),login_response.getString(Constants.KEY_MESSAGE),Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();
                        Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();

                        // gets the files in the directory
                        //File fileDirectory = new File(Environment.getDataDirectory()+"/YourDirectory/");


                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();

                String file_upload_contents = fIleOperations.readFromFile("ticket_upload_queue.txt", DashboardActivity.this);

                params.put("user_id", "" + session.getUserId());

                params.put("ticket_data", ""+ file_upload_contents);

                return params;
            }
        };

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void displayLoader() {
        pDialog = new ProgressDialog(DashboardActivity.this);
        pDialog.setMessage("Uploading.. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }
}