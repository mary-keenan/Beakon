package erica.beakon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import erica.beakon.Adapters.FirebaseHandler;


public class LoginPage extends Activity {
    private CallbackManager callbackManager;
    String databaseURL = "https://beakon-5fa96.firebaseio.com/";
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference ref = db.getReferenceFromUrl(databaseURL);
    FirebaseHandler firebaseHandler = new FirebaseHandler(db,ref);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());

        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.login_fragment);

        final Intent intent = new Intent(this, MainActivity.class);
        if (Profile.getCurrentProfile() != null) {
            startActivity(intent);
        }


        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            private ProfileTracker profileTracker;

            @Override
            public void onSuccess(LoginResult loginResult) {
                if (Profile.getCurrentProfile() == null) {
                    profileTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                            profileTracker.stopTracking();
                            Profile.setCurrentProfile(currentProfile);

                        }
                    };
                    startActivity(intent);
                } else {
                    firebaseHandler.getUser(Profile.getCurrentProfile().getId(), new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                startActivity(intent);
                            } else {
                                firebaseHandler.addUser(Profile.getCurrentProfile().getId(),Profile.getCurrentProfile().getFirstName());
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

                Log.d("FacebookLogin", "onSuccess" + loginResult);
            }

            @Override
            public void onCancel() {
                Log.d("FacebookLogin", "Login attempt cancelled");
            }

            @Override
            public void onError(FacebookException e) {
                Log.d("FacebookLogin", e.getMessage());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public String getCurrentUserID(){
        return Profile.getCurrentProfile().getId();
    }

}
