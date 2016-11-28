package erica.beakon.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import erica.beakon.Objects.Movement;
import erica.beakon.Objects.User;
import erica.beakon.R;

/**
 * Created by mary on 11/27/16.
 */
public class FollowerAdapter extends ArrayAdapter<User> {

    ArrayList<User> followerList;

    public FollowerAdapter(Context context, ArrayList<User> followerList) {
        super(context, 0, followerList);
        this.followerList = followerList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final User follower = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.hashtag_item, parent, false);
        }

        //set movement name
        TextView movementNameView = (TextView) convertView.findViewById(R.id.hashtag_name);
        movementNameView.setText(follower.getName());

        return convertView;
    }

    public void add(User follower) {
        followerList.add(follower);
        notifyDataSetChanged();
    }
}
