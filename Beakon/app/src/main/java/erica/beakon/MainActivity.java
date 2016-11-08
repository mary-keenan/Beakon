package erica.beakon;

import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import erica.beakon.location.LocationHandler;

public class MainActivity extends AppCompatActivity implements  ActivityCompat.OnRequestPermissionsResultCallback {
    static final String TAG = "MainActivity";
    String databaseURL = "https://beakon-5fa96.firebaseio.com/";
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference ref = database.getReferenceFromUrl(databaseURL);

    FirebaseHandler handler = new FirebaseHandler(database, ref);
    FragmentManager fragmentManager;
    LocationHandler locationHandler;
    ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        locationHandler = new LocationHandler(this);

        setupPager();
        setupButtons();

    }

    private void setupPager() {
        PagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(pagerAdapter);
    }

    private void setupButtons() {
        final Button myMovementsButton = (Button) findViewById(R.id.my_movements);
        final Button suggestedMovementsButton = (Button) findViewById(R.id.movements);
        myMovementsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pager.setCurrentItem(0);
                myMovementsButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
                suggestedMovementsButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
            }
        });

        suggestedMovementsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pager.setCurrentItem(ViewPagerAdapter.NUM_PAGES - 1);
                suggestedMovementsButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
                myMovementsButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
            }
        });
    }

    public void onRequestPermissionsResult(int requestCode, String[] args, int[] grantResults) {
        switch (requestCode)  {
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
        }
    }

}
