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

import erica.beakon.MainActivity;


public class LocationHandler {

    public static final int PERMISSIONS_REQUEST_GPS = 1;

    LocationManager locationManager;
    Context context;
    Location location;

    public LocationHandler(Context context) {
        this.context = context;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        this.location = null;
        getCurrentLocation();
    }

    public void getCurrentLocation() {
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                updateLocation(location);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        } else {
            verifyLocationPermissionStatus();
        }
    }

    private void updateLocation(Location location) {
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
}




