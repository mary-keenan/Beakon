package erica.beakon.Pages;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import erica.beakon.Adapters.FirebaseHandler;
import erica.beakon.Adapters.UserPreferencesAdapter;
import erica.beakon.MainActivity;
import erica.beakon.Objects.User;
import erica.beakon.R;

/**
 * Created by mafaldaborges on 11/17/16.
 */
public class UserPreferencesPage extends android.support.v4.app.Fragment {


    UserPreferencesAdapter userPreferencesAdapter;
    ArrayList<String> hashtagList;
    User currentUser;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_preferences_page, container, false);
        view.setBackgroundColor(Color.parseColor("#ffffff"));

        hashtagList = new ArrayList<String>();

        final MainActivity activity = (MainActivity) getActivity();
        currentUser = activity.currentUser;

        activity.firebaseHandler.getHashtagsfromUser(currentUser, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue(ArrayList.class) != null){
                    for(String hash : dataSnapshot.getValue(ArrayList.class)) {
                        hashtagList.add(hash);

                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        userPreferencesAdapter = new UserPreferencesAdapter(getActivity(),hashtagList);


        ImageButton addButton = (ImageButton) view.findViewById(R.id.add_user_preference);
        final EditText newInterest = (EditText) view.findViewById(R.id.user_preferences_new);



        addButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String inputText = newInterest.getText().toString();
                Log.d("UserPref", inputText);
                hashtagList.add(inputText);
                currentUser = new User(currentUser.getId(),currentUser.getName(),hashtagList);
                activity.firebaseHandler.updateUser(currentUser);
//                ((MainActivity) getActivity()).currentUser.addHashtag(inputText);
//                ((MainActivity) getActivity()).firebaseHandler.updateUser(((MainActivity) getActivity()).currentUser);

            }
        });





        return view;
    }
}
