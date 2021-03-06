package mytechbus.hpie.com.mytechbus;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

public class BookingActivity extends AppCompatActivity implements View.OnClickListener {

    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;

    OutputStream mmOutputStream;
    InputStream mmInputStream;

    private SessionHandler session;
    private ProgressDialog pDialog;
    private FIleOperations fIleOperations;

    private JSONObject end_stages, fare_km, fare_full, fare_half, fare_luggage, discountObject;

    private TextView fullRate, halfRate, luggageRate;

    private EditText etFullPassengers, etHalfPassengers, etLuggage, etTotal, etMobile;

    private Button fullplus, fullminus, halfplus, halfminus, luggageplus, luggageminus, btnAllowPrint, btnCanclePrint;

    private String ticket_total;
    LocationManager manager;

    // Start and end stage variables to get fare from fare object
    private String start_stage = "";
    private String end_stage = "";
    private String ticket_km = "";
    private float full_rate = 0.0f;
    private float half_rate = 0.0f;
    private float luggage_rate = 0.0f;
    String route_stages_data = "";
     /*
    private Integer total_full_passangers = 0;
    private Integer total_half_passangers = 0;
    private Integer total_luggage_quantity = 0;
    */

    private Integer total_full_passangers = 0;
    private Integer total_half_passangers = 0;
    private Integer total_luggage_quantity = 0;

    private String discount_string = "0";
    private Integer total_full_cost = 0;
    private Integer total_half_cost = 0;
    private Integer total_luggage_cost = 0;
    private Integer total_ticket_cost = 0;
    private Integer discount_applied = 0;
    private Integer discounted_ticket_cost = 0;

    public Integer ticket_number;
    TextView etTicketNumber;

    String fileName;
    String file_date;
    String last_ticket;

    Spinner spinner, endspinner, spinnerDiscount;
    ArrayList<String> StartStage, EndStage, discountArray;

    JSONObject journey_type_object;
    RadioGroup radioGroup;

    String discount_key;

    //---------------------------------------------------------------------

    //--------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
        }

        session = new SessionHandler(getApplicationContext());
        fIleOperations = new FIleOperations();

        DateFormat file_dt = new SimpleDateFormat("yyyy_MM_dd");
        file_date = file_dt.format(Calendar.getInstance().getTime());
        fileName = session.getIMEI() + "_" + file_date.toString() + ".txt";

        setContentView(R.layout.activity_booking);

        TextView etTxtRoute = findViewById(R.id.textRoute);
        etTicketNumber = (TextView) findViewById(R.id.textTicketNumber);
        Button book = findViewById(R.id.btnBook);
        Button Reprint = findViewById(R.id.btnReprint);

        etTxtRoute.setText(session.GetRoute());

        ticket_number = fIleOperations.getTicketCount(fileName, this);

        etTicketNumber.setText(String.valueOf(ticket_number));

        // plus minus buttons for adding
        fullplus = (Button) findViewById(R.id.fullPlus);
        fullplus.setOnClickListener(this);
        fullminus = (Button) findViewById(R.id.fullMinus);
        fullminus.setOnClickListener(this);

        halfplus = (Button) findViewById(R.id.halfPlus);
        halfplus.setOnClickListener(this);
        halfminus = (Button) findViewById(R.id.halfMinus);
        halfminus.setOnClickListener(this);

        luggageplus = (Button) findViewById(R.id.luggagePlus);
        luggageplus.setOnClickListener(this);
        luggageminus = (Button) findViewById(R.id.luggageMinus);
        luggageminus.setOnClickListener(this);

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
        spinnerDiscount = (Spinner)findViewById(R.id.spinnerDiscount);
        radioGroup = (RadioGroup) findViewById(R.id.journeyType);

        // Initialize the dropdown with values
        route_stages();


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                RadioButton rb = (RadioButton)findViewById(checkedId);

                session.setJourneyType(rb.getText().toString());
                route_stages();
            }
        });

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

                get_km(start_stage, end_stage);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }
        });

        // Discount change event
        spinnerDiscount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //clear_fields();
                discount_key = spinnerDiscount.getItemAtPosition(spinnerDiscount.getSelectedItemPosition()).toString();


                try {
                    if (discountObject.has(discount_key)) {
                        discount_string = discountObject.getString(discount_key);
                       // Toast.makeText(getApplicationContext(), "Discount found" + discount_string,Toast.LENGTH_LONG).show();

                    } else {
                        discount_string = "0";
                               // Toast.makeText(getApplicationContext(), "No discount found" + discount_string,Toast.LENGTH_LONG).show();
                    }
                    calculate_total();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
                if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                    buildAlertMessageNoGps();
                    return;
                }

                ticket_total = etTotal.getText().toString().toLowerCase().trim();

                if (validateInputs()) {
                    //book_ticket();
                   confirmDialog();

                   // throw new RuntimeException("Test runtime exception");
                }
            }
        });

        Reprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialogReprint();
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

    public void confirmDialogReprint() {

        last_ticket = fIleOperations.readFromFile("last_ticket.txt", this);

        JSONObject last_ticket_obj = null;
        try {
            last_ticket_obj = new JSONObject(last_ticket);


        Log.d("myLogs", "Last Ticket : " + last_ticket);

        // custom dialog
        final Dialog dialog_reprint = new Dialog(BookingActivity.this);

        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.90);

            dialog_reprint.getWindow().setLayout(width, height);

        LayoutInflater inflater = this.getLayoutInflater();

        View promptTicketView = inflater.inflate(R.layout.dialog_ticket2,null);
        //------------------------------------------------------------------------------------
        final TextView proptFullRate =(TextView)promptTicketView.findViewById(R.id.proptFullRate);
        final TextView proptFullPassengers =(TextView)promptTicketView.findViewById(R.id.proptFullPassengers);
        final TextView proptFullTotal =(TextView)promptTicketView.findViewById(R.id.proptFullTotal);

        final TextView proptHalfRate =(TextView)promptTicketView.findViewById(R.id.proptHalfRate);
        final TextView proptHalfPassengers =(TextView)promptTicketView.findViewById(R.id.proptHalfPassengers);
        final TextView proptHalfTotal =(TextView)promptTicketView.findViewById(R.id.proptHalfTotal);

        final TextView proptLuggageRate =(TextView)promptTicketView.findViewById(R.id.proptLuggageRate);
        final TextView proptLugguage=(TextView)promptTicketView.findViewById(R.id.proptLuggage);
        final TextView proptLuggageTotal =(TextView)promptTicketView.findViewById(R.id.proptLuggageTotal);

        final TextView proptDiscountRate =(TextView)promptTicketView.findViewById(R.id.proptDiscountRate);
        final TextView proptTotalForDiscount=(TextView)promptTicketView.findViewById(R.id.proptTotalForDiscount);
        final TextView proptDiscountApplied =(TextView)promptTicketView.findViewById(R.id.proptDiscountApplied);

        final TextView proptTicketTotal =(TextView)promptTicketView.findViewById(R.id.proptTicketTotal);
        final TextView proptDiscountedTicketTotal =(TextView)promptTicketView.findViewById(R.id.proptDiscountedTicketTotal);

        //------------------------------------------

        //Log.d("myLogs", "discount_string : " + discount_string + " ||| discounted_ticket_cost : " + discounted_ticket_cost+ " ||| discount_applied : " + discount_applied);

            ticket_number = Integer.valueOf(last_ticket_obj.getString("ticket_number"));
            start_stage = last_ticket_obj.getString("start_stage");
            end_stage = last_ticket_obj.getString("end_stage");
            ticket_km = last_ticket_obj.getString("ticket_km");

            total_full_passangers = Integer.valueOf(last_ticket_obj.getString("fare_full_passengers"));
            full_rate = Float.parseFloat(last_ticket_obj.getString("fare_full_cost"));
            total_full_cost = Integer.valueOf(last_ticket_obj.getString("total_full_cost"));

            total_half_passangers = Integer.valueOf(last_ticket_obj.getString("fare_half_passengers"));
            half_rate = Float.parseFloat(last_ticket_obj.getString("fare_half_cost"));
            total_half_cost = Integer.valueOf(last_ticket_obj.getString("total_half_cost"));

            total_luggage_quantity = Integer.valueOf(last_ticket_obj.getString("fare_luggage"));
            luggage_rate = Float.parseFloat(last_ticket_obj.getString("fare_luggage_cost"));
            total_luggage_cost = Integer.valueOf(last_ticket_obj.getString("total_luggage_cost"));

            discount_string = last_ticket_obj.getString("discount");
            //total_full_cost = last_ticket_obj.getString("end_stage");
            discount_applied = Integer.valueOf(last_ticket_obj.getString("discount_applied"));

            discounted_ticket_cost = Integer.valueOf(last_ticket_obj.getString("discounted_total"));

        proptDiscountRate.setText(last_ticket_obj.getString("discount"));
        proptTotalForDiscount.setText(last_ticket_obj.getString("total_full_cost"));
        proptDiscountApplied.setText(last_ticket_obj.getString("discount_applied"));


        proptFullRate.setText(last_ticket_obj.getString("fare_full_cost"));
        proptFullPassengers.setText(last_ticket_obj.getString("fare_full_passengers"));

        proptHalfRate.setText(last_ticket_obj.getString("fare_half_cost"));
        proptHalfPassengers.setText(last_ticket_obj.getString("fare_half_passengers"));

        proptLuggageRate.setText(last_ticket_obj.getString("fare_luggage_cost"));
        proptLugguage.setText(last_ticket_obj.getString("fare_luggage"));

        //Double fulltotal = Double.valueOf(etFullPassengers.getText().toString()) * full_rate;
        proptFullTotal.setText(last_ticket_obj.getString("total_full_cost"));

        //Double halftotal = Double.valueOf(etHalfPassengers.getText().toString()) * half_rate;
        proptHalfTotal.setText(last_ticket_obj.getString("total_half_cost"));

        //Double luggagetotal = Double.valueOf(etLuggage.getText().toString()) * luggage_rate;
        proptLuggageTotal.setText(last_ticket_obj.getString("total_luggage_cost"));

        proptTicketTotal.setText(last_ticket_obj.getString("total_fare"));
        proptDiscountedTicketTotal.setText(last_ticket_obj.getString("discounted_total"));
        //----------------------------------------------------------------------------------------

            dialog_reprint.setContentView(promptTicketView);

        //dialog.setContentView(R.layout.dialog_ticket2);
            dialog_reprint.setTitle("Title...");

        btnCanclePrint = (Button) dialog_reprint.findViewById(R.id.btnAllowPrint);

        btnCanclePrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                activity_log("reprint", last_ticket);

                PrintBill();
                dialog_reprint.dismiss();
            }
        });

        btnCanclePrint = (Button) dialog_reprint.findViewById(R.id.btnCanclePrint);
        // if button is clicked, close the custom dialog
        btnCanclePrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_reprint.dismiss();
            }
        });

            dialog_reprint.show();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void confirmDialog() {
        // custom dialog
        final Dialog dialog = new Dialog(BookingActivity.this);

        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.90);

        dialog.getWindow().setLayout(width, height);

        LayoutInflater inflater = this.getLayoutInflater();

        View promptTicketView = inflater.inflate(R.layout.dialog_ticket2,null);

        //------------------------------------------------------------------------------------
        final TextView proptFullRate =(TextView)promptTicketView.findViewById(R.id.proptFullRate);
        final TextView proptFullPassengers =(TextView)promptTicketView.findViewById(R.id.proptFullPassengers);
        final TextView proptFullTotal =(TextView)promptTicketView.findViewById(R.id.proptFullTotal);

        final TextView proptHalfRate =(TextView)promptTicketView.findViewById(R.id.proptHalfRate);
        final TextView proptHalfPassengers =(TextView)promptTicketView.findViewById(R.id.proptHalfPassengers);
        final TextView proptHalfTotal =(TextView)promptTicketView.findViewById(R.id.proptHalfTotal);

        final TextView proptLuggageRate =(TextView)promptTicketView.findViewById(R.id.proptLuggageRate);
        final TextView proptLugguage=(TextView)promptTicketView.findViewById(R.id.proptLuggage);
        final TextView proptLuggageTotal =(TextView)promptTicketView.findViewById(R.id.proptLuggageTotal);

        final TextView proptDiscountRate =(TextView)promptTicketView.findViewById(R.id.proptDiscountRate);
        final TextView proptTotalForDiscount=(TextView)promptTicketView.findViewById(R.id.proptTotalForDiscount);
        final TextView proptDiscountApplied =(TextView)promptTicketView.findViewById(R.id.proptDiscountApplied);

        final TextView proptTicketTotal =(TextView)promptTicketView.findViewById(R.id.proptTicketTotal);
        final TextView proptDiscountedTicketTotal =(TextView)promptTicketView.findViewById(R.id.proptDiscountedTicketTotal);

        //------------------------------------------

        //Log.d("myLogs", "discount_string : " + discount_string + " ||| discounted_ticket_cost : " + discounted_ticket_cost+ " ||| discount_applied : " + discount_applied);

        proptDiscountRate.setText(discount_string);
        proptTotalForDiscount.setText(String.valueOf(total_full_cost));
        proptDiscountApplied.setText(String.valueOf(discount_applied));


        proptFullRate.setText(String.valueOf(full_rate));
        proptFullPassengers.setText(etFullPassengers.getText().toString());

        proptHalfRate.setText(String.valueOf(half_rate));
        proptHalfPassengers.setText(etHalfPassengers.getText().toString());

        proptLuggageRate.setText(String.valueOf(luggage_rate));
        proptLugguage.setText(etLuggage.getText().toString());

        //Double fulltotal = Double.valueOf(etFullPassengers.getText().toString()) * full_rate;
        proptFullTotal.setText(String.valueOf(total_full_cost));

        //Double halftotal = Double.valueOf(etHalfPassengers.getText().toString()) * half_rate;
        proptHalfTotal.setText(String.valueOf(total_half_cost));

        //Double luggagetotal = Double.valueOf(etLuggage.getText().toString()) * luggage_rate;
        proptLuggageTotal.setText(String.valueOf(total_luggage_cost));

        proptTicketTotal.setText(etTotal.getText().toString());
        proptDiscountedTicketTotal.setText(String.valueOf(discounted_ticket_cost));
        //----------------------------------------------------------------------------------------

        dialog.setContentView(promptTicketView);

        //dialog.setContentView(R.layout.dialog_ticket2);
        dialog.setTitle("Title...");

        btnCanclePrint = (Button) dialog.findViewById(R.id.btnAllowPrint);

        btnCanclePrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                book_ticket();
                dialog.dismiss();
            }
        });


        btnCanclePrint = (Button) dialog.findViewById(R.id.btnCanclePrint);
        // if button is clicked, close the custom dialog
        btnCanclePrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void displayLoader() {
        pDialog = new ProgressDialog(BookingActivity.this);
        pDialog.setMessage("Logging In.. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }

    private void clear_fields() {
        etFullPassengers.setText("0");
        etHalfPassengers.setText("0");
        etLuggage.setText("0");
        etTotal.setText("");
        etMobile.setText("");
        spinnerDiscount.setSelection(0);
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
            total_full_passangers   = Integer.valueOf(etFullPassengers.getText().toString());
            total_half_passangers   = Integer.valueOf(etHalfPassengers.getText().toString());
            total_luggage_quantity  = Integer.valueOf(etLuggage.getText().toString());

            /**
            *   Check if rate is less than minimum ticket price
             */

            //Log.d("myLogs", "Min Ticket : " + session.getMinTicket());
            if(full_rate < session.getMinTicket()) {
                total_full_cost         = Math.round(total_full_passangers * session.getMinTicket());
            } else {
                total_full_cost         = Math.round(total_full_passangers * full_rate);
            }

            if(half_rate < session.getMinTicket()) {
                total_half_cost         = Math.round(total_half_passangers * session.getMinTicket());
            } else {
                total_half_cost         = Math.round(total_half_passangers * half_rate);
            }


            total_luggage_cost      = Math.round(total_luggage_quantity * luggage_rate);

            total_ticket_cost =  total_full_cost + total_half_cost + total_luggage_cost;

            //Log.d("myLogs", "total_full_cost : " + total_full_cost + " ||| total_half_cost : " + total_half_cost+ " ||| total_luggage_cost : " + total_luggage_cost);


            discount_applied = (total_full_cost * Integer.valueOf(discount_string))/100;

            discounted_ticket_cost = total_ticket_cost - discount_applied;

            //Log.d("myLogs", "total_ticket_cost : " + total_ticket_cost + " ||| discount_string : " + discount_string+ " ||| discount_applied : " + discount_applied);


            etTotal.setText(Integer.toString(total_ticket_cost));

        } catch (Exception e) {
            Log.d("myLogs", "Error occured");
            e.printStackTrace();
        }

    }

    private void get_fare(String start, String end) {
        try {
            EndStage=new ArrayList<>();
            JSONObject fare_full_rates = fare_full.getJSONObject(start);
            JSONObject fare_half_rates = fare_half.getJSONObject(start);
            JSONObject fare_luggage_rates = fare_luggage.getJSONObject(start);


            full_rate = Integer.valueOf(fare_full_rates.getString(end)) ;
            half_rate = Integer.valueOf(fare_half_rates.getString(end));


            if(full_rate < session.getMinTicket()) {
                full_rate         = session.getMinTicket();
            }

            if(half_rate < session.getMinTicket()) {
                half_rate         = session.getMinTicket();
            }

            luggage_rate = Integer.valueOf(fare_luggage_rates.getString(end));
            /*
            fullRate.setText(fare_full_rates.getString(end));
            halfRate.setText(fare_half_rates.getString(end));
            */
            fullRate.setText(String.valueOf(full_rate));
            halfRate.setText(String.valueOf(half_rate));

            luggageRate.setText(fare_luggage_rates.getString(end));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void get_km(String start, String end) {
        try {
            JSONObject fare_km_obj = fare_km.getJSONObject(start);

            ticket_km = fare_km_obj.getString(end);

            //Log.d("myLogs", "Ticket Kms : " + ticket_km);
        } catch (JSONException e) {
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
        //route_stages_data = fIleOperations.readFromFile("route_stages.txt", this);

        route_stages_data = fIleOperations.readFromFile("trip_data.txt", this);
        if(!route_stages_data.equals("")) {
            update_routes_spinner2(route_stages_data);
            //Toast.makeText(getApplicationContext(),"Data exists",Toast.LENGTH_LONG).show();
            //Log.d("Hiiiiii if: ", route_stages_data);
            return;
        } else  {
            //Toast.makeText(getApplicationContext(),"No Data exists",Toast.LENGTH_LONG).show();

            //Log.d("Hiiielseiii : ", route_stages_data);
        }

        displayLoader();
        /*
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.route_stages_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.dismiss();
                        //Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();

                        //Log.d("Route stages response", response);

                        try {
                            JSONObject route_response = new JSONObject(response);

                            if (route_response.getInt(Constants.KEY_STATUS) == 1) {
                                //fIleOperations.writeToFile("route_stages.txt", response, BookingActivity.this, "0");

                                //update_routes_spinner(response);

                            }else{
                                //Toast.makeText(getApplicationContext(),route_response.getString(Constants.KEY_MESSAGE),Toast.LENGTH_LONG).show();
                                loadDashboard(route_response.getString(Constants.KEY_MESSAGE));
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
                params.put(Constants.KEY_ROUTE,session.GetRoute());

                return params;
            }

        };

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
        */
    }

    /**
     * Launch Dashboard Activity if no route data found
     */
    private void loadDashboard(String message) {
        Intent i = new Intent(getApplicationContext(), DashboardActivity.class);

        //Create the bundle
        Bundle bundle = new Bundle();

        //Add your data to bundle
        bundle.putString(Constants.KEY_MESSAGE, message);

        //Add the bundle to the intent
        i.putExtras(bundle);

        startActivity(i);
        finish();
    }

    public void update_routes_spinner2(String response) {
        try {
            JSONObject trip_data = new JSONObject(response);

            JSONObject routes_object = trip_data.getJSONObject("routes");

            //routes_object

           // Log.d("myLogs", "In update_routes_spinner2 : " + routes_object.length());


           // Toast.makeText(getApplicationContext(), session.getJourneyType(), Toast.LENGTH_SHORT).show();

            Iterator<String> iter = routes_object.keys();
            Integer j = 0;
            String journey_type = "";

            RadioButton radioButton;

            //Add te
            while (iter.hasNext()) {
                String key = iter.next();

                if(key.equals("ONWARD")) {
                    radioButton = findViewById(R.id.radio_onward);
                    radioButton.setVisibility(View.VISIBLE);
                    if(session.getJourneyType().equals(key)) {
                        radioButton.setChecked(true);
                    }
                }

                if(key.equals("RETURN")) {
                    radioButton = findViewById(R.id.radio_return);
                    radioButton.setVisibility(View.VISIBLE);
                    if(session.getJourneyType().equals(key)) {
                        radioButton.setChecked(true);
                    }
                }

                if(key.equals("CIRCULAR")) {
                    radioButton = findViewById(R.id.radio_circular);
                    radioButton.setVisibility(View.VISIBLE);
                    if(session.getJourneyType().equals(key)) {
                        radioButton.setChecked(true);
                    }
                }

                if(j == 0) {
                    journey_type = key;
                }
                j++;
            }

            if(session.getJourneyType().equals(Constants.KEY_EMPTY)) {
                session.setJourneyType(journey_type);
            }

            journey_type_object = routes_object.getJSONObject(session.getJourneyType());

            //Log.d("myLogs", "In update_routes_spinner2 1111111111111111111 key : " + session.getJourneyType() + " |||| " + journey_type_object);

            JSONArray startStagesArray = journey_type_object.getJSONArray("start_stages");

            //Log.d("myLogs", "In update_routes_spinner2 222startStagesArray22222 key : " + session.getJourneyType() + " |||| " + startStagesArray);

            end_stages      = journey_type_object.getJSONObject("end_stages");
            fare_km         = journey_type_object.getJSONObject("fare_km");
            fare_full       = journey_type_object.getJSONObject("fare_full");
            fare_half       = journey_type_object.getJSONObject("fare_half");
            fare_luggage    = journey_type_object.getJSONObject("fare_luggage");
            discountObject  = trip_data.getJSONObject("discounts");

            //------------------------------------------------------------------------------------------

            discountArray = new ArrayList<>();

            Iterator<String> discountItr = discountObject.keys();

            discountArray.add("No Discount");
            //Create discount spinner data
            while (discountItr.hasNext()) {
                String key = discountItr.next();
                discountArray.add(key);
            }

            //Log.d("myLogs", "Object : " + discountItr + " ||| array : " + discountArray);

            spinnerDiscount.setAdapter(new ArrayAdapter<String>(BookingActivity.this, android.R.layout.simple_spinner_dropdown_item, discountArray));
            //-----------------------------------------------------------------------
            StartStage=new ArrayList<>();

            for(int i=0;i<startStagesArray.length();i++){
                String start=startStagesArray.getString(i);
                StartStage.add(start);
            }
            spinner.setAdapter(new ArrayAdapter<String>(BookingActivity.this, android.R.layout.simple_spinner_dropdown_item, StartStage));

            /*
            JSONArray jsonArray = trip_data.getJSONArray("start_stages");

            end_stages      = route_response.getJSONObject("end_stages");
            fare_km         = route_response.getJSONObject("fare_km");
            fare_full       = route_response.getJSONObject("fare_full");
            fare_half       = route_response.getJSONObject("fare_half");
            fare_luggage    = route_response.getJSONObject("fare_luggage");

            for(int i=0;i<jsonArray.length();i++){
                String start=jsonArray.getString(i);
                StartStage.add(start);
            }
            spinner.setAdapter(new ArrayAdapter<String>(BookingActivity.this, android.R.layout.simple_spinner_dropdown_item, StartStage));
            */
        } catch (JSONException e) {
            Log.w("myLogs", "In update_routes_spinner2 key : " + session.getJourneyType() + " |||| " + journey_type_object);
            e.printStackTrace();
        }
    }

    // Book ticket
    private void book_ticket() {

        displayLoader();

        //Create JSON Object

        JSONObject file_data_store = new JSONObject();
        try {
            // file_data_store.put("id", "3");

            String full_text    = etFullPassengers.getText().toString();
            String half_text    = etHalfPassengers.getText().toString();
            String luggage_text = etLuggage.getText().toString();

            // Toast.makeText(getApplicationContext(),full_text + " " +half_text + " " +luggage_text, Toast.LENGTH_LONG).show();

            DateFormat booking_reference_df = new SimpleDateFormat("yyMMddHHmmssZ");
            String booking_reference = booking_reference_df.format(Calendar.getInstance().getTime());

            file_data_store.put("booking_reference", session.getIMEI() + "_" + booking_reference);
            file_data_store.put("route_code",session.GetRoute());
            file_data_store.put("ticket_number", fIleOperations.getTicketCount(fileName, this) );
            file_data_store.put("start_stage",start_stage);
            file_data_store.put("end_stage",end_stage);
            file_data_store.put("fare_full_cost", String.valueOf(full_rate));
            file_data_store.put("fare_half_cost", String.valueOf(half_rate));
            file_data_store.put("fare_luggage_cost", String.valueOf(luggage_rate));
            file_data_store.put("fare_full_passengers",full_text);
            file_data_store.put("fare_half_passengers",half_text);
            file_data_store.put("fare_luggage",luggage_text);
            file_data_store.put("mobile",etMobile.getText().toString());

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = df.format(Calendar.getInstance().getTime());

            file_data_store.put("booking_time",date);
            file_data_store.put("total_fare",etTotal.getText().toString());


            file_data_store.put("discount", discount_string);
            file_data_store.put("discount_applied", discount_applied);
            file_data_store.put("discounted_total", discounted_ticket_cost);


            file_data_store.put("vehicle_number", session.getVehicleNumber());
            file_data_store.put("operator_id", session.getOperatorId());
            file_data_store.put("created_by", session.getUserId());

            //Log.d("Ticket Booked : ", String.valueOf(file_data_store));

            //------------------------------------------------------------------------------


            // Add ticket details in local log daily file

            // Add ticket details in local log daily file in log folder
            fIleOperations.writeToLogFile(fileName, file_data_store.toString(), this, "1");

            // Add ticket details server upload waiting queue file
            fIleOperations.writeToFile("ticket_wait_queue.txt", file_data_store.toString(), this, "1");

            // Add Last ticket details in local file
            file_data_store.put("duplicate_ticket","1");

            file_data_store.put("total_full_cost",total_full_cost);
            file_data_store.put("total_half_cost", total_half_cost);
            file_data_store.put("total_luggage_cost",total_luggage_cost);
            file_data_store.put("ticket_km",ticket_km);

            fIleOperations.writeToFile("last_ticket.txt", file_data_store.toString(), this, "0");

            //------------------------------------------------------------------------------

            Toast.makeText(getApplicationContext(),"Ticket Booked successfully", Toast.LENGTH_SHORT).show();


            //----------------------------

            activity_log("book_ticket", file_data_store.toString());

            PrintBill();
            ticket_number = fIleOperations.getTicketCount(fileName, this);

            etTicketNumber.setText(String.valueOf(ticket_number));

            clear_fields();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            activity_log("book_ticket", "Ticket not booked. Please try again! " + e.toString() + " || Ticket Data : " + file_data_store.toString());
            Toast.makeText(getApplicationContext(),"Ticket not booked. Please try again!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        //String file_contents = fIleOperations.readFromFile(fileName, this);

        //Log.d("myLogs", "Ticket Booking Data : " + file_contents);

        pDialog.dismiss();

    }

    /**
     * Validates inputs and shows error if any
     * @return
     */
    private boolean validateInputs() {
        if(Constants.KEY_EMPTY.equals(ticket_total)){
            //etTotal.setError("Please add passangers or luggage");

            Toast.makeText(getApplicationContext(),"Please addd fare details.",Toast.LENGTH_SHORT).show();
            return false;
        } else {
            Double validate_total = Double.valueOf(ticket_total);

            if(validate_total <= 0) {
                Toast.makeText(getApplicationContext(),"Please addd fare details.",Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.fullPlus:
                updateNumber("plus", etFullPassengers);
                break;

            case R.id.fullMinus:
                updateNumber("minus", etFullPassengers);
                break;

            case R.id.halfPlus:
                updateNumber("plus", etHalfPassengers);
                break;

            case R.id.halfMinus:
                updateNumber("minus", etHalfPassengers);
                break;

            case R.id.luggagePlus:
                updateNumber("plus", etLuggage);
                break;

            case R.id.luggageMinus:
                updateNumber("minus", etLuggage);
                break;

            default:
                break;
        }
    }

    public void updateNumber(String type, EditText editText) {

        String quantity = editText.getText().toString().trim();

        if(type == "plus") {
            quantity = String.valueOf(1 + Integer.valueOf(quantity));
        } else if(type == "minus") {

            if(!quantity.equals("0")) {
                quantity = String.valueOf(Integer.valueOf(quantity) - 1);
            }
        }

        editText.setText(quantity);
    }

    //----------------------------------------------------------------

    public void PrintBill() {
        ticket_number = fIleOperations.getTicketCount(fileName, this) - 1;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String currentDateandTime = sdf.format(new Date());

        Log.d("myLogs", "Print ticket called : " + currentDateandTime + " ||| full_rate : " + full_rate + " ||| total_full_cost : " + total_full_cost + " ||| half_rate : " + half_rate + " ||| discount_applied : " + discount_applied + " ||| ticket_km : " + ticket_km);


        Log.d("myLogs", "Print ticket called : " + currentDateandTime + " ||| ticket_number : " + ticket_number );
        String msg = "";
        byte[] format;
        byte[] center;
        byte[] arrayOfByte;
        try {
            if (Constants.mmSocket != null && Constants.mmOutputStream != null && Constants.mmInputStream != null) {
                /*Title*/
                // msg = "My Tech Bus"+ "\n";
                msg = session.getOperator() + "\n";
                format = new byte[]{27, 33, 20};
                center = new byte[]{0x1b, 0x61, 0x01};
                //arrayOfByte = new byte[]{27, 33, 0};
                //format[2] = ((byte) (0x8 | arrayOfByte[2]));
                Constants.mmOutputStream.write(center);
                Constants.mmOutputStream.write(format);
                Constants.mmOutputStream.write(msg.getBytes(), 0, msg.getBytes().length);

                /*Operator Address*/
                msg = session.getOperatorAddress1() +  ", \n" + session.getOperatorAddress2() +  ", " + session.getOperatorCity() +  ", Ph: \n" + session.getOperatorHelpline() + "\n";

                //format = new byte[]{27, 33, 0};
                arrayOfByte = new byte[]{27, 33, 0};
                // Bold
                //format[2] = ((byte)(0x8 | arrayOfByte[2]));
                // Height
                //format[2] = ((byte)(0x10 | arrayOfByte[2]));
                // Width
                //format[2] = ((byte) (0x20 | arrayOfByte[2]));
                // Underline
                // format[2] = ((byte)(0x80 | arrayOfByte[2]));
                // Small
                format[2] = ((byte)(0x1 | arrayOfByte[2]));
                Constants.mmOutputStream.write(center);
                Constants.mmOutputStream.write(format);
                Constants.mmOutputStream.write(msg.getBytes(), 0, msg.getBytes().length);


                /*Bus number*/
                msg = "Bus No. "+ session.getVehicleNumber() +"  "+ "\n";
                //format = new byte[]{27, 33, 0};
                arrayOfByte = new byte[]{27, 33, 0};

                // Bold
                //format[2] = ((byte)(0x8 | arrayOfByte[2]));
                // Height
                //format[2] = ((byte)(0x10 | arrayOfByte[2]));
                // Width
                //format[2] = ((byte) (0x20 | arrayOfByte[2]));
                // Underline
                // format[2] = ((byte)(0x80 | arrayOfByte[2]));
                // Small
                format[2] = ((byte)(0x1 | arrayOfByte[2]));
                Constants.mmOutputStream.write(center);
                Constants.mmOutputStream.write(format);
                Constants.mmOutputStream.write(msg.getBytes(), 0, msg.getBytes().length);

                /*Journey Datetime*/
                msg = "Date:  " +currentDateandTime+ "\n";
                format = new byte[]{27, 33, 0};
                arrayOfByte = new byte[]{27, 33, 0};
                format[2] = ((byte) (0x8 | arrayOfByte[2]));
                Constants.mmOutputStream.write(center);
                Constants.mmOutputStream.write(format);
                Constants.mmOutputStream.write(msg.getBytes(), 0, msg.getBytes().length);

                /*Ticket number*/
                msg = "T.No: "+ticket_number+"  " + "\n";
                format = new byte[]{27, 33, 0};
                arrayOfByte = new byte[]{27, 33, 0};
                format[2] = ((byte) (0x8 | arrayOfByte[2]));
                Constants.mmOutputStream.write(center);
                Constants.mmOutputStream.write(format);
                Constants.mmOutputStream.write(msg.getBytes(), 0, msg.getBytes().length);

                // From and ton in two lines
                /*From*/
                /*msg = "From: "+ start_stage + "\n";
                format = new byte[]{27, 33, 0};
                arrayOfByte = new byte[]{27, 33, 0};
                format[2] = ((byte) (0x8 | arrayOfByte[2]));
                Constants.mmOutputStream.write(center);
                Constants.mmOutputStream.write(format);
                Constants.mmOutputStream.write(msg.getBytes(), 0, msg.getBytes().length);*/

                /*To*/
                /*msg = "To: "+ end_stage + "\n";
                //format = new byte[]{27, 33, 0};
                arrayOfByte = new byte[]{27, 33, 0};
                format[2] = ((byte) (0x8 | arrayOfByte[2]));
                Constants.mmOutputStream.write(center);
                Constants.mmOutputStream.write(format);
                Constants.mmOutputStream.write(msg.getBytes(), 0, msg.getBytes().length);*/

                /*From To Single line */
                msg = start_stage + " to " +end_stage+ "\n";
                format = new byte[]{27, 33, 0};
                //arrayOfByte = new byte[]{27, 33, 0};
                //format[2] = ((byte) (0x8 | arrayOfByte[2]));
                Constants.mmOutputStream.write(center);
                Constants.mmOutputStream.write(format);
                Constants.mmOutputStream.write(msg.getBytes(), 0, msg.getBytes().length);

                /*Journey Km*/
                msg = "(Journey KM: "+ ticket_km + ")\n";
                format = new byte[]{27, 33, 0};
                arrayOfByte = new byte[]{27, 33, 0};

                // Bold
                //format[2] = ((byte)(0x8 | arrayOfByte[2]));
                // Height
                //format[2] = ((byte)(0x10 | arrayOfByte[2]));
                // Width
                //format[2] = ((byte) (0x20 | arrayOfByte[2]));
                // Underline
                // format[2] = ((byte)(0x80 | arrayOfByte[2]));
                // Small
                format[2] = ((byte)(0x1 | arrayOfByte[2]));
                Constants.mmOutputStream.write(center);
                Constants.mmOutputStream.write(format);
                Constants.mmOutputStream.write(msg.getBytes(), 0, msg.getBytes().length);

                /*Bill*/
                StringBuilder sb = new StringBuilder("");
                //sb.append("------------------------").append("\n");  // for normal
                sb.append("--------------------------------").append("\n"); // for small
                //sb.append("TYPE  ").append("QTY").append("  FARE").append("   AMT").append("\n");  // for normal
                sb.append("TYPE  ").append("QTY").append("  FARE").append("     AMT").append("\n"); // for small
                //sb.append("------------------------").append("\n"); // for normal
                sb.append("--------------------------------").append("\n"); // for small

                /*
                sb.append("Full    x").append(" 10").append("  120").append("    1200").append("\n");
                sb.append("Half    x").append(" 10").append("   60").append("     600").append("\n");
                sb.append("Lugg    x").append("  2").append("  100").append("     200").append("\n");
                sb.append("Disc    %").append("  5").append("  120").append("     100").append("\n");

                sb.append("------------------------").append("\n");
                sb.append("Total              "+discounted_ticket_cost).append("\n");

                */
                if(total_full_passangers !=0) {
                    sb.append("Full:").append(" " + total_full_passangers).append("x " + full_rate).append("     " + total_full_cost).append("\n");
                }

                if(total_half_passangers !=0) {
                    sb.append("Half:").append(" " + total_half_passangers).append("x "+half_rate).append("     "+total_half_cost).append("\n");
                }

                if(total_luggage_quantity !=0) {
                    sb.append("Lugg:").append(" " + total_luggage_quantity).append("x "+luggage_rate).append("     "+total_luggage_cost).append("\n");
                }

                if(discount_applied !=0) {
                    sb.append("Disc:").append(" " + discount_string).append("%of "+total_full_cost).append("   -"+discount_applied).append("\n");
                }


                //sb.append("------------------------").append("\n");
                //sb.append("Total:       ").append("    "+discounted_ticket_cost).append("\n");
                ////sb.append("GST (5%)            85").append("\n");
                //sb.append("------------------------");
                msg = sb.toString() + "\n";
                //fIleOperations.writeToFile("ticketprint.txt", msg, this, "0");
                Log.d("myLogs", msg);
                //format = new byte[]{27, 33, 0};
                // Bold
                //format[2] = ((byte)(0x8 | arrayOfByte[2]));
                // Height
                //format[2] = ((byte)(0x10 | arrayOfByte[2]));
                // Width
                //format[2] = ((byte) (0x20 | arrayOfByte[2]));
                // Underline
                // format[2] = ((byte)(0x80 | arrayOfByte[2]));
                // Small
                format[2] = ((byte)(0x1 | arrayOfByte[2]));
                Constants.mmOutputStream.write(format);
                Constants.mmOutputStream.write(msg.getBytes(), 0, msg.getBytes().length);


                //msg ="------------------------" + "\n";  // for Normal
                msg ="--------------------------------" + "\n";  // for small
                //format = new byte[]{27, 33, 0};
                arrayOfByte = new byte[]{27, 33, 0};
                //format = new byte[]{27, 33, 0};
                // Bold
                //format[2] = ((byte)(0x8 | arrayOfByte[2]));
                // Height
                //format[2] = ((byte)(0x10 | arrayOfByte[2]));
                // Width
                //format[2] = ((byte) (0x20 | arrayOfByte[2]));
                // Underline
                // format[2] = ((byte)(0x80 | arrayOfByte[2]));
                // Small
                format[2] = ((byte)(0x1 | arrayOfByte[2]));
                Constants.mmOutputStream.write(format);
                Constants.mmOutputStream.write(msg.getBytes(), 0, msg.getBytes().length);

                msg ="Total:           "+discounted_ticket_cost+ "\n";
                format = new byte[]{27, 33, 20};
                Constants.mmOutputStream.write(format);
                Constants.mmOutputStream.write(msg.getBytes(), 0, msg.getBytes().length);

                //msg ="------------------------" + "\n";  // for Normal
                msg ="--------------------------------" + "\n";  // for small
                //format = new byte[]{27, 33, 0};
                arrayOfByte = new byte[]{27, 33, 0};
                //format = new byte[]{27, 33, 0};
                // Bold
                //format[2] = ((byte)(0x8 | arrayOfByte[2]));
                // Height
                //format[2] = ((byte)(0x10 | arrayOfByte[2]));
                // Width
                //format[2] = ((byte) (0x20 | arrayOfByte[2]));
                // Underline
                // format[2] = ((byte)(0x80 | arrayOfByte[2]));
                // Small
                format[2] = ((byte)(0x1 | arrayOfByte[2]));
                Constants.mmOutputStream.write(format);
                Constants.mmOutputStream.write(msg.getBytes(), 0, msg.getBytes().length);

                //msg = "Total Rs.  "+discounted_ticket_cost + "\n";
                //format = new byte[]{27, 33, 15};
                //Constants.mmOutputStream.write(center);
                //Constants.mmOutputStream.write(format);
                //Constants.mmOutputStream.write(msg.getBytes(), 0, msg.getBytes().length);

                //msg = "Have a nice journey" + "\n";

                msg = session.getTicketMessage() + "\n";
                //format = new byte[]{27, 33, 0};
                arrayOfByte = new byte[]{27, 33, 0};
                //format = new byte[]{27, 33, 0};
                // Bold
                //format[2] = ((byte)(0x8 | arrayOfByte[2]));
                // Height
                //format[2] = ((byte)(0x10 | arrayOfByte[2]));
                // Width
                //format[2] = ((byte) (0x20 | arrayOfByte[2]));
                // Underline
                // format[2] = ((byte)(0x80 | arrayOfByte[2]));
                // Small
                format[2] = ((byte)(0x1 | arrayOfByte[2]));
                Constants.mmOutputStream.write(center);
                Constants.mmOutputStream.write(format);
                Constants.mmOutputStream.write(msg.getBytes(), 0, msg.getBytes().length);

                msg = "--- Not Transferable ---" + "\n";
                //format = new byte[]{27, 33, 0};
                arrayOfByte = new byte[]{27, 33, 0};
                //format = new byte[]{27, 33, 0};
                // Bold
                //format[2] = ((byte)(0x8 | arrayOfByte[2]));
                // Height
                //format[2] = ((byte)(0x10 | arrayOfByte[2]));
                // Width
                //format[2] = ((byte) (0x20 | arrayOfByte[2]));
                // Underline
                // format[2] = ((byte)(0x80 | arrayOfByte[2]));
                // Small
                format[2] = ((byte)(0x1 | arrayOfByte[2]));
                Constants.mmOutputStream.write(center);
                Constants.mmOutputStream.write(format);
                Constants.mmOutputStream.write(msg.getBytes(), 0, msg.getBytes().length);

                LineFeed();
                //LineFeed();
                Log.d("myLogs", "End ticket " + sb.toString() + "\n");
            }
            activity_log("PrintBill", "Constants.mmSocket != null && Constants.mmOutputStream != null && Constants.mmInputStream != null");
        } catch (IOException e) {
            Log.e("myLogs", "print ticket error occured: " + e.toString());

            activity_log("PrintBill", "print ticket error occured: " + e.toString());
            e.printStackTrace();
        }
    }

    public void LineFeed() {
        try {
            String msg = "     " + "\n" + "     ";
            msg = msg + "\n";
            Constants.mmOutputStream.write(msg.getBytes(), 0, msg.getBytes().length);
        } catch (IOException e) {
            e.printStackTrace();
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
        fIleOperations.writeToFile(activity_log_file, activity_log.toString(), BookingActivity.this, "1", "activity_log");
        //--------------------------------------------------------------
    }
}
