package cat.proven.petAlert.model;

import com.google.gson.annotations.Expose;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * ADT Alert
 *
 * @author Pet Alert
 */
@Entity
@Table(name = "alerts")
@XmlRootElement
/* QUERIES */
@NamedQueries({
    @NamedQuery(name = "Alert.findAll", query = "SELECT a FROM Alert a WHERE a.active = true")
    , @NamedQuery(name = "Alert.findById", query = "SELECT a FROM Alert a WHERE a.id = :id")
    , @NamedQuery(name = "Alert.findByCreationDate", query = "SELECT a FROM Alert a WHERE a.creationDate = :creationDate")
    , @NamedQuery(name = "Alert.findByActive", query = "SELECT a FROM Alert a WHERE a.active = :active")
    , @NamedQuery(name = "Alert.findByReportNumber", query = "SELECT a FROM Alert a WHERE a.reportNumber >= 3 AND a.active = true ORDER BY a.reportNumber")
    , @NamedQuery(name = "Alert.findByAnimalKind", query = "SELECT a FROM Alert a JOIN a.animalId b WHERE b.kind = :kind AND a.active = true")
    , @NamedQuery(name = "Alert.findByAnimalKind_Sex", query = "SELECT a FROM Alert a JOIN a.animalId b WHERE b.kind = :kind and b.sex = :sex AND a.active = true")
    , @NamedQuery(name = "Alert.findByAnimalKind_Race", query = "SELECT a FROM Alert a JOIN a.animalId b WHERE b.kind = :kind and b.race = :race AND a.active = true")
    , @NamedQuery(name = "Alert.findByAnimalKind_Race_Sex", query = "SELECT a FROM Alert a JOIN a.animalId b WHERE b.kind = :kind and b.race = :race and b.sex = :sex AND a.active = true")})
public class Alert implements Serializable {

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
    @Column(name = "creation_date")
    @Expose
    private long creationDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "active")
    @Expose
    private boolean active;
    @Column(name = "report_number")
    @Expose
    private Integer reportNumber;
    @JoinColumn(name = "animal_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Expose
    private Animal animalId;
    @JoinColumn(name = "coord_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Expose
    private Coordinate coordId;
    @JoinColumn(name = "desc_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Expose
    private Description descId;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Expose
    private User userId;

    /* CONSTRUCTORS */
    public Alert() {
    }

    public Alert(Integer id) {
        this.id = id;
    }

    public Alert(long creationDate, boolean active, Animal animalId, Coordinate coordId, Description descId, User userId) {
        this.creationDate = creationDate;
        this.active = active;
        this.animalId = animalId;
        this.coordId = coordId;
        this.descId = descId;
        this.userId = userId;
    }

    public Alert(Integer id, long creationDate, boolean active) {
        this.id = id;
        this.creationDate = creationDate;
        this.active = active;
    }

    /* GETTERS AND SETTERS */
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(long creationDate) {
        this.creationDate = creationDate;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Integer getReportNumber() {
        return reportNumber;
    }

    public void setReportNumber(Integer reportNumber) {
        this.reportNumber = reportNumber;
    }

    public Animal getAnimalId() {
        return animalId;
    }

    public void setAnimalId(Animal animalId) {
        this.animalId = animalId;
    }

    public Coordinate getCoordId() {
        return coordId;
    }

    public void setCoordId(Coordinate coordId) {
        this.coordId = coordId;
    }

    public Description getDescId() {
        return descId;
    }

    public void setDescId(Description descId) {
        this.descId = descId;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    @Override
    public int hashCode() {
        int hash = 5;
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
        final Alert other = (Alert) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Alert{");
        sb.append("id=").append(id);
        sb.append(", creationDate=").append(creationDate);
        sb.append(", active=").append(active);
        sb.append(", reportNumber=").append(reportNumber);
        sb.append(", animalId=").append(animalId);
        sb.append(", coordId=").append(coordId);
        sb.append(", descId=").append(descId);
        sb.append(", userId=").append(userId);
        sb.append('}');
        return sb.toString();
    }
}
