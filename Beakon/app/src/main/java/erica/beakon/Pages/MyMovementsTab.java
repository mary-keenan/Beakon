package erica.beakon.Pages;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import erica.beakon.Adapters.MyMovementAdapter;
import erica.beakon.MainActivity;
import erica.beakon.Objects.Movement;
import erica.beakon.R;

public class MyMovementsTab extends Fragment {
    public MyMovementsTab() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_movements_tab, container, false);

        ArrayList<Movement> movements = new ArrayList<>();
        ArrayList<String> hashtags = new ArrayList<>();
        hashtags.add("#stillwithher");
        hashtags.add("#feelthebern");
        hashtags.add("#yay");
        hashtags.add("#bob");
        Movement rally = new Movement("2", "Rally", "description", "steps", "resources", hashtags);
        movements.add(rally);
        movements.add(rally);

        ListView movementsList = (ListView) view.findViewById(R.id.my_movements_list);
        MyMovementAdapter movementsAdapter = new MyMovementAdapter(getActivity(), movements);
        movementsList.setAdapter(movementsAdapter);

//        final Button myMovementsButton = (Button) view.findViewById(R.id.my_movements);
        final Button suggestedMovementsButton = (Button) view.findViewById(R.id.movements);
        //set background colors of buttons -- can just hardcode color now
//        myMovementsButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorBackground));
//        suggestedMovementsButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAccentLight));


        suggestedMovementsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((MainActivity) getActivity()).changeFragment(new AddMovementPage()); //should change to SuggestedMovements page
            }
        });
        return view;
    }
}