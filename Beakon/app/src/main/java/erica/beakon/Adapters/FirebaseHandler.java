//package erica.beakon.Adapters;
//
//
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.Query;
//import com.google.firebase.database.ValueEventListener;
//
//import erica.beakon.Objects.Movement;
//import erica.beakon.Objects.User;
//import erica.beakon.Objects.UserMovement;
//
//
//public class FirebaseHandler {
//    FirebaseDatabase db;
//    DatabaseReference ref;
//
//
//    public FirebaseHandler(FirebaseDatabase db, DatabaseReference ref) {
//        this.ref = ref;
//        this.db = db;
//    }
//
//    public void addUser(String name, String email) {
//        DatabaseReference userRef = ref.child("Users").push();
//        String userId = userRef.getKey();
//        User user = new User(userId, name, email);
//        ref.child("Users").child(userId).setValue(user);
//
//    }
//
//    public void addMovement(String name, String description, String steps, String resources) {
//        DatabaseReference movementRef = ref.child("Movements").push();
//        String movementId = movementRef.getKey();
//        Movement movement = new Movement(movementId, name, description, steps, resources);
//        ref.child("Movements").child(movementId).setValue(movement);
//    }
//
//    public void getById(final long id, ValueEventListener listener) {
////        final User[] user = new User[1];
//        Query dataRef = ref.orderByChild("id").equalTo(id).getRef();
//        dataRef.addListenerForSingleValueEvent(listener);
////                @Override
////                public void onDataChange(DataSnapshot dataSnapshot) {
////                    user[0] = dataSnapshot.child("Users").child(String.valueOf(id)).getValue(User.class);
////                    Log.d("*****", user[0].getName());
////                }
////
////                @Override
////                public void onCancelled(DatabaseError databaseError) {
////
////                }
//    }
//
//    public void addUsertoMovement(User user, Movement movement) {
//        DatabaseReference umRef = ref.child("UserMovements").push();
//        String umId = umRef.getKey();
//        UserMovement userMovement = new UserMovement(umId, false, user, movement);
//        ref.child("UserMovements").child(umId).setValue(userMovement);
//    }
//
//    public void completeUserMovement(String umId) {
//        ref.child("UserMovements").child(umId).child("completed").setValue(true);
//    }
//
//    public void removeUserfromMovement(String umId) {
//        ref.child("UserMovements").child(umId).removeValue();
//    }
//
//}
//
//
//

package erica.beakon.Adapters;


import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import erica.beakon.Objects.Hashtag;
import erica.beakon.Objects.Movement;
import erica.beakon.Objects.User;


public class FirebaseHandler {
    FirebaseDatabase db;
    DatabaseReference ref;


    public FirebaseHandler(FirebaseDatabase db, DatabaseReference ref) {
        this.ref = ref;
        this.db = db;
    }

    public User addUser(String name, String email, ArrayList<String> hashtagList) {
        DatabaseReference userRef = ref.child("Users").push();
        String userId = userRef.getKey();
        User user = new User(userId, name, email, hashtagList);
        ref.child("Users").child(userId).setValue(user);
        return user;
    }

    public Movement addMovement(String name, String description, String steps, String resources, ArrayList<String> hashtagList) {
        DatabaseReference movementRef = ref.child("Movements").push();
        String movementId = movementRef.getKey();
        Movement movement = new Movement(movementId, name, description, steps, resources, hashtagList);
        ref.child("Movements").child(movementId).setValue(movement);
        return movement;
    }

    public void addHashtag(String name, ArrayList<String> movementList, ArrayList<String> userList) {
        Hashtag hashtag = new Hashtag(name, movementList, userList);
        ref.child("Hashtags").child(hashtag.getName()).setValue(hashtag);
    }

    public void getById(String id, ValueEventListener listener) {
        Query dataRef;
        if (id == null) {
            dataRef = ref.orderByChild("id").getRef();
        } else {
            dataRef = ref.orderByChild("id").equalTo(id).getRef();
        }

        dataRef.addListenerForSingleValueEvent(listener);
    }

    public void getMovements(ChildEventListener listener) {
        ref.child("Movements").addChildEventListener(listener);
    }

    public void getUserChild(String id, String child, ChildEventListener listener) {
        Query dataRef = ref.child("Users").child(id).child(child);
        dataRef.addChildEventListener(listener);
    }

    public void getUser(String id, ValueEventListener listener) {
        Query dataRef = ref.child("Users").child(id);
        dataRef.addValueEventListener(listener);
    }

    public void getMovement(String id, ValueEventListener listener) {
        Query dataRef = ref.child("Movements").child(id);
        dataRef.addValueEventListener(listener);
    }

    public void getHashtag(String name, ValueEventListener listener){
        DatabaseReference dataRef = ref.child("Hashtags").child(name);
        dataRef.addValueEventListener(listener);
    }

    public void addUsertoMovement(User user, Movement movement) {
        user.addMovement(movement);
        movement.addUser(user);
        updateUser(user);
        updateMovement(movement);
    }

    public void removeUserfromMovement(User user, Movement movement) {
        user.removeMovement(movement);
        movement.removeUser(user);
        updateMovement(movement);
        updateUser(user);
    }

    public void addUsertoHashtag(User user, Hashtag hashtag) {
        user.addHashtag(hashtag.getName());
        hashtag.addUser(user);
        updateUser(user);
        updateHashtag(hashtag);
    }

    public void addMovementtoHashtag(Movement movement, Hashtag hashtag) {
        movement.addHashtag(hashtag.getName());
        hashtag.addMovement(movement);
        updateMovement(movement);
        updateHashtag(hashtag);
    }

    public void removeUserfromHashtag(User user, Hashtag hashtag) {
        user.removeHashtag(hashtag.getName());
        hashtag.removeUser(user);
        updateHashtag(hashtag);
        updateUser(user);
    }

    public void removeMovementfromHashtag(Movement movement, Hashtag hashtag) {
        movement.removeHashtag(hashtag.getName());
        hashtag.removeMovement(movement);
        updateHashtag(hashtag);
        updateMovement(movement);
    }

    public void updateUser(User user) {
        ref.child("Users").child(user.getId()).setValue(user);
    }

    public void updateMovement(Movement movement) {
        ref.child("Movements").child(movement.getId()).setValue(movement);
    }

    public void updateHashtag(Hashtag hashtag) {
        ref.child("Hashtags").child(hashtag.getName()).setValue(hashtag);
    }

    public void getBatchHashtags(ArrayList<String> hashtagBatch, ValueEventListener listener){ // for adding new movements
        for (int i = 0; i < hashtagBatch.size(); i++) {
            DatabaseReference dataRef = ref.child("Hashtags").child(hashtagBatch.get(i));
            dataRef.addListenerForSingleValueEvent(listener);
        }
    }

    public void getBatchMovements(ArrayList<String> movementBatch, ValueEventListener listener){ // for expanded hashtag page
        for (int i = 0; i < movementBatch.size(); i++) {
            DatabaseReference dataRef = ref.child("Movements").child(movementBatch.get(i));
            dataRef.addListenerForSingleValueEvent(listener);
        }
    }

    public void getBatchUsers(ArrayList<String> userBatch, ValueEventListener listener){ // for expanded hashtag and users page
        for (int i = 0; i < userBatch.size(); i++) {
            DatabaseReference dataRef = ref.child("Users").child(userBatch.get(i));
            dataRef.addListenerForSingleValueEvent(listener);
        }
    }
}



