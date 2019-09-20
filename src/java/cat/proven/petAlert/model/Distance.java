package cat.proven.petAlert.model;

import com.google.gson.annotations.Expose;

/**
 * ADT Distance
 *
 * @author Pet Alert
 */
public class Distance {

    /* ATTRIBUTES */
    @Expose
    private int id;
    @Expose
    private Double distance;

    /* CONSTRUCTORS */
    public Distance() {
    }

    public Distance(int id, Double distance) {
        this.id = id;
        this.distance = distance;
    }

    /* GETTERS AND SETTERS */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Distance{");
        sb.append("id=").append(id);
        sb.append(", distance=").append(distance);
        sb.append('}');
        return sb.toString();
    }

}
