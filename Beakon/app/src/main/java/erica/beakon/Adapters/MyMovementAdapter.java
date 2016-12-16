package erica.beakon.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import junit.framework.Test;

import java.util.ArrayList;
import java.util.HashMap;

import erica.beakon.MainActivity;
import erica.beakon.Objects.Movement;
import erica.beakon.Objects.User;
import erica.beakon.OnSwipeTouchListener;
import erica.beakon.Pages.ExpandedHashtagPage;
import erica.beakon.Pages.ExpandedMovementPage;
import erica.beakon.Pages.MyMovementsTab;
import erica.beakon.R;


public class MyMovementAdapter extends MovementAdapter {

    String databaseURL = "https://beakon-5fa96.firebaseio.com/";
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference ref = db.getReferenceFromUrl(databaseURL);
    FirebaseHandler firebaseHandler = new FirebaseHandler(db, ref);
    User currentUser;
    private long thisTime = 0;
    private long prevTime = 0;
    private boolean firstTap = true;
    protected static final long DOUBLE_CLICK_MAX_DELAY = 200L;

    public MyMovementAdapter(Context context, ArrayList<Movement> movements) {
        super(context, movements, R.layout.my_movement_item);

    }

    protected void setUpView(final MainActivity activity, final Movement movement, final View convertView, final int position) {
        currentUser = activity.getCurrentUser();

        Button deleteBtn = (Button) convertView.findViewById(R.id.deleteButton);

        //http://stackoverflow.com/questions/4804798/doubletap-in-android
        convertView.findViewById(R.id.card_view_layout).setOnTouchListener(new View.OnTouchListener() {

            private GestureDetector gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    if (currentUser!= null) {
                        if (currentUser.getMovements().get(movement.getId()).get("status") != true) {
                            Toast.makeText(activity, "Completed!", Toast.LENGTH_SHORT).show();
                            firebaseHandler.setMovementofUserStatus(currentUser, movement, true);
                            currentUser.updateMovements(movement.getId(), true);
                            notifyDataSetChanged();
                        } else {
                            Toast.makeText(activity, "Not completed.", Toast.LENGTH_SHORT).show();
                            firebaseHandler.setMovementofUserStatus(currentUser, movement, false);
                            currentUser.updateMovements(movement.getId(), false);
                            notifyDataSetChanged();
                        }
                    }
                    return super.onDoubleTap(e);
                }
                // implement here other callback methods like onFling, onScroll as necessary
            });

            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                Log.d("TEST", "Raw event: " + event.getAction() + ", (" + event.getRawX() + ", " + event.getRawY() + ")");
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });

//        convertView.findViewById(R.id.card_view_layout).setOnTouchListener(new OnSwipeTouchListener(activity) {
//
//            public void onSwipeRight() {
//                Toast.makeText(activity, "Completed!", Toast.LENGTH_SHORT).show();
//                if (currentUser!= null) {
//                    firebaseHandler.setMovementofUserStatus(currentUser, movement, true);
//                    currentUser.updateMovements(movement.getId(), true);
//                    notifyDataSetChanged();
////                    setViewtoCheckedStyle(convertView, checkBox);
//                }
//            }
//            public void onSwipeLeft() {
//                Toast.makeText(activity, "Not completed.", Toast.LENGTH_SHORT).show();
//                firebaseHandler.setMovementofUserStatus(currentUser, movement, false);
//                currentUser.updateMovements(movement.getId(), false);
//                notifyDataSetChanged();
////                setViewtoUncheckedStyle(convertView, checkBox);
//            }
//        });

        firebaseHandler.getMovementofUserStatus(currentUser, movement, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    if (dataSnapshot.getValue().equals(true)) {
                        setViewtoCheckedStyle(convertView);
                    } else if (dataSnapshot.getValue().equals(false)) {
                        setViewtoUncheckedStyle(convertView);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });


        //final Movement finalMovement = movement;
//        checkBox.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (currentUser!= null) {
//                    firebaseHandler.setMovementofUserStatus(currentUser, movement, checkBox.isChecked());
//                    currentUser.updateMovements(movement.getId(), checkBox.isChecked());
//                    notifyDataSetChanged();
//                    if (checkBox.isChecked()) {
//                        setViewtoCheckedStyle(convertView, checkBox);
//                    } else {
//                        setViewtoUncheckedStyle(convertView, checkBox);
//                    }
//                }
//            }
//        });

        //final Movement finalMovement2 = movement;
        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                firebaseHandler.removeUserfromMovement(currentUser, movement);
                movements.remove(movement);
                activity.firebaseHandler.removeUserfromMovement(activity.currentUser, movement);
                notifyDataSetChanged();
            }
        });


    }

    private void setViewtoCheckedStyle(View view) {
        view.findViewById(R.id.card_view_layout).setBackgroundColor(Color.parseColor("#cccccc"));
        view.findViewById(R.id.deleteButton).setBackgroundColor(Color.parseColor("#aaaaaa"));
    }

    private void setViewtoUncheckedStyle(View view) {
        view.findViewById(R.id.card_view_layout).setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAccentLight));
        view.findViewById(R.id.deleteButton).setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
    }

    public void add(Movement movement) {
        movements.add(movement);
        notifyDataSetChanged();
    }
}