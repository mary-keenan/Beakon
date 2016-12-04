package erica.beakon.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import erica.beakon.MainActivity;
import erica.beakon.Objects.User;
import erica.beakon.Pages.ExpandedHashtagPage;
import erica.beakon.R;

/**
 * Created by mafaldaborges on 11/17/16.
 */
public class UserPreferencesAdapter extends ArrayAdapter<String> {

    public UserPreferencesAdapter(Context context, ArrayList<String> hashtag) {
        super(context, 0, hashtag);
    }


    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        final String hashtag = getItem(position);

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.user_preferences_item, parent, false);
        }

        final MainActivity activity = (MainActivity)getContext();


        final TextView userPreferenceString = (TextView) view.findViewById(R.id.user_preferences_string);
        userPreferenceString.setText(hashtag);

        Button deleteHashtag = (Button) view.findViewById(R.id.delete_user_preference);

        deleteHashtag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.currentUser.removeHashtag(hashtag);
                activity.firebaseHandler.updateUser(activity.currentUser);
            }
        });



        return view;
    }
}
