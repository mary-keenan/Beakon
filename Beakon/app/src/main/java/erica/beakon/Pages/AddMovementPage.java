package erica.beakon.Pages;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

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
        final View view = inflater.inflate(R.layout.fragment_add_movement_page, container, false);
        final FirebaseHandler firebaseHandler = ((MainActivity) getActivity()).getHandler();

        //create buttons
        final ImageButton backBtn = (ImageButton) view.findViewById(R.id.back_add_movement_btn);
        ImageButton addMovementBtn = (ImageButton) view.findViewById(R.id.add_movement_button);
        final EditText nameInput = (EditText) view.findViewById(R.id.movement_name);
        final EditText descriptionInput = (EditText) view.findViewById(R.id.movement_description);
        final EditText stepsInput = (EditText) view.findViewById(R.id.movement_steps);
        final EditText resourcesInput = (EditText) view.findViewById(R.id.movement_res);
        final EditText hashtagsInput = (EditText) view.findViewById(R.id.movement_hashtags);

        setEnterKeySendToNextEditText(nameInput, descriptionInput);
        setEnterKeySendToNextEditText(descriptionInput, stepsInput);
        setEnterKeySendToNextEditText(stepsInput, resourcesInput);

        backBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        addMovementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> movementHashtags = new ArrayList<>(Arrays.asList(hashtagsInput.getText().toString().replace("#","").split(" "))); // get movement hashtags (list)
                if (!hashtagsInput.getText().toString().equals("") && movementHashtags.size() <= 3 && Hashtag.areHashtagsShortEnough(movementHashtags)) {
                    String movementName = nameInput.getText().toString(); // get movement ID
                    String movementDescription = descriptionInput.getText().toString(); // get movement description
                    String movementSteps = stepsInput.getText().toString(); // get movement steps
                    String movementResources = resourcesInput.getText().toString(); // get movement resources
                    final ArrayList<String> userList = new ArrayList(); // create empty user list to put in new hashtag
                    final Movement movement = firebaseHandler.addMovement(movementName,movementDescription,movementSteps,movementResources,movementHashtags, userList); // create movement with data
                    final ArrayList<String> movementList = new ArrayList<>(); // create empty movement list to put in new hashtag
                    movementList.add(movement.getId());
                    firebaseHandler.addUsertoMovement(((MainActivity) getActivity()).currentUser, movement);
                    //loop through hashtags in handler, check if they already exist here --> update, or if they don't --> add
                    firebaseHandler.getBatchHashtags(movementHashtags, new ValueEventListener() { // called for each hashtag in list; handler loops through them
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null) { // if the hashtag exists in database
                                final Hashtag hashtag = dataSnapshot.getValue(Hashtag.class);// get hashtag info from database
                                firebaseHandler.addMovementtoHashtag(movement, hashtag); // add new movement id to hashtag
                            } else { // if the hashtag doesn't exist in the database
                                firebaseHandler.addHashtag(dataSnapshot.getKey(), movementList, userList); // add hashtag to database
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

                    ((MainActivity) getActivity()).changeFragment(new MyMovementsTab(), "MyMovementsTab");
                } else if (movementHashtags.size() > 3){
                    // Use strings.xml for all these
                    sendToastWarning("Please enter no more than 3 hashtags.");
                } else if (!Hashtag.areHashtagsShortEnough(movementHashtags)) {
                    sendToastWarning("Your hashtags cannot be more than 13 characters each.");
                } else {
                    sendToastWarning("Please enter up to 3 hashtags.");
                }
            }
        });

        return view;
    }

    private void setEnterKeySendToNextEditText(final EditText currentET, final EditText nextET) {
        currentET.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_ENTER:
                            nextET.requestFocus();
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
    }

    private void sendToastWarning(String text) {
        int duration = Toast.LENGTH_SHORT;
        Toast.makeText(getContext(), text, duration).show();
    }
}
