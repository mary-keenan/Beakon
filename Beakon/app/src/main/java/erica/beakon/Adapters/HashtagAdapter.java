package erica.beakon.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import erica.beakon.MainActivity;
import erica.beakon.Pages.ExpandedHashtagPage;
import erica.beakon.R;

/**
 * Created by mary on 11/16/16.
 */
public class HashtagAdapter extends ArrayAdapter<String> {

    public HashtagAdapter(Context context, ArrayList<String> hashtags) {
        super(context, 0, hashtags);
    }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            final String hashtag = getItem(position);

            if (view == null) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.hashtag_item, parent, false);
            }

            final TextView hashtagName = (TextView) view.findViewById(R.id.hashtag_name);
            hashtagName.setText(hashtag);

            //if hashtag is clicked, go to ExpandedHashtagsPage fragment
            hashtagName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ExpandedHashtagPage hashtagFragment = new ExpandedHashtagPage();
                    hashtagFragment.setHashtag((String) hashtagName.getText()); //give it the hashtag it's expanding
                    ((MainActivity) getContext()).changeFragment(hashtagFragment); //changes fragments
                }
            });

            return view;
        }
    }
