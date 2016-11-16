package erica.beakon.Objects;

/**
 * Created by mafaldaborges on 11/7/16.
 */
public class Movement {

    String id;
    private String name;
    private String description;
    private String steps;
    private String resources;


    public Movement(String id, String name, String description, String steps, String resources){
        this.id = id;
        this.name = name;
        this.description = description;
        this.steps = steps;
        this.resources = resources;

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
}
