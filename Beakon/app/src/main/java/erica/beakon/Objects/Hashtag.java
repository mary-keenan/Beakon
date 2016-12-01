package erica.beakon.Objects;

import java.util.ArrayList;

/**
 * Created by mary on 11/16/16.
 */
public class Hashtag {
    private String name;
    private ArrayList<String> movementList;
    private ArrayList<String> userList;

    public Hashtag(){}

    public Hashtag(String name, ArrayList<String> movementList, ArrayList<String> userList) {
        this.name = name;
        this.movementList = movementList;
        this.userList = userList;
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
}
