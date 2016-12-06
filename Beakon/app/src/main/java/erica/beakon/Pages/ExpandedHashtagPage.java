package erica.beakon.Pages;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import erica.beakon.Adapters.FirebaseHandler;
import erica.beakon.Adapters.FollowerAdapter;
import erica.beakon.Adapters.RecommendedMovementsAdapter;
import erica.beakon.MainActivity;
import erica.beakon.Adapters.MyMovementAdapter;
import erica.beakon.Objects.Hashtag;
import erica.beakon.Objects.Movement;
import erica.beakon.Objects.User;
import erica.beakon.R;


public class ExpandedHashtagPage extends Fragment {

    String hashtagName;
    ArrayList<String> movementIDList = new ArrayList<>(); //list of movement IDs (stored in hashtag)
    ArrayList<String> userIDList = new ArrayList<>(); //list of user IDs (stored in hashtag)
    ArrayList<Movement> movementList = new ArrayList<>(); //list of movements fetched using movement IDs
    ArrayList<User> followerList = new ArrayList<>(); //list of followers fetched using user IDs
    Hashtag hashtag;
    String name = "na";

    public ExpandedHashtagPage(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_expanded_hashtag_page, container, false);
        Bundle bundle = this.getArguments();

        if (bundle != null){
            String name_before = bundle.getString("name");
            name = name_before.substring(0,name_before.length()-1);
            Log.d("!!!", name);
        }

        final FirebaseHandler firebaseHandler = ((MainActivity) getActivity()).getHandler();

        //create hashtag TV and set it to the hashtag set in setHashtag by previous fragment
        final TextView hashtagView = (TextView) view.findViewById(R.id.hashtag_title);
        hashtagView.setText("#" + name);

        //create buttons
        final ImageButton backButton = (ImageButton) view.findViewById(R.id.backButtonHashtag);
        final ImageButton followButton = (ImageButton) view.findViewById(R.id.followButtonHashtag);

        //create movement list view and its adapter
        ListView movementsLV = (ListView) view.findViewById(R.id.hashtag_movements_list);
        final RecommendedMovementsAdapter movementsAdapter = new RecommendedMovementsAdapter(getActivity(), movementList);
        movementsLV.setAdapter(movementsAdapter); //starts empty

        //create follower list view and its adapter
        ListView followerLV = (ListView) view.findViewById(R.id.hashtag_followers_list);
        final FollowerAdapter followerAdapter = new FollowerAdapter(getActivity(), followerList);
        followerLV.setAdapter(followerAdapter); //starts empty

        //search firebase for hashtag information (movement and user ID lists) using hashtag name
        firebaseHandler.getHashtag(name, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(":::", String.valueOf(dataSnapshot.getValue()));
                if (dataSnapshot.getValue() != null) { // if the hashtag exists in database
                    hashtag = dataSnapshot.getValue(Hashtag.class); // store hashtag info in hashtag object
                    Log.d("[[[", hashtag.getName());
                    movementIDList = hashtag.getMovementList(); // get movement id list from hashtag
                    userIDList = hashtag.getUserList(); // get user id list from hashtag
                    if (movementIDList != null){ // if the movement list isn't empty
                        firebaseHandler.getBatchMovements(movementIDList, new ValueEventListener() { //get all the movements
                            @Override
                            public void onDataChange(DataSnapshot movementSnapshot) {
                                Movement movement = movementSnapshot.getValue(Movement.class); //store movement info in movement object
                                movementsAdapter.add(movement); //add movement to adapter/list view (updates each loop, rather than all at once)
                            } //updates gradually so you don't end up with a blank screen for a while

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });}
                    if (userIDList != null){ // if the user list isn't empty
                        firebaseHandler.getBatchUsers(userIDList, new ValueEventListener() { //get all the users
                        @Override
                        public void onDataChange(DataSnapshot userSnapshot) {
                            ArrayList<String> hashtagList = ((MainActivity) getActivity()).getHashtagList(userSnapshot);
                            HashMap<String, HashMap<String, Boolean>> movementList = ((MainActivity) getActivity()).getMovements(userSnapshot);
//                            User follower = userSnapshot.getValue(User.class); //store user info in user object
                            User follower = new User(userSnapshot.child("id").getValue().toString(), userSnapshot.child("name").getValue().toString(), hashtagList, movementList);
                            followerAdapter.add(follower); //add user to follower adapter, updates list view
                        } //updates gradually (each iteration) so you don't end up with a blank screen for a while

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });}
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed(); //might not work if multiple backs pressed in a row?
            }
        });

        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User currentUser = ((MainActivity) getActivity()).getCurrentUser();
                firebaseHandler.getUser(currentUser.getId(), new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<String> hashtagList = ((MainActivity) getActivity()).getHashtagList(dataSnapshot);
                        HashMap<String, HashMap<String, Boolean>> movementList = ((MainActivity) getActivity()).getMovements(dataSnapshot);
                        User user = new User(dataSnapshot.child("id").getValue().toString(), dataSnapshot.child("name").getValue().toString(), hashtagList, movementList);
                        Log.d("~~~", user.getName());
                        firebaseHandler.addUsertoHashtag(user, hashtag);
                        followButton.setBackgroundResource(R.drawable.check);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
            }
        });



        return view;
    }


}
