package erica.beakon.Objects;

import java.util.ArrayList;

/**
 * Created by mafaldaborges on 11/7/16.
 */
public class User {
    private String id;
    private String name;
    private String email;
    private ArrayList<String> movements;

    public User() {

    }

    public User(String id, String name, String email){
        this.id = id;
        this.name = name;
        this.email = email;
        this.movements = new ArrayList<>();
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

    public ArrayList<String> getMovements() { return this.movements; }

    public void setMovements(ArrayList<String> movements){ this.movements = movements; }

    public void setEmptyMovements() {setMovements(new ArrayList<String>());}

    public void addMovement(Movement movement) { this.movements.add(movement.getId()); }

    public void addMovement(String movementId) { this.movements.add(movementId); }

    public void removeMovement(Movement movement) { this.movements.remove(movement.getId()); }

    public boolean isInMovement(Movement movement) {
        return this.getMovements().contains(movement.getId());
    }
}
