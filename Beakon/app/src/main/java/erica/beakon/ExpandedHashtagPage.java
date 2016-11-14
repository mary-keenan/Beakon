package erica.beakon;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;


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

        return view;
    }
}
