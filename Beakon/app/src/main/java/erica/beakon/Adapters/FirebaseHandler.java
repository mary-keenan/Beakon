package erica.beakon.Adapters;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import erica.beakon.Objects.Hashtag;
import erica.beakon.Objects.Movement;
import erica.beakon.Objects.User;
import erica.beakon.Objects.UserMovement;


public class FirebaseHandler {
    FirebaseDatabase db;
    DatabaseReference ref;


    public FirebaseHandler(FirebaseDatabase db, DatabaseReference ref) {
        this.ref = ref;
        this.db = db;
    }

    public void addUser(String name, String email, ArrayList<String> hashtagList) {
        DatabaseReference userRef = ref.child("Users").push();
        String userId = userRef.getKey();
        User user = new User(userId, name, email, hashtagList);
        ref.child("Users").child(userId).setValue(user);
    }

    public void addMovement(String name, String description, String steps, String resources, ArrayList<String> hashtagList) {
        DatabaseReference movementRef = ref.child("Movements").push();
        String movementId = movementRef.getKey();
        Movement movement = new Movement(movementId, name, description, steps, resources, hashtagList);
        ref.child("Movements").child(movementId).setValue(movement);
    }

    public void getData(final long id, ValueEventListener listener) {
//        final User[] user = new User[1];
        Query dataRef = ref.orderByChild("id").equalTo(id).getRef();
        dataRef.addListenerForSingleValueEvent(listener);
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    user[0] = dataSnapshot.child("Users").child(String.valueOf(id)).getValue(User.class);
//                    Log.d("*****", user[0].getName());
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
    }

    public void addUsertoMovement(User user, Movement movement) {
        DatabaseReference umRef = ref.child("UserMovements").push();
        String umId = umRef.getKey();
        UserMovement userMovement = new UserMovement(umId, false, user, movement);
        ref.child("UserMovements").child(umId).setValue(userMovement);
    }

    public void completeUserMovement(String umId) {
        ref.child("UserMovements").child(umId).child("completed").setValue(true);
    }

    public void removeUserfromMovement(String umId) {
        ref.child("UserMovements").child(umId).removeValue();
    }

}



