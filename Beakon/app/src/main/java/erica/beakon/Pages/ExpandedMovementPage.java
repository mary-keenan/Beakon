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
import erica.beakon.Adapters.HashtagAdapter;
import erica.beakon.Adapters.RecommendedMovementsAdapter;
import erica.beakon.MainActivity;
import erica.beakon.Objects.Hashtag;
import erica.beakon.Objects.Movement;
import erica.beakon.Objects.User;
import erica.beakon.R;

public class ExpandedMovementPage extends Fragment {

    ArrayList<String> hashtagNameList = new ArrayList<>(); //list of movement IDs (stored in hashtag)
    ArrayList<String> userIDList = new ArrayList<>(); //list of user IDs (stored in hashtag)
    ArrayList<Hashtag> hashtagList = new ArrayList<>(); //list of movements fetched using movement IDs
    ArrayList<User> followerList = new ArrayList<>(); //list of followers fetched using user IDs
    Movement movement;
    String ID = "no ID";
    String name = "no name";

    public ExpandedMovementPage() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_expanded_movements_page, container, false);
        Bundle bundle = this.getArguments();

        if (bundle != null){
            ID = bundle.getString("ID");
            name = bundle.getString("name");
            Log.d("!!!", ID);
        }

        final FirebaseHandler firebaseHandler = ((MainActivity) getActivity()).getHandler();

        //create hashtag TV and set it to the hashtag set in setHashtag by previous fragment
        final TextView movementView = (TextView) view.findViewById(R.id.movement_name);
        movementView.setText(name);

        //create buttons
        final ImageButton backButton = (ImageButton) view.findViewById(R.id.backButtonMovement);
        final ImageButton followButton = (ImageButton) view.findViewById(R.id.followButtonMovement);

        //create hashtag list view and its adapter
        ListView hashtagLV = (ListView) view.findViewById(R.id.movement_hashtags_list);
        final HashtagAdapter hashtagAdapter = new HashtagAdapter(getActivity(), hashtagList);
        hashtagLV.setAdapter(hashtagAdapter); //starts empty

        //create follower list view and its adapter
        ListView followerLV = (ListView) view.findViewById(R.id.movement_followers_list);
        final FollowerAdapter followerAdapter = new FollowerAdapter(getActivity(), followerList);
        followerLV.setAdapter(followerAdapter); //starts empty


        //search firebase for movement information (hashtag and user ID lists) using movement ID
        firebaseHandler.getMovement(ID, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("&&&", String.valueOf(dataSnapshot));
                if (dataSnapshot.getValue() != null) { // if the movement exists in database
                    movement = dataSnapshot.getValue(Movement.class); // store movement info in movement object
                    if (movement.getHashtagList() != null){
                        hashtagNameList = movement.getHashtagList();}; // get hashtag name list from movement}
                    if(movement.getFollowers() != null) {
                        userIDList = movement.getFollowers();} // get user id list from movement
                    if (hashtagNameList != null){ // if the hashtag list isn't empty
                        firebaseHandler.getBatchHashtags(hashtagNameList, new ValueEventListener() { //get all the movements
                            @Override
                            public void onDataChange(DataSnapshot hashtagSnapshot) {
                                Hashtag hashtag = hashtagSnapshot.getValue(Hashtag.class); //store hashtag info in hashtag object
                                hashtagAdapter.add(hashtag); //add hashtag to adapter/list view (updates each loop, rather than all at once)
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
                        User user = new User(dataSnapshot.child("id").getValue().toString(), dataSnapshot.child("ID").getValue().toString(), hashtagList, movementList);
                        firebaseHandler.addUsertoMovement(user, movement);
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
