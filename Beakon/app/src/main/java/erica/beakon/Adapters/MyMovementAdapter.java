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

    String hashtagName;

    public MyMovementAdapter(Context context, ArrayList<Movement> movements) {
        super(context, 0, movements);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Movement movement = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.my_movement_item, parent, false);
        }

        //set movement name
        TextView movementNameView = (TextView) convertView.findViewById(R.id.movement_name);
        movementNameView.setText(movement.getName());
        //create the hashtag table and first row
        TableRow.LayoutParams tableParams = new TableRow.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT,1.0f);
        TableLayout hashtagTable = (TableLayout) convertView.findViewById(R.id.hashtag_table);
        TableRow hashtagRow = new TableRow(getContext());
        //get list of hashtags from movement
        ArrayList<String> hashtagList = movement.getHashtagList();
        //initialize some variables here
        int rowWidth = 800; //placeholder value -- ideally, we'd programatically find out view width since it's dynamic
        int counter = 0; //keep track of number of characters in row

        //loop through hashtag list, put them in rows, set onClickListeners, etc
        for (int i = 0; i < hashtagList.size(); i++){
            hashtagName = hashtagList.get(i) + " "; //add space at end to shows diff between hashtags
            TextView hashtagTV = new TextView(getContext()); //create hashtag TV
            hashtagTV.setText(hashtagName); //set text of hashtag TV
            setOnClick(hashtagTV); //set on click listener
            hashtagTV.measure(0,0); //measure hashtag TV dimensions
            int hashtagWidth = hashtagTV.getMeasuredWidth(); //save measured width in variable
            //add to existing row or make a new one based on length of string
            if (counter + hashtagWidth < rowWidth) { //if adding new hashtag won't go over the limit
                hashtagRow.addView(hashtagTV); //add the TV to the row
                counter+= hashtagWidth; //update the counter
            }
            else { //if row would be too long with hashtag, put it in new row
                hashtagTable.addView(hashtagRow, tableParams); //add row to table without adding new hashtag to row
                counter = hashtagWidth; //start new counter with next hashtagWidth
                hashtagRow = new TableRow(getContext()); //make new hashtag row
                hashtagRow.addView(hashtagTV); //add hashtag TV to new row
            }
        }
        hashtagTable.addView(hashtagRow, tableParams); //add last row to table so we don't leave a row behind
        return convertView;
    }

    //basically the HashtagAdapter, but since I'm using a Table Layout I did it differently (i.e. this instead)
    private void setOnClick(final TextView tv){
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hashtagName = (String) tv.getText();
                ExpandedHashtagPage hashtagFragment = new ExpandedHashtagPage();
                hashtagFragment.setHashtag(hashtagName); //give it the hashtag it's expanding
                ((MainActivity) getContext()).changeFragment(hashtagFragment); //changes fragments
            }
        });
    }
}
