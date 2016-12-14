package erica.beakon.Pages;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

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
    Button logoutButton;
    Button userPrefButton;
    Boolean alreadyLoaded = false;
    ArrayList<String> movementsShown = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_my_movements_tab, container, false);

        listView = (ListView) view.findViewById(R.id.my_movements_list);
        message = (TextView) view.findViewById(R.id.no_movments_message);
        setUpChangeFragmentsButton(view, new RecommendedMovementsTab(), R.id.movements, R.id.my_movements);
//        setUpChangeFragmentsButton(view, new AddMovementPage(), R.id.goto_add_movement_button);
        ImageButton addMovementBtn = (ImageButton) view.findViewById(R.id.goto_add_movement_btn);
        addMovementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).changeFragment(new AddMovementPage(), "AddMovementPage");
            }
        });

        final Switch showCompleted = (Switch) view.findViewById(R.id.showCompleted);

        showCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (showCompleted.isChecked()) {
                    Toast.makeText(getContext(),"Showing completed movements", Toast.LENGTH_LONG).show();
                    for(int i = 0; i<listView.getChildCount(); i++) {
                        if(listView.getChildAt(i).getVisibility() == View.GONE) {
                            listView.getChildAt(i).setVisibility(View.VISIBLE);
                        }
                    }
                } else {
                    Toast.makeText(getContext(),"Hiding completed movements", Toast.LENGTH_LONG).show();
                    for(int i = 0; i<listView.getChildCount(); i++) {
                        final int finalI = i;
                        firebaseHandler.getMovementofUserStatus(getMainActivity().currentUser, movements.get(i), new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getValue().equals(true)) {
                                    listView.getChildAt(finalI).setVisibility(View.GONE);
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                    }

                }
            }
        });

        setMenuButtonOnClickListener(R.id.my_movement_tab);
        setUpAddButton();

        setUsersMovementsListener();  //this is where the problems start
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

    protected ValueEventListener getMovementAddedValueEventListener() { //set in getMovement()
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
                    HashMap movementMap = (HashMap) dataSnapshot.getValue();
                    Log.d("SNAPSHOT", String.valueOf(dataSnapshot));
                    Log.d("MOVEMENT MAP", String.valueOf(movementMap));
                    ArrayList movementIdList = new ArrayList(movementMap.keySet());
                    getMovement(movementIdList);}
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }
}
