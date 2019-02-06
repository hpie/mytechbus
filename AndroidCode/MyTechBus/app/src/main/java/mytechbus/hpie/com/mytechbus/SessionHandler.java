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

    public String GetRoute() {
        return mPreferences.getString(Constants.KEY_ROUTE_CODE, Constants.KEY_EMPTY);
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

    public void setTicketNumber( Integer ticket) {
        mEditor.putInt(Constants.KEY_TICKET_NUMBER, ticket);
        mEditor.commit();
        //Toast.makeText(mContext, "In Set session : " + imei,Toast.LENGTH_LONG).show();
    }

    public int getTicketNumber() {
        //Toast.makeText(mContext, "In get session : " + mPreferences.getString(KEY_IMEI, KEY_EMPTY),Toast.LENGTH_LONG).show();
        return mPreferences.getInt(Constants.KEY_TICKET_NUMBER, 2);
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
    public void loginUser(String username, String fullName, String routecode) {
        mEditor.putString(Constants.KEY_USERNAME, username);
        mEditor.putString(Constants.KEY_FULL_NAME, fullName);
        mEditor.putString(Constants.KEY_ROUTE_CODE, routecode);
        Date date = new Date();

        //Set user session for next 7 days
        long millis = date.getTime() + (7 * 24 * 60 * 60 * 1000);
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
