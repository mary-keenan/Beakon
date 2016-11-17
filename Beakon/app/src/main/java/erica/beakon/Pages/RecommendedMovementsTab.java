package erica.beakon.Pages;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import erica.beakon.Adapters.MyMovementAdapter;
import erica.beakon.Adapters.RecommendedMovementsAdapter;
import erica.beakon.MainActivity;
import erica.beakon.Objects.Movement;
import erica.beakon.R;

/**
 * Created by erica on 11/7/16.
 */
public class RecommendedMovementsTab extends MovementsTab {

    public static final String TAG = "REC_MOVEMENTS_TAB";
    View view;
    RecommendedMovementsAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_recommended_movements, container, false);
//        movements = new ArrayList<>();
        setUpChangeFragmentsButton(view, new MyMovementsTab(), R.id.my_movements);
        setMovementsListener();
        setUsersMovementsListener();
        adapter = new RecommendedMovementsAdapter(getContext(), movements);
        if (!movements.isEmpty()) {
            setUpListView(view);
        }
        return view;
    }

    private void setUpListView(View view) {
        ListView movementsList = (ListView) view.findViewById(R.id.recommended_movements_list);
        movementsList.setAdapter(adapter);
    }

    private void setMovementsListener() {
        getMainActivity().handler.getMovements(populateMovementsValueEventListener());
    }

    private void setUsersMovementsListener() {
        getMainActivity().handler.getUserChild(getMainActivity().currentUser.getId(), "movements", userMovementsChangeListener());
    }

    private ChildEventListener userMovementsChangeListener() {
        return new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                removeMovement(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                addMovement(dataSnapshot.getValue(String.class));
                if (movements.size() == 1) {
                    setUpListView(view);
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }

    public ChildEventListener populateMovementsValueEventListener() {
       return new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Movement movement = dataSnapshot.getValue(Movement.class);
                if (!getMainActivity().currentUser.getMovements().contains(movement.getId())) {
                    addMovement(movement.getId());
                    if (movements.size() == 1) {
                        setUpListView(view);
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                // do something with the changed data
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                removeMovement(Integer.valueOf(dataSnapshot.getValue(String.class)));
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
                movements.add(dataSnapshot.getValue(Movement.class));
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

}



