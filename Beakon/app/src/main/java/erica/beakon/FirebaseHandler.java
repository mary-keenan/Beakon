package erica.beakon;


import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class FirebaseHandler {
    FirebaseDatabase db;
    DatabaseReference ref;

    public FirebaseHandler(FirebaseDatabase db, DatabaseReference ref) {
        this.ref = ref;
        this.db = db;
    }

    public void addUser(HashMap<String,Person> user) {
        ref.child("bill").setValue(user);
    }

//    public void addMovement(Movement movement) {
//
//    }
//
    public DatabaseReference getUserRef(){
        DatabaseReference userRef = ref.child("user").getRef();
        Log.d("***", userRef.toString());
        return userRef;
    }
//
//    public DatabaseReference getMovementRef() {
//
//    }
//
//    public void addUsertoMovement(User user, Movement movement) {
//
//    }
//
//    public void removeUserfromMovement(User user, Movment movement) {
//
//    }

}

