package mytechbus.hpie.com.mytechbus;

public class Constants {
    //Api server url
    public static final String base_url = "http://mytechbus.hpie.in/";

    public static final int LOCATION_INTERVAL = 120000;
    public static final int FASTEST_LOCATION_INTERVAL = 60000;

    public static final String KEY_STATUS = "status";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_FULL_NAME = "loginid";
    public static final String KEY_ROUTE_CODE = "route_code";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_UUID = "uuid";
    public static final String KEY_IMEI = "imei";
    public static final String KEY_EMPTY = "";

    public static final String KEY_ROUTE = "route_code";

    public static final String PREF_NAME = "UserSession";
    public static final String KEY_EXPIRES = "expires";

    public static final String login_url = base_url+"login";
    public static final String route_stages_url = base_url+"routes_stages";
    public static final String book_ticket_url = base_url+"book_ticket";
    public static final String position_log_url = base_url+"position_log";
    public static final String book_offline_ticket_url = base_url+"book_ticket_call";
}
