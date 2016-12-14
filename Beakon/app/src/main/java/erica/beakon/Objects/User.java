package erica.beakon.Objects;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mafaldaborges on 11/7/16.
 */
public class User {
    private String id;
    private String name;
    private String email;
    private ArrayList<String> hashtagList;
    private HashMap<String, HashMap<String,Boolean>> movements;

    public User() {}

    public User(String id, String name, ArrayList<String> hashtagList, HashMap<String, HashMap<String,Boolean>> movementList){
        this.id = id;
        this.name = name;
        this.hashtagList = hashtagList;
        this.movements = movementList;
    }

    public User(String id, String name, ArrayList<String> hashtagList){
        this.id = id;
        this.name = name;
        this.hashtagList = hashtagList;
        this.movements = new HashMap<String, HashMap<String,Boolean>>();
    }

    public User(String id, String name) {
        this.id = id;
        this.name = name;
        this.hashtagList = new ArrayList<>();
        this.movements = new HashMap<String, HashMap<String,Boolean>>();
    }

    public User initializeLists(User user){
        if (user.getMovements() == null){
            user.setMovements(new HashMap<String, HashMap<String,Boolean>>());}
        if (user.getHashtagList() == null){
            user.setHashtagList(new ArrayList<String>());}
        return user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<String> getHashtagList() {
        return hashtagList;
    }

    public void setHashtagList(ArrayList<String> hashtagList) {
        this.hashtagList = hashtagList;}

    public void addHashtag(String hashtag){
        hashtagList.add(hashtag);
    }

    public void removeHashtag(String hashtag){
        hashtagList.remove(hashtag);
    }

    public HashMap<String, HashMap<String,Boolean>> getMovements() {
            return this.movements;}

    public void setMovements(HashMap<String, HashMap<String,Boolean>> movements){ this.movements = movements; }

    public void setEmptyMovements() {setMovements(new HashMap<String, HashMap<String,Boolean>>());}

    public void addMovement(Movement movement) {
        this.movements.put(movement.getId(), getEmptyStatusMap());
    }

    public void addMovement(String movementId) { this.movements.put(movementId, getEmptyStatusMap());  }

    public void removeMovement(Movement movement) { this.movements.remove(movement.getId()); }

    public boolean isInMovement(Movement movement) {
        return this.getMovements().keySet().contains(movement.getId());
    }

    public void updateMovements(String movementID, boolean status) {
        HashMap<String,Boolean> statusHash = new HashMap<>();
        statusHash.put("status", status);
        movements.put(movementID,statusHash);
    }

    private HashMap<String, Boolean> getEmptyStatusMap() {
        HashMap<String, Boolean> empty = new HashMap<>();
        empty.put("status", false);
        return empty;
    }
}
