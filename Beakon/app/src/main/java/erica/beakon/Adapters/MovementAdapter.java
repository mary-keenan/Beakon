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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public abstract class MovementAdapter extends ArrayAdapter<Movement> {

    protected ArrayList<Movement> movements;
    protected View previousView;
    protected int previousPosition;
    protected int layout;

    public MovementAdapter(Context context, ArrayList<Movement> mvs, int layout) {
        super(context, 0, mvs);
        movements = mvs;
        previousView = null;
        previousPosition = -1;
        this.layout = layout;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Movement movement = getItem(position);
        final MainActivity activity = (MainActivity)getContext();

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(layout, parent, false);
        }

        TextView movementNameView = (TextView) convertView.findViewById(R.id.movement_name);
        movementNameView.setText(movement.getName());
        setOnClickMovement(movementNameView, movement);

        ArrayList<String> hashtagList = movement.getHashtagList();
        LinearLayout hashtagLayout = (LinearLayout) convertView.findViewById(R.id.hashtag_layout);
        if (hashtagLayout.getChildCount() == 0) {
            if (hashtagList != null) {
                for (String hashtag: hashtagList) {
                    //add the hashtag to the view and update totalWidth with the new value
                    addHashtagtoView(hashtag, hashtagLayout);
                }
            }
        }

        setUpView(activity, movement, convertView, position);

        return convertView;
    }

    abstract void setUpView(MainActivity activity, Movement movement, View convertView, int position);

    private void setOnClickMovement(final TextView tv, final Movement movement){
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExpandedMovementPage movementFragment = new ExpandedMovementPage();
                Bundle bundle = new Bundle();
                bundle.putString("name", movement.getName()); //give new fragment the hashtag it's expanding
                bundle.putString("ID", movement.getId()); //give new fragment the hashtag it's expanding
                movementFragment.setArguments(bundle);
                ((MainActivity) getContext()).changeFragment(movementFragment, "expandedMovementPage"); //changes fragments
            }
        });
    }

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
