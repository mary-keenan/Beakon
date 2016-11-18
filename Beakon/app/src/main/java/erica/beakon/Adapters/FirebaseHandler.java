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


import android.location.Location;
import android.util.Log;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.firebase.geofire.LocationCallback;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import erica.beakon.LoginPage;
import erica.beakon.Objects.Hashtag;
import erica.beakon.Objects.Movement;
import erica.beakon.Objects.User;


public class FirebaseHandler {

    static final String TAG = "FIREBASE_HANDLER";
    FirebaseDatabase db;
    DatabaseReference ref;
    LoginPage loginPage;


    public FirebaseHandler(FirebaseDatabase db, DatabaseReference ref) {
        this.ref = ref;
        this.db = db;
    }

    private GeoFire getGeoFire() {
        DatabaseReference geoRef = ref.child("GeoFire");
        return new GeoFire(geoRef);
    }

    public void addUser(String name, String email) {
        DatabaseReference userRef = ref.child("Users").push();
        String userId = userRef.getKey();
        User user = new User(userId, name, email);
        ref.child("Users").child(userId).setValue(user);
    }

    public void addUser(String name, String email, ArrayList<String> hashtagList) {
//        DatabaseReference userRef = ref.child("Users").push();
//        String userId = userRef.getKey();
        String userId = loginPage.getCurrentUserID();
        User user = new User(userId, name, email, hashtagList);
        ref.child("Users").child(userId).setValue(user);
    }

    public void setUserGeoLocation(User user, Location location) {
        getGeoFire().setLocation(user.getId(), new GeoLocation(location.getLatitude(), location.getLongitude()), new GeoFire.CompletionListener() {
            @Override
            public void onComplete(String key, DatabaseError error) {
                if (error != null) {
                    Log.e(TAG, "There was an error saving the location to GeoFire: " + error);
                } else {
                    Log.d(TAG, "Location saved on server successfully!");
                }
            }
        });
    }

    public void getUserGeoLocation(User user, LocationCallback callback) {
        getGeoFire().getLocation(user.getId(), callback);
    }

    public void getNearbyUsers(GeoLocation location, GeoQueryEventListener listener) {
        GeoQuery geoQuery = getGeoFire().queryAtLocation(location, 0.6);
        geoQuery.addGeoQueryEventListener(listener);
    }

    public void addMovement(String name, String description, String steps, String resources) {
        DatabaseReference movementRef = ref.child("Movements").push();
        String movementId = movementRef.getKey();
        Movement movement = new Movement(movementId, name, description, steps, resources);
        ref.child("Movements").child(movementId).setValue(movement);
    }

    public void addMovement(String name, String description, String steps, String resources, ArrayList<String> hashtagList) {
        DatabaseReference movementRef = ref.child("Movements").push();
        String movementId = movementRef.getKey();
        Movement movement = new Movement(movementId, name, description, steps, resources, hashtagList);
        ref.child("Movements").child(movementId).setValue(movement);
    }

//    public void addMovementtoHashtag(String name, ArrayList<Movement> movementList, ArrayList<User> userList) {
//        Hashtag hashtag = new Hashtag(name, movementList, userList);
//        ref.child("Hashtags").child(hashtag.getName()).setValue(hashtag);
//    }

//    public void getData(final long id, ValueEventListener listener) {
////        final User[] user = new User[1];
//        Query dataRef = ref.orderByChild("id").equalTo(id).getRef();

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

    public void updateUser(User user) {
        ref.child("Users").child(user.getId()).setValue(user);
    }

    public void updateMovement(Movement movement) {
        ref.child("Movements").child(movement.getId()).setValue(movement);
    }

}



