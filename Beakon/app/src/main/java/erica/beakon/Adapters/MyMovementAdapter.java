package erica.beakon.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import erica.beakon.MainActivity;
import erica.beakon.Objects.Movement;
import erica.beakon.Pages.ExpandedHashtagPage;
import erica.beakon.R;


public class MyMovementAdapter extends ArrayAdapter<Movement> {

    public MyMovementAdapter(Context context, ArrayList<Movement> movements) {
        super(context, 0, movements);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Movement movement = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.my_movement_item, parent, false);
        }

        TextView movementNameView = (TextView) convertView.findViewById(R.id.movement_name);
        movementNameView.setText(movement.getName());

        //if hashtag is clicked, go to ExpandedHashtagsPage fragment
        final TextView hashtagsView = (TextView) convertView.findViewById(R.id.hashtag_names);
        hashtagsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExpandedHashtagPage hashtagFragment = new ExpandedHashtagPage();
                hashtagFragment.setHashtag((String) hashtagsView.getText()); //give it the hashtag it's expanding
                ((MainActivity) getContext()).changeFragment(hashtagFragment); //changes fragments
            }
        });

        return convertView;
    }
}
