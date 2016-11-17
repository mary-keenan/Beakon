package erica.beakon.Pages;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

import erica.beakon.MainActivity;
import erica.beakon.Objects.Movement;
import erica.beakon.R;


abstract public class MovementsTab extends Fragment {

    ArrayList<Movement> movements;

    public MovementsTab() {
        movements = new ArrayList<>();
        // Required empty public constructor
    }

    protected void setUpChangeFragmentsButton(View view, final Fragment fragment, int nextFragmentButtonId) {
        //create buttons
//        final Button myMovementsButton = (Button) view.findViewById(R.id.my_movements);
        final Button tabChangeButton = (Button) view.findViewById(nextFragmentButtonId);

        //set background colors of buttons -- can just hardcode color now
//        myMovementsButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorBackground));
//        suggestedMovementsButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAccentLight));

        tabChangeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((MainActivity) getActivity()).changeFragment(fragment); //should change to SuggestedMovements page
            }
        });
    }

    protected void removeMovement(int index) {
        this.movements.remove(index);
    }

    protected void removeMovement(String id) {
        movements.remove(id);
    }

    protected void addMovement(String id) {
        getMainActivity().handler.getMovement(id, getMovementAddedValueEventListener());
    }

    abstract ValueEventListener getMovementAddedValueEventListener();

    abstract ChildEventListener populateMovementsValueEventListener();

    protected MainActivity getMainActivity() {
        return ((MainActivity)getActivity());
    }

}
