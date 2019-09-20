package cat.proven.petAlert.model;

import com.google.gson.annotations.Expose;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * ADT Coordinate
 *
 * @author Pet Alert
 */
@Entity
@Table(name = "coordinates")
@XmlRootElement
/* QUERIES */
@NamedQueries({
    @NamedQuery(name = "Coordinate.findAll", query = "SELECT c FROM Coordinate c")
    , @NamedQuery(name = "Coordinate.findById", query = "SELECT c FROM Coordinate c WHERE c.id = :id")
    , @NamedQuery(name = "Coordinate.findByLatitude", query = "SELECT c FROM Coordinate c WHERE c.latitude = :latitude")
    , @NamedQuery(name = "Coordinate.findByLongitude", query = "SELECT c FROM Coordinate c WHERE c.longitude = :longitude")})
public class Coordinate implements Serializable {

    /* ATTRIBUTES */
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    @Expose
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "latitude")
    @Expose
    private BigDecimal latitude;
    @Basic(optional = false)
    @NotNull
    @Column(name = "longitude")
    @Expose
    private BigDecimal longitude;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "coordId", fetch = FetchType.LAZY)
    private List<Alert> alertsList;

    /* CONSTRUCTORS */
    public Coordinate() {
    }

    public Coordinate(Integer id) {
        this.id = id;
    }

    public Coordinate(BigDecimal latitude, BigDecimal longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Coordinate(Integer id, BigDecimal latitude, BigDecimal longitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /* GETTERS AND SETTERS */
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    @XmlTransient
    public List<Alert> getAlertsList() {
        return alertsList;
    }

    public void setAlertsList(List<Alert> alertsList) {
        this.alertsList = alertsList;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Coordinate other = (Coordinate) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Coordinate{");
        sb.append("id=").append(id);
        sb.append(", latitude=").append(latitude);
        sb.append(", longitude=").append(longitude);
        sb.append('}');

        return sb.toString();
    }
}
