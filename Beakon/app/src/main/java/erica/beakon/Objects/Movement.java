package erica.beakon.Objects;

import java.util.ArrayList;

/**
 * Created by mafaldaborges on 11/7/16.
 */
public class Movement {

    String id;
    private String name;
    private String description;
    private String steps;
    private String resources;
    private ArrayList<String> hashtagList;
    private ArrayList<String> users;

    public Movement() {}

    public Movement(String id, String name, String description, String steps, String resources, ArrayList<String> hashtagList){
        this.id = id;
        this.name = name;
        this.description = description;
        this.steps = steps;
        this.resources = resources;
        this.hashtagList = hashtagList;
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

    public ArrayList<String> getUsers() { return users; }

    public void setUsers(ArrayList<String> users){ this.users = users; }

    public void addUser(User user) {
        if (this.users == null) {
            setEmptyUsers();
        }
        this.users.add(user.getId());
    }

    private void setEmptyUsers() { this.setUsers(new ArrayList<String>()); }

    public void removeUser(User user) { this.users.remove(user.getId()); }
}
