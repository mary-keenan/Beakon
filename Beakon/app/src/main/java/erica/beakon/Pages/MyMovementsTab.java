package erica.beakon.Pages;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import erica.beakon.Adapters.MyMovementAdapter;
import erica.beakon.LoginPage;
import erica.beakon.MainActivity;
import erica.beakon.Objects.Movement;
import erica.beakon.R;

public class MyMovementsTab extends MovementsTab {

    public static final String TAG = "MY MOVEMENTS TAB";
    MyMovementAdapter adapter;
    ListView listView;
    TextView message;
    Button logoutButton;
    Button userPrefButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_my_movements_tab, container, false);

        listView = (ListView) view.findViewById(R.id.my_movements_list);
        message = (TextView) view.findViewById(R.id.no_movments_message);
        setUpChangeFragmentsButton(view, new RecommendedMovementsTab(), R.id.movements);
//        setUpChangeFragmentsButton(view, new AddMovementPage(), R.id.goto_add_movement_button);
        ImageButton addMovementBtn = (ImageButton) view.findViewById(R.id.goto_add_movement_btn);
        addMovementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).changeFragment(new AddMovementPage(), "AddMovementPage");
            }
        });

        setMenuButtonOnClickListener(R.id.my_movement_tab);
        setUpAddButton();

        setUsersMovementsListener();
        if (!movements.isEmpty() && movements != null) {
            setUpListView();
        }

        return view;
    }


    private void setUsersMovementsListener() {
        getMainActivity().firebaseHandler.getUserChild(getMainActivity().currentUser.getId(), "movements", populateMovementsEventListener());
    }

    private void setUpListView() {
        adapter = new MyMovementAdapter(getContext(), movements);
        message.setVisibility(View.INVISIBLE);
        listView.setVisibility(View.VISIBLE);

        listView.setAdapter(adapter);
    }

    protected ValueEventListener getMovementAddedValueEventListener() {
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                movements.add(dataSnapshot.getValue(Movement.class));
                if (movements.size() == 1) {
                    setUpListView();
                }
                if (adapter!=null) {
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("MyMovementsTab", "there is a problem on the listener for the movement added to my movements");
            }
        };
    }

    protected ChildEventListener populateMovementsEventListener() {
        return new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                getMovement(dataSnapshot.getKey().toString());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                if (movements.isEmpty()) {
                    listView.setVisibility(View.INVISIBLE);
                    message.setVisibility(View.VISIBLE);
                } else {
                    removeMovement(getMovementById(dataSnapshot.getKey()));
                }
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
}
