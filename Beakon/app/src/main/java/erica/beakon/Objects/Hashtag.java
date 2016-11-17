package erica.beakon.Objects;

import java.util.ArrayList;

/**
 * Created by mary on 11/16/16.
 */
public class Hashtag {
    private String name;
    private ArrayList<Movement> movementList;
    private ArrayList<User> userList;

    public Hashtag(String name, ArrayList<Movement> movementList, ArrayList<User> userList) {
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

    public ArrayList<Movement> getMovementList() {
        return movementList;
    }

    public void setMovementList(ArrayList<Movement> movementList) {
        this.movementList = movementList;
    }

    public ArrayList<User> getUserList() {
        return userList;
    }

    public void setUserList(ArrayList<User> userList) {
        this.userList = userList;
    }
}
