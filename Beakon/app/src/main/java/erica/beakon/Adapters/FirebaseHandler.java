package erica.beakon.Adapters;


import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import android.location.Location;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
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
    // Not used

    String TAG = "FirebaseHandler";


    public FirebaseHandler(FirebaseDatabase db, DatabaseReference ref) {
        this.ref = ref;
        this.db = db;
    }
    private GeoFire getGeoFire() {
        DatabaseReference geoRef = ref.child("GeoFire");
        return new GeoFire(geoRef);
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

    // Not used

    public void getNearbyUsers(GeoLocation location, GeoQueryEventListener listener) {
        GeoQuery geoQuery = getGeoFire().queryAtLocation(location, 0.6);
        geoQuery.addGeoQueryEventListener(listener);
    }


    public void addUser(String fbId, String name) {
        // Delete commented-out code before
        User user = new User(fbId, name);
        ref.child("Users").child(fbId).setValue(user);
    }
    // This function is never used, delete it

    public Movement addMovement(String name, String description, String steps, String resources, ArrayList<String> hashtagList, ArrayList<String> followerList) {
        DatabaseReference movementRef = ref.child("Movements").push();
        String movementId = movementRef.getKey();
        Movement movement = new Movement(movementId, name, description, steps, resources, hashtagList, followerList);
        ref.child("Movements").child(movementId).setValue(movement);
        return movement;
    }

    public Hashtag addHashtag(String name, ArrayList<String> movementList, ArrayList<String> userList) {
        Hashtag hashtag = new Hashtag(name, movementList, userList);
        ref.child("Hashtags").child(hashtag.getName()).setValue(hashtag);
        return hashtag;
    }

    public Hashtag addHashtag(Hashtag hashtag) {
        ref.child("Hashtags").child(hashtag.getName()).setValue(hashtag);
        return hashtag;
    }

    // Not used

    public void getMovements(ChildEventListener listener) {
        ref.child("Movements").addChildEventListener(listener);
    }

    public void getUserChild(String id, String child, ValueEventListener listener) {
        Query dataRef = ref.child("Users").child(id).child(child);
        dataRef.addListenerForSingleValueEvent(listener);
    }

    public void getUser(String id, ValueEventListener listener) {
        Query dataRef = ref.child("Users").child(id);
        dataRef.addListenerForSingleValueEvent(listener);
    }

    public void getMovement(String id, ValueEventListener listener) {
        DatabaseReference dataRef = ref.child("Movements").child(id);
        dataRef.addListenerForSingleValueEvent(listener);
    }

    public void getHashtag(String name, ValueEventListener listener){
        DatabaseReference dataRef = ref.child("Hashtags").child(name);
        dataRef.addValueEventListener(listener);
    }

    public void getHashtagOnce(String name, ValueEventListener listener){
        DatabaseReference dataRef = ref.child("Hashtags").child(name);
        dataRef.addListenerForSingleValueEvent(listener);
    }

    public void addUsertoMovement(User user, Movement movement) {
        user.addMovement(movement);
        movement.addFollower(user);
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
        hashtag.addUser(user.getId());
        updateUser(user);
        updateHashtag(hashtag);
    }

    public void addMovementtoHashtag(Movement movement, Hashtag hashtag) {
        movement.addHashtag(hashtag.getName());
        hashtag.addMovement(movement.getId());
        updateMovement(movement);
        updateHashtag(hashtag);
    }

    public void removeUserfromHashtag(User user, Hashtag hashtag) {
        user.removeHashtag(hashtag.getName());
        hashtag.removeUser(user.getId());
        updateHashtag(hashtag);
        updateUser(user);
    }

    // Not used

    public void updateUser(User user) {
        ref.child("Users").child(user.getId()).setValue(user);
    }

    // Not used

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

    public void getBatchMovementofUserStatus(User user, ArrayList<String> movementBatch, ChildEventListener listener) {
        for (int i = 0; i < movementBatch.size(); i++) {
            Query dataRef = ref.child("Users").child(user.getId()).child("movements").child(movementBatch.get(i));
            dataRef.addChildEventListener(listener);
        }
    }
    public void getMovementofUserStatus(User user, Movement movement, ValueEventListener listener) {
        Query dataRef = ref.child("Users").child(user.getId()).child("movements").child(movement.getId()).child("status");
        dataRef.addValueEventListener(listener);
    }

    public void setMovementofUserStatus(User user, Movement movement, boolean isComplete) {
        ref.child("Users").child(user.getId()).child("movements").child(movement.getId()).child("status").setValue(isComplete);
    }

    public void getHashtagsfromUser(User user, ValueEventListener listener){
        Query dataRef = ref.child("Users").child(user.getId()).child("hashtagList");
        dataRef.addValueEventListener(listener);
    }
}
