package erica.beakon.Pages;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import erica.beakon.MainActivity;
import erica.beakon.Objects.Movement;


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
                ((MainActivity) getActivity()).changeFragment(fragment, "TabSwitch"); //should change to SuggestedMovements page
            }
        });
    }

    protected void removeMovement(int index) {
        this.movements.remove(index);
    }

    protected void removeMovement(Movement movement) {
        this.movements.remove(movement);
    }

    protected void getMovement(String id) {
        if (isAdded()) {
            getMainActivity().firebaseHandler.getMovement(id, getMovementAddedValueEventListener());
        }
    }

    abstract ValueEventListener getMovementAddedValueEventListener();

    abstract ChildEventListener populateMovementsEventListener();

    protected MainActivity getMainActivity() {
        return ((MainActivity)getActivity());
    }

    protected Movement getMovementById(String id) {
        for (Movement m: movements) {
            if (m != null){
                if (m.getId().equals(id)) {
                    return m;
                }
            }
        }
        return null;
//        throw new NullPointerException("No movement exists with that id in nearby movements");
    }

}
