package erica.beakon.Pages;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import erica.beakon.MainActivity;
import erica.beakon.R;


public class AddMovementPage extends Fragment {


    public AddMovementPage() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_movement_page, container, false);

        //create buttons
        final Button myMovementsButton = (Button) view.findViewById(R.id.my_movements);
//        final Button suggestedMovementsButton = (Button) view.findViewById(R.id.movements);

        myMovementsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((MainActivity) getActivity()).changeFragment(new MyMovementsTab()); //should change to SuggestedMovements page
            }
        });

        return view;
    }
}
