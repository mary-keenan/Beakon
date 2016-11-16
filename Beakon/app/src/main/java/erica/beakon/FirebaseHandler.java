package erica.beakon;


import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class FirebaseHandler {
    FirebaseDatabase db;
    DatabaseReference ref;


    public FirebaseHandler(FirebaseDatabase db, DatabaseReference ref) {
        this.ref = ref;
        this.db = db;
    }

    public void addUser(String name, String email) {
        DatabaseReference userRef = ref.child("Users").push();
        String userId = userRef.getKey();
        User user = new User(userId, name, email);
        ref.child("Users").child(userId).setValue(user);

    }

    public void addMovement(String name, String description, String steps, String resources) {
        DatabaseReference movementRef = ref.child("Movements").push();
        String movementId = movementRef.getKey();
        Movement movement = new Movement(movementId, name, description, steps, resources);
        ref.child("Movements").child(movementId).setValue(movement);
    }

    public void getData(String id, ValueEventListener listener) {
        Query dataRef;
        if (id == null) {
            dataRef = ref.orderByChild("id").getRef();
        } else {
            dataRef = ref.orderByChild("id").equalTo(id).getRef();
        }

        dataRef.addListenerForSingleValueEvent(listener);
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



