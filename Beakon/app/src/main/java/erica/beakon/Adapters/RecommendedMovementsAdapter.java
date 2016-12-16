package erica.beakon.Adapters;

import erica.beakon.Objects.Movement;
import erica.beakon.MainActivity;
import erica.beakon.Pages.ExpandedHashtagPage;
import erica.beakon.Pages.ExpandedMovementPage;
import erica.beakon.R;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class RecommendedMovementsAdapter extends MovementAdapter {

    public RecommendedMovementsAdapter(Context context, ArrayList<Movement> movements) {
        super(context, movements, R.layout.recommended_movement_item);
    }

    protected void setUpView(final MainActivity activity,final Movement movement, View convertView, final int position) {
        final Button join = (Button) convertView.findViewById(R.id.join);

        if (activity.currentUser.getMovements().keySet().contains(movement.getId())) {
            //rejectBtn.setColorFilter(Color.argb(tintColor, tintColor, tintColor, tintColor)); // White Tint) {
            movement.joined = true;
        }

        join.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View currentView) {
                activity.firebaseHandler.addUsertoMovement(activity.currentUser, movement);

                //make sure that only the add button on the current element disappears
                if(previousView!=null){
                    Movement previousMovement = movements.get(previousPosition);
                    previousMovement.joined = false;
                }

                movement.joined = true;
                previousView = currentView;
                previousPosition = position;
                notifyDataSetChanged();
            }

        });

        if(movement.joined){
            join.setVisibility(View.GONE);
        } else {
            join.setVisibility(View.VISIBLE);
        }
    }

    // Not used

    //basically the HashtagAdapter, but since I'm using a Table Layout I did it differently (i.e. this instead)
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
}
