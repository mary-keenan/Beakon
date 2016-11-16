package erica.beakon.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

import erica.beakon.MainActivity;
import erica.beakon.Objects.Movement;
import erica.beakon.Pages.ExpandedHashtagPage;
import erica.beakon.R;

/**
 * Created by Cecelia on 11/6/16.
 */

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

        TableRow.LayoutParams tableParams = new TableRow.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT,1.0f);
        TableLayout hashtagTable = (TableLayout) convertView.findViewById(R.id.hashtag_table);

        ArrayList<String> hashtagList = movement.getHashtagList();
        TableRow hashtagRow = new TableRow(getContext());

        movementNameView.measure(0,0);
        Log.d("TV-WIDTH_____", String.valueOf(movementNameView.getMeasuredWidth()));

        //initialize some variables here
        int rowWidth = 800; //placeholder value
        int counter = 0; //keep track of number of characters in row

        for (int i = 0; i < hashtagList.size(); i++){ //loop through hashtag list
            String hashtagText = hashtagList.get(i) + " "; //add space at end to shows diff between hashtags
            TextView hashtagTV = new TextView(getContext()); //create hashtag TV
            hashtagTV.setText(hashtagText); //set text of hashtag TV
            hashtagTV.measure(0,0); //measure hashtag TV dimensions
            int hashtagWidth = hashtagTV.getMeasuredWidth(); //save measured width in variable
            //add to existing row or make a new one based on length of string
            if (counter + hashtagWidth < rowWidth) { //if adding new hashtag won't go over the limit
                hashtagRow.addView(hashtagTV); //add the TV to the row
                counter+= hashtagWidth; //update the counter
            }
            else { //if row would be too long with hashtag
                hashtagTable.addView(hashtagRow, tableParams); //add row to table without adding new hashtag to row
                counter = hashtagWidth; //start new counter with next hashtagWidth
                hashtagRow = new TableRow(getContext()); //make new hashtag row
                hashtagRow.addView(hashtagTV); //add hashtag TV to new row
            }
        }
        hashtagTable.addView(hashtagRow, tableParams); //add last row to table so we don't leave a row behind


        //if hashtag is clicked, go to ExpandedHashtagsPage fragment
//        final TextView hashtagsView = (TextView) convertView.findViewById(R.id.hashtag_names);
//        final ListView hashtagView = (ListView) convertView.findViewById(R.id.hashtag_list);
//        HashtagAdapter hashtagAdapter = new HashtagAdapter(getContext(), movement.getHashtagList());
//        hashtagView.setAdapter(hashtagAdapter);

        return convertView;
    }
}
