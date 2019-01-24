package mytechbus.hpie.com.mytechbus;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.PrivateKey;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class BookingActivity extends AppCompatActivity {

    private SessionHandler session;
    private ProgressDialog pDialog;
    private String route_stages_url = "http://mytechbus.hpie.in/routes_stages";
    private String book_ticket_url = "http://mytechbus.hpie.in/book_ticket";
    private static final String KEY_ROUTE = "route_code";
    private static final String KEY_EMPTY = "";

    private JSONObject end_stages;
    private JSONObject fare_km;
    private JSONObject fare_full;
    private JSONObject fare_half;
    private JSONObject fare_luggage;


    private TextView fullRate;
    private TextView halfRate;
    private TextView luggageRate;

    private EditText etFullPassengers;
    private EditText etHalfPassengers;
    private EditText etLuggage;
    private EditText etTotal;
    private EditText etMobile;

    // Start and end stage variables to get fare from fare object
    private String start_stage = "";
    private String end_stage = "";
    private Float full_rate = 0.0f;
    private Float half_rate = 0.0f;
    private Float luggage_rate = 0.0f;



    Spinner spinner, endspinner;
    ArrayList<String> StartStage, EndStage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        session = new SessionHandler(getApplicationContext());

        setContentView(R.layout.activity_booking);

        TextView etTxtBook = findViewById(R.id.textBook);

        etTxtBook.setText(session.GetRoute());

        Button book = findViewById(R.id.btnBook);

        //------------

        fullRate = findViewById(R.id.fullRate);
        halfRate = findViewById(R.id.halfRate);
        luggageRate = findViewById(R.id.luggageRate);

        etFullPassengers = findViewById(R.id.etFullPassengers);
        etHalfPassengers = findViewById(R.id.etHalfPassengers);
        etLuggage = findViewById(R.id.etLuggage);
        etTotal = findViewById(R.id.etTotal);
        etMobile = findViewById(R.id.etMobile);

        StartStage=new ArrayList<>();

        spinner = (Spinner)findViewById(R.id.startStage);
        endspinner = (Spinner)findViewById(R.id.endStage);
        route_stages();

        // Start stage change event
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                clear_fields();
                start_stage= spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();
                //Toast.makeText(getApplicationContext(),start_stage,Toast.LENGTH_LONG).show();

                set_end_stage(start_stage);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }
        });

        // End stage change event
        endspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                clear_fields();
                end_stage= endspinner.getItemAtPosition(endspinner.getSelectedItemPosition()).toString();
                //Toast.makeText(getApplicationContext(),end_stage,Toast.LENGTH_LONG).show();

                get_fare(start_stage, end_stage);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }
        });


        etLuggage.addTextChangedListener(new TextWatcher() {
            // the user's changes are saved here
            public void onTextChanged(CharSequence c, int start, int before, int count) {
                calculate_total();
            }

            public void beforeTextChanged(CharSequence c, int start, int count, int after) {
                // this space intentionally left blank
            }

            public void afterTextChanged(Editable c) {
                // this one too
            }
        });

        etHalfPassengers.addTextChangedListener(new TextWatcher() {
            // the user's changes are saved here
            public void onTextChanged(CharSequence c, int start, int before, int count) {
                calculate_total();
            }

            public void beforeTextChanged(CharSequence c, int start, int count, int after) {
                // this space intentionally left blank
            }

            public void afterTextChanged(Editable c) {
                // this one too
            }
        });

        etFullPassengers.addTextChangedListener(new TextWatcher() {
            // the user's changes are saved here
            public void onTextChanged(CharSequence c, int start, int before, int count) {
                calculate_total();
            }

            public void beforeTextChanged(CharSequence c, int start, int count, int after) {
                // this space intentionally left blank
            }

            public void afterTextChanged(Editable c) {
                // this one too
            }
        });

        //----------------



        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                book_ticket();
            }
        });

        Button back = findViewById(R.id.btnBack);
        //Launch Registration screen when Register Button is clicked
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BookingActivity.this, DashboardActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void displayLoader() {
        pDialog = new ProgressDialog(BookingActivity.this);
        pDialog.setMessage("Logging In.. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }

    private void clear_fields() {
        etFullPassengers.setText("");
        etHalfPassengers.setText("");
        etLuggage.setText("");
        etTotal.setText("");
    }

    private void add_data() {
        for(int i=0;i<10;i++){
            //JSONObject jsonObject1=jsonArray.getJSONObject(i);
            StartStage.add("Hi");

            }
            spinner.setAdapter(new ArrayAdapter<String>(BookingActivity.this, android.R.layout.simple_spinner_dropdown_item, StartStage));
    }

    private void calculate_total() {
        try {
            String full_text = (!etFullPassengers.getText().toString().trim().equals("")) ? etFullPassengers.getText().toString(): "0" ;
            String half_text = (!etHalfPassengers.getText().toString().trim().equals("")) ? etHalfPassengers.getText().toString(): "0" ;
            String luggage_text = (!etLuggage.getText().toString().trim().equals("")) ? etLuggage.getText().toString(): "0" ;

            Float passengers_full = Float.valueOf(full_text);


            Float passengers_half = Float.valueOf(half_text);
            Float passengers_luggage = Float.valueOf(luggage_text);

            Float total = (passengers_full * full_rate) + (passengers_half * half_rate) + (passengers_luggage * luggage_rate);

            etTotal.setText(Float.toString(total));

        } catch (Exception e) {
            Log.d("Suresh12345 : error", "Error occured");
            e.printStackTrace();
        }
    }

    private void get_fare(String start, String end) {
        try {
            EndStage=new ArrayList<>();
            JSONObject fare_full_rates = fare_full.getJSONObject(start);
            JSONObject fare_half_rates = fare_half.getJSONObject(start);
            JSONObject fare_luggage_rates = fare_luggage.getJSONObject(start);

            full_rate = Float.valueOf(fare_full_rates.getString(end));
            half_rate = Float.valueOf(fare_half_rates.getString(end));
            luggage_rate = Float.valueOf(fare_luggage_rates.getString(end));

            fullRate.setText(fare_full_rates.getString(end));
            halfRate.setText(fare_half_rates.getString(end));
            luggageRate.setText(fare_luggage_rates.getString(end));

        } catch (JSONException e) {
            Log.d("Suresh12345 : error", "Error occured");
            e.printStackTrace();
        }
    }

    private void set_end_stage(String start) {
        try {
            EndStage=new ArrayList<>();
            JSONArray jsonArray = end_stages.getJSONArray(start);

            for(int i=0;i<jsonArray.length();i++){
                String end=jsonArray.getString(i);

                EndStage.add(end);
            }
            endspinner.setAdapter(new ArrayAdapter<String>(BookingActivity.this, android.R.layout.simple_spinner_dropdown_item, EndStage));


        } catch (JSONException e) {
            Log.d("Suresh12345 : error", "Error occured");
            e.printStackTrace();
        }
    }

    // Get route details
    private void route_stages() {
        displayLoader();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, route_stages_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.dismiss();
                        //Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();

                        Log.d("Suresh12345 : response", response);

                        try {
                            //Check if user got logged in successfully
                            JSONObject route_response = new JSONObject(response);

                            JSONArray jsonArray=route_response.getJSONArray("start_stages");

                            end_stages =route_response.getJSONObject("end_stages");
                            fare_km =route_response.getJSONObject("fare_km");
                            fare_full =route_response.getJSONObject("fare_full");
                            fare_half =route_response.getJSONObject("fare_half");
                            fare_luggage =route_response.getJSONObject("fare_luggage");

                            for(int i=0;i<jsonArray.length();i++){
                                String start=jsonArray.getString(i);

                                StartStage.add(start);
                            }
                            spinner.setAdapter(new ArrayAdapter<String>(BookingActivity.this, android.R.layout.simple_spinner_dropdown_item, StartStage));

                        } catch (JSONException e) {
                            Log.d("Suresh12345 : error", "Error occured");
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
                params.put(KEY_ROUTE,session.GetRoute());

                return params;
            }

        };

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    // Book ticket
    private void book_ticket() {
        displayLoader();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, book_ticket_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.dismiss();
                        //Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();

                        Log.d("Suresh12345 : response", response);

                        try {
                            //Check if user got logged in successfully
                            JSONObject booking_response = new JSONObject(response);

                            if (booking_response.getInt("status") == 1) {
                                Toast.makeText(getApplicationContext(),"Ticket Booked",Toast.LENGTH_LONG).show();
                            }else{
                                Log.d("Suresh12345 : error", response);
                                Toast.makeText(getApplicationContext(),
                                        booking_response.getString("message"), Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            Log.d("Suresh12345 : error", "Error occured");
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

                String full_text = (!etFullPassengers.getText().toString().trim().equals("")) ? etFullPassengers.getText().toString(): "0" ;
                String half_text = (!etHalfPassengers.getText().toString().trim().equals("")) ? etHalfPassengers.getText().toString(): "0" ;
                String luggage_text = (!etLuggage.getText().toString().trim().equals("")) ? etLuggage.getText().toString(): "0" ;

                DateFormat booking_reference_df = new SimpleDateFormat("yyMMddHHmmssZ");
                String booking_reference = booking_reference_df.format(Calendar.getInstance().getTime());




                params.put("booking_reference", session.GetRoute() + "_" + booking_reference);
                params.put("route_code",session.GetRoute());
                params.put("start_stage",start_stage);
                params.put("end_stage",end_stage);
                params.put("fare_full_cost", String.valueOf(full_rate));
                params.put("fare_half_cost", String.valueOf(half_rate));
                params.put("fare_luggage_cost", String.valueOf(luggage_rate));
                params.put("fare_full_passengers",full_text);
                params.put("fare_half_passengers",half_text);
                params.put("fare_luggage",luggage_text);
                params.put("mobile",etMobile.getText().toString());

                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String date = df.format(Calendar.getInstance().getTime());

                params.put("booking_time",date);
                params.put("total_fare",etTotal.getText().toString());
                params.put("created_by","1");

                return params;
            }

        };

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}
