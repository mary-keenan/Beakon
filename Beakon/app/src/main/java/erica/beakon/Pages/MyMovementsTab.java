package erica.beakon.Pages;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import erica.beakon.Adapters.FirebaseHandler;
import erica.beakon.Adapters.MyMovementAdapter;
import erica.beakon.MainActivity;
import erica.beakon.Objects.Movement;
import erica.beakon.R;

public class MyMovementsTab extends MovementsTab {

    public static final String TAG = "MY MOVEMENTS TAB";
    String databaseURL = "https://beakon-5fa96.firebaseio.com/";
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference ref = db.getReferenceFromUrl(databaseURL);
    FirebaseHandler firebaseHandler = new FirebaseHandler(db,ref);
    MyMovementAdapter adapter;
    ListView listView;
    TextView message;
    Boolean alreadyLoaded = false;
    ArrayList<String> movementsShown = new ArrayList<>();
    ArrayList<String> completedMovements = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_movements_tab, container, false);
        listView = (ListView) view.findViewById(R.id.my_movements_list);
        message = (TextView) view.findViewById(R.id.no_movments_message);
        setUpChangeFragmentsButton(view, new RecommendedMovementsTab(), R.id.movements, R.id.my_movements);
        setMenuButtonOnClickListener(R.id.my_movement_tab);
        setUpAddButton();
        setUpCompletedMovementsSwitch();
        if (!movements.isEmpty() && movements != null) {
            setUpListView();
        }
        setUsersMovementsListener();  //this is where the problems start
        setMovementsStatusListener();
        return view;
    }

    private void setUpCompletedMovementsSwitch() {
        final Switch showCompleted = (Switch) view.findViewById(R.id.showCompleted);
        showCompleted.setChecked(true);
        showCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (showCompleted.isChecked()) {
                    Toast.makeText(getContext(),"Showing completed movements", Toast.LENGTH_LONG).show();
                    getMovements(completedMovements);
                } else {
                    ArrayList<Movement> toRemove = new ArrayList<Movement>();
                    for (Movement m: movements) {
                        if (completedMovements.contains(m.getId())) {
                            toRemove.add(m);
                            movementsShown.remove(m.getId());
                        }
                    }
                    for (Movement m: toRemove) { movements.remove(m); }
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getContext(),"Hiding completed movements", Toast.LENGTH_LONG).show();

                }
            }
        });
    }
    private void setUsersMovementsListener() {
        getMainActivity().firebaseHandler.getUserChild(getMainActivity().currentUser.getId(), "movements", populateMovementsEventListener());
    }

    private void setMovementsStatusListener() {
        getMainActivity().firebaseHandler.getBatchMovementofUserStatus(getMainActivity().currentUser, new ArrayList(getMainActivity().currentUser.getMovements().keySet()), movementStatusValueEventListener());
    }

    private void setUpListView() {
        adapter = new MyMovementAdapter(getContext(), movements);
        message.setVisibility(View.INVISIBLE);
        listView.setVisibility(View.VISIBLE);
        listView.setAdapter(adapter);
    }

    protected ValueEventListener getMovementAddedValueEventListener() { //set in getMovements()
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                    Movement newMovement = dataSnapshot.getValue(Movement.class);

                    if (!movementsShown.contains(newMovement.getId())) { //make sure we're not already showing movement -- onBackPressed will duplicate movements otherwise
                        movements.add(newMovement);
                        movementsShown.add(newMovement.getId());
                    }

                    if (movements.size() == 1) {
                        setUpListView();
                        alreadyLoaded = true;
                    }
                    if (adapter != null) {
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("MyMovementsTab", "there is a problem on the listener for the movement added to my movements");
            }
        };
    }

    protected ValueEventListener populateMovementsEventListener() {
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null && dataSnapshot.getValue() != null){
                    HashMap<String , HashMap<String, Boolean>> movementMap = (HashMap) dataSnapshot.getValue();
                    ArrayList<String> movementIdList = new ArrayList(movementMap.keySet());
                    getMovements(movementIdList);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }

    private ChildEventListener movementStatusValueEventListener() {
        return new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                updateCompletedList(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                updateCompletedList(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
    }

    private void updateCompletedList(DataSnapshot dataSnapshot) {
        Boolean completed = (Boolean) dataSnapshot.getValue();
        String movementId = dataSnapshot.getRef().getParent().getKey();
        if (completed) {
            completedMovements.add(movementId);
        } else {
            completedMovements.remove(movementId);
        }
    }
}
