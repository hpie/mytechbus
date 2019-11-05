package mytechbus.hpie.com.mytechbus;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FilenameFilter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class LoginActivity extends AppCompatActivity {
    private FIleOperations fIleOperations;

    private  DeviceUuidFactory dvuid;

    private EditText etUsername, etPassword;
    private String username, password, latitude = "", longitude = "", last_latitude = "", last_longitude = "", current_latitude = "", current_longitude = "", vehicle_code = "";

    private ProgressDialog pDialog;
    private SessionHandler session;
    private boolean mAlreadyStartedService = false;
    private static String DEVICE_IMEI = "";

    String wait_queue_contents = "";
    String upload_queue_contents = "";

    FetchLocation fetchLocation;
    LocationManager manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        session = new SessionHandler(getApplicationContext());
        fIleOperations = new FIleOperations();

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }

        //session.loginUser("hpie@hpie.in","hpie@hpie.in", "R-001");

        if (session.isLoggedIn()) {
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                buildAlertMessageNoGps();
            }
            // Start periodic location logging
            startStep3();

            // Redirect dashboard if session exists
            loadDashboard();
        }
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etLoginUsername);
        etPassword = findViewById(R.id.etLoginPassword);

        Button login = findViewById(R.id.btnLogin);

        // The request code used in ActivityCompat.requestPermissions()
        // and returned in the Activity's onRequestPermissionsResult()
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                android.Manifest.permission.READ_PHONE_STATE,
                android.Manifest.permission.ACCESS_FINE_LOCATION
        };

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        } else {
            fetchLocation = new FetchLocation(LoginActivity.this, LoginActivity.this);
        }

        /**
         * Login button functionality
         */
        login.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                //Retrieve the data entered in the edit texts
                username = etUsername.getText().toString().toLowerCase().trim();
                password = etPassword.getText().toString().trim();

                //-------------------------------------------------------------------
                TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }

                String IMEINumber = tm.getDeviceId();
                //IMEINumber = null;
                if(IMEINumber == "" || IMEINumber == null) {
                    dvuid = new DeviceUuidFactory(LoginActivity.this);
                    IMEINumber = dvuid.getDeviceUuid().toString();
                    //showDialog("IMEINumber : " + IMEINumber);
                }

                DEVICE_IMEI = IMEINumber;
                session.setIMEI(IMEINumber);
                showDialog("IMEINumber : " + IMEINumber);
                startStep3();
                //---------------------------------------------------------------------
                if (validateInputs()) {
                    if(isNetworkAvailable()) {
                        login();
                    } else {
                        activity_log("check_network", "Network not available.");
                        //Toast.makeText(getApplicationContext(), "Network not available.",Toast.LENGTH_LONG).show();
                        showDialog("Network not available.");
                    }
                }
            }
        });

        //------------------------------------------------------------------------------------
        /**
         * Receive location updates periodically and update it on server
         */
        LocalBroadcastManager.getInstance(this).registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        Log.d("myLogs", "Log position called ");
                        if( vehicle_code.equals("")) {
                            vehicle_code = session.getVehicleCode();
                        }

                       // if( !vehicle_code.equals("")) {
                        if( !session.getUserId().equals("")) {


                        current_latitude = String.valueOf(intent.getStringExtra(LocationMonitoringService.EXTRA_LATITUDE).trim());
                        current_longitude = String.valueOf(intent.getStringExtra(LocationMonitoringService.EXTRA_LONGITUDE).trim());

                       Log.d("myLogs : track location", " current_latitude = " +current_latitude+" AND latitude = "+latitude+" Condition : " +  current_latitude.equals(latitude) + " || current_longitude = " +current_longitude+" AND longitude = "+longitude+" Condition :  " + current_longitude.equals(longitude));

                            // Maintain location log on local file system
                            DateFormat file_dt = new SimpleDateFormat("yyyy_MM_dd");
                            String file_date = file_dt.format(Calendar.getInstance().getTime());
                            String fileName = session.getIMEI() + "_" + file_date.toString() + ".loc";

                            JSONObject location_data_store = new JSONObject();
                            try {
                                location_data_store.put("user_id", session.getUserId());
                                location_data_store.put("latitude", current_latitude);
                                location_data_store.put("longitude", current_longitude);

                                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String date = df.format(Calendar.getInstance().getTime());

                                location_data_store.put("location_time",date);

                                // Add location details in local log daily file in log folder
                                fIleOperations.writeToLocationLog(fileName, location_data_store.toString(), LoginActivity.this, "1");
                               // Toast.makeText(getApplicationContext(),"Location updated!", Toast.LENGTH_SHORT).show();

                                //------------ manage activity log ----------------------------
                                activity_log("auto_location_log", location_data_store.toString());
                                //--------------------------------------------------------------

                            } catch (JSONException e) {
                                // TODO Auto-generated catch block

                                //Toast.makeText(getApplicationContext(),"Location Not updated!", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                            //----------------------------------------------------------------------------

                        if (current_latitude != null && current_longitude != null) {
                            if (!last_latitude.equals(current_latitude) || !last_longitude.equals(current_longitude)) {
                                last_latitude = current_latitude;
                                last_longitude = current_longitude;
                                //------------------------------------------------------------------------------------------------------------
                                StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.position_log_url,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {

                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                //Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
                                            }
                                        }) {
                                    @Override
                                    protected Map<String, String> getParams() {
                                        Map<String, String> params = new HashMap<String, String>();

                                        //Log.d("myLogs", "Log vehicle code : " + session.getVehicleCode());

                                        params.put("vehicle_code", "" + session.getVehicleCode());
                                        params.put("user_id", "" + session.getUserId());
                                        params.put("username", "" + session.getUserName());
                                        params.put("device_imie", "" + session.getIMEI());
                                        params.put("latitude", "" + current_latitude);
                                        params.put("longitude", "" + current_longitude);
                                        params.put("route_code", "" + session.GetRoute());
                                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        String date = df.format(Calendar.getInstance().getTime());

                                        params.put("timestamp", date);

                                        //params.put(KEY_EMAIL, email);
                                        return params;
                                    }
                                };

                                // Access the RequestQueue through your singleton class.
                                mytechbus.hpie.com.mytechbus.MySingleton.getInstance(LoginActivity.this).addToRequestQueue(stringRequest);
                                //------------------------------------------------------------------------------------------------------------
                            } else {
                                Log.d("myLogs", "Same location");
                            }
                        }
                            //Log.d("myLogs", "track location");
                        //Log.d("myLogs : track location", " vehicle Code : " + vehicle_code + " || current_latitude = " + current_latitude + " AND latitude = " + latitude + " Condition : " + current_latitude.equals(latitude) + " || current_longitude = " + current_longitude + " AND longitude = " + longitude + " Condition :  " + current_longitude.equals(longitude));
                    } else {
                       Log.d("myLogs", "Vehicle code empty");
                        }
                    }
                }, new IntentFilter(LocationMonitoringService.ACTION_LOCATION_BROADCAST)
        );

        //------------------------------------------------------------

        /**
         * A periodic timer to update ticket booking data to server from local files
         */
        Timer t = new Timer();
        TimerTask tt = new TimerTask() {
            @Override
            public void run() {

                wait_queue_contents = fIleOperations.readFromFile("ticket_wait_queue.txt", LoginActivity.this);
                upload_queue_contents = fIleOperations.readFromFile("ticket_upload_queue.txt", LoginActivity.this);

                if(!wait_queue_contents.equals("") || !upload_queue_contents.equals("")) {
                fIleOperations.writeToFile("ticket_wait_queue.txt", "", LoginActivity.this, "0");

                if(!wait_queue_contents.equals("")) {
                    fIleOperations.writeToFile("ticket_upload_queue.txt", wait_queue_contents, LoginActivity.this, "1");
                }
                    //------------------------------------------------------------------------------------------------------------
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.book_offline_ticket_url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                   Log.d("myLogs", "Ticket Booked : " + response);
                                    try {

                                        JSONObject ticket_response = new JSONObject(response);

                                        if (ticket_response.getInt(Constants.KEY_STATUS) == 1) {

                                              //------------ manage activity log ----------------------------
                                            String file_upload_contents = fIleOperations.readFromFile("ticket_upload_queue.txt", LoginActivity.this);
                                            activity_log("auto_book_ticket_upload", file_upload_contents.toString());
                                            //--------------------------------------------------------------

                                            fIleOperations.writeToFile("ticket_upload_queue.txt", "", LoginActivity.this, "0");
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    //Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
                                }
                            }){
                        @Override
                        protected Map<String,String> getParams(){
                            Map<String,String> params = new HashMap<String, String>();

                            String file_upload_contents = fIleOperations.readFromFile("ticket_upload_queue.txt", LoginActivity.this);

                            params.put("user_id", "" + session.getUserId());
                            params.put(Constants.KEY_LATITUDE, ""+ fetchLocation.getLat());
                            params.put(Constants.KEY_LONGITUDE,""+ fetchLocation.getLong());

                            params.put("ticket_data", ""+ file_upload_contents);

                            return params;
                        }
                    };

                    // Access the RequestQueue through your singleton class.
                    mytechbus.hpie.com.mytechbus.MySingleton.getInstance(LoginActivity.this).addToRequestQueue(stringRequest);
                    //------------------------------------------------------------------------------------------------------------
                    //Log.d("myLogs", " Book Ticket If : current_latitude = " +current_latitude+" AND latitude = "+latitude+" Condition : " +  current_latitude.equals(latitude) + " || current_longitude = " +current_longitude+" AND longitude = "+longitude+" Condition :  " + current_longitude.equals(longitude));

                } else {
                //Log.d("myLogs", " Book Ticket else : wait_queue_contents : " + wait_queue_contents + " |||||| upload_queue_contents " +upload_queue_contents);
                }
            };
        };
        t.scheduleAtFixedRate(tt,new Date(),120000);

        // Sets the default uncaught exception handler. This handler is invoked
        // in case any Thread dies due to an unhandled exception.
        Thread.setDefaultUncaughtExceptionHandler(new CustomizedExceptionHandler( this));

        getCrashReports();
    }

    /**
     *  Check if gps enabled
     */
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     *  Check if carash report file exsits
     * @return
     */
    private String[] GetErrorFileList() {
        File mydir = this.getDir("crashReports", this.MODE_PRIVATE);
        //File dir = new File( FilePath + "/");
        // Try to create the files folder if it doesn't exist
        //dir.mkdir();
        // Filter for ".stacktrace" files
        FilenameFilter filter = new FilenameFilter() {
            public boolean accept(File mydir, String name) {
                return name.endsWith(".stacktrace");
            }
        };
        return mydir.list(filter);
    }
    private boolean bIsThereAnyErrorFile() {
        return GetErrorFileList().length > 0;
    }

    public void getCrashReports() {
       // Log.d("myLogs", "Crash report called : ");
        if ( bIsThereAnyErrorFile() ) {
            String[] ErrorFileList = GetErrorFileList();
            String WholeErrorText = "";
            int curIndex = 0;
            // We limit the number of crash reports to send ( in order not to be too slow )
            final int MaxSendMail = 5;
            for ( String curString : ErrorFileList ) {
                if ( curIndex++ <= MaxSendMail ) {
                    WholeErrorText+="New Trace collected :\n";
                    WholeErrorText+="=====================\n ";
                    WholeErrorText+= curString;
                }

                // DELETE FILES !!!!
                //File curFile = new File( FilePath + "/" + curString );
                //curFile.delete();
            }

            //Log.d("myLogs", "Crash report files : " + WholeErrorText);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
        String permissions[], int[] grantResults) {
        fetchLocation = new FetchLocation(LoginActivity.this,LoginActivity.this);
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
     *  Check network status
     * @return
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /**
     * Launch Dashboard Activity on Successful Login
     */
    private void loadDashboard() {
        Intent i = new Intent(getApplicationContext(), DashboardActivity.class);
        startActivity(i);
        finish();
    }

    /**
     * Display Progress bar while Logging in
     */
    private void displayLoader() {
        pDialog = new ProgressDialog(LoginActivity.this);
        pDialog.setMessage("Logging In.. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.toLowerCase().startsWith(manufacturer.toLowerCase())) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    public String getAndroidVersion() {
        String versionRelease = Build.VERSION.RELEASE;

        return versionRelease;
    }


    String versionRelease = Build.VERSION.RELEASE;

    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    private void housekeeping() {

        activity_log("housekeeping", "");

        fIleOperations.writeToFile("ticket_wait_queue.txt", "", LoginActivity.this, "0");
        fIleOperations.writeToFile("ticket_upload_queue.txt", "", LoginActivity.this, "0");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.housekeeping_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.dismiss();

                        try {
                            Log.d("myLogs", response);
                            //Check if user got logged in successfully
                            JSONObject housekeeping_response = new JSONObject(response);

                            if (housekeeping_response.getInt(Constants.KEY_STATUS) == 1) {
                                Toast.makeText(getApplicationContext(),housekeeping_response.getString(Constants.KEY_MESSAGE),Toast.LENGTH_LONG).show();
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
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(Constants.KEY_IMEI, session.getIMEI());
                params.put(Constants.KEY_USER_ID, session.getUserId());
                params.put(Constants.KEY_LATITUDE, ""+ fetchLocation.latitude);
                params.put(Constants.KEY_LONGITUDE,""+ fetchLocation.longitude);
                return params;
            }
        };

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void login() {
        /*
        String lat = "19.9975";
        String lng = "73.7898";

        Log.d("myLogs : username : ",  ""+ username);
        Log.d("myLogs : password : ",  ""+ password);
        Log.d("myLogs : DEVICE_IMEI : ",  ""+ DEVICE_IMEI);
        Log.d("myLogs : Lat : ",  ""+ fetchLocation.getLat());
        Log.d("myLogs : Lonng : ",  ""+ fetchLocation.getLong());

        */

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
            return;
        }
        displayLoader();

        fIleOperations.writeToFile("route_stages.txt", "", LoginActivity.this, "0");

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

                               Log.d("myLogs", "Received vehicle code : " + login_response.getString("vehicle_code") + " ||| get as Integer : " + login_response.getInt("vehicle_code"));

                                vehicle_code = login_response.getString("vehicle_code");

                                session.loginUser(login_response.getString(Constants.KEY_USER_ID), login_response.getString(Constants.KEY_OPERATOR_ID), username,login_response.getString(Constants.KEY_FULL_NAME),
                                        login_response.getString(Constants.KEY_ROUTE_CODE), login_response.getString("routes_available"), login_response.getString("is_multiroute"),
                                        login_response.getString("operator_name"), login_response.getString("operator_address1"), login_response.getString("operator_address2"), login_response.getString("operator_city"),
                                        login_response.getString("operator_helpline"), login_response.getString("vehicle_code"), login_response.getString("vehicle_number"),
                                        login_response.getString("vehicle_type"), login_response.getString(Constants.KEY_TICKET_MESSAGE), login_response.getString(Constants.KEY_MIN_TICKET));
                                //loadDashboard();
                                fIleOperations.writeToFile("trip_data.txt", response, LoginActivity.this, "0");

                                activity_log("login", login_response.toString());

                                if(login_response.getInt(Constants.KEY_HOUSEKEEPING) == 1) {
                                    housekeeping();
                                }

                                if(isNetworkAvailable()) {
                                    loadDashboard();
                                } else {
                                    //Toast.makeText(getApplicationContext(), "Network not available.",Toast.LENGTH_LONG).show();
                                    showDialog("Network not available.");
                                }

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
                        Toast.makeText(getApplicationContext(),error.toString() + "hiiiii",Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){

                //username = "hrtc@hpie.in";
                //password = "55555";
                //DEVICE_IMEI = "911436258233786";

               //username = "kalta1";
                //password = "12345";
                //DEVICE_IMEI = "868494036290456";

                Map<String,String> params = new HashMap<String, String>();
                params.put(Constants.KEY_USERNAME,username);
                params.put(Constants.KEY_PASSWORD,password);
                params.put(Constants.KEY_IMEI,DEVICE_IMEI);

                params.put("device_model",  ""+ getDeviceName());


                Log.d("myLogs : Device name : ",  ""+ getDeviceName());
                params.put("android_version",  ""+ getAndroidVersion());

                // params.put(Constants.KEY_LATITUDE, ""+ fetchLocation.getLat());
               // params.put(Constants.KEY_LONGITUDE,""+ fetchLocation.getLong());

                params.put(Constants.KEY_LATITUDE, ""+ fetchLocation.getLat());
                params.put(Constants.KEY_LONGITUDE,""+ fetchLocation.getLong());

                //params.put(KEY_EMAIL, email);
                return params;
            }

        };

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }

    /**
     * Validates inputs and shows error if any
     * @return
     */
    private boolean validateInputs() {
        if(Constants.KEY_EMPTY.equals(username)){
            etUsername.setError("Username cannot be empty");
            etUsername.requestFocus();
            return false;
        }
        if(Constants.KEY_EMPTY.equals(password)){
            etPassword.setError("Password cannot be empty");
            etPassword.requestFocus();
            return false;
        }
        return true;
    }


    //----------------------------------------------------------------------

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    //------------------------------------------------------------

    /**
     * Step 3: Start the Location Monitor Service
     */
    private void startStep3() {

        //And it will be keep running until you close the entire application from task manager.
        //This method will executed only once.

        if (!mAlreadyStartedService) {

            //Start location sharing service to app server.........
            Intent intent = new Intent(this, LocationMonitoringService.class);
            startService(intent);
            //Log.d("myLogs", "Start Step 3 for Location ");
            mAlreadyStartedService = true;
            //Ends................................................
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        activity_log("onResume", "");
    }
    @Override
    protected void onPause() {
        super.onPause();
        activity_log("onPause", "");
    }
    @Override
    protected void onStop() {
        super.onStop();
        activity_log("onStop", "");
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        activity_log("onRestart", "");
    }

    @Override
    public void onDestroy() {

        //Stop location sharing service to app server.........
        stopService(new Intent(this, LocationMonitoringService.class));
        mAlreadyStartedService = false;
        //Ends................................................

        super.onDestroy();
        activity_log("app_destroy", "");
    }

    public void activity_log(String activity_name, String activity_message) {
        //------------ manage activity log ----------------------------
        JSONObject activity_log = new JSONObject();
        try {
            activity_log.put("activity_name", activity_name);
            activity_log.put("activity_data", activity_message);
        } catch (JSONException ee) {
            ee.printStackTrace();
        }
        String activity_log_file = fIleOperations.getTodayFileNmaePrefix("log");
        fIleOperations.writeToFile(activity_log_file, activity_log.toString(), LoginActivity.this, "1", "activity_log");
        //--------------------------------------------------------------
    }
}