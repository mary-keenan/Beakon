package erica.beakon.location;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;

import erica.beakon.MainActivity;
import erica.beakon.Objects.User;


public class LocationHandler {

    static final private String TAG = "LOCATION_HANDLER";
    public static final int PERMISSIONS_REQUEST_GPS = 1;

    public LocationManager locationManager;
    Context context;
    Location location;
    LocationListener listener;
    ArrayList<String> nearbyUsers;

    public LocationHandler(Context context) {
        this.context = context;
        this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        this.location = null;
        this.listener = null;
        this.nearbyUsers = new ArrayList<>();
    }

    public void getCurrentLocation() {
        if (this.listener != null) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);
            } else {
                verifyLocationPermissionStatus();
            }
        } else {
            throw new NullPointerException("The location listener has not been set.");
        }

    }

    public Location getLocation() {
        return this.location;
    }

    public void updateLocation(Location location) {
       this.location = location;
    }

    public void requestLocationPermission() {
        ActivityCompat.requestPermissions(((Activity)this.context),
                new String[]{Manifest.permission.READ_CONTACTS},
                PERMISSIONS_REQUEST_GPS);
    }

    private void verifyLocationPermissionStatus() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(((Activity)context),
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            //we need to explain why we want the permission and then ask again
            executeNotifyPermissionsReasonTask();

        } else {
            // No explanation needed, we can request the permission.
            requestLocationPermission();
        }
    }

    public void executeNotifyPermissionsReasonTask() {
        new NotifyPermissionsReasonTask(context, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d("LOCATION_HANDLER", "LOCATION PERMISSION DOUBLE DENIED");
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                requestLocationPermission();
            }
        }).execute();
    }

    public void setLocationListener(LocationListener locationListener) {
        listener = locationListener;
    }

    public GeoLocation geoLocationFromLocation(Location location) {
        return new GeoLocation(location.getLatitude(), location.getLongitude());
    }

    public GeoQueryEventListener getNearbyLocationsListener() {
        return new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                nearbyUsers.add(key);
            }

            @Override
            public void onKeyExited(String key) {
                nearbyUsers.remove(key);
            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {
                Log.d(TAG, error.getMessage());
            }
        };
    }

    public ArrayList<String> getNearbyUsers() {
        return nearbyUsers;
    }
}




