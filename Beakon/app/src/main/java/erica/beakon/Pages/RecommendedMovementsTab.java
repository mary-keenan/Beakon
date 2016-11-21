package erica.beakon.Pages;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import erica.beakon.Adapters.RecommendedMovementsAdapter;
import erica.beakon.Objects.Movement;
import erica.beakon.R;

import static java.util.Collections.min;

public class RecommendedMovementsTab extends MovementsTab {

    public static final String TAG = "REC_MOVEMENTS_TAB";

    final String POPULAR_MOVEMENTS_FILENAME = "movements";
    final String POPULAR_MOVEMENTS_RANKS_FILENAME = "movementsRanks";
    final int POPULAR_MOVEMENTS_LIMIT = 2;
    View view;
    ListView nearbyListView;
    ListView popularListView;
    ArrayList<Movement> popularMovements;
    RecommendedMovementsAdapter nearbyAdapter;
    RecommendedMovementsAdapter popularAdapter;
    HashMap<String, Integer> movementNearbyRanks;
    HashMap<String, Integer> movementPopularRanks;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        popularMovements = new ArrayList<>();
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


    @Override
    public void onPause() {
        super.onPause();
        savePopularMovements();
        savePopularMovementsRanks();
    }

    @Override
    public void onResume() {
        super.onResume();
        popularMovements = (ArrayList<Movement>) loadSavedObject(POPULAR_MOVEMENTS_FILENAME);
        movementPopularRanks = (HashMap<String, Integer>) loadSavedObject(POPULAR_MOVEMENTS_RANKS_FILENAME);
        initializeListViews();
        popularAdapter.notifyDataSetChanged();
    }

    private void savePopularMovements() {
        saveToInternalStorage(POPULAR_MOVEMENTS_FILENAME, popularMovements);
    }

    private void savePopularMovementsRanks() {
        saveToInternalStorage(POPULAR_MOVEMENTS_RANKS_FILENAME, movementPopularRanks);
    }

    private void saveToInternalStorage(String fileName, Object o) {
        try {
            File file = new File(getContext().getFilesDir(), fileName + ".ser");
            FileOutputStream fileOut = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOut);
            objectOutputStream.writeObject(o);
            objectOutputStream.close();
            fileOut.close();
        } catch(Exception ex) {
            Log.d(TAG, ex.getMessage());
        }
    }

    private Object loadSavedObject(String fileName) {
        String filePath = getContext().getFilesDir() + "/" + fileName + ".ser";
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath));
            return ois.readObject(); //need to check for case where popularmovements was empty
        } catch (Exception ex) {
            Log.d(TAG, ex.getMessage());
            if (fileName.equals(POPULAR_MOVEMENTS_RANKS_FILENAME)) {
                //in case the movements ranks are not saved but the popular movements were
                //this should only happen during developement/testing
                return getMovementRanksFromPopularMovements();
            } else {
                //return an empty arraylist because there are not any movements saved
                return new ArrayList<Movement>();
            }

        }
    }

    private void initializeView() {
        setUpChangeFragmentsButton(view, new MyMovementsTab(), R.id.my_movements);
        initializeListViews();
    }

    // initializing views

    private void initializeListViews() {
        nearbyListView = (ListView) view.findViewById(R.id.recommended_nearby_movements_list);
        popularListView = (ListView) view.findViewById(R.id.recommended_popular_movements_list);
        nearbyAdapter = new RecommendedMovementsAdapter(getContext(), movements);
        popularAdapter = new RecommendedMovementsAdapter(getContext(), popularMovements);

        if (!movements.isEmpty()) {
            setUpNearbyListView();
        }

        if (!popularMovements.isEmpty()) {
            setUpPopularListView();
        }
    }

    private void setUpNearbyListView() {
        nearbyListView.setAdapter(nearbyAdapter);
    }

    private void setUpPopularListView() {
        popularListView.setAdapter(popularAdapter);
    }

    //getting movements for nearby and popular lists

    private void setMovementsListeners() {
        //get popular movements
        getMainActivity().firebaseHandler.getMovements(populateMovementsFromAllEventListener());

        //get movements from nearby users
        for (String userId: getMainActivity().locationHandler.getNearbyUsers()) {
            getMainActivity().firebaseHandler.getUserChild(userId, "movements", populateMovementsEventListener());
        }
    }

    //popular movements list
    public ChildEventListener populateMovementsFromAllEventListener() {
        return new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Movement movement = dataSnapshot.getValue(Movement.class);
                addPopularMovementIfRanking(movement);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Movement movement = dataSnapshot.getValue(Movement.class);
                if (popularMovementsAlreadyHas(movement)) {
                    updatePopularMovement(movement);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Movement movement = dataSnapshot.getValue(Movement.class);
                removePopularMovement(movement);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG,databaseError.getMessage());
            }
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
                if (movement.hasUsers() && movement.getUsers().size() >= minimumMovement.getUsers().size()) {
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
        movementPopularRanks.put(movement.getId(), movement.getUsers().size());
        popularMovements.add(movement);

        // if the dataset was just populated for the first time, need to set up the list view.
        if (popularMovements.size() == 1) {
            setUpPopularListView();
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

    //NEARBY MOVEMENTS LIST

    public ChildEventListener populateMovementsEventListener() {
       return new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                updateNearbyMovementRanks(dataSnapshot.getValue(String.class));
                getMovement(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                // do something with the changed data
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String movementId = dataSnapshot.getValue(String.class);
                if (movementsAlreadyHas(movementId)) {
                    movements.remove(getMovementById(movementId));
                }
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
                addMovement(movement);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("MyMovementsTab", "there is a problem on the listener for the movement added to my movements");
            }
        };
    }

    private void addMovement(Movement movement) {
        if (!movementsAlreadyHas(movement.getId())) {
            movements.add(movement);
        }
        if (movements.size() == 1) {
            setUpNearbyListView();
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

    private HashMap<String, Integer> getMovementRanksFromPopularMovements() {
        HashMap<String, Integer> ranks = new HashMap<>();
        for (Movement m: popularMovements) {
            ranks.put(m.getId(), m.getUsers().size());
        }
        return ranks;
    }

}



