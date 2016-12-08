package erica.beakon.Pages;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import erica.beakon.LoginPage;
import erica.beakon.MainActivity;
import erica.beakon.Objects.Movement;
import erica.beakon.R;


abstract public class MovementsTab extends Fragment {

    static final String SETTINGS_TITLE = "Settings";
    ArrayList<Movement> movements;
    ImageButton menuButton;
    View view;

    public MovementsTab() {
        movements = new ArrayList<>();
        // Required empty public constructor
    }

    protected void setUpChangeFragmentsButton(View view, final Fragment fragment, int nextFragmentButtonId) {
        //create buttons
//        final Button myMovementsButton = (Button) view.findViewById(R.id.my_movements);
        final Button tabChangeButton = (Button) view.findViewById(nextFragmentButtonId);

        //set background colors of buttons -- can just hardcode color now
//        myMovementsButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorBackground));
//        suggestedMovementsButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAccentLight));

        tabChangeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((MainActivity) getActivity()).changeFragment(fragment, "TabSwitch"); //should change to SuggestedMovements page
            }
        });
    }

    protected void removeMovement(int index) {
        this.movements.remove(index);
    }

    protected void removeMovement(Movement movement) {
        this.movements.remove(movement);
    }

    protected void getMovement(String id) {
        if (isAdded()) {
            getMainActivity().firebaseHandler.getMovement(id, getMovementAddedValueEventListener());
        }
    }

    abstract ValueEventListener getMovementAddedValueEventListener();

    abstract ChildEventListener populateMovementsEventListener();

    protected MainActivity getMainActivity() {
        return ((MainActivity)getActivity());
    }

    protected Movement getMovementById(String id) {
        for (Movement m: movements) {
            if (m != null){
                if (m.getId().equals(id)) {
                    return m;
                }
            }
        }
        return null;
//        throw new NullPointerException("No movement exists with that id in nearby movements");
    }

    protected void setUpAddButton() {
        ImageButton addMovementBtn = (ImageButton) view.findViewById(R.id.goto_add_movement_btn);
        addMovementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).changeFragment(new AddMovementPage(), "AddMovementPage");
            }
        });
    }
    protected void setMenuButtonOnClickListener(final int currentTab) {
        menuButton = (ImageButton) view.findViewById(R.id.menu_button);
        final Intent intent = new Intent(getActivity(), LoginPage.class);
        menuButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                PopupMenu popup = new PopupMenu(getActivity(), menuButton);
                popup.getMenuInflater().inflate(R.menu.popupmenu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(getActivity(),"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
                        if (item.getTitle().equals(MovementsTab.SETTINGS_TITLE)) {
                            android.support.v4.app.Fragment UserPref = new UserPreferencesPage();
                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            transaction.replace(currentTab, UserPref);
                            transaction.commit();
                        } else {
                            LoginManager.getInstance().logOut();
                            startActivity(intent);
                        }
                        return true;
                    }
                });

                popup.show();//showing popup menu
            }
        });
    }

}
