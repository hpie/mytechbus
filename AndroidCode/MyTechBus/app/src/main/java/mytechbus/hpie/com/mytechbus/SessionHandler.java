package mytechbus.hpie.com.mytechbus;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import java.util.Date;

public class SessionHandler {

    private Context mContext;
    private SharedPreferences.Editor mEditor;
    private SharedPreferences mPreferences;

    public SessionHandler(Context mContext) {
        this.mContext = mContext;
        mPreferences = mContext.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
        this.mEditor = mPreferences.edit();

        // set to 1 if ticket data uploading to server
        mEditor.putString("book_ticket_flag", "0");
    }

    public String GetRoute() {
        return mPreferences.getString(Constants.KEY_ROUTE_CODE, Constants.KEY_EMPTY);
    }

    public void setIMEI( String imei) {
        mEditor.putString(Constants.KEY_IMEI, imei);

        //Toast.makeText(mContext, "In Set session : " + imei,Toast.LENGTH_LONG).show();
    }

    public String getIMEI() {
        //Toast.makeText(mContext, "In get session : " + mPreferences.getString(KEY_IMEI, KEY_EMPTY),Toast.LENGTH_LONG).show();
        return mPreferences.getString(Constants.KEY_IMEI, Constants.KEY_EMPTY);
    }

    public void setTicketBookWaitQueue( String ticket_data) {

        String queue_data = mPreferences.getString("ticket_wait_queue", Constants.KEY_EMPTY);

        if(queue_data.equals(Constants.KEY_EMPTY)) {
            queue_data = ticket_data;
        } else {
            queue_data = queue_data + "," + ticket_data;
        }

        Log.d("Ticket_Wait Queue : ", queue_data);

        mEditor.putString("ticket_wait_queue", queue_data);

        //Toast.makeText(mContext, "In Set session : " + imei,Toast.LENGTH_LONG).show();
    }

    public String getTicketBookWaitQueue( String ticket_data) {
        //Toast.makeText(mContext, "In get session : " + mPreferences.getString(KEY_IMEI, KEY_EMPTY),Toast.LENGTH_LONG).show();
        return mPreferences.getString("ticket_wait_queue", Constants.KEY_EMPTY);
    }


    public void setTicketBookUploadQueue(String ticket_data) {
        String queue_data = mPreferences.getString("ticket_upload_queue", Constants.KEY_EMPTY);

        if(queue_data.equals(Constants.KEY_EMPTY)) {
            queue_data = ticket_data;
        } else {
            queue_data = queue_data + "," + ticket_data;
        }

        Log.d("Ticket upload Queue : ", queue_data);

        mEditor.putString("ticket_upload_queue", queue_data);
    }

    public String getTicketBookUploadQueue() {
        //Toast.makeText(mContext, "In get session : " + mPreferences.getString(KEY_IMEI, KEY_EMPTY),Toast.LENGTH_LONG).show();
        return mPreferences.getString("ticket_upload_queue", Constants.KEY_EMPTY);
    }

    /**
     * Logs in the user by saving user details and setting session
     *
     * @param username
     * @param fullName
     * @param routecode
     */
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

    /**
     * Checks whether user is logged in
     *
     * @return
     */
    public boolean isLoggedIn() {
        Date currentDate = new Date();

        long millis = mPreferences.getLong(Constants.KEY_EXPIRES, 0);

        /* If shared preferences does not have a value
         then user is not logged in
         */
        if (millis == 0) {
            return false;
        }
        Date expiryDate = new Date(millis);

        /* Check if session is expired by comparing
        current date and Session expiry date
        */
        return currentDate.before(expiryDate);
    }

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
