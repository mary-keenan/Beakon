package erica.beakon.Pages;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

import erica.beakon.Adapters.FirebaseHandler;
import erica.beakon.MainActivity;
import erica.beakon.Objects.Hashtag;
import erica.beakon.Objects.Movement;
import erica.beakon.R;


public class AddMovementPage extends Fragment {

    public AddMovementPage() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_movement_page, container, false);
        final FirebaseHandler firebaseHandler = ((MainActivity) getActivity()).getHandler();

        //create buttons
        final ImageButton backBtn = (ImageButton) view.findViewById(R.id.back_add_movement_btn);
        ImageButton addMovementBtn = (ImageButton) view.findViewById(R.id.add_movement_button);
        final EditText nameInput = (EditText) view.findViewById(R.id.movement_name);
        final EditText descriptionInput = (EditText) view.findViewById(R.id.movement_description);
        final EditText stepsInput = (EditText) view.findViewById(R.id.movement_steps);
        final EditText resourcesInput = (EditText) view.findViewById(R.id.movement_res);
        final EditText hashtagsInput = (EditText) view.findViewById(R.id.movement_hashtags);

        backBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        addMovementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String movementName = nameInput.getText().toString(); // get movement name
                String movementDescription = descriptionInput.getText().toString(); // get movement description
                String movementSteps = stepsInput.getText().toString(); // get movement steps
                String movementResources = resourcesInput.getText().toString(); // get movement resources
                final ArrayList<String> movementHashtags = new ArrayList(Arrays.asList(hashtagsInput.getText().toString().split(" "))); // get movement hashtags (list)
                final Movement movement = firebaseHandler.addMovement(movementName,movementDescription,movementSteps,movementResources,movementHashtags); // create movement with data
                final ArrayList<String> movementList = new ArrayList<>(); // create empty movement list to put in new hashtag
                final ArrayList<String> userList = new ArrayList(); // create empty user list to put in new hashtag
                movementList.add(movement.getId());
                // Todo: add userId to userList, which is put in hashtag -- get user information via FB login stuff?

                //loop through hashtags in handler, check if they already exist here --> update, or if they don't --> add
                firebaseHandler.getBatchHashtags(movementHashtags, new ValueEventListener() { // called for each hashtag in list; handler loops through them
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null) { // if the hashtag exists in database
                            Hashtag hashtag = dataSnapshot.getValue(Hashtag.class);// get hashtag info from database
                            Log.d("----------", hashtag.getName());
                            firebaseHandler.addMovementtoHashtag(movement, hashtag); // add new movement (and eventually the user) id to hashtag
                        }
                        else { // if the hasthag doesn't exist in the database
                            firebaseHandler.addHashtag(dataSnapshot.getKey(), movementList, userList); // add hashtag to database
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                    //Todo: add movement to user and user to movement
                ((MainActivity) getActivity()).changeFragment(new MyMovementsTab());
            }
        });

        return view;
    }
}
