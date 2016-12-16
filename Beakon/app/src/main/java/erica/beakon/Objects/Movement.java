package erica.beakon.Objects;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by mafaldaborges on 11/7/16.
 */
public class Movement implements Serializable {

    private String id;
    private String name;
    private String description;
    private String steps;
    private String resources;
    private ArrayList<String> hashtagList;
    private ArrayList<String> followerList;
    public boolean joined; //purely for the adapters of the listviews

    public Movement() {}

    public Movement(String id, String name, String description, String steps, String resources, ArrayList<String> hashtagList, ArrayList<String> followerList) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.steps = steps;
        this.resources = resources;
        this.hashtagList = hashtagList;
        this.followerList = followerList;
        this.joined = false;
    }

    public Movement(String id, String name, String description, String steps, String resources, ArrayList<String> hashtagList){
        this.id = id;
        this.name = name;
        this.description = description;
        this.steps = steps;
        this.resources = resources;
        this.hashtagList = hashtagList;
        this.followerList = new ArrayList<>();
    }

    public Movement(String id, String name, String description, String steps, String resources){
        this.id = id;
        this.name = name;
        this.description = description;
        this.steps = steps;
        this.resources = resources;
        this.followerList = new ArrayList<>();
        this.hashtagList = new ArrayList<>();
    }

    public Movement initializeLists(Movement movement){
        if (movement.getFollowers() == null){
            movement.setFollowers(new ArrayList<String>());}
        if (movement.getHashtagList() == null){
            movement.setHashtagList(new ArrayList<String>());}
        return movement;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public String getResources() {
        return resources;
    }

    public void setResources(String resources) {
        this.resources = resources;
    }

    public ArrayList<String> getHashtagList() {
        return hashtagList;
    }

    public void setHashtagList(ArrayList<String> hashtagList) {
        this.hashtagList = hashtagList;
    }

    public void addHashtag(String hashtag){
        hashtagList.add(hashtag);
    }

    public void removeHashtag(String hashtag){
        hashtagList.remove(hashtag);
    }

    public ArrayList<String> getFollowers() { return followerList; }

    public void setFollowers(ArrayList<String> followerList){ this.followerList = followerList; }

    public void addFollower(User user) {
        if (this.followerList == null) {
            setEmptyUsers();
        }
        this.followerList.add(user.getId());
    }

    private void setEmptyUsers() { this.setFollowers(new ArrayList<String>()); }

    public void removeUser(User user) { this.followerList.remove(user.getId()); }

    public boolean hasUsers() {
        return !(this.followerList == null || this.followerList.isEmpty());
    }
}
