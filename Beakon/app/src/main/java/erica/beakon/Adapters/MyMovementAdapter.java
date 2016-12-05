package erica.beakon.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import erica.beakon.MainActivity;
import erica.beakon.Objects.Movement;
import erica.beakon.Objects.User;
import erica.beakon.Pages.ExpandedHashtagPage;
import erica.beakon.Pages.ExpandedMovementPage;
import erica.beakon.Pages.MyMovementsTab;
import erica.beakon.R;


public class MyMovementAdapter extends ArrayAdapter<Movement> {

    String hashtagName;
    ArrayList<Movement> movements;
    static final String TAG = "FIREBASE_HANDLER";
    String databaseURL = "https://beakon-5fa96.firebaseio.com/";
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference ref = db.getReferenceFromUrl(databaseURL);
    FirebaseHandler firebaseHandler = new FirebaseHandler(db,ref);
    User currentUser;

    public MyMovementAdapter(Context context, ArrayList<Movement> movements) {
        super(context, 0, movements);
        this.movements = movements;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Movement movement = getItem(position);

//        Log.d(TAG,movement.getId());

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.my_movement_item, parent, false);
        }

        final MainActivity activity = (MainActivity) getContext();

        currentUser = activity.getCurrentUser();

//        firebaseHandler.setMovementofUserStatus(currentUser,movement, true);

        if (movement == null){
            movement = new Movement("KXgdDeEWsCExk5yZMYZ", "Houston, we have a problem", "The movement here doesn't exist", "So we made a fake one", "but it's fake so watch out");
        }

        //set movement name, onclicklistener
        TextView movementNameView = (TextView) convertView.findViewById(R.id.movement_name);
        final CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.completed_box);
        movementNameView.setText(movement.getName());
        setOnClickMovement(movementNameView);

        firebaseHandler.getMovementofUserStatus(currentUser, movement, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    if (dataSnapshot.getValue().equals(true)) {
                        checkBox.setChecked(true);
                    } else if (dataSnapshot.getValue().equals(false)) {
                        checkBox.setChecked(false);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final Movement finalMovement = movement;
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentUser!= null) {
                    firebaseHandler.setMovementofUserStatus(currentUser, finalMovement, checkBox.isChecked());
                }
            }
        });

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
        if (hashtagList != null) {
            for (int i = 0; i < hashtagList.size(); i++) {
                hashtagName = hashtagList.get(i) + " "; //add space at end to shows diff between hashtags
                TextView hashtagTV = new TextView(getContext()); //create hashtag TV
                hashtagTV.setText(hashtagName); //set text of hashtag TV
                setOnClick(hashtagTV); //set on click listener
                hashtagTV.measure(0, 0); //measure hashtag TV dimensions
                int hashtagWidth = hashtagTV.getMeasuredWidth(); //save measured width in variable
                //add to existing row or make a new one based on length of string
                if (counter + hashtagWidth < rowWidth) { //if adding new hashtag won't go over the limit
                    hashtagRow.addView(hashtagTV); //add the TV to the row
                    counter += hashtagWidth; //update the counter
                } else { //if row would be too long with hashtag, put it in new row
                    hashtagTable.addView(hashtagRow, tableParams); //add row to table without adding new hashtag to row
                    counter = hashtagWidth; //start new counter with next hashtagWidth
                    hashtagRow = new TableRow(getContext()); //make new hashtag row
                    hashtagRow.addView(hashtagTV); //add hashtag TV to new row
                }
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
            Bundle bundle = new Bundle();
            bundle.putString("name", hashtagName); //give new fragment the hashtag it's expanding
            hashtagFragment.setArguments(bundle);
            ((MainActivity) getContext()).changeFragment(hashtagFragment); //changes fragments
            }
        });
    }

    private void setOnClickMovement(final TextView tv){
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String movementName = (String) tv.getText();
                ExpandedMovementPage movementFragment = new ExpandedMovementPage();
                Bundle bundle = new Bundle();
                bundle.putString("name", movementName); //give new fragment the hashtag it's expanding
                movementFragment.setArguments(bundle);
                ((MainActivity) getContext()).changeFragment(movementFragment); //changes fragments
            }
        });
    }

    public void add(Movement movement) {
        movements.add(movement);
        notifyDataSetChanged();
    }

//    public MainActivity

}
