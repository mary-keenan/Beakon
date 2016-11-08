package erica.beakon;

/**
 * Created by mafaldaborges on 11/7/16.
 */
public class UserMovement {

    private long id;
    private Boolean completed;
    private User user;
    private Movement movement;

    public UserMovement(long id, Boolean completed, User user, Movement movement){
        this.id = id;
        this.completed = completed;
        this.user = user;
        this.movement = movement;

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Movement getMovement() {
        return movement;
    }

    public void setMovement(Movement movement) {
        this.movement = movement;
    }
}