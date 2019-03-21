package mytechbus.hpie.com.mytechbus;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

public class FetchLocation implements LocationListener {
    LocationManager locationManager;
    Context mcontext;
    Activity activity;
    TextView locationtext;
    Double latitude, longitude;
    Double last_konown_latitude, last_konown_longitude;

    //public FetchLocation(Context mContext, Activity mActivity, TextView locationText) {
    public FetchLocation(Context mContext, Activity mActivity) {
        mcontext = mContext;
        activity = mActivity;

        getDeviceLocation();
    }

    public void getDeviceLocation() {
        if (ContextCompat.checkSelfPermission(mcontext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mcontext, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }

        getLocation();
    }

    public void getLocation() {
        try {
            locationManager = (LocationManager) mcontext.getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            last_konown_latitude  = lastKnownLocation.getLatitude();
            last_konown_longitude = lastKnownLocation.getLongitude();
        } catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    public Double getLat() {
        if(latitude != null) {
            return latitude;
        }
        return last_konown_latitude;
    }

    public Double getLong() {
        if(longitude != null) {
            return longitude;
        }
        return last_konown_longitude;
    }

    @Override
    public void onLocationChanged(Location location) {

        try {
            //Geocoder geocoder = new Geocoder(mcontext, Locale.getDefault());

            String location_data = "Latitude : " + location.getLatitude() + " AND Longitude : " + location.getLongitude();

            //Toast.makeText(mcontext, location_data, Toast.LENGTH_LONG).show();

            latitude = location.getLatitude();
            longitude = location.getLongitude();

            //locationtext.setText(location_data);
        } catch(Exception e) {

        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        //Toast.makeText(mcontext, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }
}
