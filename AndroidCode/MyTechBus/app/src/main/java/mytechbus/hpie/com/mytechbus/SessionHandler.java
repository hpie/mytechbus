package mytechbus.hpie.com.mytechbus;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SessionHandler {

    private Context mContext;
    private SharedPreferences.Editor mEditor;
    private SharedPreferences mPreferences;

    public SessionHandler(Context mContext) {
        this.mContext = mContext;
        mPreferences = mContext.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
        this.mEditor = mPreferences.edit();
    }

    public void setRoute( String route_code) {
        mEditor.putString(Constants.KEY_ROUTE_CODE, route_code);
        mEditor.commit();
    }

    public String GetRoute() {
        return mPreferences.getString(Constants.KEY_ROUTE_CODE, Constants.KEY_EMPTY);
    }

    public String getIsMultiRoute() {
        return mPreferences.getString(Constants.KEY_IS_MULTIROUTE, Constants.KEY_EMPTY);
    }

    public String getOperator() {
        return mPreferences.getString(Constants.KEY_OPERATOR_NAME, Constants.KEY_EMPTY);
    }

    public Float getMinTicket() {
        return mPreferences.getFloat(Constants.KEY_MIN_TICKET, (float) 0);
    }

    public String getTicketMessage() {
        return mPreferences.getString(Constants.KEY_TICKET_MESSAGE, Constants.KEY_EMPTY);
    }

    public String getVehicleCode() {
       // Log.d("myLogs", "Return vehicle code : " + mPreferences.getString(Constants.KEY_VEHICLE_CODE, Constants.KEY_EMPTY));
        return mPreferences.getString(Constants.KEY_VEHICLE_CODE, Constants.KEY_EMPTY);
    }

    public String getVehicleNumber() {
        return mPreferences.getString(Constants.KEY_VEHICLE_NUMBER, Constants.KEY_EMPTY);
    }

    public String getVehiclType() {
        return mPreferences.getString(Constants.KEY_VEHICLE_TYPE, Constants.KEY_EMPTY);
    }

    public String getUserId() {
        return mPreferences.getString(Constants.KEY_USER_ID, Constants.KEY_EMPTY);
    }

    public String getUserName() {
        return mPreferences.getString(Constants.KEY_USERNAME, Constants.KEY_EMPTY);
    }

    public String getOperatorAddress1() {
        return mPreferences.getString(Constants.KEY_OPERATOR_ADDRESS1, Constants.KEY_EMPTY);
    }

    public String getOperatorAddress2() {
        return mPreferences.getString(Constants.KEY_OPERATOR_ADDRESS2, Constants.KEY_EMPTY);
    }

    public String getOperatorCity() {
        return mPreferences.getString(Constants.KEY_OPERATOR_CITY, Constants.KEY_EMPTY);
    }

    public String getOperatorHelpline() {
        return mPreferences.getString(Constants.KEY_OPERATOR_HELPLINE, Constants.KEY_EMPTY);
    }

    public String getOperatorId() {
        return mPreferences.getString(Constants.KEY_OPERATOR_ID, Constants.KEY_EMPTY);
    }

    public void setRouteAvailibilty(String is_available) {
        mEditor.putString(Constants.KEY_ROUTE_AVAILIBILITY, is_available);
        mEditor.commit();
    }

    public String getRouteAvailibilty() {
        return mPreferences.getString(Constants.KEY_ROUTE_AVAILIBILITY, Constants.KEY_EMPTY);
    }

    public void setIMEI( String imei) {
        mEditor.putString(Constants.KEY_IMEI, imei);
        mEditor.commit();
        //Toast.makeText(mContext, "In Set session : " + imei,Toast.LENGTH_LONG).show();
    }

    public String getIMEI() {
        //Toast.makeText(mContext, "In get session : " + mPreferences.getString(KEY_IMEI, KEY_EMPTY),Toast.LENGTH_LONG).show();
        return mPreferences.getString(Constants.KEY_IMEI, Constants.KEY_EMPTY);
    }

    public void setJourneyType( String journeytype) {
        mEditor.putString(Constants.KEY_JOURNEY_TYPE, journeytype);
        mEditor.commit();
        //Toast.makeText(mContext, "In Set session : " + imei,Toast.LENGTH_LONG).show();
    }

    public String getJourneyType() {
        //Toast.makeText(mContext, "In get session : " + mPreferences.getString(KEY_IMEI, KEY_EMPTY),Toast.LENGTH_LONG).show();
        return mPreferences.getString(Constants.KEY_JOURNEY_TYPE, Constants.KEY_EMPTY);
    }

    /**
     * Logs in the user by saving user details and setting session
     *
     * @param username
     * @param fullName
     * @param routecode
     */
    /*
    public void loginUser(String username, String fullName, String routecode) {
        mEditor.putString(Constants.KEY_USERNAME, username);
        mEditor.putString(Constants.KEY_FULL_NAME, fullName);
        mEditor.putString(Constants.KEY_ROUTE_CODE, routecode);
        Date date = new Date();

        //Set user session for next 7 days
        long millis = date.getTime() + (7 * 24 * 60 * 60 * 1000);
        mEditor.putLong(Constants.KEY_EXPIRES, millis);
        mEditor.commit();
    }
    */
    public void loginUser(String user_id, String operator_id,
                          String username, String fullName, String routecode,
                          String route_availibility,
                          String is_multiroute, String operator_name,
                          String operator_address1, String operator_address2,
                          String operator_city, String operator_helpline,
                          String vehicle_code, String vehicle_number,
                          String vehicle_type, String ticket_message, String min_ticket ) {


        //Log.d("myLogs", "Set vehicle code : " + vehicle_code);

        mEditor.putString(Constants.KEY_USER_ID, user_id);
        mEditor.putString(Constants.KEY_OPERATOR_ID, operator_id);
        mEditor.putString(Constants.KEY_USERNAME, username);
        mEditor.putString(Constants.KEY_FULL_NAME, fullName);
        mEditor.putString(Constants.KEY_ROUTE_CODE, routecode);
        mEditor.putString(Constants.KEY_ROUTE_AVAILIBILITY, route_availibility);
        mEditor.putString(Constants.KEY_IS_MULTIROUTE, is_multiroute);

        mEditor.putString(Constants.KEY_TICKET_MESSAGE, ticket_message);
        mEditor.putFloat(Constants.KEY_MIN_TICKET, Float.valueOf(min_ticket));

        mEditor.putString(Constants.KEY_OPERATOR_NAME, operator_name);

        mEditor.putString(Constants.KEY_OPERATOR_ADDRESS1, operator_address1);
        mEditor.putString(Constants.KEY_OPERATOR_ADDRESS2, operator_address2);
        mEditor.putString(Constants.KEY_OPERATOR_CITY, operator_city);
        mEditor.putString(Constants.KEY_OPERATOR_HELPLINE, operator_helpline);

        mEditor.putString(Constants.KEY_VEHICLE_CODE, vehicle_code);
        mEditor.putString(Constants.KEY_VEHICLE_NUMBER, vehicle_number);
        mEditor.putString(Constants.KEY_VEHICLE_TYPE, vehicle_type);

        Date date = new Date();

        //Set user session for next 7 days
        //long millis = date.getTime() + (7 * 24 * 60 * 60 * 1000);

        long millis = date.getTime() + (1 * 24 * 60 * 60 * 1000);
        mEditor.putLong(Constants.KEY_EXPIRES, millis);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String expire_date = df.format(Calendar.getInstance().getTime());

        String last_date = mPreferences.getString("expire_date", "");

        /* If shared preferences does not have a value
         then user is not logged in
         */
        if (last_date.equals("") || !last_date.equals(expire_date)) {
            mEditor.putInt(Constants.KEY_TICKET_NUMBER, 1);
        }

        mEditor.putString("expire_date", expire_date);

        mEditor.commit();
    }

    /**
     * Checks whether user is logged in
     *
     * @return
     */
    public boolean isLoggedIn() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String expire_date = df.format(Calendar.getInstance().getTime());

        String initial_date = mPreferences.getString("expire_date", "");

        /* If shared preferences does not have a value
         then user is not logged in
         */
        if (initial_date.equals("")) {
            return false;
        }

        //Log.d("IsLoggedIn", "initial_date : " + initial_date + " ||||| expire_date : " + expire_date);

        /* Check if session is expired by comparing
        current date and Session expiry date
        */
        return initial_date.equals(expire_date);
    }
    /**
     * Checks whether user is logged in
     *
     * @return
     */
    /*
    public boolean isLoggedIn() {
        Date currentDate = new Date();

        long millis = mPreferences.getLong(Constants.KEY_EXPIRES, 0);

        /* If shared preferences does not have a value
         then user is not logged in
         */
    /*
        if (millis == 0) {
            return false;
        }
        Date expiryDate = new Date(millis);

        /* Check if session is expired by comparing
        current date and Session expiry date
        */
    /*
        return currentDate.before(expiryDate);
    }
    */

    /**
     * Fetches and returns user details
     *
     * @return user details
     */
    public User getUserDetails() {
        //Check if user is logged in first
        if (!isLoggedIn()) {
            return null;
        }
        User user = new User();
        user.setUsername(mPreferences.getString(Constants.KEY_USERNAME, Constants.KEY_EMPTY));
        user.setFullName(mPreferences.getString(Constants.KEY_FULL_NAME, Constants.KEY_EMPTY));
        user.setSessionExpiryDate(new Date(mPreferences.getLong(Constants.KEY_EXPIRES, 0)));

        return user;
    }

    /**
     * Logs out user by clearing the session
     */
    public void logoutUser(){
        mEditor.clear();
        mEditor.commit();
    }
}
