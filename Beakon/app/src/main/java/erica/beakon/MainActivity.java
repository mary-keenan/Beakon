package erica.beakon;

import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import erica.beakon.location.LocationHandler;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import erica.beakon.Adapters.FirebaseHandler;
import erica.beakon.Pages.MyMovementsTab;
import erica.beakon.Objects.User;


public class MainActivity extends AppCompatActivity implements  ActivityCompat.OnRequestPermissionsResultCallback {
    static final String TAG = "MainActivity";
    String databaseURL = "https://beakon-5fa96.firebaseio.com/";
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference ref = database.getReferenceFromUrl(databaseURL);
    public LocationHandler locationHandler;
    public User currentUser;
    public FirebaseHandler firebaseHandler = new FirebaseHandler(database, ref);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationHandler = new LocationHandler(this);

        String id = "1";

        firebaseHandler.getUser(id, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                setCurrentUserFromData(dataSnapshot);
                locationHandler.setLocationListener(getLocationListener());
                locationHandler.getCurrentLocation();
                changeFragment(new MyMovementsTab());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                currentUser = null;
                Log.d(TAG, "there was no USER!!!");
            }
        });
    }

    public void onRequestPermissionsResult(int requestCode, String[] args, int[] grantResults) {
        switch (requestCode) {
            case LocationHandler.PERMISSIONS_REQUEST_GPS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "PERMISSION GRANTED");
                    this.locationHandler.getCurrentLocation();
                } else {
                    // do something about not being able to get the permission
                    Log.d(TAG, "NO PERMISSIONS");
                }
                return;
            }
        };
    }

    //switches fragments, new fragment is input
    public void changeFragment(android.support.v4.app.Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction().add(fragment, "tag").addToBackStack("another_tag");
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commitAllowingStateLoss();
    }

    private void  setCurrentUserFromData(DataSnapshot snapshot) {
        this.currentUser = new User(snapshot.child("id").getValue().toString(), snapshot.child("name").getValue().toString(), snapshot.child("email").getValue().toString());

        if (snapshot.hasChild("movements")) {
            for (String movementId : (ArrayList<String>) snapshot.child("movements").getValue()) {
                this.currentUser.addMovement(movementId);
            }
        }
    }

    private LocationListener getLocationListener() {
        return new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                locationHandler.updateLocation(location);
                firebaseHandler.setUserGeoLocation(currentUser, location);
                firebaseHandler.getNearbyUsers(locationHandler.geoLocationFromLocation(location), locationHandler.getNearbyLocationsListener());
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {
                Log.d("LocHand", provider);
            }

            public void onProviderDisabled(String provider) {
                Log.d("LocHand", provider);
            }

        };
    };

}
