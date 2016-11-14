package erica.beakon;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity {
    String databaseURL = "https://beakon-5fa96.firebaseio.com/";
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference ref = database.getReferenceFromUrl(databaseURL);

    FirebaseHandler handler = new FirebaseHandler(database, ref);

    static final String TAG = "MainActivity";
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler.addUser("David1", "david1@email.com");
        handler.addMovement("hillary1", "election", "vote", "yesterday");
//        handler.getData(1,);

        fragmentManager = getSupportFragmentManager();

        final ViewPagerAdapter pagerAdapter;
        final ViewPager pager;

        pagerAdapter = new

                ViewPagerAdapter(getSupportFragmentManager());

        pager = (ViewPager) findViewById(R.id.pager);

        pager.setAdapter(pagerAdapter);

        final Button myMovementsButton = (Button) findViewById(R.id.my_movements);
        final Button suggestedMovementsButton = (Button) findViewById(R.id.movements);
        myMovementsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pager.setCurrentItem(0);
                myMovementsButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBackground));
                suggestedMovementsButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccentLight));
            }
        });

        suggestedMovementsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pager.setCurrentItem(ViewPagerAdapter.NUM_PAGES-1);
                suggestedMovementsButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBackground));
                myMovementsButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccentLight));
            }
        });
    }

    //switches fragments, new fragment is input
    public void changeFragment(android.support.v4.app.Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();//.addToBackStack("tag"); //might make back button work?
        transaction.replace(R.id.abs_outer_frame, fragment);
        transaction.commit();
    }
}

