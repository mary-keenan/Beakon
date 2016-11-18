package erica.beakon.Pages;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import erica.beakon.Adapters.RecommendedMovementsAdapter;
import erica.beakon.Objects.Movement;
import erica.beakon.R;

/**
 * Created by erica on 11/7/16.
 */
public class RecommendedMovementsTab extends MovementsTab {

    public static final String TAG = "REC_MOVEMENTS_TAB";
    View view;
    RecommendedMovementsAdapter adapter;
    HashMap<String, Integer> movementRanks;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_recommended_movements, container, false);
        this.movementRanks = new HashMap<>();
        setUpChangeFragmentsButton(view, new MyMovementsTab(), R.id.my_movements);
        setMovementsListener();
        initializeListView();

        return view;
    }

    private void initializeListView() {
        adapter = new RecommendedMovementsAdapter(getContext(), movements);

        if (!movements.isEmpty()) {
            setUpListView(view);
        }
    }
    private void setUpListView(View view) {
        ListView movementsList = (ListView) view.findViewById(R.id.recommended_movements_list);
        movementsList.setAdapter(adapter);
    }

    private void setMovementsListener() {
        if (getMainActivity().locationHandler.getNearbyUsers().isEmpty()) {
            // try to get location and nearby users
            // does it make sense to try again in the flow or will it have been tried recently enough that it doesnt make sense?
        } else {
            for (String userId: getMainActivity().locationHandler.getNearbyUsers()) {
                getMainActivity().firebaseHandler.getUserChild(userId, "movements", populateMovementsEventListener());
            }
        }
    }

    public ChildEventListener populateMovementsEventListener() {
       return new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                updateMovementRanks(dataSnapshot.getValue(String.class));
                addMovement(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                // do something with the changed data
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                // i dont think we need to do anything here
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, databaseError.getMessage());
            }
        };
    }

    protected ValueEventListener getMovementAddedValueEventListener() {
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Movement movement = dataSnapshot.getValue(Movement.class);
                if (!movementsAlreadyHas(movement)) {
                    movements.add(dataSnapshot.getValue(Movement.class));
                }
                if (movements.size() == 1) {
                    setUpListView(view);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("MyMovementsTab", "there is a problem on the listener for the movement added to my movements");
            }
        };
    }

    private void updateMovementRanks(String movementId) {
        if (movementRanks.containsKey(movementId)) {
            movementRanks.put(movementId, movementRanks.get(movementId) + 1);
        } else {
            movementRanks.put(movementId, 1);
        }
    }

    private boolean movementsAlreadyHas(Movement movement) {
        for (Movement m: movements) {
            if (m.getId().equals(movement.getId())) {
                return true;
            }
        }
        return false;
    }

}



