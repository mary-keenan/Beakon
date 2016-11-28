package erica.beakon.Adapters;

import erica.beakon.Objects.Movement;
import erica.beakon.MainActivity;
import erica.beakon.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class RecommendedMovementsAdapter extends ArrayAdapter<Movement> {

    public RecommendedMovementsAdapter(Context context, ArrayList<Movement> movements) {
        super(context, 0, movements);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Movement movement = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.card_item, parent, false);
        }

        TextView movementNameView = (TextView) convertView.findViewById(R.id.card_movement_name);
        movementNameView.setText(movement.getName());

        final MainActivity activity = (MainActivity)getContext();

        final Button join = (Button) convertView.findViewById(R.id.join);

        if (activity.currentUser.getMovements().contains(movement.getId())) {
            join.setText("Leave");
        }

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(join.getText().equals(getContext().getString(R.string.join))) {
                    activity.firebaseHandler.addUsertoMovement(activity.currentUser, movement);
                    join.setText(R.string.leave);
                } else if (join.getText().equals(getContext().getString(R.string.leave))) {
                    activity.firebaseHandler.removeUserfromMovement(activity.currentUser, movement);
                    join.setText(R.string.join);
                }
            }
        });

        return convertView;
    }


}
