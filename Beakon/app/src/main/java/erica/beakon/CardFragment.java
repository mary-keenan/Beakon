package erica.beakon;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by erica on 11/7/16.
 */
public class CardFragment extends Fragment {



    public CardFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recommended_movements, container, false);

        ArrayList<String> movements = new ArrayList<>();
        movements.add("Rally");
        movements.add("Protest");
        movements.add("Dinner");
        movements.add("Check-in");

        ListView movementsList = (ListView) view.findViewById(R.id.recommended_movements_list);
        RecommendedMovementsAdapter movementsAdapter = new RecommendedMovementsAdapter(getActivity(), movements);
        movementsList.setAdapter(movementsAdapter);

        return view;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}



