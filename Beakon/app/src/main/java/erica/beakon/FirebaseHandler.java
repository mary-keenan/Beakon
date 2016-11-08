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

    public void addUser(User user) {
        ref.child("Users").child(String.valueOf(user.getId())).setValue(user);
    }

    public void addMovement(Movement movement) {
        ref.child("Movements").child(String.valueOf(movement.getId())).setValue(movement);


    }

//    public User getUser(long id){
//        User user = ref.child("Users").child(String.valueOf(id)).listener;
//        Log.d("***", user.toString());
//        return user;
//    }
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

