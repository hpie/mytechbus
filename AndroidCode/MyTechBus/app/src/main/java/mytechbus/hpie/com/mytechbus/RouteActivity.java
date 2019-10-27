package mytechbus.hpie.com.mytechbus;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RouteActivity extends AppCompatActivity {

    private SessionHandler session;
    private ProgressDialog pDialog;
    private FIleOperations fIleOperations;

    TextView etTxtRoute;

    Spinner spinnerRouteList;
    ArrayList<String> routelistKeyArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        session = new SessionHandler(getApplicationContext());
        fIleOperations = new FIleOperations();
        spinnerRouteList = (Spinner)findViewById(R.id.spinnerListRoutes);
        routelistKeyArray = new ArrayList<>();

        // Initialize the dropdown with values
        route_stages();

        etTxtRoute = findViewById(R.id.textRoute);
        Button btnUpdateRoute = findViewById(R.id.btnUpdateRoute);

        etTxtRoute.setText(session.GetRoute());
        btnUpdateRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selected_route = spinnerRouteList.getItemAtPosition(spinnerRouteList.getSelectedItemPosition()).toString();
                if(!selected_route.equals("Select Route")) {
                    update_route();
                } else {
                    Toast.makeText(getApplicationContext(), "Please select route.",Toast.LENGTH_LONG).show();
                }
            }
        });

        Button back = findViewById(R.id.btnBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RouteActivity.this, DashboardActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    // Get route details
    private void route_stages() {

        String route_stages_data = fIleOperations.readFromFile("trip_data.txt", this);
        if(!route_stages_data.equals("")) {
            update_route_spinner(route_stages_data);

            return;
        }
        displayLoader();

    }

    private void displayLoader() {
        pDialog = new ProgressDialog(RouteActivity.this);
        pDialog.setMessage("Logging In.. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }

    public void update_route_spinner(String response) {
        try {
            JSONObject trip_data = new JSONObject(response);

             JSONArray routes_list_array = trip_data.getJSONArray("routes_list_new");

            //------------------------------------------------------------------------------------------

            ArrayList<String> routelistArray = new ArrayList<>();

            routelistArray.add("Select Route");

            for(int i=0;i<routes_list_array.length();i++){
                String key = routes_list_array.getJSONObject(i).getString("key");
                String route = routes_list_array.getJSONObject(i).getString("route");

                Log.d("myLogs", "Key : " + key + " ||||| Route : " + route);

                routelistKeyArray.add(key);
                routelistArray.add(route);
            }

            spinnerRouteList.setAdapter(new ArrayAdapter<String>(RouteActivity.this, android.R.layout.simple_spinner_dropdown_item, routelistArray));

        } catch (JSONException e) {
            Log.d("myLogs", "In update_routes_spinner2 key : " + session.getJourneyType() + " |||| ");
            e.printStackTrace();
        }
    }

        private void update_route() {
            displayLoader();

            activity_log("update_route", "");

            //------------------------------------------------------------------------------------------------------------
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.update_route,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            pDialog.dismiss();

                            try {
                                Log.d("myLogs : Login : ", response);
                                //Check if user got logged in successfully
                                JSONObject update_response = new JSONObject(response);

                                if (update_response.getInt(Constants.KEY_STATUS) == 1) {

                                    fIleOperations.writeToFile("trip_data.txt", response, RouteActivity.this, "0");
                                    session.setRoute(update_response.getString(Constants.KEY_ROUTE_CODE));


                                    session.setRoute(update_response.getString(Constants.KEY_ROUTE_CODE));

                                    session.setRouteAvailibilty(update_response.getString("routes_available"));

                                    etTxtRoute.setText(session.GetRoute());

                                    Toast.makeText(getApplicationContext(), update_response.getString(Constants.KEY_MESSAGE),Toast.LENGTH_LONG).show();

                                }else{
                                    Toast.makeText(getApplicationContext(),update_response.getString(Constants.KEY_MESSAGE),Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            //Toast.makeText(getApplicationContext(), response,Toast.LENGTH_LONG).show();
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
                protected Map<String,String> getParams() {

                    Map<String,String> params = new HashMap<String, String>();

                    Integer selected_route_key = spinnerRouteList.getSelectedItemPosition();

                    String selected_route = routelistKeyArray.get(selected_route_key - 1);

                    params.put("user_id", session.getUserId());
                    params.put("selected_route", selected_route);

                    return params;
                }
            };

            // Access the RequestQueue through your singleton class.
            mytechbus.hpie.com.mytechbus.MySingleton.getInstance(RouteActivity.this).addToRequestQueue(stringRequest);
            //------------------------------------------------------------------------------------------------------------
            //-----------------------------------------------------------------------------------------
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
        fIleOperations.writeToFile(activity_log_file, activity_log.toString(), RouteActivity.this, "1", "activity_log");
        //--------------------------------------------------------------
    }
}
