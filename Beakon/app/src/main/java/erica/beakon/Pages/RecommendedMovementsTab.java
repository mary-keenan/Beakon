package erica.beakon.Pages;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import erica.beakon.Adapters.RecommendedMovementsAdapter;
import erica.beakon.Adapters.StorageHandler;
import erica.beakon.Objects.Hashtag;
import erica.beakon.Objects.Movement;
import erica.beakon.R;

import static java.util.Collections.min;

public class RecommendedMovementsTab extends MovementsTab {

    public static final String TAG = "REC_MOVEMENTS_TAB";

    final int POPULAR_MOVEMENTS_LIMIT = 2;
    StorageHandler storageHandler;
    ListView listView;
    ArrayList<Movement> popularMovements;
    ArrayList<Movement> interestsMovements;
    RecommendedMovementsAdapter nearbyAdapter;
    RecommendedMovementsAdapter popularAdapter;
    RecommendedMovementsAdapter interestsAdapter;
    HashMap<String, Integer> movementNearbyRanks;
    HashMap<String, Integer> movementPopularRanks;
    int[] tabs = {R.id.joined_nearby_button, R.id.interests_button, R.id.popular_button};



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storageHandler = new StorageHandler(getContext());
        popularMovements = new ArrayList<>();
        interestsMovements = new ArrayList<>();
        this.movementNearbyRanks = new HashMap<>();
        this.movementPopularRanks = new HashMap<>();
        setMovementsListeners();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_recommended_movements, container, false);
        initializeView();
        return view;
    }



    private void initializeView() {
        setMenuButtonOnClickListener(R.id.recommended_movements_tab);
        setUpAddButton();
        setUpChangeFragmentsButton(view, new MyMovementsTab(), R.id.my_movements, R.id.movements);
        setUpChangeTabsButtons();
        initializeListViews();
    }

    // initializing views

    private void setUpChangeTabsButtons() {
        Button nearbyTab = (Button) view.findViewById(R.id.joined_nearby_button);
        Button popularTab = (Button) view.findViewById(R.id.popular_button);
        Button interestsTab = (Button) view.findViewById(R.id.interests_button);

        nearbyTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               setNearbySelected();
            }
        });

        popularTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPopularSelected();
            }
        });

        interestsTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setInterestsSelected();
            }
        });
    }
    private void initializeListViews() {
        listView = (ListView) view.findViewById(R.id.recommended_movements_list);
        nearbyAdapter = new RecommendedMovementsAdapter(getContext(), movements);
        popularAdapter = new RecommendedMovementsAdapter(getContext(), popularMovements);
        interestsAdapter = new RecommendedMovementsAdapter(getContext(), interestsMovements);

        if (!interestsMovements.isEmpty()) {
            setInterestsSelected();
        } else if (!movements.isEmpty()) {
            setNearbySelected();
        } else {
            setPopularSelected();
        }
    }

    private void setSelected(int tabId, RecommendedMovementsAdapter adapter, ArrayList<Movement> tabMovements) {
        if (!tabMovements.isEmpty()) {
            listView.setAdapter(adapter);
            listView.setVisibility(View.VISIBLE);
            view.findViewById(R.id.no_rec_movments_message).setVisibility(View.INVISIBLE);
        } else {
            listView.setVisibility(View.GONE);
            view.findViewById(R.id.no_rec_movments_message).setVisibility(View.VISIBLE);
        }
        setTabColors(tabId);
    }

    private void setInterestsSelected() {
        setSelected(R.id.interests_button, interestsAdapter, interestsMovements);
    }

    private void setNearbySelected() {
        setSelected(R.id.joined_nearby_button, nearbyAdapter, movements);
    }

    private void setPopularSelected() {
        setSelected(R.id.popular_button, popularAdapter, popularMovements);
    }


    //getting movements for nearby and popular lists

    private void setMovementsListeners() {
        //get popular movements
        getMainActivity().firebaseHandler.getMovements(populateMovementsFromAllEventListener());

        //get movements from nearby users
        for (String userId: getMainActivity().locationHandler.getNearbyUsers()) {
            getMainActivity().firebaseHandler.getUserChild(userId, "movements", populateMovementsEventListener());
        }

        //get movements based on interests
        ArrayList<String> userInterests = getMainActivity().currentUser.getHashtagList();
        for (String interest: userInterests) {
            getMainActivity().firebaseHandler.getHashtag(interest, getInterestValueEventListener());
        }
    }

    //popular movements list
    public ChildEventListener populateMovementsFromAllEventListener() {
        return new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Movement movement = dataSnapshot.getValue(Movement.class);
                movement.initializeLists(movement);
                addPopularMovementIfRanking(movement);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Movement movement = dataSnapshot.getValue(Movement.class);
                movement.initializeLists(movement);
                if (popularMovementsAlreadyHas(movement)) {
                    updatePopularMovement(movement);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Movement movement = dataSnapshot.getValue(Movement.class);
                movement.initializeLists(movement);
                removePopularMovement(movement);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {Log.d(TAG,databaseError.getMessage());}
        };
    }

    private ValueEventListener getInterestValueEventListener() {
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    Hashtag hashtag = dataSnapshot.getValue(Hashtag.class);
                    ArrayList<String> hashtagMovements = hashtag.getMovementList();
                    getMainActivity().firebaseHandler.getBatchMovements(hashtagMovements, getInterestMovementsValueEventListener());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {Log.d(TAG,databaseError.getMessage());}
        };
    }

    private ValueEventListener getInterestMovementsValueEventListener() {
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Movement movement = dataSnapshot.getValue(Movement.class);
                //if the movement is not already in the list AND the user has not already joined the movement
                if (!interestsMovements.contains(movement) && !getMainActivity().currentUser.isInMovement(movement)) {
                    interestsMovements.add(movement);
                    interestsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {Log.d(TAG,databaseError.getMessage());}
        };
    }


    private void updatePopularMovement(Movement movement) {
        removePopularMovement(getPopularMovementById(movement.getId())); //remove existing reference to movement
        addMovement(movement); //add back movement if it still makes the rank
    }

    private void addPopularMovementIfRanking(Movement movement) {
        if (!popularMovementsAlreadyHas(movement)) {
            if (popularMovements.size() > POPULAR_MOVEMENTS_LIMIT) {
                //if the list already has too many items
                Movement minimumMovement = getMinimumRankPopularMovement();
                if (movement.hasUsers() && movement.getFollowers().size() >= minimumMovement.getFollowers().size()) {
                    removePopularMovement(minimumMovement);
                    addPopularMovement(movement);
                }
            } else {
                //if the list has not reached the limit
                addPopularMovement(movement);
            }

        }
    }

    private void removePopularMovement(Movement movement) {
        if (popularMovementsAlreadyHas(movement)) {
            popularMovements.remove(getPopularMovementById(movement.getId()));
            movementPopularRanks.remove(movement.getId());
        }
        popularAdapter.notifyDataSetChanged();
    }

    private void addPopularMovement(Movement movement) {
        if (!userAlreadyIn(movement.getId())) {
            movementPopularRanks.put(movement.getId(), movement.getFollowers().size());
            popularMovements.add(movement);
        }

        popularAdapter.notifyDataSetChanged();
    }

    private boolean popularMovementsAlreadyHas(Movement movement) {
        for (Movement m: popularMovements) {
            if (m.getId().equals(movement.getId())) {
                return true;
            }
        }
        return false;
    }

    private Movement getMinimumRankPopularMovement() {
        int minimumRank = min(movementPopularRanks.values());

        //get the key that matches a certain value from the movement ranks
        for (Movement m: popularMovements) {
            if (movementPopularRanks.get(m.getId()) == minimumRank) {
                return m;
            }
        }
        throw new NullPointerException("No such key exists with that value in movementsRanks");
    }

    private Movement getPopularMovementById(String id) {
        for (Movement m: popularMovements) {
            if (m.getId().equals(id)) {
                return m;
            }
        }
        throw new NullPointerException("No movement exists with that id in popularMovements");
    }

    //NEARBY MOVEMENTS LIST -- IF THIS IS CAUSING PROBS, MAY BE BECAUSE IT USED TO BE CHILDEVENTLISTENER INSTEAD OF VALUEEVENTLISTENER
    protected ValueEventListener populateMovementsEventListener() {
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                    updateNearbyMovementRanks(dataSnapshot.getKey());
                    HashMap movementMap = (HashMap) dataSnapshot.getValue();
                    ArrayList movementIdList = new ArrayList(movementMap.keySet());
                    getMovements(movementIdList);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
    }

    protected ValueEventListener getMovementAddedValueEventListener() {
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Movement movement = dataSnapshot.getValue(Movement.class);
                addMovement(movement);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("MyMovementsTab", "there is a problem on the listener for the movement added to my movements");
            }
        };
    }

    private void addMovement(Movement movement) {
        if (!movementsAlreadyHas(movement.getId()) && !userAlreadyIn(movement.getId())) {
            movements.add(movement);
        }

        nearbyAdapter.notifyDataSetChanged();

    }

    private void updateNearbyMovementRanks(String movementId) {
        if (movementNearbyRanks.containsKey(movementId)) {
            movementNearbyRanks.put(movementId, movementNearbyRanks.get(movementId) + 1);
        } else {
            movementNearbyRanks.put(movementId, 1);
        }
    }

    private boolean movementsAlreadyHas(String movementId) {
        for (Movement m: movements) {
            if (m.getId().equals(movementId)) {
                return true;
            }
        }
        return false;
    }

    private boolean userAlreadyIn(String movementId) {
        for (String m: getMainActivity().currentUser.getMovements().keySet()) {
            if (m.equals(movementId)) {
                return true;
            }
        }
        return false;
    }

    private HashMap<String, Integer> getMovementRanksFromPopularMovements() {
        HashMap<String, Integer> ranks = new HashMap<>();
        for (Movement m: popularMovements) {
            ranks.put(m.getId(), m.getFollowers().size());
        }
        return ranks;
    }

    private void setTabColors(int selectedId) {
        //create buttons
        final Button selectedButton = (Button) view.findViewById(selectedId);
        selectedButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorBackground));

        Button unselectedButton;
        for (int id: tabs) {
            if (id != selectedId) {
                unselectedButton = (Button) view.findViewById(id);
                unselectedButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAccentLight));
            }
        }
    }

}



