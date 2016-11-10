package erica.beakon;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by erica on 11/7/16.
 */
public class CardFragment extends Fragment {




    public CardFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_recommended_movements, container, false);

        final ArrayList<Movement> movements = new ArrayList<>();

        ((MainActivity)getActivity()).handler.getData("1", new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                movements.add(dataSnapshot.child("Movements").child("1").getValue(Movement.class));

                ListView movementsList = (ListView) view.findViewById(R.id.recommended_movements_list);
                RecommendedMovementsAdapter movementsAdapter = new RecommendedMovementsAdapter(getActivity(), movements);
                movementsList.setAdapter(movementsAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("CardFragment", databaseError.getMessage());

            }
        });



        return view;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}



