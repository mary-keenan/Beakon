package erica.beakon.Adapters;

import erica.beakon.Objects.Movement;
import erica.beakon.MainActivity;
import erica.beakon.R;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class RecommendedMovementsAdapter extends ArrayAdapter<Movement> {

    private int tintColor = 220; //color we're tinting the X when the check mark is selected
    private ArrayList<Movement> movements;

    public RecommendedMovementsAdapter(Context context, ArrayList<Movement> movements) {
        super(context, 0, movements);
        this.movements = movements;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Movement movement = getItem(position);
        final MainActivity activity = (MainActivity)getContext();

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.card_item, parent, false);
        }

        TextView movementNameView = (TextView) convertView.findViewById(R.id.card_movement_name);
        movementNameView.setText(movement.getName());
        final ImageButton joinBtn = (ImageButton) convertView.findViewById(R.id.join_btn);
        final ImageButton rejectBtn = (ImageButton) convertView.findViewById(R.id.reject_btn);

        if (activity.currentUser.getMovements().contains(movement)) {
            rejectBtn.setColorFilter(Color.argb(tintColor, tintColor, tintColor, tintColor)); // White Tint

        }

        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rejectBtn.getColorFilter() == null){ //reject button hasn't been shaded yet (i.e. join isn't already selected)
                    activity.handler.addUsertoMovement(activity.currentUser, movement); //Todo: changes fragments rn for some reason
                    rejectBtn.setColorFilter(Color.argb(tintColor,tintColor,tintColor,tintColor)); //tint reject button
                }
                else { //reject button is already tinted, undoing addition
                    activity.handler.removeUserfromMovement(activity.currentUser, movement);
                    rejectBtn.setColorFilter(null); //undo tinted reject button
                }
            }
        });

        //Todo: Make it so it will permanently stop suggesting a deleted item
        rejectBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                movements.remove(movement);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }




}
