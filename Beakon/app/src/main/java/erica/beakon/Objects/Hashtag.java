package erica.beakon.Objects;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by mary on 11/16/16.
 */
public class Hashtag {
    private String name;
    private ArrayList<String> movementList = new ArrayList<>();
    private ArrayList<String> userList = new ArrayList<>();

    public Hashtag(){}

    public Hashtag(String name, ArrayList<String> movementList, ArrayList<String> userList) {
        this.name = name;
        this.movementList = movementList;
        this.userList = userList;
    }

    public Hashtag(String name) {
        this.name = name;
        this.movementList = new ArrayList<>();
        this.userList = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getMovementList() {
        return movementList;
    }

    public void addMovement(String movementID) {
        this.movementList.add(movementID);
    }

    public void removeMovement(String movementID) {
        this.movementList.remove(movementID);
    }

    public ArrayList<String> getUserList() {
        return userList;
    }

    public void addUser(String userID) {
        this.userList.add(userID);
    }

    public void removeUser(String userID) {
        this.userList.remove(userID);
    }

    public static boolean isHashtagShortEnough(String hashtag) {
        return hashtag.length() <=13;
    }

    public static boolean areHashtagsShortEnough(ArrayList<String> hashtags) {
        for (String h: hashtags) {
            if (!isHashtagShortEnough(h)) {
                return false;
            }
        }
        return true;
    }


}
