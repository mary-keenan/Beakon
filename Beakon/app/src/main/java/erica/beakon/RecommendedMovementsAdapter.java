package erica.beakon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by erica on 11/7/16.
 */
public class RecommendedMovementsAdapter extends ArrayAdapter<String> {

    public RecommendedMovementsAdapter(Context context, ArrayList<String> movements) {
        super(context, 0, movements);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final String movement = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.card_item, parent, false);
        }

        TextView movementNameView = (TextView) convertView.findViewById(R.id.card_movement_name);
        movementNameView.setText(movement);

        return convertView;
    }
}
