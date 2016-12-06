package erica.beakon.Pages;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.GenericSignatureFormatError;
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


        final MainActivity activity = (MainActivity) getActivity();
        currentUser = activity.currentUser;

        hashtagList = new ArrayList<String>();


        final ListView lv = (ListView) view.findViewById(R.id.listView);
        userPreferencesAdapter = new UserPreferencesAdapter(getActivity(),hashtagList);
        lv.setAdapter(userPreferencesAdapter);

        activity.firebaseHandler.getHashtagsfromUser(currentUser, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null){
                    hashtagList.clear();
                    ArrayList<Object> list = dataSnapshot.getValue(new GenericTypeIndicator<ArrayList<Object>>() {
                        @Override
                        public String toString() {
                            return super.toString();
                        }
                    });
                    for(Object hash : list) {
                        hashtagList.add(hash.toString());
                        Log.d("***", hashtagList.toString());
                        userPreferencesAdapter.notifyDataSetChanged();
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        ImageButton addButton = (ImageButton) view.findViewById(R.id.add_user_preference);
        final EditText newInterest = (EditText) view.findViewById(R.id.user_preferences_new);



        addButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String inputText = newInterest.getText().toString();
                newInterest.getText().clear();
                Log.d("UserPref", inputText);
                hashtagList.add(inputText);
                currentUser = new User(currentUser.getId(),currentUser.getName(),hashtagList);
                activity.firebaseHandler.updateUser(currentUser);
                userPreferencesAdapter.notifyDataSetChanged();

                View view = getActivity().getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)(getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
//                ((MainActivity) getActivity()).currentUser.addHashtag(inputText);
//                ((MainActivity) getActivity()).firebaseHandler.updateUser(((MainActivity) getActivity()).currentUser);

            }
        });





        return view;
    }
}
