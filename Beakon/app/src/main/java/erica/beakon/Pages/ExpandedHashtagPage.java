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

import java.util.ArrayList;

import erica.beakon.MainActivity;
import erica.beakon.Adapters.MyMovementAdapter;
import erica.beakon.Objects.Hashtag;
import erica.beakon.Objects.Movement;
import erica.beakon.R;


public class ExpandedHashtagPage extends Fragment {

    String hashtag;

    public ExpandedHashtagPage(){}

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_expanded_hashtag_page, container, false);

        //create hashtag TV and set it to the hashtag set in setHashtag by previous fragment
        TextView hashtagView = (TextView) view.findViewById(R.id.hashtag_title);
        hashtagView.setText(this.hashtag);

        //search firebase for hashtag using hashtag name

        ArrayList<Movement> movements = new ArrayList<>();
        ArrayList<String> hashtags = new ArrayList<>();
        hashtags.add("#stillwithher");
        hashtags.add("#feelthebern");
        hashtags.add("#yay");
        hashtags.add("#bob");
        Movement bob = new Movement("2", "Rally", "description", "steps", "resources", hashtags);
        movements.add(bob);
        movements.add(bob);

        ListView movementsList = (ListView) view.findViewById(R.id.hashtag_movements_list);
        MyMovementAdapter movementsAdapter = new MyMovementAdapter(getActivity(), movements);
        movementsList.setAdapter(movementsAdapter);

        ArrayList<Movement> followers = new ArrayList<>();

        Movement aUser = new Movement("2", "Rally", "description", "steps", "resources", new ArrayList<String>());
        followers.add(aUser);
        followers.add(aUser);

        ListView followerView = (ListView) view.findViewById(R.id.hashtag_followers_list);
        MyMovementAdapter followerAdapter = new MyMovementAdapter(getActivity(), followers);
        followerView.setAdapter(followerAdapter);

        //create buttons
        ImageButton backButton = (ImageButton) view.findViewById(R.id.backButtonHashtag);
        ImageButton followButton = (ImageButton) view.findViewById(R.id.followButtonHashtag);

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
            }
        });

        return view;
    }

//    public Hashtag searchFirebaseHashtag(String hashtagName){
//
//    }
}
