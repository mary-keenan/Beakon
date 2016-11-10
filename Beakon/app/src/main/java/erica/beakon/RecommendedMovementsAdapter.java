package erica.beakon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by erica on 11/7/16.
 */
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
        activity.handler.getData(null, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userMovementSnapshot: dataSnapshot.child("UserMovements").getChildren()){
                    if (userMovementSnapshot.child("movement").child("id").getValue().equals(movement.getId())) {
                        join.setText("Leave");
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(join.getText().equals("Join")) {
                    activity.handler.addUsertoMovement(activity.currentUser, movement);
                    join.setText("Leave");
                } else if (join.getText().equals("Leave")) {
                    activity.handler.removeUserfromMovement(activity.currentUser.getId());
                    join.setText("Join");
                }





            }
        });

        return convertView;
    }


}
