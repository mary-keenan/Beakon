package erica.beakon.Pages;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.ArrayList;

import erica.beakon.Adapters.UserPreferencesAdapter;
import erica.beakon.MainActivity;
import erica.beakon.Objects.User;
import erica.beakon.R;

/**
 * Created by mafaldaborges on 11/17/16.
 */
public class UserPreferencesPage extends Fragment {

    UserPreferencesAdapter userPreferencesAdapter;




    public UserPreferencesPage() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_preferences_page, container, false);

        adapter = new UserPreferencesAdapter()


        ImageButton addButton = (ImageButton) view.findViewById(R.id.add_user_preference);
        EditText newInterest = (EditText) view.findViewById(R.id.user_preferences_new);
        final String inputText = newInterest.getText().toString();



        addButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                ((MainActivity) getActivity()).currentUser.addHashtag(inputText);
                ((MainActivity) getActivity()).handler.updateUser(((MainActivity) getActivity()).currentUser);

            }
        });





        return view;
    }
}
