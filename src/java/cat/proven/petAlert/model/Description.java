package cat.proven.petAlert.model;

import com.google.gson.annotations.Expose;
import java.io.Serializable;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * ADT Description
 *
 * @author Pet Alert
 */
@Entity
@Table(name = "descriptions")
@XmlRootElement
/* QUERIES */
@NamedQueries({
    @NamedQuery(name = "Description.findAll", query = "SELECT d FROM Description d")
    , @NamedQuery(name = "Description.findById", query = "SELECT d FROM Description d WHERE d.id = :id")
    , @NamedQuery(name = "Description.findByTitle", query = "SELECT d FROM Description d WHERE d.title = :title")
    , @NamedQuery(name = "Description.findByLostDayHour", query = "SELECT d FROM Description d WHERE d.lostDayHour = :lostDayHour")
    , @NamedQuery(name = "Description.findByDescription", query = "SELECT d FROM Description d WHERE d.description = :description")
    , @NamedQuery(name = "Description.findByPhone", query = "SELECT d FROM Description d WHERE d.phone = :phone")})
public class Description implements Serializable {

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
    @Size(min = 1, max = 100)
    @Column(name = "title")
    @Expose
    private String title;
    @Basic(optional = false)
    @NotNull
    @Column(name = "lost_day_hour")
    @Expose
    private long lostDayHour;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "description")
    @Expose
    private String description;
    @Basic(optional = false)
    @Size(min = 1, max = 50)
    @Column(name = "phone")
    @Expose
    private String phone;
    @OneToMany(mappedBy = "descId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Alert> alertsList;

    /* CONSTRUCTORS */
    public Description() {
    }

    public Description(Integer id) {
        this.id = id;
    }

    public Description(String title, long lostDayHour, String description, String phone) {
        this.title = title;
        this.lostDayHour = lostDayHour;
        this.description = description;
        this.phone = phone;
    }

    public Description(Integer id, String title, long lostDayHour, String description, String phone) {
        this.id = id;
        this.title = title;
        this.lostDayHour = lostDayHour;
        this.description = description;
        this.phone = phone;
    }

    /* GETTERS AND SETTERS */
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getLostDayHour() {
        return lostDayHour;
    }

    public void setLostDayHour(long lostDayHour) {
        this.lostDayHour = lostDayHour;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
        int hash = 7;
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
        final Description other = (Description) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Description{");
        sb.append("id=").append(id);
        sb.append(", title=").append(title);
        sb.append(", lostDayHour=").append(lostDayHour);
        sb.append(", description=").append(description);
        sb.append(", phone=").append(phone);
        sb.append('}');

        return sb.toString();
    }

}
