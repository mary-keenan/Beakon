package erica.beakon;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.DragAndDropPermissions;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MyMovementsTab extends Fragment {

    public static final String TAG = "MY MOVEMENTS TAB";
    final ArrayList<Movement> movements = new ArrayList<>();

    public MyMovementsTab() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_my_movements_tab, container, false);

        setUpChangeFragmentsButton(view);
        setUsersMovementsListener();
        setUpListView(view);
        return view;
    }

    private void setUpChangeFragmentsButton(View view) {
        //create buttons
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
    }

    private void setUsersMovementsListener() {
        getMainActivity().handler.getUserChild(getMainActivity().currentUser.getId(), "movements", getUsersMovementsValueEventListener());
    }

    private void setUpListView(View view) {
        MyMovementAdapter adapter = new MyMovementAdapter(getContext(), movements);
        ListView listView = (ListView) view.findViewById(R.id.my_movements_list);
        listView.setAdapter(adapter);
    }

    private ValueEventListener getMovementAddedValueEventListener() {
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               movements.add(dataSnapshot.getValue(Movement.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("MyMovementsTab", "there is a problem on the listener for the movement added to my movements");
            }
        };
    }

    private ChildEventListener getUsersMovementsValueEventListener() {
        return new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                addMovement(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                removeMovement(Integer.valueOf(dataSnapshot.getValue(String.class)));
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("MyMovementsTab", databaseError.getMessage());
            }
        };
    }

    private void removeMovement(int index) {
        movements.remove(index);
    }

    private void addMovement(String id) {
        getMainActivity().handler.getMovement(id, getMovementAddedValueEventListener());
    }

    private MainActivity getMainActivity() {
        return ((MainActivity)getActivity());
    }
}
