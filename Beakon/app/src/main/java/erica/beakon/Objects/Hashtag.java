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

    public void addMovement(Movement movement) {
        this.movementList.add(movement.getId());
    }

    public void removeMovement(Movement movement) {
        this.movementList.remove(movement.getId());
    }

    public ArrayList<String> getUserList() {
        return userList;
    }

    public void addUser(User user) {
        this.movementList.add(user.getId());
    }

    public void removeUser(User user) {
        this.movementList.remove(user.getId());
    }
}
