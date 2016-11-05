package erica.beakon;


import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseHandler {
    FirebaseDatabase db;
    DatabaseReference ref;

    public FirebaseHandler(FirebaseDatabase db, DatabaseReference ref) {
        this.ref = ref;
        this.db = db;
    }

    public void addUser(String user, String value) {
        ref.child(user).setValue(value);
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
