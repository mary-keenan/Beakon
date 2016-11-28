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

import erica.beakon.Adapters.FirebaseHandler;
import erica.beakon.Adapters.FollowerAdapter;
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

    public ExpandedHashtagPage(){}

    public void setHashtag(String hashtagName) {
        //lets you set the hashtag right after you create page, before you change fragments
        this.hashtagName = hashtagName;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_expanded_hashtag_page, container, false);
        final FirebaseHandler firebaseHandler = ((MainActivity) getActivity()).getHandler();

        //create hashtag TV and set it to the hashtag set in setHashtag by previous fragment
        TextView hashtagView = (TextView) view.findViewById(R.id.hashtag_title);
        hashtagView.setText(this.hashtagName);

        //create buttons
        final ImageButton backButton = (ImageButton) view.findViewById(R.id.backButtonHashtag);
        final ImageButton followButton = (ImageButton) view.findViewById(R.id.followButtonHashtag);

        //create movement list view and its adapter
        ListView movementsLV = (ListView) view.findViewById(R.id.hashtag_movements_list);
        final MyMovementAdapter movementsAdapter = new MyMovementAdapter(getActivity(), movementList);
        movementsLV.setAdapter(movementsAdapter); //starts empty

        //create follower list view and its adapter
        ListView followerLV = (ListView) view.findViewById(R.id.hashtag_followers_list);
        final FollowerAdapter followerAdapter = new FollowerAdapter(getActivity(), followerList);
        followerLV.setAdapter(followerAdapter); //starts empty

        //search firebase for hashtag information (movement and user ID lists) using hashtag name
        firebaseHandler.getHashtag("renegade", new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) { // if the hashtag exists in database
                    hashtag = dataSnapshot.getValue(Hashtag.class); // store hashtag info in hashtag object
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
                            User follower = userSnapshot.getValue(User.class); //store user info in user object
                            followerAdapter.add(follower); //add user to follower adapter, updates list view
                            Log.d("+++", follower.getName());
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
                //follow the hashtag (see movements with this hashtag in suggested movements?)
                // Todo: add hashtag to list of hashtags the User follows (need userId)
                firebaseHandler.getUser("-KXgJqjMVKw4eLlWhmSz", new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
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
