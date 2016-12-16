package erica.beakon.Pages;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import erica.beakon.Adapters.FirebaseHandler;
import erica.beakon.Adapters.FollowerAdapter;
import erica.beakon.Adapters.HashtagAdapter;
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
    ArrayList<String> hashtagsShown = new ArrayList<>(); //prevents duplication
    ArrayList<String> followersShown = new ArrayList<>(); //prevents duplication

    FirebaseHandler firebaseHandler;
    //create TVs
    TextView numFollowersTV;
    LinearLayout hashtagLayout;
    TextView movementNameTV;
    TextView description;
    TextView steps;
    TextView resources;
    FollowerAdapter followerAdapter;
    ImageButton backButton;
    ImageButton followButton;

    public ExpandedMovementPage() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_expanded_movement_page, container, false);
        Bundle bundle = this.getArguments();

        if (bundle != null){
            ID = bundle.getString("ID");
            name = bundle.getString("name");
        }

        firebaseHandler = ((MainActivity) getActivity()).getHandler();
        //create TVs
        numFollowersTV = (TextView) view.findViewById(R.id.num_followers);
        hashtagLayout = (LinearLayout) view.findViewById(R.id.hashtag_layout);
        movementNameTV = (TextView) view.findViewById(R.id.movement_name);
        description = (TextView) view.findViewById(R.id.movement_description);
        steps = (TextView) view.findViewById(R.id.movement_steps);
        resources = (TextView) view.findViewById(R.id.movement_resources);

        //create buttons
        backButton = (ImageButton) view.findViewById(R.id.backButtonMovement);
        followButton = (ImageButton) view.findViewById(R.id.followButtonMovement);

        //create follower list view and its adapter
        ListView followerLV = (ListView) view.findViewById(R.id.movement_followers_list);
        followerAdapter = new FollowerAdapter(getActivity(), followerList);
        followerLV.setAdapter(followerAdapter); //starts empty

        populatePage();

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
                        firebaseHandler.addUsertoMovement(user, movement);
                        followButton.setImageResource(R.drawable.check);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        populatePage();
    }

    @Override
    public void onPause() {
        super.onPause();
        hashtagsShown.clear();
    }

    private void setOnClickHashtag(final TextView tv){
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hashtagName = (String) tv.getText();
                ExpandedHashtagPage hashtagFragment = new ExpandedHashtagPage();
                Bundle bundle = new Bundle();
                bundle.putString("name", hashtagName); //give new fragment the hashtag it's expanding
                hashtagFragment.setArguments(bundle);
                ((MainActivity) getContext()).changeFragment(hashtagFragment, "expandedHashtagPage"); //changes fragments
            }
        });
    }

    private String getFormattedHashtag(String hashtag) {
        return "#" + hashtag + " ";
    }

    private TextView createHashtagTextView(String hashtag) {
        TextView tv = new TextView(getContext());
        tv.setText(getFormattedHashtag(hashtag));
        tv.setTextColor(getContext().getResources().getColor(R.color.colorPrimaryDark));
        setOnClickHashtag(tv);
        return  tv;
    }

    public void addHashtagtoView(String hashtag, LinearLayout hashtagLayout) {
        TextView tv = createHashtagTextView(hashtag);
        hashtagLayout.addView(tv);
    }

    private void populatePage() {
        movementNameTV.setText(name);
        firebaseHandler.getMovement(ID, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) { // if the movement exists in database
                    movement = dataSnapshot.getValue(Movement.class); // store movement info in movement object
                    description.setText(movement.getDescription());
                    steps.setText(movement.getSteps());
                    resources.setText(movement.getResources());
                    if (movement.getHashtagList() != null){
                        hashtagNameList = movement.getHashtagList();}; // get hashtag name list from movement}
                    if(movement.getFollowers() != null) {
                        DecimalFormat formatter = new DecimalFormat("#,###");
                        numFollowersTV.setText(formatter.format(movement.getFollowers().size()));
                        userIDList = movement.getFollowers();} // get user id list from movement
                    if (hashtagNameList != null){ // if the hashtag list isn't empty
                        firebaseHandler.getBatchHashtags(hashtagNameList, new ValueEventListener() { //get all the movements
                            @Override
                            public void onDataChange(DataSnapshot hashtagSnapshot) {
                                Hashtag hashtag = hashtagSnapshot.getValue(Hashtag.class); //store hashtag info in hashtag object
                                if (!hashtagsShown.contains(hashtag.getName())) { //if already being shown, don't show movement again
                                    addHashtagtoView(hashtag.getName(), hashtagLayout);
                                    hashtagsShown.add(hashtag.getName());
                                }
                            } //updates gradually so you don't end up with a blank screen for a while

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });}
                    if (userIDList != null){ // if the user list isn't empty
                        if (userIDList.contains(((MainActivity) getActivity()).getCurrentUser().getId())){
                            followButton.setImageResource(R.drawable.check);
                        }
                        firebaseHandler.getBatchUsers(userIDList, new ValueEventListener() { //get all the users
                            @Override
                            public void onDataChange(DataSnapshot userSnapshot) {
                                ArrayList<String> hashtagList = ((MainActivity) getActivity()).getHashtagList(userSnapshot);
                                HashMap<String, HashMap<String, Boolean>> movementList = ((MainActivity) getActivity()).getMovements(userSnapshot);
//                            User follower = userSnapshot.getValue(User.class); //store user info in user object
                                User follower = new User(userSnapshot.child("id").getValue().toString(), userSnapshot.child("name").getValue().toString(), hashtagList, movementList);
                                if (!followersShown.contains(follower.getId())) { //if already being shown, don't show movement again
                                    followerAdapter.add(follower); //add user to follower adapter, updates list view
                                    followersShown.add(follower.getId());
                                }
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
    }
}
