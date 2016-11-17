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

import java.util.ArrayList;
import java.util.Arrays;

import erica.beakon.Adapters.FirebaseHandler;
import erica.beakon.MainActivity;
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
                String movementName = nameInput.getText().toString();
                String movementDescription = descriptionInput.getText().toString();
                String movementSteps = stepsInput.getText().toString();
                String movementResources = resourcesInput.getText().toString();
                ArrayList<String> movementHashtags = new ArrayList(Arrays.asList(hashtagsInput.getText().toString().split(" ")));
                firebaseHandler.addMovement(movementName,movementDescription,movementSteps,movementResources,movementHashtags);
//                firebaseHandler.addHashtag();
                //add movement to user
                ((MainActivity) getActivity()).changeFragment(new MyMovementsTab());
            }
        });

        return view;
    }
}
