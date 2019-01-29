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
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class LoginActivity extends AppCompatActivity {
    private FIleOperations fIleOperations;

    private EditText etUsername;
    private EditText etPassword;
    private String username;
    private String password;
    private String latitude, current_latitude;
    private String longitude, current_longitude;
    private ProgressDialog pDialog;
    private SessionHandler session;
    private boolean mAlreadyStartedService = false;
    private static String DEVICE_IMEI = "";

    FetchLocation fetchLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new SessionHandler(getApplicationContext());
        fIleOperations = new FIleOperations();



        if(session.isLoggedIn()){
            startStep3();
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

        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        } else {
            fetchLocation = new FetchLocation(LoginActivity.this,LoginActivity.this);
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Retrieve the data entered in the edit texts
                username = etUsername.getText().toString().toLowerCase().trim();
                password = etPassword.getText().toString().trim();

                //-------------------------------------------------------------------
                TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                @SuppressLint("MissingPermission") String IMEINumber = tm.getDeviceId();

                //Toast.makeText(getApplicationContext(), "In permission granted stuff : " + IMEINumber,Toast.LENGTH_LONG).show();

                DEVICE_IMEI = IMEINumber;
                session.setIMEI(IMEINumber);

                startStep3();
                //---------------------------------------------------------------------
                if (validateInputs()) {
                    login();
                }
            }
        });

        //------------------------------------------------------------------------------------
        LocalBroadcastManager.getInstance(this).registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        current_latitude = intent.getStringExtra(LocationMonitoringService.EXTRA_LATITUDE).trim();
                        current_longitude = intent.getStringExtra(LocationMonitoringService.EXTRA_LONGITUDE).trim();

                        //if (current_latitude != null && current_longitude != null && !(current_latitude.equals(latitude) && current_longitude.equals(longitude))) {
                        if (current_latitude != null && current_longitude != null) {

                            Log.d("Suresh12345 : if", " current_latitude = " +current_latitude+" AND latitude = "+latitude+" Condition : " +  current_latitude.equals(latitude) + " || current_longitude = " +current_longitude+" AND longitude = "+longitude+" Condition :  " + current_longitude.equals(longitude));

                            //mMsgView.setText(getString(R.string.msg_location_service_started) + "\n Latitude : " + latitude + "\n Longitude: " + longitude);
                            latitude = current_latitude;
                            longitude = current_longitude;
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
                                    }){
                                @Override
                                protected Map<String,String> getParams(){
                                    Map<String,String> params = new HashMap<String, String>();

                                    params.put("latitude", ""+ latitude);
                                    params.put("longitude",""+ longitude);
                                    params.put("route_code",""+ session.GetRoute());
                                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    String date = df.format(Calendar.getInstance().getTime());

                                    params.put("timestamp",date);

                                    //params.put(KEY_EMAIL, email);
                                    return params;
                                }
                            };

                            // Access the RequestQueue through your singleton class.
                            mytechbus.hpie.com.mytechbus.MySingleton.getInstance(LoginActivity.this).addToRequestQueue(stringRequest);
                            //------------------------------------------------------------------------------------------------------------

                        } else {
                            //Toast.makeText(getApplicationContext(), "Lat same " +  current_latitude.equals(latitude) + " || Long same :  " + current_longitude.equals(longitude),Toast.LENGTH_LONG).show();
                            Log.d("Suresh12345 : els", " current_latitude = " +current_latitude+" AND latitude = "+latitude+" Condition : " +  current_latitude.equals(latitude) + " || current_longitude = " +current_longitude+" AND longitude = "+longitude+" Condition :  " + current_longitude.equals(longitude));
                        }
                    }
                }, new IntentFilter(LocationMonitoringService.ACTION_LOCATION_BROADCAST)
        );

        //String file_contents = fIleOperations.readFromFile("ticket_wait_queue.txt", LoginActivity.this);
        //Log.d("Suresh12345 : els", file_contents);

        //------------------------------------------------------------
        Timer t = new Timer();
        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                String wait_queue_contents = fIleOperations.readFromFile("ticket_wait_queue.txt", LoginActivity.this);

                String upload_queue_contents = fIleOperations.readFromFile("ticket_upload_queue.txt", LoginActivity.this);

                if(!wait_queue_contents.equals("") || !upload_queue_contents.equals("")) {
                fIleOperations.writeToFile("ticket_wait_queue.txt", "", LoginActivity.this, "0");

                fIleOperations.writeToFile("ticket_upload_queue.txt", wait_queue_contents, LoginActivity.this, "1");

                    //------------------------------------------------------------------------------------------------------------
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.book_offline_ticket_url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.d("Ticket Booked : ", response);
                                    try {

                                        JSONObject ticket_response = new JSONObject(response);

                                        if (ticket_response.getInt(Constants.KEY_STATUS) == 1) {
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

                            params.put("ticket_data", ""+ file_upload_contents);

                            return params;
                        }
                    };

                    // Access the RequestQueue through your singleton class.
                    mytechbus.hpie.com.mytechbus.MySingleton.getInstance(LoginActivity.this).addToRequestQueue(stringRequest);
                    //------------------------------------------------------------------------------------------------------------
            }
            };
        };
        t.scheduleAtFixedRate(tt,new Date(),120000);
}

    @Override
    public void onRequestPermissionsResult(int requestCode,
        String permissions[], int[] grantResults) {

        Toast.makeText(getApplicationContext(),String.valueOf(requestCode),Toast.LENGTH_LONG).show();
        fetchLocation = new FetchLocation(LoginActivity.this,LoginActivity.this);
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

    private void login() {

        //Toast.makeText(getApplicationContext(),DEVICE_IMEI,Toast.LENGTH_LONG).show();
        displayLoader();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.login_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.dismiss();
                        //Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();

                        //Log.d("Suresh12345 : response", response);
                        //Log.d("Suresh12345 : login_url", Constants.login_url);

                        try {
                            //Check if user got logged in successfully
                            JSONObject login_response = new JSONObject(response);

                            //Log.d("Suresh12345 : status", login_response.getString(Constants.KEY_STATUS));

                            if (login_response.getInt(Constants.KEY_STATUS) == 1) {
                                //Log.d("Suresh12345 : username", login_response.getString(Constants.KEY_FULL_NAME));
                                session.loginUser(username,login_response.getString(Constants.KEY_FULL_NAME), login_response.getString(Constants.KEY_ROUTE_CODE));
                                loadDashboard();

                            }else{
                                //Log.d("Suresh12345 : error", response);
                                //Toast.makeText(getApplicationContext(), login_response.getString(Constants.KEY_MESSAGE), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            //Log.d("Suresh12345 : error", "Error occured");
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
                params.put(Constants.KEY_USERNAME,username);
                params.put(Constants.KEY_PASSWORD,password);
                params.put(Constants.KEY_UUID,"e082317ec532818");
                params.put(Constants.KEY_IMEI,DEVICE_IMEI);

                params.put(Constants.KEY_LATITUDE, ""+ fetchLocation.latitude);
                params.put(Constants.KEY_LONGITUDE,""+ fetchLocation.longitude);

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

            //mMsgView.setText(R.string.msg_location_service_started);

            //Start location sharing service to app server.........
            Intent intent = new Intent(this, LocationMonitoringService.class);
            startService(intent);

            mAlreadyStartedService = true;
            //Ends................................................
        }
    }


    @Override
    public void onDestroy() {

        //Stop location sharing service to app server.........

        stopService(new Intent(this, LocationMonitoringService.class));
        mAlreadyStartedService = false;
        //Ends................................................

        super.onDestroy();
    }






}