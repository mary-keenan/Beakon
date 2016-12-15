package erica.beakon.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

import erica.beakon.MainActivity;
import erica.beakon.Objects.Hashtag;
import erica.beakon.Objects.User;
import erica.beakon.Pages.ExpandedHashtagPage;
import erica.beakon.R;

/**
 * Created by mafaldaborges on 11/17/16.
 */
public class UserPreferencesAdapter extends ArrayAdapter<String> {

    public UserPreferencesAdapter(Context context, ArrayList<String> hashtagList) {
        super(context, 0, hashtagList);
    }


    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        final String hashtag = getItem(position);

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.user_preferences_item, parent, false);
        }
        final TextView userPreferenceString = (TextView) view.findViewById(R.id.user_preferences_string);
        userPreferenceString.setText(hashtag);

        ImageButton deleteHashtag = (ImageButton) view.findViewById(R.id.delete_user_preference);

        deleteHashtag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeInterest(hashtag);
            }
        });

        View wrapper = view.findViewById(R.id.interest_wrapper);

        userPreferenceString.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v) {
                String hashtagName = (String) ((TextView)v).getText();
                ExpandedHashtagPage hashtagFragment = new ExpandedHashtagPage();
                Bundle bundle = new Bundle();
                bundle.putString("name", hashtagName); //give new fragment the hashtag it's expanding
                hashtagFragment.setArguments(bundle);
                ((MainActivity) getContext()).changeFragment(hashtagFragment, "expandedHashtagPage"); //changes fragments
            }
        });

        return view;
    }

    private void removeInterest(String hashtag) {
        final MainActivity activity = (MainActivity)getContext();
        activity.firebaseHandler.getHashtagOnce(hashtag, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                activity.firebaseHandler.removeUserfromHashtag(activity.currentUser, dataSnapshot.getValue(Hashtag.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        this.remove(hashtag);
        this.notifyDataSetChanged();
    }
}
