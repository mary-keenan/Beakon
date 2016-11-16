package erica.beakon.Pages;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import erica.beakon.MainActivity;
import erica.beakon.Adapters.MyMovementAdapter;
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

        ArrayList<String> hashtagMovements = new ArrayList<>();
        hashtagMovements.add("Partay");
        hashtagMovements.add("Prank");
        hashtagMovements.add("Sleep");

        ListView hashtagMovementsList = (ListView) view.findViewById(R.id.hashtag_movements_list);
        MyMovementAdapter hashtagMovementsAdapter = new MyMovementAdapter(getActivity(), hashtagMovements);
        hashtagMovementsList.setAdapter(hashtagMovementsAdapter);

        ArrayList<String> hashtagFollowers = new ArrayList<>();
        hashtagFollowers.add("Gary");
        hashtagFollowers.add("Cecelia");
        hashtagFollowers.add("Loki");

        ListView hashtagFollowersList = (ListView) view.findViewById(R.id.hashtag_followers_list);
        MyMovementAdapter hashtagFollowersAdapter = new MyMovementAdapter(getActivity(), hashtagFollowers);
        hashtagFollowersList.setAdapter(hashtagFollowersAdapter);

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
}
