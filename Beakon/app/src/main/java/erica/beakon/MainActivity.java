package erica.beakon;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import java.util.ArrayList;

import erica.beakon.Adapters.FirebaseHandler;
import erica.beakon.Pages.MyMovementsTab;
import erica.beakon.Objects.User;


public class MainActivity extends AppCompatActivity {
    String databaseURL = "https://beakon-5fa96.firebaseio.com/";
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference ref = database.getReferenceFromUrl(databaseURL);

    public User currentUser;

    public FirebaseHandler handler = new FirebaseHandler(database, ref);

    static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String id = "1";

        handler.getUser(id, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                setCurrentUserFromData(dataSnapshot);
                changeFragment(new MyMovementsTab());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                currentUser = null;
                Log.d(TAG, "there was no USER!!!");
            }
        });
    }

    //switches fragments, new fragment is input
    public void changeFragment(android.support.v4.app.Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction().add(fragment, "tag").addToBackStack("another_tag");
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commitAllowingStateLoss();
    }

    private void setCurrentUserFromData(DataSnapshot snapshot) {
        this.currentUser = new User(snapshot.child("id").getValue().toString(), snapshot.child("name").getValue().toString(),
                snapshot.child("email").getValue().toString(), (ArrayList<String>) snapshot.child("hashtags").getValue());

        if (snapshot.hasChild("movements")) {
            for (String movementId : (ArrayList<String>) snapshot.child("movements").getValue()) {
                this.currentUser.addMovement(movementId);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //No call for super(). Bug on API Level > 11.
    }

    public FirebaseHandler getHandler() {
        return handler;
    }

    public void setHandler(FirebaseHandler handler) {
        this.handler = handler;
    }
}

