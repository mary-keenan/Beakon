package erica.beakon.Objects;

import java.util.ArrayList;

/**
 * Created by mafaldaborges on 11/7/16.
 */
public class User {
    private String id;
    private String name;
    private String email;
    private ArrayList<String> hashtagList;


    public User() {

    }

    public User(String id, String name, String email, ArrayList<String> hashtagList){
        this.id = id;
        this.name = name;
        this.email = email;
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
        this.hashtagList = hashtagList;
    }
}
